package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class TestMethodsVisitor extends VoidVisitorAdapter<ClassOrInterfaceDeclaration> {
    @Override
    public void visit(MethodDeclaration methodDeclaration, ClassOrInterfaceDeclaration arg) {
        if (!methodDeclaration.isPrivate()) {
            MethodDeclaration testMethod = new MethodDeclaration();
            testMethod.setType("void");
            testMethod.setName("test" + CodeUtils.capitalize(methodDeclaration.getNameAsString()));
            AnnotationExpr expr = new MarkerAnnotationExpr();
            expr.setName("Test");
            testMethod.addAnnotation(expr);
            arg.addMember(testMethod);
        }
        super.visit(methodDeclaration, arg);
    }
}
