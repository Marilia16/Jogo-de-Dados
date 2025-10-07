package com.example.jogodedados.repository;

import com.example.jogodedados.model.Jogador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JogadorRepository extends JpaRepository<Jogador, Integer> {

    // Método para buscar jogador pelo email
    Optional<Jogador> findByEmail(String email);
}