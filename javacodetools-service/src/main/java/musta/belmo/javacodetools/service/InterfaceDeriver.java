package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;

import java.util.Optional;

public class InterfaceDeriver extends AbstractJavaCodeTools {


    public CompilationUnit generate(CompilationUnit src) {
        CompilationUnit lRet = src.clone();

        for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : lRet.findAll(ClassOrInterfaceDeclaration.class)) {
            classOrInterfaceDeclaration.setInterface(true);
            classOrInterfaceDeclaration.removeModifier(Modifier.FINAL);
            classOrInterfaceDeclaration.getImplementedTypes().clear();
            classOrInterfaceDeclaration.getMembers().removeIf(BodyDeclaration::isConstructorDeclaration);
            classOrInterfaceDeclaration.getMembers().removeIf(BodyDeclaration::isFieldDeclaration);
            classOrInterfaceDeclaration.getMembers().removeIf(BodyDeclaration::isClassOrInterfaceDeclaration);
            classOrInterfaceDeclaration.getMembers().removeIf(dec -> dec.isMethodDeclaration()
                    && (dec.asMethodDeclaration().isStatic() || dec.asMethodDeclaration().isPrivate()));

            classOrInterfaceDeclaration.setName("I" + classOrInterfaceDeclaration.getNameAsString());
            for (MethodDeclaration methodDeclaration : classOrInterfaceDeclaration.findAll(MethodDeclaration.class)) {
                Optional<AnnotationExpr> override = methodDeclaration.getAnnotationByName("Override");
                if (override.isPresent()) {
                    override.get().remove();
                }
                methodDeclaration.removeJavaDocComment();
                methodDeclaration.setBody(null);
                methodDeclaration.setPublic(false);
            }
            classOrInterfaceDeclaration.getImplementedTypes().clear();
            classOrInterfaceDeclaration.getExtendedTypes().clear();
            deleteFields(classOrInterfaceDeclaration);
        }
        return lRet;
    }


    private void deleteFields(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        classOrInterfaceDeclaration.getMembers().removeIf(member -> member instanceof FieldDeclaration);
    }


}
