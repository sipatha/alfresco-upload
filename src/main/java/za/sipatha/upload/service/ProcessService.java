package za.sipatha.upload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessService {

    private final AttachmentService attachmentService;

    private final UploadService uploadService;

    private final TagService tagService;

    public String process(String filename) {
        var id = uploadService.upload(filename);

        tagService.tag(id);

        attachmentService.deleteFile(filename);

        return id;
    }

}
