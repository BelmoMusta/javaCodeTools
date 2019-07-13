package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ObjectSafeAccessor extends AbstractJavaCodeTools {

    private static final Predicate<MethodDeclaration> IS_PRIVATE = (MethodDeclaration::isPrivate);

    private FieldDeclaration createField(ClassOrInterfaceDeclaration srcClass, ClassOrInterfaceDeclaration destClass) {
        String srcClassName = srcClass.getNameAsString();
        return destClass.addField(srcClassName, String.format("m%s", srcClassName)).
                addModifier(Modifier.PRIVATE, Modifier.FINAL);
    }

    @Override
    public CompilationUnit generate(CompilationUnit compilationUnit) {
        final CompilationUnit resultUnit = new CompilationUnit();
        List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        classes.forEach(srcClass -> {
            ClassOrInterfaceDeclaration destClass = resultUnit.addClass(srcClass.getNameAsString() + "SafeAccessor");

            FieldDeclaration field = createField(srcClass, destClass);
            addConstructor(srcClass, destClass, field);

// add methods
            Predicate<MethodDeclaration> isStatic = MethodDeclaration::isStatic;
            List<MethodDeclaration> methods = srcClass.findAll(MethodDeclaration.class).stream()
                    .filter(isStatic.negate()).collect(Collectors.toList());

            methods.stream()
                    .filter(IS_PRIVATE.negate())
                    .forEach(methodDeclaration -> {
                methodDeclaration.getAnnotationByName("Override").ifPresent(AnnotationExpr::remove);
                MethodDeclaration addedMethod = methodDeclaration.clone();
                destClass.addMember(addedMethod);
                BlockStmt body = new BlockStmt();
                addedMethod.setBody(body);

                if (methodDeclaration.getType().isVoidType()) {
                    addIfStatementForSetters(field, methodDeclaration, body);
                } else {
                    // add variable
                    NameExpr variableName = addVariable(methodDeclaration);
                    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
                    variableDeclarationExpr.setModifiers(EnumSet.of(Modifier.FINAL));
                    VariableDeclarator variableDeclarator = new VariableDeclarator();
                    variableDeclarator.setType(methodDeclaration.getType());
                    variableDeclarator.setName(variableName.getName());
                    variableDeclarationExpr.addVariable(variableDeclarator);
                    body.addStatement(variableDeclarationExpr);
                    //add if statement
                    addIfStatementForGetters(field, methodDeclaration, body, variableName);
                    // add return statement
                    addReturnStatement(body, variableName);
                }
            });
        });
        return resultUnit;
    }

    private NameExpr addVariable(MethodDeclaration methodDeclaration) {
        VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
        variableDeclarationExpr.addModifier(Modifier.FINAL);
        VariableDeclarator variableDeclarator = new VariableDeclarator();

        String methodName = methodDeclaration.getNameAsString();
        String variableName = methodName;
        if (methodName.startsWith("get")) {
            variableName = methodName.substring(3);
        } else if (methodName.toLowerCase().startsWith("is")) {
            variableName = CodeUtils.capitalize(methodName).substring(2);
        }

        variableDeclarator.setName("l" + variableName);
        variableDeclarator.setType(methodDeclaration.getType());
        variableDeclarationExpr.addVariable(variableDeclarator);
        methodDeclaration.getBody().ifPresent(blockStmt -> blockStmt.addStatement(variableDeclarationExpr));

        return variableDeclarator.getNameAsExpression();
    }

    private void addReturnStatement(BlockStmt body, NameExpr variableName) {
        ReturnStmt returnStmt = new ReturnStmt();
        returnStmt.setExpression(variableName);
        body.addStatement(returnStmt);
    }

    private void addIfStatementForGetters(FieldDeclaration field, MethodDeclaration methodDeclaration, BlockStmt body, NameExpr variableName) {
        Expression nullLiteralExpr = new NullLiteralExpr();
        final Expression condition = new BinaryExpr(getFieldAsExpression(field),
                nullLiteralExpr, BinaryExpr.Operator.EQUALS);

        BlockStmt thenStatement = new BlockStmt();
        AssignExpr assignExpr = new AssignExpr(variableName, getDefaultExpression(methodDeclaration.getType()), AssignExpr.Operator.ASSIGN);
        thenStatement.addStatement(assignExpr);

        BlockStmt elseStatement = new BlockStmt();
        MethodCallExpr methodCallExpr = new MethodCallExpr(getFieldAsExpression(field), methodDeclaration.getName());
        AssignExpr elsAssignExpr = new AssignExpr(variableName, methodCallExpr, AssignExpr.Operator.ASSIGN);
        methodCallExpr.setName(methodDeclaration.getName());
        elseStatement.addStatement(elsAssignExpr);

        IfStmt ifStmt = CodeUtils.createIfStamtement(condition, thenStatement, elseStatement);
        body.addStatement(ifStmt);
    }

    private void addIfStatementForSetters(FieldDeclaration field, MethodDeclaration methodDeclaration, BlockStmt body) {

        Expression nullLiteralExpr = new NullLiteralExpr();

        final Expression condition = new BinaryExpr(getFieldAsExpression(field),
                nullLiteralExpr, BinaryExpr.Operator.NOT_EQUALS);

        BlockStmt thenStatement = new BlockStmt();
        MethodCallExpr methodCallExpr = new MethodCallExpr(getFieldAsExpression(field), methodDeclaration.getName());


        List<NameExpr> args = convertParamsToArgs(methodDeclaration);
        methodCallExpr.getArguments().addAll(args);
        methodCallExpr.setName(methodDeclaration.getName());
        thenStatement.addStatement(methodCallExpr);
        IfStmt ifStmt = CodeUtils.createIfStamtement(condition, thenStatement, null);
        body.addStatement(ifStmt);
    }

    private List<NameExpr> convertParamsToArgs(MethodDeclaration methodDeclaration) {
        return methodDeclaration.getParameters()
                .stream()
                .map(parameter -> new NameExpr(parameter.getName()))
                .collect(Collectors.toList());
    }

    private Expression getDefaultExpression(Type type) {
        final Expression expression;

        if ("boolean".equals(type.toString())) {
            expression = new NameExpr("false");
        } else if (type.isPrimitiveType()) {
            expression = new NameExpr(CodeUtils.getTypeDefaultValue(type.asPrimitiveType()));
        } else {
            expression = new NullLiteralExpr();
        }
        return expression;
    }

    private void addConstructor(ClassOrInterfaceDeclaration srcClass, ClassOrInterfaceDeclaration destClass, FieldDeclaration field) {
        ConstructorDeclaration defaultConstructor = destClass.addConstructor(Modifier.PUBLIC);
        Parameter parameter = new Parameter(new TypeParameter(srcClass.getNameAsString()),
                String.format("p%s", srcClass.getNameAsString())).addModifier(Modifier.FINAL);
        defaultConstructor.addParameter(parameter);
        BlockStmt body = defaultConstructor.getBody();
        Expression expression = getFieldAsExpression(field);

        body.addStatement(new AssignExpr(expression,
                new NameExpr(parameter.getName()), AssignExpr.Operator.ASSIGN));
    }

    private NameExpr getFieldAsExpression(FieldDeclaration field) {
        return new NameExpr("this." + field.getVariable(0).getNameAsString());
    }
}
