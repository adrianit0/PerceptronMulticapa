/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redNeuronal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 * @author Adrián 
 */
public class Neurona {
    
    /**
     * Manera para llamarlo con el toString()
     */
    private int capaID, neuronaID;
    
    /**
     * Valor actual de la neurona.
     * Es el valor que se le da al input, va pasando por todas las capas y llega hasta el output.
     */
    private double valorActual;
    
    /**
     * Enlaces entrantes de la otras neuronas a esta.
     * 
     * Aparte de la neurona de entrada, tiene un valor por cada ramificación a esta neurona.
     */
    private HashMap<Neurona, Double> enlacesEntrantes; 
    
    /**
     * Enlaces salientes de este neurona a otras neuronas.
     * 
     * Necesario para enseñarle al machine learning
     */
    private HashSet<Neurona> enlacesSalientes;
    
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
    public Neurona (int capaID, int neuronaID) {
        this.capaID=capaID;
        this.neuronaID=neuronaID;
        
        iniciarlizarValores();
        setEnlaces();
    }
    
    /**
     * Crea una neurona con todas las conexiones a otras neuronas
     */
    public Neurona(int capaID, int neuronaID, Neurona... neuronas) {
        this.capaID=capaID;
        this.neuronaID=neuronaID;
        
        iniciarlizarValores();
        setEnlaces(neuronas);
    }
    
    private void iniciarlizarValores() {
        enlacesEntrantes = new HashMap<>();
        enlacesSalientes = new HashSet<>();
        
        umbral = Math.random() / division;
        delta = Math.random() / division;
        valorActual = Math.random() / division;
    }
    
    /**
     * Hace todos los calculos que tenga que hacer la neurona y lo devuelve.
     * Esta manera hace más sencilla y facil de usar el perceptrón, reduciendo el código.
     * 
     * Lo que hace es coger todas las conexiones que tenga una neurona a todas las de capas anteriores
     * y multiplicarlo por el valor del enlace.
     * 
     * Ejemplo:                             <br>
     *                                      <br>
     * (0.5) ---- 0.3 ---- \                <br>
     *                                      <br>
     * (0.3) ---- 0.8 -------(   )          <br>
     *                                      <br>
     * (0.1) ---- 0.1 ---- /                <br>
     *                                      <br>
     * 0.5*0.3=0.15                         <br>
     * 0.3*0.8=0.24                         <br>
     * 0.1*0.1=0.01                         <br>
     *                                      <br>
     * 0.15+0.24+0.01 = 0.40                <br>
     * 
     * @return El valor total de todos los calculos
     * 
     */
    public double calcularValorEjecucion () {
        double valor = 0;
        for (Map.Entry<Neurona, Double> neurona : enlacesEntrantes.entrySet()){
            valor += neurona.getValue() * neurona.getKey().getValor();
        }
        
        return valor;
    }
    
    public double calcularErrorBackPropagation () {
        double error = 0;
        for (Neurona neurona : enlacesSalientes) {
            error += neurona.getDelta() * neurona.getEnlace(this);
        }
        
        return error;
    }
    
    public void calcularPesosBackPropagation (double rate) {
        for (Neurona neurona : enlacesEntrantes.keySet()) {
            aumentarPesoEnlace(neurona, rate * getDelta() * neurona.getValor());
        }
    }
    
    public double getValor() {
        return valorActual;
    }

    public double getEnlace(Neurona index) {
        return enlacesEntrantes.get(index);
    }
    
    public double[] getEnlaces () {
        double[] _enlaces = new double [enlacesEntrantes.size()];
        
        int i = 0;
        for (Double v : enlacesEntrantes.values()) {
            _enlaces[i]=v;
            i++;
        }
        
        return _enlaces;
    }
    
    public HashMap<Neurona, Double> getEnlacesEntrantes () {
        return this.enlacesEntrantes;
    }

    public int getLengthEnlace() {
        return enlacesEntrantes.size();
    }
    
    public int getLengthEnlaceSalientes() {
        return enlacesSalientes.size();
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
     * Revisión: Ahora se puede incluir todos los enlaces a otras neuronas que quiera
     * siempre y cuando esta neurona no esté en la misma capa o en una superior.
     * 
     */
    public void setEnlaces (Neurona... neuronas) {
        for(Neurona n: neuronas)
            addEnlace(n);
    }
    
    /**
     * Añade un enlace a la neurona. 
     * 
     * Este enlace debe estar en la misma posición de una neurona existente en 
     * la capa anterior.
     * 
     * La inicializa a un valor cercano a 0
     * 
     * TO DO: Hacer la restricción para no poder añadir neuronas en la misma capa o en la superior
     */
    public void addEnlace (Neurona neurona) {
        enlacesEntrantes.put(neurona, Math.random() / division);
        // Añadimos el enlace saliente de esta neurona a la de destino
        neurona.addEnlaceSaliente(this);
    }
    
    private void addEnlaceSaliente (Neurona neurona) {
        enlacesSalientes.add(neurona);
    }
    
    public boolean hasEnlace (int index) {
        return enlacesEntrantes.containsKey(index);
    }

    public void setPesoEnlace(Neurona n, double valor) {
        enlacesEntrantes.replace(n, valor);
    }

    public void aumentarPesoEnlace(Neurona n, double aumento) {
        enlacesEntrantes.replace(n, enlacesEntrantes.get(n)+ aumento);
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

    public int getCapaID() {
        return capaID;
    }

    public int getNeuronaID() {
        return neuronaID;
    }
    
    @Override
    public String toString() {
        return "[C"+this.capaID+"-N"+this.neuronaID+"]";
    }

}
