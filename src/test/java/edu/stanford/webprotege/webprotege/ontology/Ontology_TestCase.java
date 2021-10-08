package edu.stanford.webprotege.webprotege.ontology;

import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-08-20
 */
@SpringBootTest
public class Ontology_TestCase {

    private Ontology ontology;

    @Mock
    private OWLOntologyID ontologyId;

    @Mock
    private OWLAnnotation annotation;

    @Mock
    private OWLAxiom axiom;

    @Mock
    private OWLImportsDeclaration importsDeclaration;

    @BeforeEach
    public void setUp() {
        ontology = Ontology.get(ontologyId,
                                ImmutableSet.of(importsDeclaration),
                                ImmutableSet.of(annotation),
                                ImmutableSet.of(axiom));
    }

    @Test
    public void shouldGetSuppliedOntologyId() {
        assertThat(ontology.ontologyId(), is(ontologyId));
    }

    @Test
    public void shouldGetSuppliedImportsDeclaration() {
        assertThat(ontology.importsDeclarations(), contains(importsDeclaration));
    }

    @Test
    public void shouldGetSuppliedAnnotations() {
        assertThat(ontology.ontologyAnnotations(), contains(annotation));
    }

    @Test
    public void shouldGetSuppliedAxioms() {
        assertThat(ontology.ontologyAxioms(), contains(axiom));
    }

}
