package com.example.server.controller;

import com.example.server.services.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Autowired
    private AiService geminiService;

    @PostMapping("/explain")
    public ResponseEntity<?> explainCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String language = body.get("language");
        String explanation = geminiService.getExplanation(code, language);
        return ResponseEntity.ok(Map.of("explanation", explanation));
    }

    @PostMapping("/fix")
    public ResponseEntity<?> fixCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String output = body.get("output");
        String language = body.get("language");
        String fixedCode = geminiService.fixCode(code, output, language);
        return ResponseEntity.ok(Map.of("fixedCode", fixedCode));
    }

    @PostMapping("/optimize")
    public ResponseEntity<?> optimizeCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String language = body.get("language");
        String optimizedCode = geminiService.optimizeCode(code, language);
        return ResponseEntity.ok(Map.of("optimizedCode", optimizedCode));
    }
}
