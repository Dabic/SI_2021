package rs.raf.broker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "META_SCHEME_VALUE")
public class MetaSchemeValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "meta_scheme_key_id")
    private MetaSchemeKey metaSchemeKey;

    @Override
    public String toString() {
        return "MetaSchemeValue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
