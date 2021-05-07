package rs.raf.usecase.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "D_LINK")
public class DLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @ManyToOne
    @JoinColumn(name = "class_diagram_id")
    private ClassDiagram classDiagram;
}
