package za.sipatha.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;
import za.sipatha.upload.ex.AlfrescoUploadException;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private static final String SUB_PATH = "alfresco/api/-default-/public/alfresco/versions/1/nodes";

    private final OkHttpClient httpClient;

    private final AlfrescoUploadProperties properties;

    private final AuthenticationService authService;

    public void tag(String id) {

        var request = new Request.Builder()
            .url(getUrl(id))
            .post(RequestBody.create("[{\"tag\":\"auto-uploads\"},{\"tag\":\"erpnext-attachment\"}]", MediaType.parse("application/json")))
            .addHeader("Authorization", authService.getAuthHeader())
            .build();

        try(var response = httpClient.newCall(request).execute()) {
            if(!response.isSuccessful()) {
                throw new AlfrescoUploadException("failed to tag node: " + id + " status: " + response.code());
            }
            log.info("tagged node: {}", id);
        } catch (IOException ioe) {
            log.error("failed to tag node: {}", id, ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public String getUrl(String id) {
        return HttpUrl.parse(properties.getBaseUrl())
            .newBuilder()
            .addPathSegments(SUB_PATH)
            .addPathSegment(id)
            .addPathSegment("tags")
            .build()
            .toString();
    }

}
