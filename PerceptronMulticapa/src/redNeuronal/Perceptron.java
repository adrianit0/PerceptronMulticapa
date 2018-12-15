package redNeuronal;

import funciones.*;
import java.io.File;

/**
 *
 * @author Adrián
 */
public class Perceptron {

    private double rateAprendizaje = 0.6;
    private Capa[] capas;
    private Funciones funcionTransferencia;

    /**
     * Crea una red neuronal perceptrón multicapa.
     * 
     * Un ejemplo de creación de un perceptrón es el siguiente:
     *  {2, 3, 1} para una red con 2 inputs, 1 capa oculta en la caja negra con 3 neuronas y 1 output
     * 
     * 
     * @param capas Número de capas que tendrá el perceptrón.
     * @param rateAprendizaje Ratio de aprendizaje.
     * @param funcion Función de la derivada a utilizar
     */
    public Perceptron(int[] capas, double rateAprendizaje, Funciones funcion) {
        this.rateAprendizaje = rateAprendizaje;
        this.funcionTransferencia = funcion;

        this.capas = new Capa[capas.length];

        for (int i = 0; i < capas.length; i++) {
            if (i != 0) {
                this.capas[i] = new Capa(capas[i], capas[i - 1]);
            } else {
                this.capas[i] = new Capa(capas[i], 0);
            }
        }
    }
    
    /**
    * Crea un perceptrón con valores de entradas predeterminadas:
    *  - Valor de aprendizaje: 0.6
    *  - Función de transferencia: Sigmoide
    */
    public Perceptron(int[] capas) {
        this.rateAprendizaje = 0.6;
        this.funcionTransferencia = new Sigmoide();

        this.capas = new Capa[capas.length];

        for (int i = 0; i < capas.length; i++) {
            if (i != 0) {
                this.capas[i] = new Capa(capas[i], capas[i - 1]);
            } else {
                this.capas[i] = new Capa(capas[i], 0);
            }
        }
    }

    /**
     * Ejecuta la red neuronal dando unos valores de entradas.
     * 
     * @param input El input, su tamaño debe coincidir con el valor inicial de entradas dadas.
     * 
     */
    public double[] Ejecutar(double[] input) {
        int i,j,k;
        double nuevoValor;

        double[] output = new double[capas[capas.length - 1].getLength()];

        // Introduce los inputs
        for (i = 0; i < capas[0].getLength(); i++) {
            capas[0].getNeurona(i).setValor(input[i]);
        }

        // Ejecuta: Capas ocultas y la output
        for (k = 1; k < capas.length; k++) {
            for (i = 0; i < capas[k].getLength(); i++) {
                nuevoValor = 0f;
                for (j = 0; j < capas[k - 1].getLength(); j++) {
                    nuevoValor += capas[k].getNeurona(i).getEnlace(j) * capas[k - 1].getNeurona(j).getValor();
                }

                nuevoValor += capas[k].getNeurona(i).getUmbral();

                capas[k].getNeurona(i).setValor(funcionTransferencia.evaluar(nuevoValor));
            }
        }

        // Devuelve el output
        for (i = 0; i < capas[capas.length - 1].getLength(); i++) {
            output[i] = capas[capas.length - 1].getNeurona(i).getValor();
        }

        return output;
    }
    
