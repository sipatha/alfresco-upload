package za.sipatha.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.sipatha.upload.config.AlfrescoUploadProperties;
import za.sipatha.upload.ex.AlfrescoUploadException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AlfrescoUploadProperties properties;

    public List<String> getAttachments() {
        try (Stream<Path> stream = Files.list(Paths.get(properties.getLocalFolder()))) {
            return stream
                .filter(file -> !Files.isDirectory(file))
                .map(Path::getFileName)
                .map(Path::toString)
                .toList();
        } catch (IOException ioe) {
            log.error("failed to get file list", ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public byte[] getFile(String filename) {
        try {
            return Files.readAllBytes(Paths.get(properties.getLocalFolder(), filename));
        } catch (IOException ioe) {
            log.error("failed to read file {}", filename, ioe);
            throw new AlfrescoUploadException(ioe);
        }
    }

    public void deleteFile(String filename) {
        try {
            Files.deleteIfExists(Paths.get(properties.getLocalFolder(), filename));
        } catch (IOException ioe) {
            log.error("failed to delete file {}", filename, ioe);
        }
    }

}
