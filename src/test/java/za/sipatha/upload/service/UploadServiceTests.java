package za.sipatha.upload.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UploadServiceTests {

    @Autowired
    private UploadService uploadService;

    @Test
    public void testGetUrl() {
        var url = uploadService.getUrl();

        Assertions.assertNotNull(url);
    }

    @Test
    public void testUpload() {
        var filename = "DomainRenewalHosting20192020.pdf";

        var id = uploadService.upload(filename);

        Assertions.assertNotNull(id);
    }

}
