package rs.raf.usecase.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ClassDiagram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String modelType;

    @JsonProperty("dnodes")
    @OneToMany(mappedBy = "classDiagram", cascade = CascadeType.ALL)
    private List<DNode> dNodes;

    @JsonProperty("dlinks")
    @OneToMany(mappedBy = "classDiagram", cascade = CascadeType.ALL)
    private List<DLink> dLinks;

    public void setdLinks(List<DLink> dLinks) {
        this.dLinks = dLinks;
        this.dLinks.forEach(dLink -> dLink.setClassDiagram(this));
    }

    public void setdNodes(List<DNode> dNodes) {
        this.dNodes = dNodes;
        this.dNodes.forEach(dNode -> dNode.setClassDiagram(this));
    }

    private String teamName;
}
