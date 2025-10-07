package com.example.jogodedados.model;

import jakarta.persistence.*;

@Entity
@Table(name = "jogador") // garante que a tabela será "jogador"
public class Jogador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private Integer vitorias = 0;

    private Integer aposta;

    // Getters e Setters
    public Jogador() {}

    public Jogador(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.vitorias = 0;
        this.aposta = null;
    }


        // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getVitorias() { return vitorias; }
    public void setVitorias(int vitorias) { this.vitorias = vitorias; }

    public Integer getAposta() { return aposta; }
    public void setAposta(Integer aposta) { this.aposta = aposta; }
}
