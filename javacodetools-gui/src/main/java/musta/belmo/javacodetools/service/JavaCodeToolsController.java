package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import musta.belmo.javacodetools.service.controls.CustomButton;
import musta.belmo.javacodetools.service.controls.MustaPane;
import musta.belmo.javacodetools.service.gui.PrimaryStageBridge;
import musta.belmo.javacodetools.service.util.FileReaderTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @version 0.0.0
 * @since 0.0.0.SNAPSHOT
 */
public class JavaCodeToolsController implements ButtonBinder, PrimaryStageBridge {

    private static final Logger LOG = LoggerFactory.getLogger(JavaCodeToolsController.class);
    /**
     * The {@link #mustaPane} attribute.
     */
    @FXML
    public MustaPane mustaPane;
    private Stage stage;

    /**
     * TODO: Complete the description of this method
     */
    @FXML
    public void initialize() {
        addMapperGenerator();
        addInterfaceDerivator();
        addOnDemandHolder();
        addSafeAccessorGenerator();
        addFieldsFromGetterGenerator();
        addTestsGenerator();
        addInterfaceImplementorGenerator();
        addMenus();
    }

    private void addMenus() {
        final Menu file = mustaPane.addMenuGroup("File");
        MenuItem open = mustaPane.addMenuItemToGroup("Open", file);
        open.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File openedFile = fileChooser.showOpenDialog(null);
            try {
                if(openedFile!=null) {
                    FileReaderTools.readFileToNode(openedFile, mustaPane.getTextArea());
                    stage.setTitle(String.format("Code tools - %s", openedFile.getAbsolutePath()));
                }

            } catch (IOException e) {
                LOG.error("File not found", e);
            }
        });
    }

    private void addInterfaceImplementorGenerator() {
        CustomButton generateImplementation = mustaPane.addButton("Generate the implementation class", "fa-fire", "Generate Tests");
        bindServiceTuButton(generateImplementation, new InterfaceImplementation());
    }

    private void addTestsGenerator() {
        CustomButton generateTests = mustaPane.addButton("Generate tests for this class", "fa-fire", "Generate Tests");
        bindServiceTuButton(generateTests, new TestGenerator());
    }

    private void addFieldsFromGetterGenerator() {
        CustomButton generateFieldFromGetter = mustaPane.addButton("Generate Fields from getters", "fa-fire", "Generate Fileds");
        bindServiceTuButton(generateFieldFromGetter, new FieldsFromGetters());
    }

    private void addSafeAccessorGenerator() {
        CustomButton generateSafeAccessor = mustaPane.addButton("safe accessors", "fa-fire", "Generate safe accessors");
        bindServiceTuButton(generateSafeAccessor, new ObjectSafeAccessor());
    }

    private void addOnDemandHolder() {
        CustomButton generateOnDemandHolder = mustaPane.addButton("ODH pattern", "fa-fire", "Generate ODH pattern");
        bindServiceTuButton(generateOnDemandHolder, new GenerateOnDemandHolderPattern());
    }

    private void addInterfaceDerivator() {
        CustomButton deriveInterface = mustaPane.addButton("derive interface", "fa-fire", "Derive interface");
        bindServiceTuButton(deriveInterface, new InterfaceDeriver());
    }

    private void addMapperGenerator() {
        CustomButton generateMapper = mustaPane.addButton("generate mapper", "fa-save", "Generate mapper");
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindServiceTuButton(Button button, AbstractJavaCodeTools toolService) {
        button.setOnAction(event -> {
            CompilationUnit compilationUnit = toolService.generate(mustaPane.getTextArea().getText());
            mustaPane.setText(compilationUnit);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void bindServiceTuButton(Button button, EventHandler<ActionEvent> event) {
        button.setOnAction(event);
    }

    @Override
    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }
}
