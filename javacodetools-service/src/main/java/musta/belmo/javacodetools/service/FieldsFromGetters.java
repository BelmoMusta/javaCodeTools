package musta.belmo.javacodetools.service;


import com.github.javaparser.ast.CompilationUnit;
import musta.belmo.javacodetools.service.visitor.FieldsFromGettersVisitor;

/**
 * Generates Fields from gettes
 */
public class FieldsFromGetters extends AbstractJavaCodeTools {

    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();
        compilationUnit.accept(new FieldsFromGettersVisitor(),compilationUnit);
        return compilationUnit;

    }


}
