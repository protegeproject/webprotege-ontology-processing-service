package edu.stanford.webprotege.webprotege.ontology;

import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class OntologyDocumentsCollectionLoader {

    public OntologyDocumentsCollectionLoader() {
    }

    /**
     * Load the collection of ontology documents into the given manager using the specified
     * loader configuration.  All ontology documents in the collection will be parsed and loaded.
     * @param ontologyDocumentsCollection The collection of ontology documents
     * @param manager The manager that ontology will be loaded into
     * @param loaderConfig The configuration for ontology loading
     * @throws OntologyProcessingException if there was a problem loading an ontology
     */
    public void loadOntologies(OntologyDocumentsCollection ontologyDocumentsCollection,
                               OWLOntologyManager manager,
                               OWLOntologyLoaderConfiguration loaderConfig) throws OntologyProcessingException {
        try {
            manager.getIRIMappers().add(ontologyDocumentsCollection.getOntologyIRIMapper());
            for (OWLOntologyDocumentSource documentSource : ontologyDocumentsCollection.getDocumentSources()) {
                manager.loadOntologyFromOntologyDocument(documentSource, loaderConfig);
            }
        }
        catch (Exception e) {
            throw new OntologyProcessingException(e);
        }
        finally {
            manager.getIRIMappers().remove(ontologyDocumentsCollection.getOntologyIRIMapper());
        }
    }

}
