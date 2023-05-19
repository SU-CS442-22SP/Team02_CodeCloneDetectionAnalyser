package jp.naist.se.simplecc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SimpleCCExample {

    public static void main(String[] args) throws IOException, InterruptedException, JSONException {
        String directory = "trialDataset/";

        Path filePath = Paths.get("dataset/data.jsonl");
        BufferedReader reader = Files.newBufferedReader(filePath);
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        for (int i = 0; i < lines.size(); i++) {
            JSONObject jsonObject1 = new JSONObject(lines.get(i));
            String func1 = jsonObject1.getString("func");
            String fileName1 = directory + "file01.java";
            try (PrintWriter out = new PrintWriter(fileName1)) {
                out.println(func1);
            }

            for (int j = i + 1; j < lines.size(); j++) {
                JSONObject jsonObject2 = new JSONObject(lines.get(j));
                String func2 = jsonObject2.getString("func");
                String fileName2 = directory + "file02.java";
                try (PrintWriter out = new PrintWriter(fileName2)) {
                    out.println(func2);
                }

                // Avoid comparison if both lines are from the same file
                if (!func1.equals(func2)) {
                    // Running SimpleCC
                    ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "/Users/mataonbas/.m2/repository/org/antlr/antlr4-runtime/4.7/antlr4-runtime-4.7.jar:/Users/mataonbas/IdeaProjects/SimpleCC/simplecc.jar", "jp.naist.se.simplecc.CloneDetectionMain", fileName1, fileName2);
                    processBuilder.redirectErrorStream(true); // Redirect stderr to stdout
                    Process process = processBuilder.start();

                    // Capturing the output of SimpleCC
                    BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String simpleccOutput;
                    while ((simpleccOutput = in.readLine()) != null) {
                        if (simpleccOutput.contains("<pair>")) {
                            String output1 = in.readLine();
                            String output2 = in.readLine();

                            System.out.println(simpleccOutput);
                            System.out.println(output1);
                            System.out.println(output2);
                        }
                    }

                    // Wait for SimpleCC to finish
                    int exitCode = process.waitFor();
                    if (exitCode != 0) {
                        System.err.println("Clone detection process exited with error code: " + exitCode);
                    }
                }
            }
        }
    }
}
