package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import musta.belmo.javacodetools.service.visitor.HibernateVisitor;

import java.util.List;

/**
 * Generates Fields from gettes
 *
 * @author default author
 * @version 0.0.0
 * @since 0.0.0.SNAPSHOT
 */
public class HibernateAnnotationsTransformer extends AbstractJavaCodeTools {

    /**
     * @param compilationUnitSrc {@link CompilationUnit}
     * @return CompilationUnit
     */
    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();

       compilationUnit.accept(new HibernateVisitor(), compilationUnit);
        return compilationUnit;
    }
}
