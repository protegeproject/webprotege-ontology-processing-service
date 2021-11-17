package edu.stanford.webprotege.webprotege.ontology;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
public record DocumentId(String id) {

    @JsonCreator
    public static DocumentId valueOf(String id) {
        return new DocumentId(id);
    }

    @Override
    @JsonValue
    public String id() {
        return id;
    }
}
