package rs.raf.modelvalidator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NodeField {

    private Long id;

    private Integer nodeField;

    private String text;

    private String info;

    private String type;

    @JsonIgnore
    private DNode dNode;

    @Override
    public String toString() {
        return "NodeField{" +
                "id=" + id +
                ", nodeField=" + nodeField +
                ", text='" + text + '\'' +
                ", info='" + info + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
