package rs.raf.modelvalidator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class DLink {

    private Long id;

    @JsonProperty("key")
    private Integer linkKey;

    @JsonProperty("from")
    private Integer fromNode;

    @JsonProperty("to")
    private Integer toNode;

    private String type;

    private String label;

    @JsonIgnore
    private ClassDiagram classDiagram;

    @Override
    public String toString() {
        return "DLink{" +
                "id=" + id +
                ", linkKey=" + linkKey +
                ", fromNode=" + fromNode +
                ", toNode=" + toNode +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
