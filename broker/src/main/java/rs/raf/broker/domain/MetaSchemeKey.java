package rs.raf.broker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "META_SCHEME_KEY")
public class MetaSchemeKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "meta_scheme_id", referencedColumnName = "id")
    private MetaScheme metaScheme;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "metaSchemeKey")
    private MetaSchemeValue metaSchemeValue;

    public void setMetaSchemeValue(MetaSchemeValue metaSchemeValue) {
        this.metaSchemeValue = metaSchemeValue;
        this.metaSchemeValue.setMetaSchemeKey(this);
    }

    @Override
    public String toString() {
        return "MetaSchemeKey{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", metaSchemeValue=" + metaSchemeValue +
                '}';
    }
}
