package edu.stanford.webprotege.webprotege.ontology;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2021-10-02
 */
@ConfigurationProperties(prefix = "webprotege.minio")
public class MinioProperties {

    private String accessKey;

    private String secretKey;

    private String endPoint;

    private String uploadsBucketName;

    private String processedOntologiesBucketName;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getUploadsBucketName() {
        return uploadsBucketName;
    }

    public void setUploadsBucketName(String uploadsBucketName) {
        this.uploadsBucketName = uploadsBucketName;
    }

    public String getProcessedOntologiesBucketName() {
        return processedOntologiesBucketName;
    }

    public void setProcessedOntologiesBucketName(String processedOntologiesBucketName) {
        this.processedOntologiesBucketName = processedOntologiesBucketName;
    }
}
