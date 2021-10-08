package edu.stanford.webprotege.webprotege.ontology;

import com.google.common.collect.ImmutableSet;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import java.util.Set;

public record Ontology(@Nonnull OWLOntologyID ontologyId,
                       @Nonnull ImmutableSet<OWLImportsDeclaration> importsDeclarations,
                       @Nonnull ImmutableSet<OWLAnnotation> ontologyAnnotations,
                       @Nonnull ImmutableSet<OWLAxiom> ontologyAxioms) {

    public static Ontology get(@Nonnull OWLOntologyID ontologyId,
                               @Nonnull Set<OWLImportsDeclaration> importsDeclarations,
                               @Nonnull Set<OWLAnnotation> ontologyAnnotations,
                               @Nonnull Set<OWLAxiom> ontologyAxioms) {
        return new Ontology(ontologyId,
                                      ImmutableSet.copyOf(importsDeclarations),
                                      ImmutableSet.copyOf(ontologyAnnotations),
                                      ImmutableSet.copyOf(ontologyAxioms));
    }
}
