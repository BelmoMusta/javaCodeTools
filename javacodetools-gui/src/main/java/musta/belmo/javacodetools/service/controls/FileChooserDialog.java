package musta.belmo.javacodetools.service.controls;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

public class FileChooserDialog {
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    private File file;
    private Type type;

    public void setType(Type type) {
        this.type = type;
        if (Type.FILE.equals(type)) {
            fileChooser = new FileChooser();
        } else {
            directoryChooser = new DirectoryChooser();
        }
    }

    public void show() {
        if (Type.FILE.equals(type)) {
            file = fileChooser.showOpenDialog(null);
        } else if (Type.FOLDER.equals(type)) {
            file = directoryChooser.showDialog(null);
        }
    }

    public void addExtensions(String description, String... extensions) {
        final FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter(description, extensions);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
    }

    public Optional<File> get() {
        return Optional.ofNullable(file);
    }

    public enum Type {
        FOLDER, FILE;
    }
}
