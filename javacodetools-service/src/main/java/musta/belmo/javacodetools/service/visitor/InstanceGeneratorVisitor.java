package musta.belmo.javacodetools.service.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.apache.commons.lang3.StringUtils;

public class InstanceGeneratorVisitor extends VoidVisitorAdapter<BlockStmt> {

    private String classType;
    @Override
    public void visit(FieldDeclaration fieldDeclaration, BlockStmt arg) {
        if(!fieldDeclaration.isStatic()) {
            final VariableDeclarator variableDeclarator = fieldDeclaration.getVariables().get(0);
            Expression nullLiteral = new NullLiteralExpr();
            final MethodCallExpr call = new MethodCallExpr(
                    new NameExpr(StringUtils.uncapitalize(classType)), "set" + StringUtils.capitalize(variableDeclarator.getNameAsString()), new NodeList<>(nullLiteral));
            arg.addAndGetStatement(call);
        }
        super.visit(fieldDeclaration, arg);
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}
