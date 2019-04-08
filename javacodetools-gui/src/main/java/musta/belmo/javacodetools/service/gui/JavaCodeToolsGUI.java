package musta.belmo.javacodetools.service.gui;

import java.net.URL;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class JavaCodeToolsGUI extends AbstractJavaFXApplication {

    /**
     * {@inheritDoc}
     */
    @Override
    public URL loadFXMLFile() {
        return JavaCodeToolsGUI.class.getClassLoader().getResource("window-fx.fxml");
    }
}
