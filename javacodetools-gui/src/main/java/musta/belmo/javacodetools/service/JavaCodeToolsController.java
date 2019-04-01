package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextInputDialog;
import musta.belmo.javacodetools.service.controls.CustomButton;
import musta.belmo.javacodetools.service.controls.MustaPane;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class JavaCodeToolsController {
    @FXML
    public MustaPane mustaPane;

    @FXML
    public void initialize() {
        CustomButton generateMapper = mustaPane.addButton("generate mapper", "fa-save", "Generate mapper");
        CustomButton deriveInterface = mustaPane.addButton("derive interface", "fa-fire", "Derive interface");
        CustomButton generateOnDemandeHolder = mustaPane.addButton("ODH pattern", "fa-fire", "Generate ODH pattern");
        CustomButton generateSafeAccessor = mustaPane.addButton("safe accessors", "fa-fire", "Generate safe accessors");
        CustomButton generateFactory = mustaPane.addButton("Generate Factory", "fa-fire", "Generate Factory");
        CustomButton generateFieldFromGetter = mustaPane.addButton("Generate Fields from getters", "fa-fire", "Generate Fileds");


        bindServiceTuButton(deriveInterface, new InterfaceDeriver());
        bindServiceTuButton(generateFactory, new FactoryCreator());
        bindServiceTuButton(generateFieldFromGetter, new FieldsFromGetters());


        bindServiceTuButton(generateMapper, event -> {
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
            mappingGenerator.createMapper();
            mustaPane.setText(mappingGenerator.getResult());
        });

        bindServiceTuButton(generateOnDemandeHolder, new GenerateOnDemandHolderPattern());
        bindServiceTuButton(generateSafeAccessor, new ObjectSafeAccessor());

        mustaPane.addMenuGroup("File");
        mustaPane.addMenuItemToGroup("Save", "File");
    }

    private void bindServiceTuButton(CustomButton generateFieldFromGetter, AbstractJavaCodeTools toolService) {
        generateFieldFromGetter.setOnAction(event -> {
            CompilationUnit compilationUnit = toolService
                    .generate(mustaPane.getTextArea().getText());
            mustaPane.setText(compilationUnit);
        });
    }

    private void bindServiceTuButton(CustomButton generateFieldFromGetter, EventHandler<ActionEvent> event) {
        generateFieldFromGetter.setOnAction(event);
    }
}
