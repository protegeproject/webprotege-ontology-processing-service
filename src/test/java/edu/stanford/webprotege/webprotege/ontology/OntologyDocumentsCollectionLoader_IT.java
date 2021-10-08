package edu.stanford.webprotege.webprotege.ontology;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.*;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-29
 */
@SpringBootTest
public class OntologyDocumentsCollectionLoader_IT {

    private OntologyDocumentsCollectionLoader loader;

    private final OWLDataFactory dataFactory = new OWLDataFactoryImpl();

    @TempDir
    Path tmpDir;

    private Path ontologyDocumentA;

    private Path ontologyDocumentB;

    private OWLOntologyManager loadingManager;

    private IRI ontologyAIri;

    private IRI ontologyBIri;

    @BeforeEach
    void setUp() throws IOException, OWLOntologyCreationException, OWLOntologyStorageException {
        loadingManager = OWLManager.createOWLOntologyManager();
        loader = new OntologyDocumentsCollectionLoader();
        ontologyDocumentA = Files.createTempFile(tmpDir, null, null);
        ontologyDocumentB = Files.createTempFile(tmpDir, null, null);

        var ontologyManager = OWLManager.createOWLOntologyManager();
        ontologyAIri = IRI.create("http://exmple.org/ontologyA");
        var ontA = ontologyManager.createOntology(ontologyAIri);
        var clsA = dataFactory.getOWLClass(IRI.create("http://example.org/A"));
        ontologyManager.applyChange(new AddAxiom(ontA, dataFactory.getOWLDeclarationAxiom(clsA)));

        ontologyBIri = IRI.create("http://exmple.org/ontologyB");
        var ontB = ontologyManager.createOntology(ontologyBIri);
        var clsB = dataFactory.getOWLClass(IRI.create("http://example.org/A"));
        ontologyManager.applyChange(new AddAxiom(ontA, dataFactory.getOWLDeclarationAxiom(clsB)));
        ontologyManager.applyChange(new AddImport(ontB, dataFactory.getOWLImportsDeclaration(ontologyAIri)));

        ontologyManager.saveOntology(ontA, Files.newOutputStream(ontologyDocumentA));
        ontologyManager.saveOntology(ontB, Files.newOutputStream(ontologyDocumentB));

    }

    @Test
    void shouldLoadAllSourcesAndResolveImports() throws OWLOntologyCreationException {
        var rawProjectSources = new OntologyDocumentsCollection() {
            @Override
            public Collection<OWLOntologyDocumentSource> getDocumentSources() {
                return Arrays.asList(new FileDocumentSource(ontologyDocumentA.toFile()),
                                     new FileDocumentSource(ontologyDocumentB.toFile()));
            }

            @Override
            public OWLOntologyIRIMapper getOntologyIRIMapper() {
                return new OWLOntologyIRIMapper() {
                    @Nullable
                    @Override
                    public IRI getDocumentIRI(@Nonnull IRI ontologyIRI) {
                        if(ontologyIRI.equals(ontologyAIri)) {
                            return IRI.create(ontologyDocumentA.toFile());
                        }
                        else if(ontologyIRI.equals(ontologyBIri)) {
                            return IRI.create(ontologyDocumentB.toFile());
                        }
                        else {
                            return null;
                        }
                    }
                };
            }

            @Override
            public void cleanUpTemporaryFiles() throws IOException {

            }
        };
        loader.loadOntologies(rawProjectSources, loadingManager, new OWLOntologyLoaderConfiguration());
        var loadedOntologies = loadingManager.getOntologies();
        assertThat(loadedOntologies).hasSize(2);
        var ontologyList = List.copyOf(loadedOntologies);
        var ontologyA = ontologyList.get(0);
        assertThat(ontologyA.getOntologyID()).isEqualTo(new OWLOntologyID(ontologyAIri));
        var ontologyB = ontologyList.get(1);
        assertThat(ontologyB.getOntologyID()).isEqualTo(new OWLOntologyID(ontologyBIri));
        assertThat(loadingManager.getImports(ontologyB)).contains(ontologyA);
        assertThat(loadingManager.getImports(ontologyA)).isEmpty();

    }
}
