package rs.raf.usecase.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "D_NODE")
public class DNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("key")
    private Integer nodeKey;

    private String type;

    private String text;

    private String loc;

    @JsonProperty("fields")
    @OneToMany(mappedBy = "dNode", cascade = CascadeType.ALL)
    private List<NodeField> nodeFields;

    public void setNodeFields(List<NodeField> nodeFields) {
        this.nodeFields = nodeFields;
        this.nodeFields.forEach(nodeField -> nodeField.setDNode(this));
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "class_diagram_id")
    private ClassDiagram classDiagram;
}
