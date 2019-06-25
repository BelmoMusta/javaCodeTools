package musta.belmo.javacodetools.service;

import java.io.File;
import java.io.IOException;

public interface CodeTransformer extends Transformer<File, Void> {
    Void transform(File file) throws IOException;
}
