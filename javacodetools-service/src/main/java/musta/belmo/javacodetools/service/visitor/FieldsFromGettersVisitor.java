package musta.belmo.javacodetools.service.visitor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import musta.belmo.javacodetools.service.CodeUtils;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class FieldsFromGettersVisitor extends VoidVisitorAdapter<CompilationUnit> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration aClass, CompilationUnit compilationUnit) {
        aClass.setInterface(false);
        aClass.setName(aClass.getNameAsString() + "Impl");
        CodeUtils.reversedStream(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_GETTER).forEach(aMethod -> {
            aMethod.setPublic(true);
            String methodName = aMethod.getName().toString().substring(3);
            FieldDeclaration fieldDeclaration = CodeUtils.newField(aMethod.getType(), "a" + methodName, Modifier.PRIVATE);
            aClass.getMembers().add(0, fieldDeclaration);
            ReturnStmt returnStmt = new ReturnStmt(fieldDeclaration.getVariable(0).getNameAsExpression());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt.addStatement(returnStmt);
            aMethod.setBody(blockStmt);
            aClass.getMethodsByName("set" + methodName).forEach(setterMethod -> {
                BlockStmt setterBlockStmt = new BlockStmt();
                Expression assignStmt = new AssignExpr(fieldDeclaration.getVariables().get(0).getNameAsExpression(), setterMethod.getParameter(0).getNameAsExpression(), AssignExpr.Operator.ASSIGN);
                setterBlockStmt.addStatement(assignStmt);
                setterMethod.setBody(setterBlockStmt);
            });
        });
        CodeUtils.reversedStream(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_BOOLEAN_ACCESSOR).forEach(booleanAccessor -> {
            booleanAccessor.setPublic(true);
            String methodName = booleanAccessor.getNameAsString().substring(2);
            FieldDeclaration fieldDeclaration = CodeUtils.newField(booleanAccessor.getType(), "a" + methodName, Modifier.PRIVATE);
            ReturnStmt returnStmt = new ReturnStmt(fieldDeclaration.getVariable(0).getNameAsExpression());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt.addStatement(returnStmt);
            booleanAccessor.setBody(blockStmt);
        });
        CodeUtils.reversedStream(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_NORMAL_METHOD).forEach(method -> {
            method.setPublic(true);
            BlockStmt body = new BlockStmt();
            ThrowStmt throwExpression = new ThrowStmt();
            ObjectCreationExpr expression = new ObjectCreationExpr();
            expression.setType("UnsupportedOperationException");
            throwExpression.setExpression(expression);
            body.addStatement(throwExpression);
            method.setBody(body);
        });
        super.visit(aClass, compilationUnit);
    }
}
