package rs.raf.usecase.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CredentialsHolder {

    private String username;
    private String password;
}
