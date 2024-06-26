package edu.stanford.webprotege.webprotege.ontology;

import edu.stanford.protege.webprotege.common.BlobLocation;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.*;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import javax.annotation.Nonnull;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-10-01
 */
public class ProcessedOntologyStorer {

    private final MinioProperties minioProperties;

    private final MinioClient minioClient;

    public ProcessedOntologyStorer(MinioProperties minioProperties, MinioClient minioClient) {
        this.minioProperties = minioProperties;
        this.minioClient = minioClient;
    }

    @Nonnull
    public BlobLocation storeOntology(OWLOntology ontology) throws OntologyProcessingException {
        try {
            var tempFile = createTempFile();
            storeOntologyInBinaryOwlFormat(ontology, tempFile);
            var location = storeFile(tempFile);
            Files.delete(tempFile);
            return location;
        } catch (IOException | OWLOntologyStorageException | XmlParserException | ServerException | NoSuchAlgorithmException | InvalidResponseException | InvalidKeyException | InternalException | InsufficientDataException | ErrorResponseException e) {
            throw new OntologyProcessingException(e);
        }
    }

    private BlobLocation storeFile(Path tempFile) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        var location = generateBlobLocation();
        // Create bucket if necessary
        createBucketIfNecessary(location);
        minioClient.uploadObject(UploadObjectArgs.builder()
                                                 .filename(tempFile.toString())
                                                 .bucket(location.bucket())
                                                 .object(location.name())
                                                 .contentType("application/octet-stream")
                                                 .build());
        return location;
    }

    private void createBucketIfNecessary(BlobLocation location) throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException, InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        if(!minioClient.bucketExists(BucketExistsArgs.builder().bucket(location.bucket()).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(location.bucket()).build());
        }
    }

    private static Path createTempFile() throws IOException {
        return Files.createTempFile("webprotege-", null);
    }

    private static void storeOntologyInBinaryOwlFormat(OWLOntology ontology, Path tempFile) throws IOException, OWLOntologyStorageException {
        var storer = new BinaryOWLOntologyDocumentStorer();
        var outputStream = Files.newOutputStream(tempFile);
        var target = new StreamDocumentTarget(new BufferedOutputStream(outputStream));
        storer.storeOntology(ontology, target, new BinaryOWLOntologyDocumentFormat());
    }

    private BlobLocation generateBlobLocation() {
        return new BlobLocation(minioProperties.getProcessedOntologiesBucketName(), generateObjectName());
    }

    private static String generateObjectName() {
        return "ontology-" + UUID.randomUUID() + ".owl.bin";
    }
}
