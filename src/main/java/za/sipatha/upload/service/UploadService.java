package za.sipatha.upload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;
import za.sipatha.upload.ex.AlfrescoUploadException;
import za.sipatha.upload.model.EntryWrapper;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private static final String SUB_PATH = "alfresco/api/-default-/public/alfresco/versions/1/nodes";

    private final OkHttpClient httpClient;

    private final ObjectMapper objectMapper;

    private final AlfrescoUploadProperties properties;

    private final AuthenticationService authService;

    private final AttachmentService attachmentService;

    public String upload(String filename) {

        var name = filename.lastIndexOf(".") > 0 ? filename.substring(0, filename.lastIndexOf(".")) : filename;

        var multipart = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", filename)
            .addFormDataPart("nodeType", "cm:content")
            .addFormDataPart("cm:title", name)
            .addFormDataPart("cm:description", String.format("ERPNext file %s bulk upload", name))
            .addFormDataPart("filedata", filename, RequestBody.create(attachmentService.getFile(filename)))
            .build();

        var request = new Request.Builder()
            .url(getUrl())
            .post(multipart)
            .addHeader("Authorization", authService.getAuthHeader())
            .build();

        try(var response = httpClient.newCall(request).execute()) {
            if(response.isSuccessful()) {
                return objectMapper.readValue(response.body().bytes(), EntryWrapper.class).getEntry().getId();
            }
            throw new AlfrescoUploadException("failed to upload file: " + filename + " status: " + response.code());
        } catch (IOException ioe) {
            log.error("failed to upload file: {}", filename, ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public String getUrl() {
        return HttpUrl.parse(properties.getBaseUrl())
            .newBuilder()
            .addPathSegments(SUB_PATH)
            .addPathSegment(properties.getRemoteFolder())
            .addPathSegment("children")
            .build()
            .toString();
    }

}
