package musta.belmo.javacodetools.service;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import musta.belmo.javacodetools.service.annotations.ComboBox;
import musta.belmo.javacodetools.service.annotations.TextArea;
import musta.belmo.javacodetools.service.annotations.TextField;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class Binder {

    /**
     * TODO: Complete the description of this method
     *
     * @param element {@link T}
     * @return Pane
     * @throws Exception the raised exception if error.
     */
    public <T> Pane bind(T element) throws Exception {
        final Pane pane;
        Class<?> elementClass = element.getClass();
        if (elementClass.isAnnotationPresent(musta.belmo.javacodetools.service.annotations.Pane.class)) {
            pane = new Pane();
            Field[] declaredFields = elementClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                Object o = declaredField.get(element);
                if (declaredField.isAnnotationPresent(TextField.class)) {
                    TextField textFieldAnnotation = declaredField.getAnnotation(TextField.class);
                    String name = textFieldAnnotation.name();
                    javafx.scene.control.TextField textField = new javafx.scene.control.TextField();
                    textField.setId(name);
                    textField.setText(String.valueOf(o));
                    Label label = new Label(textFieldAnnotation.label());
                    pane.getChildren().add(label);
                    pane.getChildren().add(textField);
                } else if (declaredField.isAnnotationPresent(TextArea.class)) {
                    TextArea textAreaAnnotation = declaredField.getAnnotation(TextArea.class);
                    String name = textAreaAnnotation.name();
                    javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea();
                    textArea.setId(name);
                    textArea.setText(String.valueOf(o));
                    Label label = new Label(textAreaAnnotation.label());
                    pane.getChildren().add(label);
                    pane.getChildren().add(textArea);
                } else if (declaredField.isAnnotationPresent(ComboBox.class)) {
                    ComboBox comboBoxAnnotation = declaredField.getAnnotation(ComboBox.class);
                    String name = comboBoxAnnotation.name();
                    javafx.scene.control.ComboBox comboBox = new javafx.scene.control.ComboBox();
                    comboBox.setId(name);
                    Collection collection = (Collection) o;
                    comboBox.getItems().addAll(collection);
                    Label label = new Label(comboBoxAnnotation.label());
                    pane.getChildren().add(label);
                    pane.getChildren().add(comboBox);
                }
            }
        } else
            throw new Exception("the object cannot be bound");
        return pane;
    }
}
