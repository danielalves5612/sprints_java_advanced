package br.com.fiap.previsaoSafra.controller;

import br.com.fiap.previsaoSafra.controller.dto.ColheitaDTO;
import br.com.fiap.previsaoSafra.model.Colheita;
import br.com.fiap.previsaoSafra.service.ColheitaService;
import br.com.fiap.previsaoSafra.service.FazendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/colheita")
public class ColheitaController {

    private final ColheitaService colheitaService;
    private final FazendaService fazendaService;

    @Autowired
    public ColheitaController(ColheitaService colheitaService, FazendaService fazendaService) {
        this.colheitaService = colheitaService;
        this.fazendaService = fazendaService;
    }

    @GetMapping("/form")
    public String mostrarFormularioColheita(Model model) {
        model.addAttribute("colheita", new ColheitaDTO());
        model.addAttribute("fazendas", fazendaService.listarFazendas()); 
        return "cadastro_colheita";
    }

    @PostMapping("/cadastrar")
    public String cadastrarColheita(@ModelAttribute ColheitaDTO colheitaDTO) {
        colheitaService.cadastrarColheita(colheitaDTO);
        return "redirect:/"; 
    }

    @GetMapping("/edit/{id}")
    public String mostrarFormularioEdicaoColheita(@PathVariable Long id, Model model) {
        Colheita colheita = colheitaService.buscarColheitaPorId(id);
        model.addAttribute("colheita", colheita);
        model.addAttribute("fazendas", fazendaService.listarFazendas()); 
        return "cadastro_colheita"; 
    }

    @PostMapping("/edit/{id}")
    public String atualizarColheita(@PathVariable Long id, @ModelAttribute ColheitaDTO colheitaDTO) {
        colheitaService.atualizarColheita(id, colheitaDTO);
        return "redirect:/home"; 
    }

    @GetMapping
    public ResponseEntity<List<Colheita>> listarColheita() {
        List<Colheita> colheitas = colheitaService.listarColheitas();
        return ResponseEntity.ok(colheitas);
    }

    @GetMapping("/buscar")
    public List<Colheita> buscarColheitas(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String estacaoDoAno) {
        return colheitaService.listarColheitaPorAtributo(nome, tipo, estacaoDoAno);
    }

    @DeleteMapping("/{id}")
    public String removerColheita(@PathVariable Long id) {
        colheitaService.removerColheita(id);
        return "redirect:/home"; 
    }
}
