package magnbred.ntnu.oblig5.cppcompiler.dockercompiler.repo;


import magnbred.ntnu.oblig5.cppcompiler.dockercompiler.model.CodeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

@Repository
public class CodeCompilerRepo {

    Logger logger = LoggerFactory.getLogger(CodeCompilerRepo.class);

    public CodeDTO codeCompiler(CodeDTO code){
        writeProgramToFile(code.getCode());

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "src/main/resources/docker-script.sh");
        processBuilder.redirectErrorStream(true);

        StringBuilder output = new StringBuilder();

        try {
            Process process = processBuilder.start();

            BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String processLine;
            while((processLine = processReader.readLine()) != null){
                logger.info(processLine);
                output.append(processLine).append("\n");
            }

            process.waitFor();
            logger.info("Ok!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CodeDTO(output.toString());
    }

    private void writeProgramToFile(String code){
        try{
            FileWriter writer = new FileWriter("/home/magnus/github/network-programming/oblig-5-docker/docker-compiler/src/main/resources/programToCompile.cpp");
            writer.write(code);
            writer.close();
            logger.info("Successfully wrote to the file!");
        } catch (IOException e) {
            logger.info("An error occurred.");
            e.printStackTrace();
        }
    }
}
