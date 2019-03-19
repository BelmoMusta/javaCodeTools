package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.TypeParameter;

import java.util.List;
import java.util.stream.Collectors;

public class GenerateOnDemandeHolderPattern extends AbstractJavaCodeTools {

    public CompilationUnit generate(CompilationUnit compilationUnitSrc) {
        CompilationUnit compilationUnit = compilationUnitSrc.clone();
        List<ClassOrInterfaceDeclaration> classes = compilationUnit.findAll(ClassOrInterfaceDeclaration.class);
        classes.stream()
                .filter(aClass -> !aClass.isInterface())
                .filter(aClass -> !aClass.isInnerClass())
                .filter(aClass -> !aClass.isStatic())
                .filter(aClass -> !aClass.isPrivate())
                .filter(aClass -> !aClass.isAbstract())
                .forEach(classOrInterfaceDeclaration -> {

                    final String className = classOrInterfaceDeclaration.getNameAsString();
                    final MethodDeclaration getInstanceMethod;
                    final List<MethodDeclaration> getInstanceMethods = getGetInstanceMethods(classOrInterfaceDeclaration);

                    if (!getInstanceMethods.isEmpty()) {
                        getInstanceMethod = getInstanceMethods.get(0);
                    } else {
                        getInstanceMethod = classOrInterfaceDeclaration.addMethod("getInstance",
                                Modifier.PUBLIC, Modifier.STATIC);
                    }

                    getInstanceMethod.setType(new TypeParameter(classOrInterfaceDeclaration.getNameAsString()));
                    BlockStmt blockStmt = new BlockStmt();
                    ReturnStmt returnStmt = new ReturnStmt();
                    NameExpr nameExpr = new NameExpr();

                    final String staticInnerClassName = className + "Holder";

                    List<ClassOrInterfaceDeclaration> innerClasses = classOrInterfaceDeclaration.getMembers()
                            .stream()
                            .filter(BodyDeclaration::isClassOrInterfaceDeclaration)
                            .map(BodyDeclaration::asClassOrInterfaceDeclaration)
                            .filter(cls -> staticInnerClassName.equals(cls.getNameAsString()))
                            .collect(Collectors.toList());

                    nameExpr.setName(staticInnerClassName + ".INSTANCE");
                    returnStmt.setExpression(nameExpr);
                    blockStmt.addStatement(returnStmt);
                    getInstanceMethod.setBody(blockStmt);
                    ClassOrInterfaceDeclaration staticInnerClass;

                    NodeList<BodyDeclaration<?>> list = new NodeList<>();

                    if (!innerClasses.isEmpty()) {
                        staticInnerClass = innerClasses.get(0);
                    } else {
                        staticInnerClass = new ClassOrInterfaceDeclaration();

                        ObjectCreationExpr objectCreationExpr = new ObjectCreationExpr();
                        objectCreationExpr.setType(className);
                        staticInnerClass.addFieldWithInitializer(new TypeParameter(className), "INSTANCE", objectCreationExpr, Modifier.STATIC, Modifier.FINAL, Modifier.PUBLIC);
                        ConstructorDeclaration constructorDeclaration = staticInnerClass.addConstructor(Modifier.PRIVATE);
                        constructorDeclaration.getBody().addOrphanComment(new LineComment("Constructeur par défaut"));
                        list.add(staticInnerClass);////
                    }
                    staticInnerClass.setName(staticInnerClassName);
                    staticInnerClass.setStatic(true);
                    staticInnerClass.setPrivate(true);

                    list.addAll(classOrInterfaceDeclaration.getMembers());
                    classOrInterfaceDeclaration.getMembers().clear();
                    classOrInterfaceDeclaration.getMembers().addAll(list);

                });

        return compilationUnit;
    }

    private List<MethodDeclaration> getGetInstanceMethods(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        return classOrInterfaceDeclaration.findAll(MethodDeclaration.class)
                .stream()
                .filter(methodDeclaration -> "getInstance".equals(methodDeclaration.getNameAsString())
                        && methodDeclaration.isStatic()
                        && methodDeclaration.isPublic()
                        && methodDeclaration.getParameters().isEmpty()).collect(Collectors.toList());

    }

}
