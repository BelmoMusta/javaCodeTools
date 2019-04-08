package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.Optional;

public class InterfaceDeriver extends AbstractJavaCodeTools {


    public CompilationUnit generate(CompilationUnit srcCompilationUnit) {
        CompilationUnit destCompilationUnit = srcCompilationUnit.clone();
        for (ClassOrInterfaceDeclaration aClass : destCompilationUnit.findAll(ClassOrInterfaceDeclaration.class)) {
            aClass.setInterface(true);
            aClass.removeModifier(Modifier.FINAL);
            aClass.getImplementedTypes().clear();
            NodeList<BodyDeclaration<?>> members = aClass.getMembers();

            members.removeIf(BodyDeclaration::isConstructorDeclaration);
            members.removeIf(BodyDeclaration::isFieldDeclaration);
            members.removeIf(BodyDeclaration::isClassOrInterfaceDeclaration);
            members.removeIf(dec -> dec.isMethodDeclaration()
                    && (dec.asMethodDeclaration().isStatic() || dec.asMethodDeclaration().isPrivate()));

            aClass.setName("I" + aClass.getNameAsString());
            for (MethodDeclaration methodDeclaration : aClass.findAll(MethodDeclaration.class)) {
                Optional<AnnotationExpr> override = methodDeclaration.getAnnotationByName("Override");
                if (override.isPresent()) {
                    override.get().remove();
                }
                methodDeclaration.removeJavaDocComment();
                methodDeclaration.setBody(null);
                methodDeclaration.setPublic(false);
            }
            aClass.getImplementedTypes().clear();
            aClass.getExtendedTypes().clear();
            deleteFields(aClass);
        }
        return destCompilationUnit;
    }


    private void deleteFields(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        classOrInterfaceDeclaration.getMembers().removeIf(member -> member instanceof FieldDeclaration);
    }


}
