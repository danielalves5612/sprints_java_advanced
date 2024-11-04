package br.com.fiap.previsaoSafra.controller;

import br.com.fiap.previsaoSafra.controller.dto.FazendaDTO;
import br.com.fiap.previsaoSafra.model.Fazenda;
import br.com.fiap.previsaoSafra.service.FazendaService;
import br.com.fiap.previsaoSafra.service.ColheitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/fazenda")
public class FazendaController {

    private final FazendaService fazendaService;
    private final ColheitaService colheitaService;

    @Autowired
    public FazendaController(FazendaService fazendaService, ColheitaService colheitaService) {
        this.fazendaService = fazendaService;
        this.colheitaService = colheitaService;
    }

    @GetMapping("/inicio")
    public String mostrarInicio(Model model) {
        List<Fazenda> fazendas = fazendaService.listarFazendas();
        model.addAttribute("fazendas", fazendas);
        model.addAttribute("colheitas", colheitaService.listarColheitas()); 
        return "index";
    }

    @GetMapping("/formFazenda")
    public String mostrarFormularioFazenda(Model model) {
        model.addAttribute("fazenda", new FazendaDTO());
        return "cadastro_fazenda";
    }

    @PostMapping("/cadastrarFazenda")
    public String cadastrarFazenda(@ModelAttribute FazendaDTO fazendaDTO) {
        fazendaService.cadastrarFazenda(fazendaDTO);
        return "redirect:/api/fazenda/inicio";
    }

    @GetMapping("/editFazenda/{id}")
    public String editarFazenda(@PathVariable Long id, Model model) {
        Fazenda fazenda = fazendaService.buscarFazendaPorId(id);
        model.addAttribute("fazenda", fazenda);
        return "cadastro_fazenda";
    }

    @PostMapping("/editFazenda/{id}")
    public String atualizarFazenda(@PathVariable Long id, @ModelAttribute FazendaDTO fazendaDTO) {
        fazendaService.atualizarFazenda(id, fazendaDTO);
        return "redirect:/api/fazenda/inicio";
    }

    @DeleteMapping("/deleteFazenda/{id}")
    public String excluirFazenda(@PathVariable Long id, Model model) {
        try {
            fazendaService.removerFazenda(id);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Não é possível excluir a fazenda, pois há colheitas associadas.");
        }
        return "redirect:/api/fazenda/inicio";
    }
}
