package rs.raf.usecase.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ModelDTO {

    private Long id;
    private String name;
    private String type;

    public ModelDTO(Long id, String name) {
        this.id = id;
        this.name = name;
        this.type = "use-case";
    }
}

