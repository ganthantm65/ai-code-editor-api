package com.example.server.controller;

import com.example.server.Model.RequestModel;
import com.example.server.Model.ResponseModel;
import com.example.server.services.CompilerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/code")
public class CompilerController {
    @Autowired
    private CompilerService compilerService;

    @PostMapping("/run")
    public ResponseModel runCode(@RequestParam String language, @RequestBody RequestModel code) throws IOException, InterruptedException {
        System.out.println(code.getCode());
        return compilerService.executeCode(language,code.getCode());
    }
}
