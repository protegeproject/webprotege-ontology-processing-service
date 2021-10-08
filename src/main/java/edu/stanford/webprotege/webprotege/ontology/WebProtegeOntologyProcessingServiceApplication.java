package edu.stanford.webprotege.webprotege.ontology;

import io.minio.MinioClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WebProtegeOntologyProcessingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebProtegeOntologyProcessingServiceApplication.class, args);
	}

	@Bean
	MinioClient minioClient() {
		return MinioClient.builder()
				.credentials(accessKey, secretKey)
				.endpoint("endpoint")
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

}
