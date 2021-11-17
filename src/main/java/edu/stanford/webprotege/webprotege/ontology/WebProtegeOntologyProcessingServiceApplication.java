package edu.stanford.webprotege.webprotege.ontology;

import edu.stanford.protege.webprotege.ipc.WebProtegeIpcApplication;
import io.minio.MinioClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(WebProtegeIpcApplication.class)
public class WebProtegeOntologyProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebProtegeOntologyProcessingServiceApplication.class, args);
	}

	@Bean
	MinioProperties minioProperties() {
		return new MinioProperties();
	}

	@Bean
	MinioClient minioClient(MinioProperties properties) {
		return MinioClient.builder()
				.credentials(properties.getAccessKey(), properties.getSecretKey())
				.endpoint(properties.getEndPoint())
						  .build();
	}

	@Bean
	ZipInputStreamChecker zipInputStreamChecker() {
		return new ZipInputStreamChecker();
	}

	@Bean
	ZipArchiveOntologyDocumentsExtractor zipArchiveProjectSourcesExtractor() {
		return new ZipArchiveOntologyDocumentsExtractor();
	}

	@Bean
	SingleOntologyDocumentExtractor singleOntologyDocumentExtractor() {
		return new SingleOntologyDocumentExtractor();
	}

	@Bean
	UploadedOntologyDocumentsExtractor uploadedOntologyDocumentsExtractor(ZipInputStreamChecker p1,
																		  ZipArchiveOntologyDocumentsExtractor p2,
																		  SingleOntologyDocumentExtractor p3) {
		return new UploadedOntologyDocumentsExtractor(p1, p2, p3);
	}

	@Bean
	ProcessedOntologyStorer processedOntologyStorer(MinioClient minioClient) {
		return new ProcessedOntologyStorer(minioClient);
	}

	@Bean
	UploadedOntologyDocumentsProcessor uploadedOntologyDocumentsProcessor(OWLOntologyManagerFactory p1,
																		  UploadedOntologyDocumentsExtractor p2,
																		  ProcessedOntologyStorer p3, MinioClient p4) {
		return new UploadedOntologyDocumentsProcessor(p1, p2, p3, p4);
	}

	@Bean
	OWLOntologyManagerFactory ontologyManagerFactory() {
		return new OWLOntologyManagerFactory();
	}

}
