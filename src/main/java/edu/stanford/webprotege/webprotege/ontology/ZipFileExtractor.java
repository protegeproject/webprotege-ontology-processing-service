package edu.stanford.webprotege.webprotege.ontology;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 19/02/2014
 */
public class ZipFileExtractor {

    public ZipFileExtractor() {
    }

    public void extractFileToDirectory(Path zip, Path outputDirectory) throws IOException {
        ZipFile zipFile = new ZipFile(zip.toFile());
        Enumeration<? extends ZipEntry> zipEntryEnumeration = zipFile.entries();
        while(zipEntryEnumeration.hasMoreElements()) {
            extractZipEntry(outputDirectory, zipFile, zipEntryEnumeration);
        }
    }

    private void extractZipEntry(Path outputDirectory,
                                 ZipFile zipFile,
                                 Enumeration<? extends ZipEntry> zipEntryEnumeration) throws IOException {
        ZipEntry entry = zipEntryEnumeration.nextElement();
        String entryName = entry.getName();
        File entryFile = new File(outputDirectory.toFile(), entryName);
        if(entryName.endsWith("/")) {
            entryFile.mkdirs();
        }
        else {
            File entryParentFile = entryFile.getParentFile();
            entryParentFile.mkdirs();
            BufferedOutputStream entryOutputStream = new BufferedOutputStream(new FileOutputStream(entryFile));
            InputStream entryInputStream = zipFile.getInputStream(entry);
            IOUtils.copy(entryInputStream, entryOutputStream);
            entryInputStream.close();
            entryOutputStream.close();
        }

    }
}
