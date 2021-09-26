package za.sipatha.upload.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.sipatha.upload.model.EntryWrapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
public class DeserializationTests {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testDeserializeUploadResponse() throws IOException {
        var payload = Files.readString(Path.of("src/test/resources/sample-upload-response.json"));

        var obj = objectMapper.readValue(payload, EntryWrapper.class);

        Assertions.assertNotNull(obj);
    }

}
