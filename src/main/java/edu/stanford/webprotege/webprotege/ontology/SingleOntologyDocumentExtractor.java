package edu.stanford.webprotege.webprotege.ontology;

import com.google.common.collect.Lists;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.NonMappingOntologyIRIMapper;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class SingleOntologyDocumentExtractor implements OntologyDocumentsExtractor {

    @Override
    public OntologyDocumentsCollection extractOntologyDocuments(Path documentsBlob) {
        return new SingleFileOntologyDocumentsCollection(documentsBlob.toFile());
    }

    @Inject
    public SingleOntologyDocumentExtractor() {
    }

    private static class SingleFileOntologyDocumentsCollection implements OntologyDocumentsCollection {

        public static final NonMappingOntologyIRIMapper EMPTY_IRI_MAPPER = new NonMappingOntologyIRIMapper();

        private final File ontologyDocument;

        private SingleFileOntologyDocumentsCollection(File ontologyDocument) {
            this.ontologyDocument = ontologyDocument;
        }

        @Override
        public Collection<OWLOntologyDocumentSource> getDocumentSources() {
            return Lists.newArrayList(new FileDocumentSource(ontologyDocument));
        }

        @Override
        public OWLOntologyIRIMapper getOntologyIRIMapper() {
            return EMPTY_IRI_MAPPER;
        }

        @Override
        public void cleanUpTemporaryFiles() {
        }
    }
}
