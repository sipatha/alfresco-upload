package za.sipatha.upload.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Entry implements Serializable {

    private String name;

    private String id;

    private String parentId;

    private OffsetDateTime createdAt;

}
