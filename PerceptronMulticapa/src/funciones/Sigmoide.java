package funciones;

import redNeuronal.Funciones;

/**
 *
 * Función sigmoide, la función más famosa y facil de usar para redes neuronales
 * 
 * @author Adrián
 */
public class Sigmoide implements Funciones{
    
    /**
     * Función de evaluación:
     * 
     * P(t) = 1  /  1 + e^-t
     * 
     */
    @Override
    public double evaluar(double value) {
        return 1 / (1 + Math.exp(-value));
    }
    
    /**
     * Derivada de la función de evaluación:
     * 
     * y' = y(1-y)
     * 
     * Tiene un punto de inflexión en y=0
     * 
     */
    @Override
    public double evaluarDerivada(double value) {
        return (value - Math.pow(value, 2));
    }
}
