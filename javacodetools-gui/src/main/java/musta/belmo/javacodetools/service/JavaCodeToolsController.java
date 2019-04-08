package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import musta.belmo.javacodetools.service.controls.CustomButton;
import musta.belmo.javacodetools.service.controls.MustaPane;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class JavaCodeToolsController implements ButtonBinder {
    @FXML
    public MustaPane mustaPane;

    @FXML
    public void initialize() {
        CustomButton generateMapper = mustaPane.addButton("generate mapper", "fa-save", "Generate mapper");
        CustomButton deriveInterface = mustaPane.addButton("derive interface", "fa-fire", "Derive interface");
        CustomButton generateOnDemandHolder = mustaPane.addButton("ODH pattern", "fa-fire", "Generate ODH pattern");
        CustomButton generateSafeAccessor = mustaPane.addButton("safe accessors", "fa-fire", "Generate safe accessors");

        CustomButton generateFieldFromGetter = mustaPane.addButton("Generate Fields from getters", "fa-fire", "Generate Fileds");
        CustomButton generateTests = mustaPane.addButton("Generate tests for this class", "fa-fire", "Generate Tests");
        CustomButton generateImplementation = mustaPane.addButton("Generate the implementation class", "fa-fire", "Generate Tests");

        bindServiceTuButton(deriveInterface, new InterfaceDeriver());
        bindServiceTuButton(generateFieldFromGetter, new FieldsFromGetters());
        bindServiceTuButton(generateTests, new TestGenerator());
        bindServiceTuButton(generateImplementation, new InterfaceImplementation());


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

        bindServiceTuButton(generateOnDemandHolder, new GenerateOnDemandHolderPattern());
        bindServiceTuButton(generateSafeAccessor, new ObjectSafeAccessor());

        mustaPane.addMenuGroup("File");
        mustaPane.addMenuItemToGroup("Save", "File");
    }

    @Override
    public void bindServiceTuButton(Button button, AbstractJavaCodeTools toolService) {
        button.setOnAction(event -> {
            CompilationUnit compilationUnit = toolService
                    .generate(mustaPane.getTextArea().getText());
            mustaPane.setText(compilationUnit);
        });
    }

    @Override
    public void bindServiceTuButton(Button button, EventHandler<ActionEvent> event) {
        button.setOnAction(event);
    }
}
