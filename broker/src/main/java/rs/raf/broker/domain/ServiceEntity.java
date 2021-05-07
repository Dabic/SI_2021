package rs.raf.broker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "SERVICE")
public class ServiceEntity {

    @Id
    private String name;

    private String domain;

    private Integer port;

    @OneToMany(mappedBy = "service", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Endpoint> endpoints;

    public void setEndpoints(List<Endpoint> endpoints) {
        this.endpoints = endpoints;
        this.endpoints.forEach(endpoint -> endpoint.setService(this));
    }

    @Override
    public String toString() {
        return "ServiceEntity{" +
                "name='" + name + '\'' +
                ", domain='" + domain + '\'' +
                ", port=" + port +
                ", endpoints=" + endpoints +
                '}';
    }
}
