package edu.stanford.webprotege.webprotege.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.Request;

import static edu.stanford.webprotege.webprotege.ontology.ProcessUploadedOntologiesRequest.CHANNEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
@JsonTypeName(CHANNEL)
public record ProcessUploadedOntologiesRequest(@JsonProperty("fileSubmissionId") FileSubmissionId fileSubmissionId) implements Request<ProcessUploadedOntologiesResponse> {

    public static final String CHANNEL = "webprotege.ontology-processing.ProcessUploadedOntologies";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
