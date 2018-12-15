package funciones;

import redNeuronal.Funciones;

/**
 *
 * @author Adrián
 */
public class TangenteHiperbolica implements Funciones {

    @Override
    public double evaluar(double valor) {
        return Math.tanh(valor);
    }

    @Override
    public double evaluarDerivada(double valor) {
        return 1 - Math.pow(Math.tanh(valor), 2);
    }
    
}
