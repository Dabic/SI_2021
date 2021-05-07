package rs.raf.rqm.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class RqmDiagram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String teamName;

    @OneToMany(mappedBy = "rqmDiagram", cascade = CascadeType.ALL)
    private List<RqmRow> rqmRows;

    public void setRqmRows(List<RqmRow> rqmRows) {
        this.rqmRows = rqmRows;
        this.rqmRows.forEach(row -> row.setRqmDiagram(this));
    }
}
