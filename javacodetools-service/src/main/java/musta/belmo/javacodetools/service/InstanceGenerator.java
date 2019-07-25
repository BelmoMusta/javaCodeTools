package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.TypeParameter;
import musta.belmo.javacodetools.service.visitor.InstanceGeneratorVisitor;
import musta.belmo.javacodetools.service.visitor.TestMethodsVisitor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * TODO: Complete the description of this class
 *
 * @author default author
 * @since 0.0.0.SNAPSHOT
 * @version 0.0.0
 */
public class InstanceGenerator extends AbstractJavaCodeTools {

    /**
     * {@inheritDoc}
     */
    @Override
    public CompilationUnit generate(CompilationUnit compilationUnit) {
        final CompilationUnit resultUnit = new CompilationUnit();
        List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        classes.forEach(aClass -> {
            final ClassOrInterfaceDeclaration mainClass = resultUnit.addClass("MainClass");
            final MethodDeclaration methodDeclaration = mainClass.addMethod("generateFor"+aClass.getNameAsString(), Modifier.PUBLIC,Modifier.STATIC);
            methodDeclaration.setType(new TypeParameter("void"));
            final BlockStmt body = new BlockStmt();
            methodDeclaration.setBody(body);
            final ClassOrInterfaceDeclaration classOrInterfaceDeclaration = classes.get(0);
            ClassOrInterfaceType destClassType = new ClassOrInterfaceType()
                    .setName(classOrInterfaceDeclaration.getNameAsString());

            VariableDeclarator variableDeclarator = CodeUtils.variableDeclaratorFromType(destClassType,

                    StringUtils.uncapitalize(CodeUtils.getSimpleClassName(classOrInterfaceDeclaration.getNameAsString())));
            VariableDeclarationExpr variableDeclarationExpr =
                    CodeUtils.variableDeclarationExprFromVariable(variableDeclarator)
                            .addModifier(Modifier.FINAL);
            final ObjectCreationExpr objectCreationExpr = CodeUtils.objectCreationExpFromType(destClassType);
            final AssignExpr assignExpr = new AssignExpr(variableDeclarationExpr,objectCreationExpr, AssignExpr.Operator.ASSIGN);
            body.addAndGetStatement(assignExpr);
            final InstanceGeneratorVisitor generatorVisitor = new InstanceGeneratorVisitor();
            generatorVisitor.setClassType(destClassType.asString());
            aClass.accept(generatorVisitor, body);
        });
        return resultUnit;
    }
}
