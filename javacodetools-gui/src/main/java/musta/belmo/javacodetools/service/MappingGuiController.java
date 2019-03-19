package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import musta.belmo.javacodetools.service.controls.CustomButton;
import musta.belmo.javacodetools.service.controls.MustaPane;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class MappingGuiController {
    @FXML
    public MustaPane mustaPane;

    @FXML
    public void initialize() {
        CustomButton generateMapper = mustaPane.addButton("generate mapper", "fa-save", "Generate mapper");
        CustomButton deriveInterface = mustaPane.addButton("derive interface", "fa-fire", "Derive interface");
        CustomButton generateOnDemandeHolder = mustaPane.addButton("ODH pattern", "fa-fire", "Generate ODH pattern");
        deriveInterface.setOnAction(event -> {
            InterfaceDeriver interfaceDeriver = new InterfaceDeriver();
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("interface name");
            inputDialog.setHeaderText("Interface");
            inputDialog.setContentText("Enter the interface name");

            Optional<String> stringMyOptional = inputDialog.showAndWait().filter(StringUtils::isNotBlank);

            String iterfaceName = stringMyOptional.orElse("I");
            CompilationUnit compilationUnit = interfaceDeriver
                    .deriveInterfaceFromClass(mustaPane.getTextArea().getText(), iterfaceName);
            mustaPane.setText(compilationUnit);
        });
        generateMapper.setOnAction(event -> {
            MappingGenerator mappingGenerator = new MappingGenerator();
            mappingGenerator.setSource(mustaPane.getTextArea().getText());
            TextInputDialog inputDialog = new TextInputDialog();
            inputDialog.setTitle("Class name");
            inputDialog.setHeaderText("");
            inputDialog.setContentText("Destination class name");
            Optional<String> stringMyOptional = inputDialog.showAndWait().filter(StringUtils::isNotBlank);
            String interfaceName = stringMyOptional.orElse("");

            mappingGenerator.setDestinationClassName(interfaceName);
            mappingGenerator.setDestinationPackage("logic.book");
            mappingGenerator.setMappingMethodPrefix("map");
            mappingGenerator.setMapperClassPrefix("Mapper");
            mappingGenerator.setStaticMethod(true);
            mappingGenerator.setAccessCollectionByGetter(true);
            mappingGenerator.mapField("title", "titre");
            mappingGenerator.createMapper();
            mappingGenerator.createMapper();
            mustaPane.setText(mappingGenerator.getResult());
        });
        generateOnDemandeHolder.setOnAction(event -> {
            GenerateOnDemandeHolderPattern generateOnDemandeHolderPattern = new GenerateOnDemandeHolderPattern();
            CompilationUnit compilationUnit = generateOnDemandeHolderPattern.generate(mustaPane.getTextArea().getText());
            mustaPane.setText(compilationUnit);
        });

        mustaPane.addMenuGroup("File");
        mustaPane.addMenuItemToGroup("Save", "File");
        //

        Binder binder = new Binder();
        TextFieldFormController textFieldFormController = new TextFieldFormController();
        textFieldFormController.setName("a simple name");

        try {
            Pane bind = binder.bind(textFieldFormController);
            mustaPane.getChildren().add(bind);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
