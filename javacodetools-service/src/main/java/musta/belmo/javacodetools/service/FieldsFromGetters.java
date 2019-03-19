package musta.belmo.javacodetools.service;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Generates Fields from gettes
 */
public class FieldsFromGetters extends AbstractJavaCodeTools {
    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(aClass -> {
            Predicate<MethodDeclaration> isGetter = aMethod -> aMethod.getName().toString().startsWith("get");
            Predicate<MethodDeclaration> isBooleanAccessor = aMethod -> aMethod.getName().toString().startsWith("is");

            reversedStream(aClass.findAll(MethodDeclaration.class)
                    .stream())
                    .filter(isGetter)
                    .forEach(aMethod -> {
                        String methodeName = aMethod.getName().toString().substring(3);
                        FieldDeclaration fieldDeclaration = CodeUtils.newField(aMethod.getType(),
                                "a" + methodeName,
                                Modifier.PRIVATE);

                        aClass.getMembers().add(0, fieldDeclaration);
                        ReturnStmt returnStmt = new ReturnStmt(fieldDeclaration.getVariable(0).getNameAsExpression());
                        BlockStmt blockStmt = new BlockStmt();
                        blockStmt.addStatement(returnStmt);
                        aMethod.setBody(blockStmt);
                        aClass.getMethodsByName("set" + methodeName).forEach(setterMethod -> {
                            BlockStmt setterBlockStmt = new BlockStmt();
                            Expression assignStmt = new AssignExpr(fieldDeclaration.getVariables().get(0).getNameAsExpression(),
                                    setterMethod.getParameter(0).getNameAsExpression(), AssignExpr.Operator.ASSIGN);
                            setterBlockStmt.addStatement(assignStmt);
                            setterMethod.setBody(setterBlockStmt);
                        });
                    });

            reversedStream(aClass.findAll(MethodDeclaration.class).stream())
                    .filter(isBooleanAccessor)
                    .forEach(booleanAccessor -> {
                        String methodeName = booleanAccessor.getNameAsString().substring(2);
                        FieldDeclaration fieldDeclaration = CodeUtils.newField(booleanAccessor.getType(),
                                "a" + methodeName,
                                Modifier.PRIVATE);
                        ReturnStmt returnStmt = new ReturnStmt(fieldDeclaration.getVariable(0).getNameAsExpression());
                        BlockStmt blockStmt = new BlockStmt();
                        blockStmt.addStatement(returnStmt);
                        booleanAccessor.setBody(blockStmt);
                    });
        });

        return compilationUnit;

    }

    private static <T> Stream<T> reversedStream(Stream<T> input) {
        Object[] temp = input.toArray();
        return (Stream<T>) IntStream.range(0, temp.length)
                .mapToObj(i -> temp[temp.length - i - 1]);
    }


}
