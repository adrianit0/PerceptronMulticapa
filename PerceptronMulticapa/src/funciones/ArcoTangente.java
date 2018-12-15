/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funciones;

import redNeuronal.Funciones;

/**
 *
 * @author Adrián
 */
public class ArcoTangente implements Funciones {
    /**
     * Función de evaluación:
     * 
     * P(y) = atan(y)
     * 
     */
    @Override
    public double evaluar(double value) {
        return Math.atan(value);
    }
    
    /**
     * Derivada de la función de evaluación:
     * 
     * y' = 1 /(y^2 + 1)
     * 
     * Tiene un punto de inflexión en y=0
     * 
     */
    @Override
    public double evaluarDerivada(double value) {
        return 1 / (Math.pow(value, 2)+1);
    }
}
