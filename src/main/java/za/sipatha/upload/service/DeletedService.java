package za.sipatha.upload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;
import za.sipatha.upload.ex.AlfrescoUploadException;
import za.sipatha.upload.model.Entry;
import za.sipatha.upload.model.EntryListWrapper;
import za.sipatha.upload.model.EntryWrapper;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeletedService {

    private static final String SUB_PATH = "alfresco/api/-default-/public/alfresco/versions/1/deleted-nodes";

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final AlfrescoUploadProperties properties;

    private final AuthenticationService authService;

    public List<EntryWrapper> get() {

        var request = new Request.Builder()
            .url(getUrl())
            .get()
            .addHeader("cache-control", "no-cache")
            .addHeader("Authorization", authService.getAuthHeader())
            .build();

        try(var response = httpClient.newCall(request).execute()) {
            if(response.isSuccessful()) {
                return objectMapper.readValue(response.body().bytes(), EntryListWrapper.class).getList().getEntries();
            }
            throw new AlfrescoUploadException("failed to get deleted nodes status: " + response.code());
        } catch (IOException ioe) {
            log.error("failed to get deleted nodes", ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public String getUrl() {
        return HttpUrl.parse(properties.getBaseUrl())
            .newBuilder()
            .addPathSegments(SUB_PATH)
            .addQueryParameter("skipCount", "0")
            .addQueryParameter("maxItems", "1500")
            .build()
            .toString();
    }

}
