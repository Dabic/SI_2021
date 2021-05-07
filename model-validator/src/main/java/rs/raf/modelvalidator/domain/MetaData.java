package rs.raf.modelvalidator.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@Document("model-validator")
public class MetaData {

    @Id
    private String id;

    private String modelType;

    private Integer generalisationCount;

    private Boolean checkFieldTypeValidity;

    private Boolean checkClassNameDuplicates;

    private Boolean checkAuthorToStoryLinks;

    private Boolean checkGeneralisationUsage;

    private Boolean checkImplementationUsage;

    private Boolean checkIncludeUsage;

    private Boolean checkExtendUsage;

    private Boolean checkRelationUsage;
}
