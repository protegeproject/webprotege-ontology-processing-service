package edu.stanford.webprotege.webprotege.ontology;

import org.apache.commons.io.FileUtils;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.io.OWLOntologyDocumentSource;
import org.semanticweb.owlapi.model.OWLOntologyIRIMapper;
import org.semanticweb.owlapi.util.AutoIRIMapper;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
@Component
public class ZipArchiveOntologyDocumentsExtractor implements OntologyDocumentsExtractor {

    public ZipArchiveOntologyDocumentsExtractor() {
    }

    @Override
    public OntologyDocumentsCollection extractOntologyDocuments(Path documentsBlob) throws IOException {
        return extractZipFile(documentsBlob);
    }


    private OntologyDocumentsCollection extractZipFile(Path zipFile) throws IOException {
        var tempDirectory = Files.createTempDirectory("webprotege-extracted-project-sources");
        ZipFileExtractor extractor = new ZipFileExtractor();
        extractor.extractFileToDirectory(zipFile, tempDirectory);
        return new DirectoryBasedOntologyDocumentsCollection(tempDirectory);
    }

    private static class DirectoryBasedOntologyDocumentsCollection implements OntologyDocumentsCollection {

        private final Path baseDirectory;

        private DirectoryBasedOntologyDocumentsCollection(Path baseDirectory) {
            this.baseDirectory = baseDirectory;
        }

        @Override
        public Collection<OWLOntologyDocumentSource> getDocumentSources() {
            try {
                var files = Files.walk(baseDirectory, Integer.MAX_VALUE);
                return files.map(Path::toFile)
                        .filter(file -> file.getName().endsWith(".owl"))
                        .map(FileDocumentSource::new)
                        .collect(Collectors.toList());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public OWLOntologyIRIMapper getOntologyIRIMapper() {
            return new AutoIRIMapper(baseDirectory.toFile(), true);
        }

        @Override
        public void cleanUpTemporaryFiles() throws IOException {
            FileUtils.deleteDirectory(baseDirectory.toFile());
        }


    }

}
