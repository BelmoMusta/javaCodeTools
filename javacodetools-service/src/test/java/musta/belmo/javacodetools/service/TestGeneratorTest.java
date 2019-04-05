package musta.belmo.javacodetools.service;

import com.github.javaparser.ast.CompilationUnit;
import org.junit.Test;

import java.io.File;

public class TestGeneratorTest {
    @Test
    public void testGenerate() throws Exception {
        TestGenerator testGenerator = new TestGenerator();
        CompilationUnit generate = testGenerator.generate(new File("D:\\platformsg2_R_64\\workspace\\movalnsa_2_2_LOT4\\movalnsa-ws\\target\\couvrant\\fr\\cnav\\rgcu\\Numeric.java"));
        System.out.println(generate);
    }
}