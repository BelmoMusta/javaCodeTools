package musta.belmo.javacodetools.service.visitor;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import musta.belmo.javacodetools.service.CodeUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class HibernateVisitor extends VoidVisitorAdapter<CompilationUnit> {
    @Override
    public void visit(MethodDeclaration methodDeclaration, CompilationUnit compilationUnit) {
        final FieldDeclaration fieldFromMethodDeclaration = getFieldFromMethodDeclaration(compilationUnit, methodDeclaration);
        if (fieldFromMethodDeclaration != null) {
            fieldFromMethodDeclaration.getAnnotations().addAll(methodDeclaration.getAnnotations());
            methodDeclaration.getAnnotations().clear();
            compilationUnit.remove(methodDeclaration);
            removeSettersAndGetters(fieldFromMethodDeclaration, compilationUnit);
        }
        super.visit(methodDeclaration, compilationUnit);
    }

    private void removeSettersAndGetters(FieldDeclaration fieldFromMethodDeclaration, CompilationUnit compilationUnit) {
        List<String> methodsToDelete = fieldFromMethodDeclaration.getVariables().stream()
                .map(f -> f.getNameAsString())
                .map(CodeUtils::capitalize)
                .flatMap(aName -> Arrays.asList("set" + aName, "get" + aName).stream())
                .collect(Collectors.toList());

        Iterator<MethodDeclaration> iterator = compilationUnit.findAll(MethodDeclaration.class).iterator();
        for (String toDelete : methodsToDelete) {
            while (iterator.hasNext()) {
                MethodDeclaration next = iterator.next();
                if (next.getNameAsString().equals(toDelete)) {
                    next.remove();
                }
            }
        }
    }

    private FieldDeclaration getFieldFromMethodDeclaration(CompilationUnit cls, MethodDeclaration methodDeclaration) {
        final String methodName = methodDeclaration.getName()
                .asString().substring(3);

        List<FieldDeclaration> fields = cls.findAll(FieldDeclaration.class);
        FieldDeclaration field = fields
                .stream()
                .filter(fieldDeclaration ->
                        fieldDeclaration.getVariables()
                                .stream()
                                .anyMatch(variable -> variable.getNameAsString()
                                        .equals(CodeUtils.toLowerCaseFirstLetter(methodName))))
                .findFirst()
                .orElse(null);
        return field;
    }
}
