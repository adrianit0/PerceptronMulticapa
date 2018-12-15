/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import redNeuronal.Perceptron;

/**
 *
 * Test de OR: A partir de X iteracciones (Por defecto: 10000), la red neuronal
 * debe aprender como funciona una puerta lógica OR. Usará 2 inputs, 1 capa
 * oculta con 2 neuronas y un output
 *
 * Valores deseados de aprendizaje: 0 0 | 0 0 1 | 1 1 0 | 1 1 1 | 0
 *
 * @author Adrián
 */
public class TestXOR {

    private Perceptron net;
    private int iteracciones = 10000;

    // debuggearlo?
    private final boolean debug = false;

    public TestXOR() {
        this.iteracciones = 10000;
    }

    public TestXOR(int iteracciones) {
        this.iteracciones = iteracciones;
    }

    public void start() {
        int[] capas = new int[]{6, 8, 1};

        net = new Perceptron(capas);

        /* Aprendiendo */
        for (int i = 0; i < iteracciones; i++) {
            double x1 = Math.round(Math.random());
            double x2 = Math.round(Math.random());

            double nand1 = nand(x1, x2);

            double nand2 = nand(x1, nand1);
            double nand3 = nand(x2, nand1);

            double nand4 = nand(nand2, nand3);

            double[] _inputs = new double[]{x1, x2, nand1, nand2, nand3, nand4};
            double[] _output = new double[1];
            double error;

            // Si no cumple con la tabla devuelve 0, si cumple devolverá 1
            if (_inputs[0] == _inputs[1]) {
                _output[0] = 0f;
            } else {
                _output[0] = 1f;
            }

            if (debug) {
                System.out.println(_inputs[0] + " xor " + _inputs[1] + " = " + _output[0]);
            }

            error = net.backPropagation(_inputs, _output);
            if (debug) {
                System.out.println("Error en el paso " + i + ": " + error);
            }
        }

        System.out.println("APRENDIZAJE PARA TEST XOR COMPLETADO:");


        /* Test */
        test(new double[]{0, 0});
        test(new double[]{0, 1});
        test(new double[]{1, 0});
        test(new double[]{1, 1});
    }

    private double nand(double x1, double x2) {
        if (x1 == x2 && x1 == 1) {
            return 1;
        }

        return 0;
    }

    private void test(double[] inputs) {
        double x1 = inputs[0];
        double x2 = inputs[1];

        double nand1 = nand(x1, x2);

        double nand2 = nand(x1, nand1);
        double nand3 = nand(x2, nand1);

        double nand4 = nand(nand2, nand3);

        inputs = new double[]{x1, x2, nand1, nand2, nand3, nand4};

        double[] output = net.Ejecutar(inputs);

        System.out.println(inputs[0] + " xor " + inputs[1] + " = " + Math.round(output[0]));
    }
}
