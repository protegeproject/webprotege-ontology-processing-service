package edu.stanford.webprotege.webprotege.ontology;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Matthew Horridge,
 *         Stanford University,
 *         Bio-Medical Informatics Research Group
 *         Date: 18/02/2014
 */
public class ZipInputStreamChecker {

    public static final char ZIP_FILE_MAGIC_NUMBER_BYTE_0 = 'P';

    public static final char ZIP_FILE_MAGIC_NUMBER_BYTE_1 = 'K';

    @Inject
    public ZipInputStreamChecker() {
    }

    public boolean isZipFile(Path file) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file.toFile()))) {
            return isZipInputStream(bufferedInputStream);
        }
    }

    public boolean isZipInputStream(BufferedInputStream in) throws IOException {
        in.mark(2);
        char ch0 = (char) in.read();
        char ch1 = (char) in.read();
        in.reset();
        return ch0 == ZIP_FILE_MAGIC_NUMBER_BYTE_0 && ch1 == ZIP_FILE_MAGIC_NUMBER_BYTE_1;
    }
}
