package musta.belmo.javacodetools;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.List;
import java.util.function.Predicate;

public class FactoryCreator extends AbstractJavaCodeGenerator {
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
                        objectCreationExpr.setType(aMethod.getType().toString());
                        ReturnStmt returnStmt = new ReturnStmt(objectCreationExpr);
                        blockStmt.addStatement(returnStmt);
                    });
        });

        return compilationUnit;

    }
}
