package edu.stanford.webprotege.webprotege.ontology;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public interface OntologyDocumentsExtractor {

    OntologyDocumentsCollection extractOntologyDocuments(Path documentsBlob) throws IOException;
}
