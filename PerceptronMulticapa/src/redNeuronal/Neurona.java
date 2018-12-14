/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redNeuronal;

/**
 *
 * @author Adrián 
 */
public class Neurona {
    // Valor actual de la neurona
    // Se pone en el input y se toma del output
    private double valorActual;
    
    // Enlaces: Valor por cada ramificación saliente de la neurona a otras neuronas
    private double[] enlaces; 
    
    // Umbral: Modifica el valor resultado o impone un límite que se debe sobrepasar antes de propagarse a otra neurona. 
    // Esta función se conoce como función de activación. 
    private double umbral;
    
    //Error cuadrático medio o delta: Es la diferencia entre el valor actual y el esperado.
    // Lo usaremos para calcular y reducir el error final al realizar el "Back propagation" en el descenso por gradiente.
    private double delta;
    
    private final double division = 10000000000000.0;   // Una cantidad muy alta para que los valores no se inicien en 0 pero muy cercano a ello
    
    public Neurona(int prevLayerSize) {
        enlaces = new double[prevLayerSize];
        umbral = Math.random() / division;
        delta = Math.random() / division;
        valorActual = Math.random() / division;

        for(int i = 0; i < enlaces.length; i++)
            enlaces[i] = Math.random() / division;
    }

    public double getValor() {
        return valorActual;
    }

    public double getEnlace(int index) {
        return enlaces[index];
    }

    public int getLengthEnlace() {
        return enlaces.length;
    }

    public double getUmbral () {
        return umbral;
    }

    public double getDelta() {
        return delta;
    }

    public void setValor (double nValor) {
        valorActual =nValor;
    }

    public void setPesos(int index, double valor) {
        enlaces[index] = valor;
    }

    public void aumentarPesos(int index, double aumento) {
        enlaces[index] += aumento;
    }

    public void setBias(double valor) {
        umbral = valor;
    }

    public void aumentarBias(double valor) {
        umbral += valor;
    }

    public void setDelta(double valor) {
        delta = valor;
    }

}
