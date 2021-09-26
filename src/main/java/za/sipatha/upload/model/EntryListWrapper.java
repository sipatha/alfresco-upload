package za.sipatha.upload.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryListWrapper {

    private EntryList list;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class EntryList {

        private List<EntryWrapper> entries;

    }

}
