package musta.belmo.javacodetools.service;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.Type;

import java.util.List;
import java.util.function.Predicate;

public class FactoryCreator extends AbstractJavaCodeTools {
    private static final Predicate<MethodDeclaration> IS_NOT_VOID = aMethod -> !aMethod.getType().isVoidType();

    @Override
    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();
        compilationUnit.findAll(ClassOrInterfaceDeclaration.class).forEach(aClass -> {
            List<MethodDeclaration> methods = aClass.findAll(MethodDeclaration.class);

            methods.stream()
                    .filter(IS_NOT_VOID)
                    .forEach(aMethod -> {
                        BlockStmt blockStmt = new BlockStmt();
                        aMethod.setBody(blockStmt);
                        ObjectCreationExpr objectCreationExpr = new ObjectCreationExpr();
                        Type type = aMethod.getType();
                        if (!type.isPrimitiveType()) {
                            objectCreationExpr.setType(type.toString());
                        }
                        ReturnStmt returnStmt = new ReturnStmt(objectCreationExpr);
                        blockStmt.addStatement(returnStmt);
                    });
        });

        return compilationUnit;

    }
}
