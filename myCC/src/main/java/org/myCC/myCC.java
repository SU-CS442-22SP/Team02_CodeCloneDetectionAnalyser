package org.myCC;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class myCC {

    public static CompilationUnit parse(String filePath) {
        try {
            String sourceCode = Files.readString(Paths.get(filePath));

            // Automatically wrap source code in a class if it does not contain one.
            if (!sourceCode.contains("class")) {
                sourceCode = "public class DummyClass {\n" + sourceCode + "\n}";
            }

            ASTParser parser = ASTParser.newParser(AST.getJLSLatest());
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            Map<String, String> options = JavaCore.getOptions();
            parser.setCompilerOptions(options);
            parser.setSource(sourceCode.toCharArray());
            parser.setResolveBindings(true);
            return (CompilationUnit) parser.createAST(null);
        } catch (IOException e) {
            throw new RuntimeException("Error parsing file", e);
        }
    }

    public static List<String> getFilePaths(String directory) {
        try (Stream<Path> paths = Files.walk(Paths.get(directory))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .filter(path -> path.endsWith(".java")) // only include .java files
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error reading file paths", e);
        }
    }

    public static boolean isSimilar(Statement statement1, Statement statement2) {
        ASTMatcher matcher = new ASTMatcher();
        return matcher.safeSubtreeMatch(normalizeStatement(statement1), normalizeStatement(statement2));
    }

    public static void findClones(List<String> filePaths) {
        for (int i = 0; i < filePaths.size() - 1; i++) {
            String filePath1 = filePaths.get(i);
            CompilationUnit cu1 = parse(filePath1);

            List<Statement> statements1 = getStatementsFromCompilationUnit(cu1);

            boolean cloneFound = false;

            for (int j = i + 1; j < filePaths.size() && !cloneFound; j++) {
                String filePath2 = filePaths.get(j);
                CompilationUnit cu2 = parse(filePath2);

                List<Statement> statements2 = getStatementsFromCompilationUnit(cu2);

                System.out.println("Comparing file " + filePath1 + " and " + filePath2);

                for (Statement statement1 : statements1) {
                    for (Statement statement2 : statements2) {
                        if (isSimilar(statement1, statement2)) {
                            // Create the output file name
                            String outputFileName = "clone_output.txt";

                            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true))) {
                                // Write the clone data to the file
                                writer.write("<pair>\n");
                                writeCloneData(writer, filePath1, statement1, cu1);
                                writeCloneData(writer, filePath2, statement2, cu2);
                                writer.write("<pair>\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            cloneFound = true;
                            break;
                        }
                    }

                    if (cloneFound) {
                        break;
                    }
                }
            }
        }
    }

    public static void writeCloneData(BufferedWriter writer, String filePath, Statement statement, CompilationUnit cu) throws IOException {
        writer.write("File: " + filePath + "\n");
        writer.write("Statement: " + statement.toString() + "\n");
        // Write additional clone data as needed
        writer.write("\n");
    }

    public static List<Statement> getStatementsFromCompilationUnit(CompilationUnit cu) {
        List<Statement> statements = new ArrayList<>();
        cu.accept(new ASTVisitor() {
            public boolean visit(Block node) {
                for (Object o : node.statements()) {
                    if (o instanceof Statement) {
                        statements.add((Statement) o);
                    }
                }
                return super.visit(node);
            }
        });
        return statements;
    }

    private static Statement normalizeStatement(Statement statement) {
        if (statement == null) return null;
        AST ast = statement.getAST();
        Statement copy = (Statement) ASTNode.copySubtree(ast, statement);
        copy.accept(new ASTVisitor() {
            public boolean visit(VariableDeclarationFragment node) {
                node.setName(ast.newSimpleName("var"));
                if (node.getInitializer() instanceof NumberLiteral) {
                    node.setInitializer(ast.newNumberLiteral("0"));
                } else if (node.getInitializer() instanceof StringLiteral) {
                    node.setInitializer(ast.newStringLiteral());
                }
                return true;
            }

            public boolean visit(SingleVariableDeclaration node) {
                node.setName(ast.newSimpleName("var"));
                node.setType(ast.newSimpleType(ast.newSimpleName("Object")));
                return true;
            }
        });
        return copy;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("You must specify a dataset.");
            System.exit(1);
        }

        // get the list of file paths
        List<String> filePaths = getFilePaths(args[0]);

        // Sort the filePaths list in numeric order
        filePaths.sort((file1, file2) -> {
            String number1 = file1.replaceAll("\\D+","");
            String number2 = file2.replaceAll("\\D+","");
            return Integer.compare(Integer.parseInt(number1), Integer.parseInt(number2));
        });

        // find clones in the list of files
        findClones(filePaths);
    }
}
