package jp.naist.se.simplecc;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputConverter {

    public static List<String> readFiles(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = reader.readLine();
        while (line != null) {
            lines.add(line);
            line = reader.readLine();
        }

        return lines;
    }

    public static String getInput() throws IOException {
        String trainFilePath = "/Users/mataonbas/IdeaProjects/CodeXGLUE-main/Code-Code/Clone-detection-BigCloneBench/dataset/train.txt";
        String testFilePath = "/Users/mataonbas/IdeaProjects/CodeXGLUE-main/Code-Code/Clone-detection-BigCloneBench/dataset/test.txt";
        String validFilePath = "/Users/mataonbas/IdeaProjects/CodeXGLUE-main/Code-Code/Clone-detection-BigCloneBench/dataset/valid.txt";

        List<String> trainLines = readFiles(trainFilePath);
        List<String> testLines = readFiles(testFilePath);
        List<String> validLines = readFiles(validFilePath);

        // Convert the input into the format that SimpleCC can use
        StringBuilder sb = new StringBuilder();
        for (String line : trainLines) {
            sb.append(line).append("\n");
        }
        for (String line : testLines) {
            sb.append(line).append("\n");
        }
        for (String line : validLines) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        try {
            String input = getInput();
            System.out.println(input);
            // Now you can pass the input string to SimpleCC
        } catch (IOException e) {
            // Handle the exception here
            System.out.println("Error: " + e.getMessage());
        }
    }
}





