package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.List;

public class TestGenerator extends AbstractJavaCodeTools {


    @Override
    public CompilationUnit generate(CompilationUnit compilationUnit) {
        final CompilationUnit resultUnit = new CompilationUnit();
        List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        classes.forEach(srcClass -> {
            srcClass.accept(new MethodVisitor(), null);
        });
        return resultUnit;
    }

}
