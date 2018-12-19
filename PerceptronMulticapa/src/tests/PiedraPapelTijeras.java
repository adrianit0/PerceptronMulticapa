/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import redNeuronal.Perceptron;

/**
*
* Test de Piedra Papel Tijeras:
* A partir de X iteracciones (Por defecto: 10000), la red neuronal debe aprender como se juega
* al piedra papel tijeras y a partir de ahí saber que movimiento jugar
* Usará 3 inputs, X capas negras y 3 outputs
* 
* Valores deseados de aprendizaje:
* 1  0  0  (Piedra)  | 0  0  1 (Tijeras)
* 0  1  0  (Papel)   | 1  0  0 (Piedra)
* 0  0  1  (Tijeras) | 0  1  0 (Papel)
* 
* Cualquier otro valor devolverá (0, 0, 0) = NULL
* 
*  @author Adrián
*/
public class PiedraPapelTijeras {
    
    private enum ESTADO {
        piedra (0,2), papel(1,0), tijeras(2,1);
        
        int valor;
        int lose;
        
        ESTADO (int valor, int lose) {
            this.valor = valor;
            this.lose = lose;
        }
        
        public int getValor() {
            return valor;
        }
        
        public int getLose() {
            return lose;
        }
        
        public boolean comprobar (ESTADO e) {
            return e.getValor()==lose;
        }
        
        public double[] getValor (int v) {
            switch (v) {
                case 0:
                    return new double[]{1,1,1,1,1};
                case 1:
                    return new double[]{0,0,1,1,1};
                case 2:
                    return new double[]{0,0,0,0,1};
                default:
                    return new double[]{0,0,0,0,0};
            }
        }
        
        public double[] getValorOutput (int v) {
            switch (v) {
                case 2:
                    return new double[]{1,0,0};
                case 1:
                    return new double[]{0,1,0};
                case 0:
                    return new double[]{0,0,1};
                default:
                    return new double[]{0,0,0};
            }
        }
    };
    
    private Perceptron net;
    private int iteracciones = 10000;
    
    // debuggearlo?
    private final boolean debug = false;
    
    public PiedraPapelTijeras () {
        this.iteracciones=10000;
    }
    
    public PiedraPapelTijeras (int iteracciones) {
        this.iteracciones=iteracciones;
    }

    public void start() {
        int[] capas = new int[]{5, 5, 5, 3};

        net = new Perceptron(capas);

        /* Aprendiendo */
        ESTADO[] estados = ESTADO.values();
        for (int i = 0; i < iteracciones; i++) {
            int value = (int)Math.ceil(Math.random()*3);
            if (value>=3)
                value=2;
            ESTADO e = estados[value];
            
            double[] _inputs = e.getValor(e.getValor());
            double[] _output = e.getValorOutput(e.getLose());
            double error;

            // Si no cumple con la tabla devuelve 0, si cumple devolverá 1

            if (debug) {
                System.out.println(_inputs[0] + " and " + _inputs[1] + " = " + _output[0]);
            }

            error = net.backPropagation(_inputs, _output);
            if (debug) {
                System.out.println("Error en el paso " + i + ": " + error);
            }
        }

        System.out.println("APRENDIZAJE PARA TEST AND COMPLETADO:");
        

        /* Test */
        System.out.println("TU MOV  = SU MOV");
        test(ESTADO.piedra);
        test(ESTADO.papel);
        test(ESTADO.tijeras);
    }

    private void test(ESTADO e) {
        double[] inputs = e.getValor(e.getValor());
        
        double[] output = net.Ejecutar(inputs);
        
        for (int i = 0; i < output.length; i++) {
            output[i] = Math.round(output[i]);
        }
        
        ESTADO x = getEstadoOutput(output);
        
        if (x!=null)
            System.out.println(e.toString() + " = " + x.toString() + ": " + ((x.comprobar(e))?"Gana": "Pierde"));
        else
            System.out.println("El valor devuelto no es ni piedra ni papel ni tijeras :" + output[0] + ", " + output[1] +  ", " + output[2]);
    }
    
    private ESTADO getEstado  (double[] e) {
        if (e.length!=5) {
            return null;
        }
        
        if (e[0]==1)
            return ESTADO.piedra;
        else if (e[2]==1)
            return ESTADO.papel;
        else if (e[4]==1)
            return ESTADO.tijeras;
        
        return null;
    }
    
    private ESTADO getEstadoOutput  (double[] e) {
        if (e.length!=3) {
            return null;
        }
        
        if (e[0]==1)
            return ESTADO.piedra;
        else if (e[1]==1)
            return ESTADO.papel;
        else if (e[2]==1)
            return ESTADO.tijeras;
        
        return null;
    }
    
    public static void main(String[] args) {
        (new PiedraPapelTijeras()).start();
    }
}
