package edu.stanford.webprotege.webprotege.ontology;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-29
 */
public class OWLOntologyManagerFactory {

    public OWLOntologyManager createOwlOntologyManager() {
        return OWLManager.createOWLOntologyManager();
    }
}
