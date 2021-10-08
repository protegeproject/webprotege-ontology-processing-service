package edu.stanford.webprotege.webprotege.ontology;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 *
 * Exctracts ontology documents from files uploaded to WebProtege.  These files can
 * either by single ontology document files or they can be zip archives of a collection
 * of ontology documents.
 */
public class UploadedOntologyDocumentsExtractor implements OntologyDocumentsExtractor {

    private final ZipInputStreamChecker zipInputStreamChecker;

    private final ZipArchiveOntologyDocumentsExtractor zipArchiveOntologyDocumentsExtractor;

    private final SingleOntologyDocumentExtractor singleDocumentProjectSourcesExtractor;

    @Inject
    public UploadedOntologyDocumentsExtractor(ZipInputStreamChecker zipInputStreamChecker,
                                              ZipArchiveOntologyDocumentsExtractor zipArchiveOntologyDocumentsExtractor,
                                              SingleOntologyDocumentExtractor
                                                   singleDocumentProjectSourcesExtractor) {
        this.zipInputStreamChecker = zipInputStreamChecker;
        this.zipArchiveOntologyDocumentsExtractor = zipArchiveOntologyDocumentsExtractor;
        this.singleDocumentProjectSourcesExtractor = singleDocumentProjectSourcesExtractor;
    }

    @Override
    public OntologyDocumentsCollection extractOntologyDocuments(Path documentsBlob) throws IOException {
        if (zipInputStreamChecker.isZipFile(documentsBlob)) {
            return zipArchiveOntologyDocumentsExtractor.extractOntologyDocuments(documentsBlob);
        }
        else {
            return singleDocumentProjectSourcesExtractor.extractOntologyDocuments(documentsBlob);
        }
    }
}
