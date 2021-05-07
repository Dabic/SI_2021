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
@Table(name = "META_SCHEME")
public class MetaScheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "metaScheme", cascade = CascadeType.ALL)
    private List<MetaSchemeKey> metaSchemeKeys;

    public void setMetaSchemeKeys(List<MetaSchemeKey> keys) {
        this.metaSchemeKeys = keys;
        this.metaSchemeKeys.forEach(key -> key.setMetaScheme(this));
    }

    @OneToOne(mappedBy = "endpointMetaScheme")
    private Endpoint endpoint;

    @Override
    public String toString() {
        return "MetaScheme{" +
                "id=" + id +
                ", metaSchemeKeys=" + metaSchemeKeys +
                '}';
    }
}
