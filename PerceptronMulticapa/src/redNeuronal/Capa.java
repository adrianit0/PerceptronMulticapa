package redNeuronal;

/**
 *
 * Es cada una de las capas de la red neuronal
 * 
 * Cada perceptrón está compuesto por 3 tipos de capas:
 *  - Inputs:       Es siempre la primera capa, y su valor es dado por el usuario
 *  - Outputs:      Es siempre la última capa, y su valor es el resultante de los inputs y las funciones realizadas dentro de la caja negra.
 *  - Caja negra:   Una cantidad ilimitada de capas que está entre las de entradas y salidas
 * 
 * @author Adrián
 */
public class Capa {

    private Neurona[] neuronas;

    /**
     * Capa de la neurona
     *
     */
    public Capa(int l, int prev) {
        neuronas = new Neurona[l];

        for (int j = 0; j < l; j++) {
            neuronas[j] = new Neurona(prev);
        }
    }

    public int getLength() {
        return neuronas.length;
    }

    public Neurona getNeurona(int index) {
        return neuronas[index];
    }
}
