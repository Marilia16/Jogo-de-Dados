package com.example.jogodedados.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class JogoService {

    private final Random random = new Random();

    public int[] rolarDados() {
        int dado1 = random.nextInt(6) + 1;
        int dado2 = random.nextInt(6) + 1;
        return new int[]{dado1, dado2};
    }
}
