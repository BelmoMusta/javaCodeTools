package musta.belmo.javacodetools.service.controls;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @version 0.0.0
 * @since 0.0.0.SNAPSHOT
 */
public class MustaPane extends BorderPane {

    /**
     * The {@link #textArea} attribute.
     */
    private TextArea textArea;

    /**
     * The {@link #buttonBox} attribute.
     */
    private HBox buttonBox;

    /**
     * The {@link #menuBar} attribute.
     */
    private MenuBar menuBar;

    /**
     * The MustaPane class constructor.
     */
    public MustaPane() {
        textArea = new TextArea();
        menuBar = new MenuBar();

        final HBox menuBarBox = new HBox(menuBar);
        buttonBox = new HBox();
        final VBox top = new VBox(menuBarBox,buttonBox);
        setTop(top);
        setCenter(textArea);
    }

    /**
     * Add menu group
     *
     * @param name {@link String}
     * @return Menu
     */
    public Menu addMenuGroup(String name) {
        Menu menu = new Menu(name);
        menu.setId(name);
        menuBar.getMenus().add(menu);
        return menu;
    }

    /**
     * Add menu group
     *
     * @param menu {@link Menu}
     * @return Menu
     */
    public Menu addMenuGroup(Menu menu) {
        menuBar.getMenus().add(menu);
        return menu;
    }

    /**
     * Add menu item to group
     *
     * @param itemName  {@link String}
     * @param icon      {@link String}
     * @param groupName {@link String}
     * @return MenuItem
     */
    public MenuItem addMenuItemToGroup(String itemName, String icon, Menu groupName) {

        MenuItemWithIcon menuItem;
        if (icon != null) {
            menuItem = new MenuItemWithIcon(itemName, icon);
        } else {
            menuItem = new MenuItemWithIcon(itemName);
        }

        Optional<Menu> first = menuBar.getMenus()
                .stream()
                .filter(menu -> menu.getId().equals(groupName.getId()))
                .findFirst();
        Menu menu = first.orElseGet(() -> addMenuGroup(groupName));
        menu.getItems().add(menuItem);
        return menuItem;
    }

    /**
     * Add menu item to group
     *
     * @param itemName  {@link String}
     * @param groupName {@link String}
     * @return MenuItem
     */
    public MenuItem addMenuItemToGroup(String itemName, Menu groupName) {
        return addMenuItemToGroup(itemName, null, groupName);
    }

    /**
     * Add menu item to group
     *
     * @param menuItem  {@link MenuItemWithIcon}
     * @param groupName {@link String}
     * @return MenuItem
     */
    public MenuItem addMenuItemToGroup(MenuItemWithIcon menuItem, String groupName) {
        Optional<Menu> first = menuBar.getMenus().stream().filter(menu -> menu.getText().equals(groupName)).findFirst();
        Menu menu;
        menu = first.orElseGet(() -> addMenuGroup(groupName));
        menu.getItems().add(menuItem);
        return menuItem;
    }


    public void setText(String value) {
        textArea.setText(value);
    }


    public void setText(Object value) {
        textArea.setText(String.valueOf(value));
    }

    /**
     * Add button
     *
     * @param text    {@link String}
     * @param icon    {@link String}
     * @param toolTip {@link String}
     * @return CustomButton
     */
    public CustomButton addButton(String text, String icon, String toolTip) {
        final CustomButton button = new CustomButton(text, icon, toolTip);
        buttonBox.getChildren().add(button);
        return button;
    }

    /**
     * Add button
     *
     * @param button {@link T}
     * @return T
     */
    public <T extends Button> T addButton(T button) {
        buttonBox.getChildren().add(button);
        return button;
    }

    /**
     * @return Attribut {@link #textArea}
     */
    public TextArea getTextArea() {
        return textArea;
    }

    /**
     * @return Attribut {@link #menuBar}
     */
    public MenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * @return Attribut {@link #buttonBox}
     */
    public HBox getButtonBox() {
        return buttonBox;
    }

    /**
     * @param buttonBox Value to be assigned to the {@link #buttonBox} attribute.
     */
    public void setButtonBox(HBox buttonBox) {
        this.buttonBox = buttonBox;
    }

    /**
     * @param menuBar Value to be assigned to the {@link #menuBar} attribute.
     */
    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    /**
     * @param textArea Value to be assigned to the {@link #textArea} attribute.
     */
    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
    }
}
