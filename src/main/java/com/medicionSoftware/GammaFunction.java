package com.medicionSoftware;
public class GammaFunction {
public double computeIntGamma(int value) {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
        double resultado = 1.0;
        for (int i = 1; i < value; i++) {
            resultado = resultado * i;
        }
        return resultado;
    }
    public double computeDblGamma(double value) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException();
        }
        double doble = value * 2;
        if (doble % 1 != 0) {
            throw new IllegalArgumentException();
        }
        int entero = (int) value;
        if (value == entero) {
            return computeIntGamma(entero);
        }
        double impares = 1.0;
        for (int i = 1; i <= entero; i++) {
            int numeroImpar = 2 * i - 1;
            impares = impares * numeroImpar;
        }
        double potencia = 1.0;
        for (int i = 1; i <= entero; i++) {
            potencia = potencia * 2;
        }
        double raizPi = Math.sqrt(Math.PI);
        double numerador = impares * raizPi;
        double resultado = numerador / potencia;
        return resultado;
    }
}