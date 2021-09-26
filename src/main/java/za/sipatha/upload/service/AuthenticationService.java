package za.sipatha.upload.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;
import za.sipatha.upload.ex.AlfrescoUploadException;
import za.sipatha.upload.model.EntryWrapper;
import za.sipatha.upload.model.LoginRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final String SUB_PATH = "alfresco/api/-default-/public/authentication/versions/1/tickets";

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final AlfrescoUploadProperties properties;

    @Cacheable(value = "authentication_ticket")
    public String getAuthHeader() {
        var request = new Request.Builder()
            .url(getUrl())
            .post(RequestBody.create(getBoody(), MediaType.parse("application/json")))
            .addHeader("cache-control", "no-cache")
            .build();

        try(var response = httpClient.newCall(request).execute()) {
            if(response.isSuccessful()) {
                var id = objectMapper.readValue(response.body().bytes(), EntryWrapper.class).getEntry().getId();

                return "Basic " + Base64.getEncoder().encodeToString(id.getBytes(StandardCharsets.UTF_8));
            }
            throw new AlfrescoUploadException("failed to get authentication ticket; status: " + response.code());
        } catch (IOException ioe) {
            log.error("failed to get authentication ticket", ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public String getUrl() {
        return HttpUrl.parse(properties.getBaseUrl())
            .newBuilder()
            .addPathSegments(SUB_PATH)
            .build()
            .toString();
    }

    public String getBoody() {
        try {
            return objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(LoginRequest.builder()
                    .userId(properties.getUsername())
                    .password(properties.getPassword())
                    .build());
        } catch (JsonProcessingException jpe) {
            log.error("failed to create login payload", jpe);
            throw new AlfrescoUploadException(jpe);
        }
    }

}
