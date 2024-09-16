package br.com.fiap.previsaoSafra.controller;

import br.com.fiap.previsaoSafra.controller.dto.FazendaDTO;
import br.com.fiap.previsaoSafra.controller.dto.ColheitaDTO;
import br.com.fiap.previsaoSafra.model.Fazenda;
import br.com.fiap.previsaoSafra.model.Colheita;
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

    // Cadastro de fazenda e redirecionamento
    @PostMapping(consumes = "application/x-www-form-urlencoded")
    public String cadastrarFazenda(@ModelAttribute FazendaDTO fazendaDTO, Model model) {
        fazendaService.cadastrarFazenda(fazendaDTO);
        return "redirect:/api/fazenda/form";
    }

    // Exibe o formulário de fazenda e colheitas cadastradas
    @GetMapping("/form")
    public String mostrarFormularioFazendaEColheita(Model model) {
        List<Fazenda> fazendas = fazendaService.listarFazendas();
        List<Colheita> colheitas = colheitaService.listarColheitas();
        model.addAttribute("fazenda", new FazendaDTO());
        model.addAttribute("colheita", new ColheitaDTO());
        model.addAttribute("fazendas", fazendas);
        model.addAttribute("colheitas", colheitas);
        return "fazenda_colheita_form";
    }

    // Edição de fazenda
    @GetMapping("/edit/{id}")
    public String editarFazenda(@PathVariable Long id, Model model) {
        Fazenda fazenda = fazendaService.buscarFazendaPorId(id);
        model.addAttribute("fazenda", fazenda);
        return "fazenda_colheita_form";
    }

    @GetMapping("/delete/{id}")
    public String excluirFazenda(@PathVariable Long id, Model model) {
        try {
            fazendaService.removerFazenda(id);
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Não é possível excluir a fazenda, pois há colheitas associadas.");
        }
        return "redirect:/api/fazenda/form";
    }

    @PostMapping("/colheita")
    public String cadastrarColheita(@ModelAttribute ColheitaDTO colheitaDTO) {
        colheitaService.cadastrarColheita(colheitaDTO);
        return "redirect:/api/fazenda/form";
    }

    @GetMapping("/colheita/edit/{id}")
    public String editarColheita(@PathVariable Long id, Model model) {
        Colheita colheita = colheitaService.buscarColheitaPorId(id);
        model.addAttribute("colheita", colheita);
        return "fazenda_colheita_form";
    }

    @GetMapping("/colheita/delete/{id}")
    public String excluirColheita(@PathVariable Long id) {
        colheitaService.removerColheita(id);
        return "redirect:/api/fazenda/form";
    }
}
