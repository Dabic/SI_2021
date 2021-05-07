package rs.raf.modelvalidator.persistance;

import org.springframework.data.mongodb.repository.MongoRepository;
import rs.raf.modelvalidator.domain.MetaData;

import java.util.Optional;

public interface MetaDataRepository extends MongoRepository<MetaData, String> {
    Optional<MetaData> findByModelType(String modelType);
}
