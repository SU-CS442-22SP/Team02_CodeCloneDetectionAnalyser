package jp.naist.se.simplecc;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleCCExample {
    public static void main(String[] args) {
        // Load the dataset from the JSON file
        String datasetPath = "dataset/newData.jsonl";
        StringBuilder functionCodeBuilder = new StringBuilder();
        try {
            String datasetString = new String(Files.readAllBytes(Paths.get(datasetPath)));
            String[] lines = datasetString.split("\n");
            for (String line : lines) {
                JSONObject obj = new JSONObject(line);
                String func = obj.getString("func");
                functionCodeBuilder.append(func);
            }
        } catch (IOException e) {
            System.out.println("Failed to read dataset from " + datasetPath);
            e.printStackTrace();
            return;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Extract the function code from the dataset
        String functionCode = functionCodeBuilder.toString();

        // Create two files with the same function code
        String file01Path = "trialDataset/file01.java";
        String file02Path = "trialDataset/file02.java";
        try {
            FileWriter file01Writer = new FileWriter(file01Path);
            file01Writer.write("class Foo {\n");
            file01Writer.write(functionCode);
            file01Writer.write("}");
            file01Writer.close();

            FileWriter file02Writer = new FileWriter(file02Path);
            file02Writer.write("class Foo {\n");
            file02Writer.write(functionCode);
            file02Writer.write("}");
            file02Writer.close();
        } catch (IOException e) {
            System.out.println("Failed to create files " + file01Path + " and " + file02Path);
            e.printStackTrace();
            return;
        }

        // Compare the two files using SimpleCC
        String[] simpleCCArgs = {file01Path, file02Path};
        try {
            Process simpleCCProcess = new ProcessBuilder("java", "-jar", "SimpleCC.jar", file01Path, file02Path).start();
            simpleCCProcess.waitFor();
        } catch (IOException e) {
            System.out.println("Failed to run SimpleCC");
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            System.out.println("SimpleCC was interrupted");
            e.printStackTrace();
            return;
        }
    }
}