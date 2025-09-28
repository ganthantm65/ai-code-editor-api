package com.example.server.services;

import com.example.server.Model.ResponseModel;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompilerService {

    public ResponseModel executeCode(String lang, String code) throws IOException, InterruptedException {
        ResponseModel responseModel = new ResponseModel();

        Path tempDir = Files.createTempDirectory("code_exec_" + UUID.randomUUID());
        Path codeFile;
        String containerFile;
        String imageName;
        String[] command;

        imageName = "code-runner-universal";

        switch (lang.toLowerCase()) {
            case "python":
                codeFile = tempDir.resolve("code.py");
                Files.writeString(codeFile, code);
                containerFile = "code.py";
                command = new String[]{"python3", "/app/" + containerFile};
                break;

            case "java":
                codeFile = tempDir.resolve("Main.java");
                Files.writeString(codeFile, code);
                containerFile = "Main.java";
                command = new String[]{"bash", "-c", "javac /app/" + containerFile + " && java -cp /app Main"};
                break;

            case "cpp":
                codeFile = tempDir.resolve("main.cpp");
                Files.writeString(codeFile, code);
                containerFile = "main.cpp";
                command = new String[]{"bash", "-c", "g++ /app/" + containerFile + " -o /app/main && /app/main"};
                break;

            case "js":
                codeFile = tempDir.resolve("code.js");
                Files.writeString(codeFile, code);
                containerFile = "code.js";
                command = new String[]{"node", "/app/" + containerFile};
                break;

            default:
                responseModel.setError(true);
                responseModel.setErrorMessage("Unexpected Language: " + lang);
                return responseModel;
        }

        ProcessBuilder pb = new ProcessBuilder(buildDockerCommand(imageName, tempDir.toAbsolutePath().toString(), "/app", command));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        boolean finished = process.waitFor(10, TimeUnit.SECONDS); // Increased timeout for interactive programs
        if (!finished) {
            process.destroyForcibly();
            responseModel.setError(true);
            responseModel.setErrorMessage("Execution Timeout!");
            return responseModel;
        }

        String output = new String(process.getInputStream().readAllBytes());
        String error = new String(process.getErrorStream().readAllBytes());

        responseModel.setOutput(output + error);
        return responseModel;
    }

    private String[] buildDockerCommand(String imageName, String hostDir, String containerDir, String[] command) {
        List<String> cmd = new ArrayList<>();
        cmd.add("docker");
        cmd.add("run");
        cmd.add("--rm");
        cmd.add("--cpus=1.0");
        cmd.add("--memory=512m");
        cmd.add("--network=none");
        cmd.add("-v");
        cmd.add(hostDir + ":/app");
        cmd.add(imageName);
        for (String s : command) {
            cmd.add(s);
        }
        return cmd.toArray(new String[0]);
    }
}
