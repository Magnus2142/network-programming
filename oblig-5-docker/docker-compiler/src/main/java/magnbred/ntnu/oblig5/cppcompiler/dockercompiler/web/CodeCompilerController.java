package magnbred.ntnu.oblig5.cppcompiler.dockercompiler.web;

import magnbred.ntnu.oblig5.cppcompiler.dockercompiler.model.CodeDTO;
import magnbred.ntnu.oblig5.cppcompiler.dockercompiler.service.CodeCompilerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CodeCompilerController {


    @Autowired
    CodeCompilerService codeCompilerService;

    Logger logger = LoggerFactory.getLogger(CodeCompilerController.class);

    @PostMapping(path = "/compile")
    public CodeDTO codeCompiler(@RequestBody CodeDTO code){
        logger.info(code.getCode());
        return codeCompilerService.compileCode(code);
    }
}
