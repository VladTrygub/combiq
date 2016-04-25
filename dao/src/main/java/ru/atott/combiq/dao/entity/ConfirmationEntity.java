package ru.atott.combiq.dao.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;
import ru.atott.combiq.dao.Types;

import java.util.Map;

@Document(indexName = "#{domainResolver.resolveSiteIndex()}", type = Types.confirmation)
public class ConfirmationEntity {

    @Id
    private String id;

    private Map<String, Object> state;

    @Field(index = FieldIndex.not_analyzed, type = FieldType.String)
    private String confirmationType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public void setState(Map<String, Object> state) {
        this.state = state;
    }

    public String getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(String confirmationType) {
        this.confirmationType = confirmationType;
    }
}
