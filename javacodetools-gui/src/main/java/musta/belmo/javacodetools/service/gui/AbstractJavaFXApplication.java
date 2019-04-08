package musta.belmo.javacodetools.service.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public abstract class AbstractJavaFXApplication extends Application {

    /**
     * Load fxml file
     *
     * @return URL
     */
    public abstract URL loadFXMLFile();

    /**
     * The {@link #LOG} Constant of type {@link Logger} holding the value LoggerFactory.getLogger(AbstractJavaFXApplication.class).
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractJavaFXApplication.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        LOG.info("start ");
        FXMLLoader fxmlLoader = new FXMLLoader(loadFXMLFile());
        primaryStage.setScene(new Scene(fxmlLoader.load(), 800, 500));
        LOG.info("root set ");
        primaryStage.show();
    }
}
