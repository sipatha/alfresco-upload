package za.sipatha.upload.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "alfresco-upload")
public class AlfrescoUploadProperties {

    private String localFolder;

    private String remoteFolder;

    private String baseUrl;

    private String username;

    private String password;

}