    /**
     * 
     * Algoritmo de Back propagation o propagación trasera.
     * Es el usado para que la red neuronal "aprenda" a base de valores correctos
     * 
     * La etapa de aprendizaje es muy importante y tiene que dar valores correctos si no la maquina dará valores no deseados posteriormente.
     * 
     * Devolverá como valor el error, que será un valor entre 0 y 1. Contra más alto sea el error más se verá afectado su comportamiento.
     * Llegará un momento en el que el perceptrón apenas aprenderá nada por cada iteracción, por lo que su error será ínfimo.
     * 
     * TO DO:
     * La convergencia no está garantizada y es bastante lenta, hay que usar como detención una regla entre los errores previos y actuales,
     * e incluir un número máximo de iteraciones.
     * 
     * @param input El valor de entrada para un suceso
     * @param output El valor de salida deseada para los inputs dados
     * @return El error medio resultante de la propagación. 
     * 
     */
    public double backPropagation(double[] input, double[] output) {
        double[] nOutput = Ejecutar(input);
        double error;
        int i,j,k;
        
        // Calculamos el error del output
        for (i = 0; i < capas[capas.length - 1].getLength(); i++) {
            error = output[i] - nOutput[i];
            capas[capas.length - 1].getNeurona(i).setDelta(error * funcionTransferencia.evaluarDerivada(nOutput[i]));
        }

        for (k = capas.length - 2; k >= 0; k--) {
            // Calcula el error de la capa actual y recalcula los umbrales
            for (i = 0; i < capas[k].getLength(); i++) {
                error = 0f;
                for (j = 0; j < capas[k + 1].getLength(); j++) {
                    error += capas[k + 1].getNeurona(j).getDelta() * capas[k + 1].getNeurona(j).getEnlace(i);
                }

                capas[k].getNeurona(i).setDelta(error * funcionTransferencia.evaluarDerivada(capas[k].getNeurona(i).getValor()));
            }

            // Actualiza los pesos de la siguiente capa
            for (i = 0; i < capas[k + 1].getLength(); i++) {
                for (j = 0; j < capas[k].getLength(); j++) {
                    capas[k + 1].getNeurona(i).aumentarPesos(j, rateAprendizaje * capas[k + 1].getNeurona(i).getDelta() * capas[k].getNeurona(j).getValor());
                }
                capas[k + 1].getNeurona(i).aumentarBias(rateAprendizaje * capas[k + 1].getNeurona(i).getDelta());
            }
        }

        // Calculamos el error final medio (El que será devuelto)
        error = 0;
        
        // El error se calculará sumando la diferencia entre los valores anteriores y el nuevo
        // y dividiendolo entre el contenido
        for (i = 0; i < output.length; i++) {
            error += Math.abs(nOutput[i] - output[i]);

            //System.out.println(output[i]+": "+nOutput[i]);
        }

        error = error / output.length;
        return error;
    }
    
    /**
     *
     * Guarda una red neuronal en un archivo para poder ser usado posteriormente
     * 
     * @param path Valor de entrada
     * @return Devuelve si ha habido algún error en la generación del archivo
     */
    public boolean GuardarEnDisco(String path) {
        File f = new File(path);
        if (f.exists()) {
            System.out.println("El archivo " + f.getName() + " no existe");
            return false;
        }
        try {
            // TODO: Almacenar el perceptrón
            return false;
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        }

        //return true;
    }

    
    /**
     *
     * Carga la red neuronal desde un archivo para poder ser utilizado.
     * Su uso es estático por lo que puede ser usado incluso sin haber instanciado la variable
     * 
     * @param path Fichero de entrada
     * @return El perceptrón cargado desde memoria
     * 
     */
    public static Perceptron load(String path) {
        //Si no existe ningun archivo en la ruta ignora la petición de carga.
        File f = new File(path);
        if (f.exists()) {
            System.out.println("El archivo " + f.getName() + " no existe");
            return null;
        }

        try {

            // TODO: Cargar el contenido del perceptrón
            return null;
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            return null;
        }
    }

    /**
     * Devuelve el rate de aprendizaje.
     */
    public double getRateAprendizaje() {
        return rateAprendizaje;
    }

    /**
     * Asigna el rate de aprendizaje. Un valor más alto hará que "aprenda" más rápido pero con más errores.
     * Contra más bajo sea más tardará en llegar al valor óptimo, pero será más exacto.
     */
    public void setRateAprendizaje(double rate) {
        rateAprendizaje = rate;
    }

    /**
     * Asigna una nueva función para evaluar el programa
     * Normalmente se utiliza la función sigmoide no por ser la mejor si no por ser la más fácil
     */
    public void setTransferFunction(Funciones fun) {
        funcionTransferencia = fun;
    }

    /**
     * Devuelve el tamaño del input
     */
    public int getInputLayerSize() {
        return capas[0].getLength();
    }

    /**
     * Devuelve el tamaño del output
     */
    public int getOutputLayerSize() {
        return capas[capas.length - 1].getLength();
    }

    /**
     * Devuelve el número de capas que tiene el perceptrón
     */
    public int getCapaLength() {
        return capas.length;
    }

    /**
     * Devuelve la cantidad de neuronas que tiene una capa
     */
    public int getCantidadNeuronasDesdeCapa(int capa) {
        return capas[capa].getLength();
    }

    /**
     * Devuelve una neurona a partir de una capa y un índice.
     */
    public Neurona getNeuronaDesdeCapa(int capa, int index) {
        return capas[capa].getNeurona(index);
    }
}
