package musta.belmo.javacodetools.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class InterfaceDeriver {


    public CompilationUnit deriveInterfaceFromClass(CompilationUnit src, String interfaceName) {
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
            classOrInterfaceDeclaration.setName(Optional.ofNullable(interfaceName)
                    .filter(StringUtils::isNotBlank).orElse("UnnamedInterface"));
            for (MethodDeclaration methodDeclaration : classOrInterfaceDeclaration.findAll(MethodDeclaration.class)) {
                Optional<AnnotationExpr> override = methodDeclaration.getAnnotationByName("Override");
                if (override.isPresent()) {
                    override.get().remove();
                }
                methodDeclaration.removeJavaDocComment();
                methodDeclaration.setBody(null);
                methodDeclaration.setPublic(false);
            }

            deleteFields(classOrInterfaceDeclaration);
        }
        return lRet;
    }


    private void deleteFields(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        classOrInterfaceDeclaration.getMembers().removeIf(member -> member instanceof FieldDeclaration);
    }

    public CompilationUnit deriveInterfaceFromClass(String src, String interfaceName) {

        return deriveInterfaceFromClass(JavaParser.parse(src), interfaceName);
    }
}
