package com.example.jogodedados.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jogadores_escolhidos")
public class JogadoresEscolhidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer jogadorId;
    private String nome;
    private String email;
    private int aposta = 0;

    public JogadoresEscolhidos() {}

    public JogadoresEscolhidos(Integer jogadorId, String nome, String email) {
        this.jogadorId = jogadorId;
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getJogadorId() { return jogadorId; }
    public void setJogadorId(Integer jogadorId) { this.jogadorId = jogadorId; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAposta() { return aposta; }
    public void setAposta(int aposta) { this.aposta = aposta; }
}
