package za.sipatha.upload.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class LoginRequest implements Serializable {

    private String userId;

    private String password;

}
