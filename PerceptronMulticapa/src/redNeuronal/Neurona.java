/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redNeuronal;

import java.util.HashMap;

/**
 *
 * @author Adrián 
 */
public class Neurona {
    // Valor actual de la neurona
    // Se pone en el input y se toma del output
    private double valorActual;
    
    // Enlaces: Valor por cada ramificación entrante de las neuronas anteriores a esta neurona.
    // Modificación: Ahora la selección de neuronas es especifica en vez de general.
    // En terminos de consumo de memoria y coste ciclomático ahora es mayor, pero ofrece
    // una ventaja que antes no tenia, y es la selección de enlaces en las neuronas.
    private HashMap<Neurona, Double> enlaces; 
    
    // Umbral: Modifica el valor resultado o impone un límite que se debe sobrepasar antes de propagarse a otra neurona. 
    // Esta función se conoce como función de activación. 
    private double umbral;
    
    //Error cuadrático medio o delta: Es la diferencia entre el valor actual y el esperado.
    // Lo usaremos para calcular y reducir el error final al realizar el "Back propagation" en el descenso por gradiente.
    private double delta;
    
    private final double division = 10000000000000.0;   // Una cantidad muy alta para que los valores no se inicien en 0 pero muy cercano a ello
    
    /**
    * Crea una neurona sin ninguna conexión.
    * No es recomendable utilizar esta neurona directamente en el perceptrón
    * Porque podría dar valores inconcluyentes al no tener ninguna conexión.
    */
    public Neurona () {
        enlaces = new HashMap<>();
        umbral = Math.random() / division;
        delta = Math.random() / division;
        valorActual = Math.random() / division;
        
        setPesos();
    }
    
    public Neurona(Neurona... neuronas) {
        enlaces = new HashMap<>();
        umbral = Math.random() / division;
        delta = Math.random() / division;
        valorActual = Math.random() / division;
        
        setPesos(neuronas);
    }
    public double getValor() {
        return valorActual;
    }

    public double getEnlace(Neurona index) {
        return enlaces.get(index);
    }

    public int getLengthEnlace() {
        return enlaces.size();
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
    
    /**
     * Crea un número limitado de enlaces.
     * 
     * El número máximo de enlaces debe ser igual al número de neuronas que
     * tiene la capa anterior, y su index debe ser [0, capaAnterior.length-1].
     */
    public void setPesos (Neurona... neuronas) {
        enlaces = new HashMap<Neurona, Double>();
        
        for(Neurona n: neuronas)
            enlaces.put(n, Math.random() / division);
    }
    
    /**
     * Añade un enlace a la neurona. 
     * 
     * Este enlace debe estar en la misma posición de una neurona existente en 
     * la capa anterior.
     * 
     * La inicializa a un valor 0
     */
    public void setPeso (Neurona neurona) {
        enlaces.put(neurona, Math.random() / division);
    }
    
    public boolean hasEnlace (int index) {
        return enlaces.containsKey(index);
    }

    public void setPesoEnlace(Neurona n, double valor) {
        enlaces.replace(n, valor);
    }

    public void aumentarPesoEnlace(Neurona n, double aumento) {
        enlaces.replace(n, enlaces.get(n)+ aumento);
    }

    public void setUmbral(double valor) {
        umbral = valor;
    }

    public void aumentarUmbral(double valor) {
        umbral += valor;
    }

    public void setDelta(double valor) {
        delta = valor;
    }

}
