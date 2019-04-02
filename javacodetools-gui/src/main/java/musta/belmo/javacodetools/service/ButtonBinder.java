package musta.belmo.javacodetools.service;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public interface ButtonBinder {
    void bindServiceTuButton(Button button,
                             AbstractJavaCodeTools toolService);

    void bindServiceTuButton(Button button,
                             EventHandler<ActionEvent> event);

}
