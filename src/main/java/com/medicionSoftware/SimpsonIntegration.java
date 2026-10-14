package com.medicionSoftware;

public class SimpsonIntegration {

    private final double x;
    private final int dof;

    public SimpsonIntegration(double x, int dof) {
        if (!Double.isFinite(x) || x < 0) {
            throw new IllegalArgumentException("x debe ser un numero positivo o cero.");
        }
        if (dof < 1) {
            throw new IllegalArgumentException("DOF debe ser un entero mayor que cero.");
        }
        this.x = x;
        this.dof = dof;
    }

    public double computeW(int numSeg, double x) {
        if (numSeg <= 0 || numSeg % 2 != 0) {
            throw new IllegalArgumentException("La cantidad de segmentos debe ser par y positiva.");
        }
        return x / numSeg;
    }

    public double[] computeXi(int numSeg) {
        double w = computeW(numSeg, x);
        double[] xi = new double[numSeg + 1];
        for (int i = 0; i <= numSeg; i++) {
            xi[i] = i * w;
        }
        return xi;
    }

    public double[] computeFirstBaseTerms(int numSeg, double[] xi, int dof) {
        if (dof < 1 || xi == null || xi.length != numSeg + 1) {
            throw new IllegalArgumentException("Datos incorrectos para calcular los terminos.");
        }
        double[] base = new double[numSeg + 1];
        for (int i = 0; i <= numSeg; i++) {
            base[i] = 1.0 + (xi[i] * xi[i]) / dof;
        }
        return base;
    }

    public double computeExponent(int dof) {
        if (dof < 1) {
            throw new IllegalArgumentException("DOF debe ser positivo.");
        }
        return -(dof + 1.0) / 2.0;
    }

    public double computeCoefficient(int dof) {
        if (dof < 1) {
            throw new IllegalArgumentException("DOF debe ser positivo.");
        }
        GammaFunction gamma = new GammaFunction();
        double arriba = gamma.computeDblGamma((dof + 1.0) / 2.0);
        double abajo = Math.sqrt(dof * Math.PI) * gamma.computeDblGamma(dof / 2.0);
        return arriba / abajo;
    }

    public double[] computeFxi(int numSeg) {
        double[] xi = computeXi(numSeg);
        double[] base = computeFirstBaseTerms(numSeg, xi, dof);
        double exponente = computeExponent(dof);
        double coeficiente = computeCoefficient(dof);
        double[] fxi = new double[numSeg + 1];
        for (int i = 0; i <= numSeg; i++) {
            fxi[i] = coeficiente * Math.pow(base[i], exponente);
        }
        return fxi;
    }

    public double[] computeFinalTerms(int numSeg) {
        double[] fxi = computeFxi(numSeg);
        double[] terminos = new double[numSeg + 1];
        for (int i = 0; i <= numSeg; i++) {
            int multiplicador = 1;
            if (i > 0 && i < numSeg) {
                if (i % 2 == 0) {
                    multiplicador = 2;
                } else {
                    multiplicador = 4;
                }
            }
            terminos[i] = fxi[i] * multiplicador;
        }
        return terminos;
    }

    public double computeFinalValue(int numSeg) {
        double[] terminos = computeFinalTerms(numSeg);
        double suma = 0;
        for (int i = 0; i < terminos.length; i++) {
            suma = suma + terminos[i];
        }
        return suma * computeW(numSeg, x) / 3.0;
    }

}
