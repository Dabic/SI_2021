package rs.raf.uml.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "NODE_FIELD")
public class NodeField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nodeField;

    private String text;

    private String info;

    private String type;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "d_node_id")
    private DNode dNode;
}
