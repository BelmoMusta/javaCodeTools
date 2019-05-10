package musta.belmo.javacodetools.service.util;

import javafx.scene.control.TextInputControl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileReaderTools {

    public static void readFileToNode(File openedFile, TextInputControl node) throws IOException {
        if(openedFile!=null) {
            readFileToNode(openedFile.getAbsolutePath(), node);
        }
    }

    public static void readFileToNode(String filePath, TextInputControl node) throws IOException {
        final File openedFile = new File(filePath);
        String content = FileUtils.readFileToString(openedFile, "UTF-8");
        node.setText(content);
    }
}
