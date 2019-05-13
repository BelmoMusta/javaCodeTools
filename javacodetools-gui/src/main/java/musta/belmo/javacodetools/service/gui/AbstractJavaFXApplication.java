package musta.belmo.javacodetools.service.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
 * @version 0.0.0
 * @since 0.0.0.SNAPSHOT
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
    private static final Logger LOG = getLogger(AbstractJavaFXApplication.class);

    public static Logger getLogger(Class cls) {
        return LoggerFactory.getLogger(cls);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        LOG.info("start ");
        final FXMLLoader fxmlLoader = new FXMLLoader(loadFXMLFile());
        final Parent root = fxmlLoader.load();

        final PrimaryStageBridge controller = fxmlLoader.getController();
        controller.setPrimaryStage(primaryStage);
        LOG.info("controller bridge set");
        primaryStage.setScene(new Scene(root, 800, 500));
        LOG.info("root set ");
        primaryStage.show();
    }
}
