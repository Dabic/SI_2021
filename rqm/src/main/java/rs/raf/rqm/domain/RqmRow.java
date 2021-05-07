package rs.raf.rqm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RqmRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rqmId;

    private String header;

    private String description;

    private String type;

    private String priority;

    private String risk;

    private String status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "rqm_diagram_id")
    private RqmDiagram rqmDiagram;

    @Override
    public String toString() {
        return "RqmRow{" +
                "id=" + id +
                ", rqmId='" + rqmId + '\'' +
                ", header='" + header + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", priority='" + priority + '\'' +
                ", risk='" + risk + '\'' +
                ", status='" + status + '\'' +
                ", rqmDiagram=" + rqmDiagram +
                '}';
    }
}
