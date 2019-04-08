package musta.belmo.javacodetools.service.visitor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.description.JavadocDescription;
import com.github.javaparser.javadoc.description.JavadocSnippet;
import musta.belmo.javacodetools.service.CodeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class InterfaceImplementationVisitor extends VoidVisitorAdapter<CompilationUnit> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration aClass, CompilationUnit arg) {
        if (aClass.isInterface()) {
            setupClassImplementation(aClass);
            setupGetters(aClass);
            setupSetters(aClass);
            setupBooleanGetters(aClass);
            setupOtherMethods(aClass);
            setupOtherVoidMethods(aClass);
            setupJavaDoc(aClass);
            setupOverrideAnnotation(aClass);
        }
        super.visit(aClass, arg);
    }

    /**
     * @param aClass Value to be assigned to the {@link #upBooleanGetters} attribute.
     */
    private void setupBooleanGetters(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_BOOLEAN_ACCESSOR).forEach(aMethod -> {
            String methodName = aMethod.getName().toString().substring(2);
            FieldDeclaration fieldDeclaration = CodeUtils.newField(aMethod.getType(), "a" + methodName, Modifier.PRIVATE);
            ReturnStmt returnStmt = new ReturnStmt(fieldDeclaration.getVariable(0).getNameAsExpression());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt.addStatement(returnStmt);
            aMethod.setBody(blockStmt);
        });
    }

    /**
     * @param aClass Value to be assigned to the {@link #upGetters} attribute.
     */
    private void setupGetters(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_GETTER).forEach(aMethod -> {
            String methodName = aMethod.getName().toString().substring(3);
            FieldDeclaration fieldDeclaration = CodeUtils.newField(aMethod.getType(), "a" + methodName, Modifier.PRIVATE);
            NodeList<BodyDeclaration<?>> members = aClass.getMembers();
            if (!members.contains(fieldDeclaration)) {
                members.add(0, fieldDeclaration);
            }
            ReturnStmt returnStmt = new ReturnStmt(fieldDeclaration.getVariable(0).getNameAsExpression());
            BlockStmt blockStmt = new BlockStmt();
            blockStmt.addStatement(returnStmt);
            aMethod.setBody(blockStmt);
        });
    }

    /**
     * @param aClass Value to be assigned to the {@link #upClassImplementation} attribute.
     */
    private void setupClassImplementation(ClassOrInterfaceDeclaration aClass) {
        aClass.setInterface(false);
        aClass.addImplementedType(aClass.getNameAsString());
        aClass.setName(aClass.getName() + "Impl");
    }

    /**
     * @param aClass Value to be assigned to the {@link #upSetters} attribute.
     */
    private void setupSetters(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_SETTER).forEach(setterMethod -> {
            NodeList<BodyDeclaration<?>> members = aClass.getMembers();
            String methodName = setterMethod.getName().toString().substring(3);
            BlockStmt blockStmt = new BlockStmt();
            if (setterMethod.getParameters().size() == 1) {
                Parameter parameter = setterMethod.getParameter(0);
                FieldDeclaration fieldDeclaration = CodeUtils.newField(parameter.getType(), "a" + methodName, Modifier.PRIVATE);
                if (!members.contains(fieldDeclaration)) {
                    members.add(0, fieldDeclaration);
                }
                NameExpr paramName = parameter.getNameAsExpression();
                String fieldName = "a" + methodName;
                if (fieldName.equals(paramName.getNameAsString())) {
                    paramName.setName("p" + StringUtils.capitalize(paramName.getNameAsString()));
                }
                Expression assign = new AssignExpr(new NameExpr(fieldName), paramName, AssignExpr.Operator.ASSIGN);
                blockStmt.addStatement(assign);
            } else {
                blockStmt.setBlockComment("TODO");
            }
            setterMethod.setBody(blockStmt);
        });
    }

    /**
     * @param aClass Value to be assigned to the {@link #upOtherMethods} attribute.
     */
    private void setupOtherMethods(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_NORMAL_METHOD.and(CodeUtils.IS_VOID.negate())).forEach(setterMethod -> {
            BlockStmt blockStmt = new BlockStmt();
            setterMethod.setBody(blockStmt);
            Type type = setterMethod.getType();
            final Expression expression;
            if (type.isPrimitiveType()) {
                String defaultValue = CodeUtils.getTypeDefaultValue(type.asPrimitiveType());
                expression = new NameExpr(defaultValue);
            } else {
                expression = new NullLiteralExpr();
            }
            blockStmt.addStatement(new ReturnStmt(expression));
        });
    }

    /**
     * @param aClass Value to be assigned to the {@link #upOtherVoidMethods} attribute.
     */
    private void setupOtherVoidMethods(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).filter(CodeUtils.IS_NORMAL_METHOD).filter(CodeUtils.IS_VOID).forEach(setterMethod -> {
            BlockStmt blockStmt = new BlockStmt();
            setterMethod.setBody(blockStmt);
            ObjectCreationExpr objectCreationExpr = new ObjectCreationExpr();
            objectCreationExpr.setType("UnsupportedOperationException");
            objectCreationExpr.addArgument(new StringLiteralExpr("FIXME : Not implemented yet"));
            ThrowStmt throwStmt = new ThrowStmt(objectCreationExpr);
            blockStmt.addStatement(throwStmt);
        });
    }

    /**
     * @param aClass Value to be assigned to the {@link #upJavaDoc} attribute.
     */
    private void setupJavaDoc(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).forEach(methodDeclaration -> {
            JavadocDescription javadocDescription = new JavadocDescription();
            Javadoc javadoc = new Javadoc(javadocDescription);
            JavadocSnippet inheritDocSnippet = new JavadocSnippet("{@inheritDoc}");
            methodDeclaration.removeJavaDocComment();
            javadocDescription.addElement(inheritDocSnippet);
            methodDeclaration.setJavadocComment(javadoc);
        });
    }

    /**
     * @param aClass Value to be assigned to the {@link #upOverrideAnnotation} attribute.
     */
    private void setupOverrideAnnotation(ClassOrInterfaceDeclaration aClass) {
        CodeUtils.reverse(aClass.findAll(MethodDeclaration.class).stream()).forEach(methodDeclaration -> {
            methodDeclaration.addModifier(Modifier.PUBLIC);
            methodDeclaration.addAnnotation(new MarkerAnnotationExpr("Override"));
        });
    }
}
