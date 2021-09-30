package za.sipatha.upload.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProcessServiceTests {

    @Autowired
    private ProcessService processService;

    @Autowired
    private AttachmentService attachmentService;

    @Test
    public void testProcessOneFile() {
        var filename = "DomainRenewalHosting20192020.pdf";

        var id = processService.process(filename);

        Assertions.assertNotNull(id);
    }

    @Test
    public void testProcessList() {

        var files = attachmentService.getAttachments();

        for(var file : files) {
            var id = processService.process(file);

            Assertions.assertNotNull(id);
        }

    }

}
