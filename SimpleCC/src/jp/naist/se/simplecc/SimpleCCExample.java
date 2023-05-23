package jp.naist.se.simplecc;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
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

        // First, create all the files
        for (int i = 0; i < lines.size(); i++) {
            JSONObject jsonObject = new JSONObject(lines.get(i));
            String func = jsonObject.getString("func");
            String fileName = directory + "file_" + (i + 1) + ".java";
            try (PrintWriter out = new PrintWriter(fileName)) {
                out.println(func);
            }
        }
        try {
            FileWriter writer = new FileWriter("clones.txt");
            boolean cloneFound = false;
            // Then, run SimpleCC on all pairs of files
            //Outer loop
            for (int i = 0; i < lines.size() && !cloneFound; i++) {
                String fileName1 = directory + "file_" + (i + 1) + ".java";
                //Inner loop
                for (int j = i + 1; j < lines.size() && !cloneFound; j++) {
                    String fileName2 = directory + "file_" + (j + 1) + ".java";

                    if (!fileName1.equals(fileName2)) {
                        System.out.println("Comparing file " + fileName1 + " and " + fileName2);
                        // Running SimpleCC
                        ProcessBuilder processBuilder = new ProcessBuilder("java", "-cp", "/Users/mataonbas/.m2/repository/org/antlr/antlr4-runtime/4.7/antlr4-runtime-4.7.jar:/Users/mataonbas/IdeaProjects/SimpleCC/simplecc.jar", "jp.naist.se.simplecc.CloneDetectionMain", fileName1, fileName2);
                        processBuilder.redirectErrorStream(true); // Redirect stderr to stdout
                        Process process = processBuilder.start();

                        // Capturing the output of SimpleCC
                        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                        String simpleccOutput;
                        //                    while ((simpleccOutput = in.readLine()) != null) {
                        //                        if (simpleccOutput.contains("<pair>")) {
                        //                            String output1 = in.readLine();
                        //                            String output2 = in.readLine();
                        //
                        //                            System.out.println(simpleccOutput);
                        //                            System.out.println(output1);
                        //                            System.out.println(output2);
                        //                        }
                        //                    }
                        while ((simpleccOutput = in.readLine()) != null) {
                            if (simpleccOutput.contains("<pair>")) {
                                String output1 = in.readLine();
                                String output2 = in.readLine();

                                // Get the file names of the two code fragments
                                String file1 = output1.split(",")[0];
                                String file2 = output2.split(",")[0];

                                // Only print comparisons between different files
                                if (!file1.equals(file2)) {
                                    System.out.println(simpleccOutput);
                                    System.out.println(output1);
                                    System.out.println(output2);

                                    writer.write(simpleccOutput + "\n");
                                    writer.write(output1 + "\n");
                                    writer.write(output2 + "\n");

                                    cloneFound = true;
                                    break;
                                }
                            }
                        }

                        // Wait for SimpleCC to finish
                        int exitCode = process.waitFor();
                        if (exitCode != 0) {
                            System.err.println("Clone detection process exited with error code: " + exitCode);
                        }
                    }
                }
                cloneFound = false;
            }
            writer.close();
        }
        catch (IOException e){
           e.printStackTrace();
        }
    }
}
