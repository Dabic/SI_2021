package rs.raf.modelvalidator.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DNode {

    private Long id;

    @JsonProperty("key")
    private Integer nodeKey;

    private String type;

    private String text;

    private String loc;

    @JsonProperty("fields")
    private List<NodeField> nodeFields;

    public void setNodeFields(List<NodeField> nodeFields) {
        this.nodeFields = nodeFields;
        this.nodeFields.forEach(nodeField -> nodeField.setDNode(this));
    }

    @JsonIgnore
    private ClassDiagram classDiagram;

    @Override
    public String toString() {
        return "DNode{" +
                "id=" + id +
                ", nodeKey=" + nodeKey +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", loc='" + loc + '\'' +
                ", nodeFields=" + nodeFields +
                '}';
    }
}
