package musta.belmo.javacodetools.service.gui;

import java.net.URL;

public class MappingGeneratorGUI extends AbstractJavaFXApplication {

@Override
    public URL loadFXMLFile() {
        return MappingGeneratorGUI.class.getClassLoader().getResource("window-fx.fxml");
    }
}
