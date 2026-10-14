package com.medicionSoftware;

import java.util.Locale;

public class Logic {

    public double calcular(double x, int dof, boolean mostrarPasos) {
        return calcular(x, dof, 10, mostrarPasos);
    }

    public double calcular(double x, int dof, int segmentos, boolean mostrarPasos) {

        SimpsonIntegration simpson = new SimpsonIntegration(x, dof);

        if (segmentos <= 0 || segmentos % 2 != 0) {
            throw new IllegalArgumentException(
                "n debe ser un numero par positivo.");
        }

        GammaFunction gamma = new GammaFunction();
        double errorPermitido = 0.00001;

        if (mostrarPasos) {
            System.out.println("\n--- Calculo de Gamma ---");
            System.out.println("Grados de libertad (DOF): " + dof);

            System.out.printf(Locale.US,
                    "Gamma((DOF + 1) / 2) = %.8f%n",
                    gamma.computeDblGamma((dof + 1.0) / 2.0));

            System.out.printf(Locale.US,
                    "Gamma(DOF / 2)       = %.8f%n",
                    gamma.computeDblGamma(dof / 2.0));

            System.out.printf(Locale.US,
                    "Coeficiente t        = %.8f%n",
                    simpson.computeCoefficient(dof));
        }

        double anterior = simpson.computeFinalValue(segmentos);

        if (mostrarPasos) {
            System.out.println("\n--- Aproximaciones con la regla de Simpson ---");
            System.out.println("Error permitido: 0.00001");
            System.out.println("Iter. | Segmentos | Ancho W   | Integral p    | Diferencia");

            System.out.printf(Locale.US,
                    "%5d | %9d | %9.6f | %13.10f | --%n",
                    1, segmentos, simpson.computeW(segmentos, x), anterior);
        }

        // Duplicar segmentos hasta obtener la precision necesaria.
        for (int iteracion = 2; iteracion <= 11; iteracion++) {

            segmentos = segmentos * 2;

            double actual = simpson.computeFinalValue(segmentos);
            double diferencia = Math.abs(actual - anterior);

            if (mostrarPasos) {
                System.out.printf(Locale.US,
                        "%5d | %9d | %9.6f | %13.10f | %.10f%n",
                        iteracion, segmentos,
                        simpson.computeW(segmentos, x),
                        actual, diferencia);
            }

            if (diferencia < errorPermitido) {
                if (mostrarPasos) {
                    System.out.println(
                        "Precision alcanzada: diferencia < 0.00001");
                }
                return actual;
            }

            anterior = actual;
        }

        throw new IllegalStateException(
            "No se alcanzo la precision requerida.");
    }
}