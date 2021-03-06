package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import musta.belmo.javacodetools.service.visitor.TestMethodsVisitor;

import java.util.List;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class TestGenerator extends AbstractJavaCodeTools {

    /**
     * {@inheritDoc}
     */
    @Override
    public CompilationUnit generate(CompilationUnit compilationUnit) {
        final CompilationUnit resultUnit = new CompilationUnit();
        List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        resultUnit.addImport("org.junit.Test");
        classes.forEach(aClass -> {
            ClassOrInterfaceDeclaration destClass = resultUnit.addClass(aClass.getNameAsString() + "Test");
            aClass.accept(new TestMethodsVisitor(), destClass);
        });
        return resultUnit;
    }
}
