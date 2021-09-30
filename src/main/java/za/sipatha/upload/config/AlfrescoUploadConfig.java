package za.sipatha.upload.config;

import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
@EnableConfigurationProperties({AlfrescoUploadProperties.class})
public class AlfrescoUploadConfig {

    private OkHttpClient httpClient;

    @Bean
    public OkHttpClient httpClient() {
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        httpClient = new OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .followRedirects(true)
            .followSslRedirects(true)
            .connectTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .connectionPool(new ConnectionPool(3, 10, TimeUnit.SECONDS))
            .build();

        return httpClient;
    }

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(
                    name,
                    CacheBuilder.newBuilder()
                        .expireAfterWrite(1, TimeUnit.HOURS)
                        .maximumSize(2)
                        .build().asMap(),
                    false);
            }
        };

        cacheManager.setCacheNames(List.of("authentication_ticket"));
        return cacheManager;
    }

    @PreDestroy
    public void clean() {
        if(Objects.nonNull(httpClient)) {
            httpClient.connectionPool().evictAll();
            ExecutorService service = httpClient.dispatcher().executorService();
            service.shutdown();
            try {
                service.awaitTermination(30, TimeUnit.SECONDS);
                log.info("http connection pool cleaned");
            } catch (InterruptedException e) {
                log.warn("InterruptedException on cleanup", e);
            }
        }
    }

}
