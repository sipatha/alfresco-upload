package za.sipatha.upload.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CleanFilenameServiceTests {

    @Autowired
    private CleanFilenameService cleanFilenameService;

    @Test
    public void testClean() {
         cleanFilenameService.clean();
    }

}
