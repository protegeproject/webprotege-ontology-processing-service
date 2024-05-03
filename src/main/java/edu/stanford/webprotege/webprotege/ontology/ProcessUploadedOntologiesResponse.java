package edu.stanford.webprotege.webprotege.ontology;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.common.Response;

import java.util.List;

import static edu.stanford.webprotege.webprotege.ontology.ProcessUploadedOntologiesRequest.CHANNEL;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
@JsonTypeName(CHANNEL)
public record ProcessUploadedOntologiesResponse(@JsonProperty("ontologies") List<BlobLocation> ontologies) implements Response {

}
