package musta.belmo.javacodetools.service.controls;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class CustomButton extends Button {

    /**
     * Default Constructor.
     */
    public CustomButton() {
    // Default Constructor.
    }

    /**
     * The CustomButton class constructor.
     *
     * @param text{@link String}
     */
    public CustomButton(String text) {
        super(text);
    }

    /**
     * The CustomButton class constructor.
     *
     * @param text{@link String}
     * @param graphic{@link Node}
     */
    public CustomButton(String text, Node graphic) {
        super(text, graphic);
    }

    /**
     * The CustomButton class constructor.
     *
     * @param text{@link String}
     * @param graphic{@link String}
     */
    public CustomButton(String text, String graphic) {
        this(text);
        setGraphic(graphic);
    }

    /**
     * The CustomButton class constructor.
     *
     * @param text{@link String}
     * @param graphic{@link String}
     * @param toolTip{@link String}
     */
    public CustomButton(String text, String graphic, String toolTip) {
        this(text);
        setGraphic(graphic);
        setTooltip(toolTip);
    }

    /**
     * Disable when
     *
     * @param booleanBinding {@link BooleanBinding}
     */
    public void disableWhen(BooleanBinding booleanBinding) {
        disableProperty().bind(booleanBinding);
    }

    /**
     * @param iconName Value to be assigned to the {@link #graphic} attribute.
     */
    public void setGraphic(String iconName) {
        FontIcon fontIcon = FontIcon.of(FontAwesome.findByDescription(iconName));
        setGraphic(fontIcon);
    }

    /**
     * @param tooltip Value to be assigned to the {@link #tooltip} attribute.
     */
    public void setTooltip(String tooltip) {
        setTooltip(new Tooltip(tooltip));
    }
}
