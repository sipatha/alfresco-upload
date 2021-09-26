package za.sipatha.upload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;
import za.sipatha.upload.ex.AlfrescoUploadException;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteService {

    private static final String SUB_PATH = "alfresco/api/-default-/public/alfresco/versions/1/deleted-nodes";

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final AlfrescoUploadProperties properties;

    private final AuthenticationService authService;

    public void delete(String id) {

        var request = new Request.Builder()
            .url(getUrl(id))
            .delete()
            .addHeader("cache-control", "no-cache")
            .addHeader("Authorization", authService.getAuthHeader())
            .build();

        try(var response = httpClient.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new AlfrescoUploadException("failed to delete node: " + id + " status: " + response.code());
            }
            log.info("permanently deleted node: {}", id);
        } catch (IOException ioe) {
            log.error("failed to delete node", ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public String getUrl(String id) {
        return HttpUrl.parse(properties.getBaseUrl())
            .newBuilder()
            .addPathSegments(SUB_PATH)
            .addPathSegment(id)
            .build()
            .toString();
    }

}
