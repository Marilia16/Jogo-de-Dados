package com.example.jogodedados.controller;

import com.example.jogodedados.model.Jogador;
import com.example.jogodedados.repository.JogadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class JogoDadosController {

    @Autowired
    private JogadorRepository jogadorRepository;

    private List<Jogador> jogadoresSelecionados = new ArrayList<>();
    private int dado1 = 0, dado2 = 0;
    private String vencedor = "";
    private boolean apostasRegistradas = false;
    private boolean rolou = false;

    private List<Jogador> listarOrdenados() {
        return jogadorRepository.findAll()
                .stream()
                .sorted(Comparator.comparingInt(Jogador::getVitorias).reversed())
                .toList();
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/Jogadores")
    public String listarJogadores(Model model, @RequestParam(value = "erro", required = false) String erro, @RequestParam(value = "sucesso", required = false) String sucesso) {
        model.addAttribute("jogadores", listarOrdenados());
        if (erro != null) model.addAttribute("erro", erro);
        if (sucesso != null) model.addAttribute("sucesso", sucesso);
        return "jogadores";
    }

    @PostMapping("/Jogadores")
    public String adicionarJogador(@RequestParam String nome, @RequestParam String email, Model model) {
        Optional<Jogador> existente = jogadorRepository.findByEmail(email);
        if (existente.isPresent()) {
            model.addAttribute("erro", "❌ Este e-mail já está cadastrado!");
        } else {
            jogadorRepository.save(new Jogador(nome, email));
            model.addAttribute("sucesso", "✅ Jogador cadastrado com sucesso!");
        }

        model.addAttribute("jogadores", listarOrdenados());
        return "jogadores";
    }

    @PostMapping("/Jogadores/delete")
    public String deletarJogadores(@RequestParam(value = "ids", required = false) List<Integer> ids, Model model) {
        if (ids != null) {
            ids.forEach(jogadorRepository::deleteById);
            model.addAttribute("sucesso", "🗑️ Jogador(es) removido(s) com sucesso!");
        } else {
            model.addAttribute("erro", "⚠️ Nenhum jogador selecionado para exclusão!");
        }

        model.addAttribute("jogadores", listarOrdenados());
        return "jogadores";
    }

    @PostMapping("/Jogadores/selecionar")
    public String selecionarJogadores(@RequestParam("ids") List<Integer> ids) {
        jogadoresSelecionados.clear();
        jogadoresSelecionados.addAll(jogadorRepository.findAllById(ids));
        apostasRegistradas = false;
        rolou = false;
        dado1 = 0;
        dado2 = 0;
        vencedor = "";
        jogadoresSelecionados.forEach(j -> j.setAposta(null));
        return "redirect:/jogo";
    }

    @GetMapping("/jogo")
    public String jogo(Model model) {
        Random random = new Random();
        if (dado1 == 0) dado1 = random.nextInt(6) + 1;
        if (dado2 == 0) dado2 = random.nextInt(6) + 1;

        model.addAttribute("jogadoresEscolhidos", jogadoresSelecionados);
        model.addAttribute("dado1", dado1);
        model.addAttribute("dado2", dado2);
        model.addAttribute("vencedor", vencedor);
        model.addAttribute("apostasRegistradas", apostasRegistradas);
        model.addAttribute("rolou", rolou);
        return "jogo";
    }

    @PostMapping("/jogo/apostas")
    public String registrarApostas(@RequestParam Map<String, String> apostas) {
        if (apostasRegistradas) return "redirect:/jogo"; 
        for (Jogador j : jogadoresSelecionados) {
            String chave = "aposta_" + j.getId();
            if (apostas.containsKey(chave)) {
                try {
                    int aposta = Integer.parseInt(apostas.get(chave));
                    if (aposta >= 2 && aposta <= 12) {
                        j.setAposta(aposta);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }

        apostasRegistradas = true;
        return "redirect:/jogo";
    }

    @GetMapping("/jogo/rolar")
    public String rolarDados(Model model) {
        if (!rolou && apostasRegistradas) {
            Random random = new Random();
            dado1 = random.nextInt(6) + 1;
            dado2 = random.nextInt(6) + 1;
            int soma = dado1 + dado2;

            List<Jogador> vencedoresJogadores = jogadoresSelecionados.stream()
                    .filter(j -> j.getAposta() != null && j.getAposta() == soma)
                    .toList();

            if (vencedoresJogadores.isEmpty()) {
                vencedor = "💻 A máquina venceu! A soma foi " + soma;
            } else if (vencedoresJogadores.size() == jogadoresSelecionados.size()) {
                vencedor = "🎉 Todos os jogadores venceram! (Soma: " + soma + ")";
                vencedoresJogadores.forEach(j -> {
                    j.setVitorias(j.getVitorias() + 1);
                    jogadorRepository.save(j);
                });
            } else {
                vencedoresJogadores.forEach(j -> {
                    j.setVitorias(j.getVitorias() + 1);
                    jogadorRepository.save(j);
                });
                String nomesVencedores = vencedoresJogadores.stream()
                        .map(Jogador::getNome)
                        .collect(Collectors.joining(", "));
                vencedor = "🎉 Vencedor(es): " + nomesVencedores + " (Soma: " + soma + ")";
            }

            rolou = true;
        }

        model.addAttribute("jogadoresEscolhidos", jogadoresSelecionados);
        model.addAttribute("dado1", dado1);
        model.addAttribute("dado2", dado2);
        model.addAttribute("vencedor", vencedor);
        model.addAttribute("apostasRegistradas", apostasRegistradas);
        model.addAttribute("rolou", rolou);
        return "jogo";
    }

    @PostMapping("/jogo/novamente")
    public String jogarNovamente(@RequestParam("acao") String acao) {
        if (acao.equals("nao")) {
            jogadoresSelecionados.forEach(j -> {
                j.setAposta(null);
                jogadorRepository.save(j);
            });
            jogadoresSelecionados.clear();
            dado1 = 0;
            dado2 = 0;
            vencedor = "";
            apostasRegistradas = false;
            rolou = false;
            return "redirect:/Jogadores";
        }

        jogadoresSelecionados.forEach(j -> j.setAposta(null));
        dado1 = 0;
        dado2 = 0;
        vencedor = "";
        apostasRegistradas = false;
        rolou = false;
        return "redirect:/jogo";
    }

    @GetMapping("/jogo/voltar")
    public String voltarParaJogadores() {
        jogadoresSelecionados.forEach(j -> j.setAposta(null));
        jogadoresSelecionados.clear();
        dado1 = 0;
        dado2 = 0;
        vencedor = "";
        apostasRegistradas = false;
        rolou = false;
        return "redirect:/Jogadores";
    }

}
