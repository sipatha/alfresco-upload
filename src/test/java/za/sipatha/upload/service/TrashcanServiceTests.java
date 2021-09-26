package za.sipatha.upload.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TrashcanServiceTests {

    @Autowired
    private TrashcanService trashcanService;

    @Test
    public void testEmpty() {
        trashcanService.empty();
    }

}
