package musta.belmo.javacodetools.service;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import musta.belmo.javacodetools.service.visitor.InterfaceImplementationVisitor;

import java.util.Optional;

public class InterfaceImplementation extends AbstractJavaCodeTools {


    @Override
    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();
        Optional<PackageDeclaration> packageDeclaration = compilationUnit.getPackageDeclaration();

        if (packageDeclaration.isPresent()) {
            compilationUnit.setPackageDeclaration(packageDeclaration.map(p -> p.getNameAsString() + ".impl").orElse("impl"));
            compilationUnit.addImport(packageDeclaration.get().getNameAsString());
        }
        compilationUnit.accept(new InterfaceImplementationVisitor(), compilationUnit);
        return compilationUnit;

    }

}
