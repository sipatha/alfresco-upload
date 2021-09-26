package za.sipatha.upload.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TagServiceTests {

    @Autowired
    private TagService tagService;

    @Test
    public void testTag() {
        tagService.tag("62d4a24d-8813-468f-872e-b2f3eee04d4b");
    }

}
