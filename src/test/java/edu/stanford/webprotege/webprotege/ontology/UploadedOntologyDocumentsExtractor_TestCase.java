package edu.stanford.webprotege.webprotege.ontology;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@SpringBootTest
public class UploadedOntologyDocumentsExtractor_TestCase {

    @Mock
    private ZipArchiveOntologyDocumentsExtractor zipArchiveOntologyDocumentsExtractor;

    @Mock
    private SingleOntologyDocumentExtractor singleOntologyDocumentExtractor;

    @Mock
    private ZipInputStreamChecker zipInputStreamChecker;

    @Mock
    private Path inputFile;


    @Test
    public void shouldExtractSourcesFromZipFile() throws IOException {
        // Given
        when(zipInputStreamChecker.isZipFile(inputFile)).thenReturn(true);
        UploadedOntologyDocumentsExtractor extractor = new UploadedOntologyDocumentsExtractor(
                zipInputStreamChecker, zipArchiveOntologyDocumentsExtractor, singleOntologyDocumentExtractor
        );
        // When
        extractor.extractOntologyDocuments(inputFile);
        // Then
        verify(zipArchiveOntologyDocumentsExtractor, times(1)).extractOntologyDocuments(inputFile);
    }

    @Test
    public void shouldExtractNonZipFileUsingSingleDocumentExtractor() throws IOException {
        // Given
        when(zipInputStreamChecker.isZipFile(inputFile)).thenReturn(false);
        UploadedOntologyDocumentsExtractor extractor = new UploadedOntologyDocumentsExtractor(
                zipInputStreamChecker, zipArchiveOntologyDocumentsExtractor, singleOntologyDocumentExtractor
        );
        // When
        extractor.extractOntologyDocuments(inputFile);
        // Then
        verify(singleOntologyDocumentExtractor, times(1)).extractOntologyDocuments(inputFile);
    }


}
