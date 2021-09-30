package za.sipatha.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanFilenameService {

    private final AlfrescoUploadProperties properties;

    private final AttachmentService attachmentService;
    
    public void clean() {
        attachmentService.getAttachments()
            .forEach(filename -> {
                var newFilename = filename
                    .replaceAll("\\/", "-")
                    .replaceAll(":", "-")
                    .replaceAll("=", "-")
                    .replaceAll("%20", "-")
                    .replaceAll("%", "-")
                    .replaceAll("\\$", "-")
                    .replaceAll("\\(", "-")
                    .replaceAll("\\)", "-");

                if(!filename.equals(newFilename)) {
                    log.info("old filename: '{}', new filename: '{}'", filename, newFilename);

                    var source = Path.of(properties.getLocalFolder(), filename);

                    try {
                        Files.move(source, source.resolveSibling(newFilename), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ioe) {
                        log.error("failed to rename file {}", filename, ioe);
                    }
                }
            });

    }
    
}
