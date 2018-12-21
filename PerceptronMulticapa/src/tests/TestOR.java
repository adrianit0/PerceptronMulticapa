/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import redNeuronal.Perceptron;

/**
*
* Test de OR:
* A partir de X iteracciones (Por defecto: 10000), la red neuronal debe aprender como funciona
* una puerta lógica OR.
* Usará 2 inputs, 1 capa oculta con 2 neuronas y un output
* 
* Valores deseados de aprendizaje:
* 0  0  | 0
* 0  1  | 1
* 1  0  | 1
* 1  1  | 1
* 
*  @author Adrián
*/
public class TestOR {
    private Perceptron net;
    private int iteracciones;
    
    // debuggearlo?
    private final boolean debug = false;
    
    public TestOR () {
        this.iteracciones=10000;
    }
    
    public TestOR (int iteracciones) {
        this.iteracciones=iteracciones;
    }

    public void start() {
        int[] capas = new int[]{2, 2, 1};

        net = new Perceptron(capas);

        /* Aprendiendo */
        for (int i = 0; i < iteracciones; i++) {
            double[] _inputs = new double[]{Math.round(Math.random()), Math.round(Math.random())};
            double[] _output = new double[1];
            double error;

            // Si no cumple con la tabla devuelve 0, si cumple devolverá 1
            if (_inputs[0] == 0 && _inputs[1] == 0) {
                _output[0] = 0f;
            } else {
                _output[0] = 1f;
            }

            if (debug) {
                System.out.println(_inputs[0] + " or " + _inputs[1] + " = " + _output[0]);
            }

            error = net.backPropagation(_inputs, _output);
            if (debug) {
                System.out.println("Error en el paso " + i + ": " + error);
            }
        }

        System.out.println("APRENDIZAJE PARA TEST OR COMPLETADO:");
        net.comprobarValores();

        /* Test */
        test(new double[]{0, 0});
        test(new double[]{0, 1});
        test(new double[]{1, 0});
        test(new double[]{1, 1});
    }

    private void test(double[] inputs) {
        double[] output = net.Ejecutar(inputs);

        System.out.println(inputs[0] + " || " + inputs[1] + " = " + Math.round(output[0]));
    }
    
    public static void main(String[] args) {
        (new TestOR()).start();
    }
}
