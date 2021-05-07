package rs.raf.modelvalidator.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassDiagram {

    private Long id;

    private String name;

    private String modelType;

    @JsonProperty("dnodes")
    private List<DNode> dNodes;

    @JsonProperty("dlinks")
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

    @Override
    public String toString() {
        return "ClassDiagram{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", modelType='" + modelType + '\'' +
                ", dNodes=" + dNodes +
                ", dLinks=" + dLinks +
                ", teamName='" + teamName + '\'' +
                '}';
    }
}
