package edu.stanford.webprotege.webprotege.ontology;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@SpringBootTest
public class SingleDocumentProjectSourcesExtractor_TestCase {

    @Mock
    private File input;

    @Test
    public void shouldExtractProvidedDocument() {
        // Given
        SingleOntologyDocumentExtractor extractor = new SingleOntologyDocumentExtractor();
        // When
        OntologyDocumentsCollection projectSources = extractor.extractOntologyDocuments(input);
        // Then
        Collection<OWLOntologyDocumentSource> documentSources = projectSources.getDocumentSources();
        assertThat(documentSources, hasSize(1));
    }

}
