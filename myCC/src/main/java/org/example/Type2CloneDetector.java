package org.example;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Type2CloneDetector {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Type2CloneDetector <file1> <file2>");
            System.exit(1);
        }

        String file1 = args[0];
        String file2 = args[1];

        String sourceCode1 = readFile(file1);
        String sourceCode2 = readFile(file2);

        List<Action> actions = compareASTs(sourceCode1, sourceCode2);

        if (actions != null && !actions.isEmpty()) {
            System.out.println("Type-2 code clone detected.");
            printIdenticalLines(sourceCode1, sourceCode2, actions);
        } else {
            System.out.println("No Type-2 code clone detected.");
        }
    }

    private static void printIdenticalLines(String sourceCode1, String sourceCode2, List<Action> actions) {
        String[] lines1 = sourceCode1.split("\n");
        String[] lines2 = sourceCode2.split("\n");

        int lineOffset1 = 0;
        int lineOffset2 = 0;

        List<int[]> identicalLineIntervals = new ArrayList<>();

        for (Action action : actions) {
            ITree node = action.getNode();
            int startLine = node.getPos();
            int length = node.getLength();

            int line1 = getLineNumber(sourceCode1, startLine);
            int line2 = getLineNumber(sourceCode2, startLine + length);

            int intervalStart1 = -1;
            int intervalStart2 = -1;

            for (int i = lineOffset1; i < line1; i++) {
                for (int j = lineOffset2; j < line2; j++) {
                    if (lines1[i].trim().equals(lines2[j].trim())) {
                        if (intervalStart1 == -1) {
                            intervalStart1 = i + 1;
                            intervalStart2 = j + 1;
                        }
                    } else {
                        if (intervalStart1 != -1) {
                            identicalLineIntervals.add(new int[]{intervalStart1, i, intervalStart2, j});
                            intervalStart1 = -1;
                            intervalStart2 = -1;
                        }
                    }
                }
            }

            if (intervalStart1 != -1) {
                identicalLineIntervals.add(new int[]{intervalStart1, line1, intervalStart2, line2});
            }

            lineOffset1 = line1;
            lineOffset2 = line2;
        }

        for (int[] interval : identicalLineIntervals) {
            System.out.printf("Identical lines interval: File1.java [%d-%d] <-> File2.java [%d-%d]\n", interval[0], interval[1], interval[2], interval[3]);
        }
    }

    private static int getLineNumber(String sourceCode, int position) {
        int line = 0;

        for (int i = 0; i < position; i++) {
            if (sourceCode.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }


    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static List<Action> compareASTs(String sourceCode1, String sourceCode2) {
        try {
            Run.initGenerators();
            JdtTreeGenerator generator = new JdtTreeGenerator();
            TreeContext treeContext1 = generator.generateFromString(sourceCode1);
            TreeContext treeContext2 = generator.generateFromString(sourceCode2);

            ITree srcTree = treeContext1.getRoot();
            ITree dstTree = treeContext2.getRoot();

            Matcher matcher = Matchers.getInstance().getMatcher(srcTree, dstTree);
            matcher.match();
            MappingStore mappings = matcher.getMappings();

            ActionGenerator actionGenerator = new ActionGenerator(srcTree, dstTree, mappings);
            actionGenerator.generate();
            List<Action> actions = actionGenerator.getActions();

            return actions;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
