package musta.belmo.javacodetools.service.gui;

import java.net.URL;

public class JavaCodeToolsGUI extends AbstractJavaFXApplication {

@Override
    public URL loadFXMLFile() {
        return JavaCodeToolsGUI.class.getClassLoader().getResource("window-fx.fxml");
    }
}
