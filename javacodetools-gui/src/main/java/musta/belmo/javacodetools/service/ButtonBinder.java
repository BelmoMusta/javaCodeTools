package musta.belmo.javacodetools.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public interface ButtonBinder {

    /**
     * Bind service to button
     *
     * @param button {@link Button}
     * @param toolService {@link AbstractJavaCodeTools}
     */
    void bindServiceTuButton(Button button, AbstractJavaCodeTools toolService);

    /**
     * Bind service to button
     *
     * @param button {@link Button}
     * @param event {@link EventHandler}
     */
    void bindServiceTuButton(Button button, EventHandler<ActionEvent> event);
}
