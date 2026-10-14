package com.medicionSoftware;

public class GammaFunction {

    // Gamma(n) = (n - 1)! para n entero positivo.
    public double computeIntGamma(int value) {
        if (value < 1) {
            throw new IllegalArgumentException("Gamma necesita un valor positivo.");
        }

        double resultado = 1.0;
        for (int i = 1; i < value; i++) {
            resultado *= i;
        }
        return resultado;
    }

    // En este ejercicio se necesitan enteros y valores terminados en .5.
    public double computeDblGamma(double value) {
        if (!Double.isFinite(value) || value <= 0 ||
                Math.abs(value * 2 - Math.rint(value * 2)) > 0.00000001) {
            throw new IllegalArgumentException("Gamma solo admite enteros y medios positivos.");
        }

        if (value == 0.5) {
            return Math.sqrt(Math.PI);
        }
        if (value == 1.0) {
            return 1.0;
        }
        return (value - 1.0) * computeDblGamma(value - 1.0);
    }
}
