package musta.belmo.javacodetools.service;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

public class CodeModifier extends AbstractJavaCodeTools {

    private CodeTransformer codeTransformer;

    public CodeTransformer getCodeTransformer() {
        return codeTransformer;
    }

    public void setCodeTransformer(CodeTransformer codeTransformer) {
        this.codeTransformer = codeTransformer;
    }

    @Override
    public CompilationUnit generate(CompilationUnit code) {
        return null;
    }

    @Override
    public CompilationUnit generate(File file) throws FileNotFoundException {
        Optional.ofNullable(codeTransformer)
                .ifPresent(transformer -> {
                    try {
                        transformer.transform(file);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                });
        return super.generate(file);
    }

    public static void main(String[] args) throws FileNotFoundException {
        CodeModifier codeModifier = new CodeModifier();
        codeModifier.setCodeTransformer(file -> {
            CompilationUnit parse = JavaParser.parse(file);

            CompilationUnit clone = parse.clone();
            List<ClassOrInterfaceDeclaration> all = clone.findAll(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration classOrInterfaceDeclaration : all) {
                SingleMemberAnnotationExpr a = new SingleMemberAnnotationExpr();
                a.setName("SuppressWarnings");
                a.setMemberValue(new StringLiteralExpr("all"));
                classOrInterfaceDeclaration.addAnnotation(a);
            }

            FileUtils.write(file, clone.toString(), "UTF-8");
            return null;
        });

        File req = new File("D:\\COSY3_env_dev\\SRC\\ihub\\referentiel-ms\\document-contrat-eversuite-impl\\src\\main\\java\\com\\apicil\\cosy\\service\\ws\\gedeversuite\\skandia");

        for (File s : req.listFiles()) {
            if(s.isFile())
            codeModifier.generate(s);
        }

    }
}
