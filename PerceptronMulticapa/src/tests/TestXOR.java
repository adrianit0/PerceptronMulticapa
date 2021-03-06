/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import redNeuronal.Perceptron;
import util.Posicion;

/**
 *
 * Test de XOR: A partir de X iteracciones (Por defecto: 10000), la red neuronal
 * debe aprender como funciona una puerta lógica OR. Usará 2 inputs, 1 capa
 * oculta con 2 neuronas y un output
 *
 * Valores deseados de aprendizaje: 
 * 0 0 | 0 
 * 0 1 | 1 
 * 1 0 | 1 
 * 1 1 | 0
 *
 * @author Adrián
 */
public class TestXOR {

    private Perceptron net;
    private int iteracciones;

    // debuggearlo?
    private final boolean debug = false;

    public TestXOR() {
        this.iteracciones = 100000;
    }

    public TestXOR(int iteracciones) {
        this.iteracciones = iteracciones;
    }

    public void start() {
        int[] capas = new int[]{2,1,2,1};

        net = new Perceptron(capas,false);
        // Capa 3
        net.addTodosLosEnlacesEnCapa(3);
        // Capa 2
        net.addEnlaceToNeurona(new Posicion(2,0), new Posicion(0,0),new Posicion(1,0));
        net.addEnlaceToNeurona(new Posicion(2,1), new Posicion(0,1),new Posicion(1,0));
        // Capa 1
        net.addTodosLosEnlacesEnCapa(1);
        
        
        /* Aprendiendo */
        for (int i = 0; i < iteracciones; i++) {
            double x1 = Math.round(Math.random());
            double x2 = Math.round(Math.random());

            double[] _inputs = new double[]{x1, x2};
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

        inputs = new double[]{x1, x2};

        double[] output = net.Ejecutar(inputs);
        
        System.out.println(inputs[0] + " xor " + inputs[1] + " = " + Math.round(output[0]));
    }
    
    public static void main(String[] args) {
        (new TestXOR()).start();
    }
}
