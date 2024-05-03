package edu.stanford.webprotege.webprotege.ontology;

import edu.stanford.protege.webprotege.common.BlobLocation;
import edu.stanford.protege.webprotege.ipc.CommandHandler;
import edu.stanford.protege.webprotege.ipc.ExecutionContext;
import edu.stanford.protege.webprotege.ipc.WebProtegeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-11-16
 */
@WebProtegeHandler
public class ProcessUploadedOntologiesCommandHandler implements CommandHandler<ProcessUploadedOntologiesRequest, ProcessUploadedOntologiesResponse> {

    private final Logger logger = LoggerFactory.getLogger(ProcessUploadedOntologiesCommandHandler.class);

    private final UploadedOntologyDocumentsProcessor processor;

    private final MinioProperties minioProperties;

    public ProcessUploadedOntologiesCommandHandler(UploadedOntologyDocumentsProcessor processor,
                                                   MinioProperties minioProperties) {
        this.processor = processor;
        this.minioProperties = minioProperties;
    }

    @Nonnull
    @Override
    public String getChannelName() {
        return ProcessUploadedOntologiesRequest.CHANNEL;
    }

    @Override
    public Class<ProcessUploadedOntologiesRequest> getRequestClass() {
        return ProcessUploadedOntologiesRequest.class;
    }

    @Override
    public Mono<ProcessUploadedOntologiesResponse> handleRequest(ProcessUploadedOntologiesRequest request,
                                                                 ExecutionContext executionContext) {
        logger.info("Processing uploaded ontologies (DocumentId: {})", request.fileSubmissionId());
        var uploadLocation = new BlobLocation(minioProperties.getUploadsBucketName(), request.fileSubmissionId().id());
        long t0 = System.currentTimeMillis();
        var processedOntologies = processor.processOntologies(uploadLocation);
        long t1 = System.currentTimeMillis();
        logger.info("Ontologies processed in {} ms", (t1 - t0));
        return Mono.just(new ProcessUploadedOntologiesResponse(new ArrayList<>(processedOntologies)));
    }
}
