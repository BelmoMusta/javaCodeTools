package musta.belmo.javacodetools.service;

import musta.belmo.javacodetools.service.annotations.Pane;
import musta.belmo.javacodetools.service.annotations.TextField;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
@Pane
public class TextFieldFormController {

    /**
     * The {@link #name} attribute.
     */
    @TextField(name = "name", label = "Name")
    private String name;

    /**
     * The {@link #value} attribute.
     */
    private String value;

    /**
     * @return Attribut {@link #name}
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Value to be assigned to the {@link #name} attribute.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Attribut {@link #value}
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value Value to be assigned to the {@link #value} attribute.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
