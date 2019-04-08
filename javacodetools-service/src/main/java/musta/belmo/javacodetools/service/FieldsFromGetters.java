package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import musta.belmo.javacodetools.service.visitor.FieldsFromGettersVisitor;

/**
 * Generates Fields from gettes
 *
 * @since 0.0.0.SNAPSHOT
 * @author default author
 * @version 0.0.0
 */
public class FieldsFromGetters extends AbstractJavaCodeTools {

    /**
     * TODO: Complete the description of this method
     *
     * @param compilationUnitSrc {@link CompilationUnit}
     * @return CompilationUnit
     */
    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();
        compilationUnit.accept(new FieldsFromGettersVisitor(), compilationUnit);
        return compilationUnit;
    }
}
