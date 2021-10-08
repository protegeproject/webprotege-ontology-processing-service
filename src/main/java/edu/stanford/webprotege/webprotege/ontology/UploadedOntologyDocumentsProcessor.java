package edu.stanford.webprotege.webprotege.ontology;

import edu.stanford.protege.webprotege.common.BlobLocation;
import io.minio.DownloadObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-09-28
 * <p>
 * Loads {@link Ontology} objects from a blob containing ontology documents
 */
public class UploadedOntologyDocumentsProcessor {

    @Nonnull
    private final OWLOntologyManagerFactory ontologyManagerFactory;

    @Nonnull
    private final UploadedOntologyDocumentsExtractor uploadedOntologyDocumentsExtractor;

    @Nonnull
    private final ProcessedOntologyStorer processedOntologyStorer;

    private final MinioClient minioClient;

    @Inject
    public UploadedOntologyDocumentsProcessor(@Nonnull OWLOntologyManagerFactory ontologyManagerFactory,
                                              @Nonnull UploadedOntologyDocumentsExtractor uploadedOntologyDocumentsExtractor,
                                              @Nonnull ProcessedOntologyStorer processedOntologyStorer,
                                              @Nonnull MinioClient minioClient) {
        this.ontologyManagerFactory = ontologyManagerFactory;
        this.uploadedOntologyDocumentsExtractor = uploadedOntologyDocumentsExtractor;
        this.processedOntologyStorer = processedOntologyStorer;
        this.minioClient = minioClient;
    }

    /**
     * Load the ontologies from a blob at the specified location
     * @param location The location of the blob containing ontologies
     * @return The collection of ontologies that were loaded from the blob
     * @throws OntologyProcessingException if there was a problem at any stage.  This includes problems downloading
     * the blob, problems extracting ontology documents from the blob and, problems loading ontologies from
     * the ontology documents.  See the cause will hold the actual underlying cause of the exception.
     */
    @Nonnull
    public Collection<BlobLocation> processOntologies(@Nonnull BlobLocation location) throws OntologyProcessingException {
        try {
            var pathToBlob = downloadOntologyDocumentsBlob(location);
            var ontologyDocuments = extractOntologyDocumentsFromBlob(pathToBlob);
            var loadedOwlOntologies = loadOntologyDocuments(ontologyDocuments);
            ontologyDocuments.cleanUpTemporaryFiles();
            return loadedOwlOntologies.stream()
                    .map(processedOntologyStorer::storeOntology)
                    .collect(toList());
        } catch (IOException e) {
            throw new OntologyProcessingException(e);
        }

    }

    private Collection<OWLOntology> loadOntologyDocuments(OntologyDocumentsCollection ontologyDocuments) throws OntologyProcessingException {
        var manager = ontologyManagerFactory.createOwlOntologyManager();
        var loaderConfig = new OWLOntologyLoaderConfiguration()
                // See https://github.com/protegeproject/webprotege/issues/700
                .setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
        var loader = new OntologyDocumentsCollectionLoader();
        loader.loadOntologies(ontologyDocuments, manager, loaderConfig);
        return manager.getOntologies();
    }

    private Ontology toOntology(OWLOntology ont) {
        return Ontology.get(ont.getOntologyID(), ont.getImportsDeclarations(), ont.getAnnotations(), ont.getAxioms());
    }

    private Path downloadOntologyDocumentsBlob(BlobLocation location) {
        var destinationPath = createTempFile();
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                                                         .filename(destinationPath.toString())
                                                         .bucket(location.bucket())
                                                         .object(location.name())
                                                         .build());

            return destinationPath;
        } catch (ErrorResponseException | XmlParserException | ServerException | NoSuchAlgorithmException | IOException | InvalidResponseException | InvalidKeyException | InternalException | InsufficientDataException e) {
            throw new OntologyProcessingException(e);
        }
    }

    private OntologyDocumentsCollection extractOntologyDocumentsFromBlob(Path pathToBlob) {
        try {
            return uploadedOntologyDocumentsExtractor.extractOntologyDocuments(pathToBlob);
        } catch (IOException e) {
            throw new OntologyProcessingException(e);
        }
    }


    private Path createTempFile() {
        try {
            return Files.createTempFile("webprotege-ontology-source", null);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
