package magnbred.ntnu.oblig5.cppcompiler.dockercompiler.service;

import magnbred.ntnu.oblig5.cppcompiler.dockercompiler.model.CodeDTO;
import magnbred.ntnu.oblig5.cppcompiler.dockercompiler.repo.CodeCompilerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CodeCompilerService {



    @Autowired
    CodeCompilerRepo codeCompilerRepo;

    public CodeDTO compileCode(CodeDTO code){
        return codeCompilerRepo.codeCompiler(code);
    }
}
