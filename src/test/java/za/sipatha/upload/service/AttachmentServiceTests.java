package za.sipatha.upload.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class AttachmentServiceTests {

    @Autowired
    private AttachmentService service;

    @Test
    public void testGetAttachments() {
        List<String> files = service.getAttachments();
        Assertions.assertFalse(files.isEmpty());
    }

}
