package com.example.jogodedados.model;

import java.util.Random;

public class Dado {
    private int valor;
    private static final Random random = new Random();

    public int getValor() { return valor; }

    public int randomNumber() {
        this.valor = random.nextInt(6) + 1;
        return valor;
    }
}
