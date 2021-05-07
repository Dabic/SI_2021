package rs.raf.uml.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialsHolder {

    private String username;
    private String password;
}
