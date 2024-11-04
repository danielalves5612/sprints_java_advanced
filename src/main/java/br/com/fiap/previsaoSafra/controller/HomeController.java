package br.com.fiap.previsaoSafra.controller;

import br.com.fiap.previsaoSafra.model.Fazenda;
import br.com.fiap.previsaoSafra.model.Colheita;
import br.com.fiap.previsaoSafra.repository.FazendaRepository;
import br.com.fiap.previsaoSafra.repository.ColheitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private FazendaRepository fazendaRepository;

    @Autowired
    private ColheitaRepository colheitaRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Fazenda> fazendas = fazendaRepository.findAll();
        List<Colheita> colheitas = colheitaRepository.findAll();

        model.addAttribute("fazendas", fazendas);
        model.addAttribute("colheitas", colheitas);

        return "index"; 
    }
}
