package rs.raf.broker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Endpoint {

    @Id
    private String name;

    private String method;

    private String path;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "ROLES_FOR_ENDPOINT",
            joinColumns = @JoinColumn(name = "endpoint_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "name")
    private ServiceEntity service;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "meta_scheme_id", referencedColumnName = "id")
    private MetaScheme endpointMetaScheme;

    public void setEndpointMetaScheme(MetaScheme endpointMetaScheme) {
        this.endpointMetaScheme = endpointMetaScheme;
        this.endpointMetaScheme.setEndpoint(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endpoint endpoint = (Endpoint) o;
        return name.equals(endpoint.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "id='" + name + '\'' +
                ", method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", roles=" + roles +
                ", endpointMetaScheme=" + endpointMetaScheme +
                '}';
    }
}
