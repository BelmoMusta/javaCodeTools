package musta.belmo.javacodetools.service.controls;

import javafx.scene.control.MenuItem;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class MenuItemWithIcon extends MenuItem {

    /**
     * The MenuItemWithIcon class constructor.
     *
     * @param text{@link String}
     */
    public MenuItemWithIcon(String text) {
        super(text);
    }

    /**
     * The MenuItemWithIcon class constructor.
     *
     * @param text{@link String}
     * @param icon{@link String}
     */
    public MenuItemWithIcon(String text, String icon) {
        super(text, FontIcon.of(FontAwesome.findByDescription(icon)));
    }
}
