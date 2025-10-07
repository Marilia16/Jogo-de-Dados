package com.example.jogodedados.repository;

import com.example.jogodedados.model.JogadoresEscolhidos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JogadoresEscolhidosRepository extends JpaRepository<JogadoresEscolhidos, Integer> {}
