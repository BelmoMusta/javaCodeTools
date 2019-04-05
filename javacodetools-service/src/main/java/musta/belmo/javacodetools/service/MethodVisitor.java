package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodVisitor extends VoidVisitorAdapter<Void> {
    @Override
    public void visit(MethodDeclaration methodDeclaration, Void arg) {
        System.out.println(methodDeclaration);
        super.visit(methodDeclaration, arg);
    }
}
