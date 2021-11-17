package edu.stanford.webprotege.webprotege.ontology;

import edu.stanford.protege.webprotege.common.Request;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
public record ProcessUploadedOntologiesRequest(DocumentId documentId) implements Request<ProcessUploadedOntologiesResponse> {

    public static final String CHANNEL = "webprotege.ontology-processing.ProcessUploadedOntologies";

    @Override
    public String getChannel() {
        return CHANNEL;
    }
}
