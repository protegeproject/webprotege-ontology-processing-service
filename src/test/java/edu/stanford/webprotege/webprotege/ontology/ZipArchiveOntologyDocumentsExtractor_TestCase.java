package edu.stanford.webprotege.webprotege.ontology;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@SpringBootTest
public class ZipArchiveOntologyDocumentsExtractor_TestCase {

    @TempDir
    Path tempDir;


    @Test
    public void shouldExtractZipFile() throws IOException {
        var document = "/ontologies/root-ontology.owl";
        var zipFile = createZipFile(document);
        var extractor = new ZipArchiveOntologyDocumentsExtractor();
        var projectSources = extractor.extractOntologyDocuments(zipFile);
        var documentSources = projectSources.getDocumentSources();
        assertThat(documentSources).hasSize(1);
//        assertThat(documentSources, hasItem(isFileDocumentSourceForFile(expectedDocumentFile)));
    }

    public Path createZipFile(String document) throws IOException {
        var zipFile = Files.createTempFile(tempDir, null, null).toFile();
        OutputStream out = new FileOutputStream(zipFile);
        var zipOutputStream = new ZipOutputStream(out);
        var entryA = new ZipEntry(document);
        entryA.setSize(1);
        zipOutputStream.putNextEntry(entryA);
        zipOutputStream.write(1);
        zipOutputStream.close();
        return zipFile.toPath();
    }

}
