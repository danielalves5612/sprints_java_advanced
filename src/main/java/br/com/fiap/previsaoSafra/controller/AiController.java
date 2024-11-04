package br.com.fiap.previsaoSafra.controller;

import br.com.fiap.previsaoSafra.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AIService aiService;

    @Autowired
    public AiController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/processar")
    public String processarPergunta(@RequestParam String pergunta) {
        return aiService.processarPergunta(pergunta);
    }
}
