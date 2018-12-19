package redNeuronal;

import excepciones.*;
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
     * Un ejemplo de creación de un perceptrón es el siguiente: {2, 3, 1} para
     * una red con 2 inputs, 1 capa oculta en la caja negra con 3 neuronas y 1
     * output
     *
     *
     * @param valorCapas Número de capas que tendrá el perceptrón.
     * @param rateAprendizaje Ratio de aprendizaje.
     * @param funcion Función de la derivada a utilizar
     */
    public Perceptron(int[] valorCapas, double rateAprendizaje, Funciones funcion) {
        this.rateAprendizaje = rateAprendizaje;
        this.funcionTransferencia = funcion;

        this.capas = new Capa[valorCapas.length];

        crearCapas(valorCapas);
    }

    /**
     * Crea un perceptrón con valores de entradas predeterminadas: 
     * 
     * - Valor de aprendizaje: 0.6 
     * - Función de transferencia: Sigmoide
     */
    public Perceptron(int[] valorCapas) {
        this.rateAprendizaje = 0.6;
        this.funcionTransferencia = new Sigmoide();

        this.capas = new Capa[valorCapas.length];

        crearCapas(valorCapas);
    }

    /**
     * Crea un perceptrón con valores de entradas predeterminadas: 
     *  
     * - Capas: {2, 2, 1} 
     * - Valor de aprendizaje: 0.6 
     * - Función de transferencia: Sigmoide
     */
    public Perceptron() {
        this.rateAprendizaje = 0.6;
        this.funcionTransferencia = new Sigmoide();

        int[] capasPorDefecto = new int[]{2, 2, 1};

        this.capas = new Capa[capasPorDefecto.length];

        crearCapas(capasPorDefecto);
    }

    private void crearCapas(int[] valorCapas) {
        for (int i = 0; i < valorCapas.length; i++) {
            if (i != 0) {
                this.capas[i] = new Capa(valorCapas[i], this.capas[i - 1]);
            } else {
                this.capas[i] = new Capa(valorCapas[i]);
            }
        }
    }

    /**
     * Ejecuta la red neuronal dando unos valores de entradas.
     *
     * @param input El input, su tamaño debe coincidir con el valor inicial de
     * entradas dadas.
     * @throws excepciones.IncorrectInputSize
     *
     */
    public double[] Ejecutar(double[] input) {
        int i, j, k;
        double nuevoValor;

        double[] output = new double[capas[capas.length - 1].getLength()];

        // Comprobamos si el input dado es del mismo tamaño que el input real del perceptrón
        int tam = capas[0].getLength();
        if (tam != input.length) {
            throw new IncorrectInputSize();
        }

        // Añadimos los valores al input
        for (i = 0; i < tam; i++) {
            capas[0].getNeurona(i).setValor(input[i]);
        }

        /**
         * Ejecuta capas ocultas y el output.
         * Modificación: Para clarificar más el cogio se ha usado varios for in.
         * 
         * El coste computacional es más elevado aunque su complejidad ciclomática es menor:
         * 
         * Complejidad = C * nCaja² * nInput * nOutput
         * 
         * Donde:
         *  - C       -> Número de capas sin contar la caja del input
         *  - nCaja   -> Número medio de neuronas que hay en cada capa de la caja negra.
         *  - nInput  -> Número de neuronas que tiene el input.
         *  - nOutput -> Número de neuronas que tiene el output.
         */
        Capa cAntes = capas[0];
        for (k = 1; k < capas.length; k++) {
            Capa cAhora = capas[k];
            for  (Neurona nAhora : cAhora.getNeuronas()) {
                nuevoValor=0f;
                
                /**
                 * Realizar cambio:
                 *      - AHORA: Usar los enlaces de la capa anterior
                 *      - CREAR: Usar los enlaces del hashMap de la capa actual
                 */
                for (Neurona nAntes : cAntes.getNeuronas()) {
                    nuevoValor += nAhora.getEnlace(nAntes) * nAntes.getValor();
                }
                
                nuevoValor += nAhora.getUmbral();
                
                nAhora.setValor(funcionTransferencia.evaluar(nuevoValor));
            }
            cAntes = cAhora;
        }

        // Devuelve el output
        for (i = 0; i < capas[capas.length - 1].getLength(); i++) {
            output[i] = capas[capas.length - 1].getNeurona(i).getValor();
        }

        return output;
    }

    /**
     *
     * Algoritmo de Back propagation o propagación trasera. Es el usado para que
     * la red neuronal "aprenda" a base de valores correctos
     *
     * La etapa de aprendizaje es muy importante y tiene que dar valores
     * correctos si no la maquina dará valores no deseados posteriormente.
     *
     * Devolverá como valor el error, que será un valor entre 0 y 1. Contra más
     * alto sea el error más se verá afectado su comportamiento. Llegará un
     * momento en el que el perceptrón apenas aprenderá nada por cada
     * iteracción, por lo que su error será ínfimo.
     *
     * TO DO: La convergencia no está garantizada y es bastante lenta, hay que
     * usar como detención una regla entre los errores previos y actuales, e
     * incluir un número máximo de iteraciones.
     *
     * @param input El valor de entrada para un suceso
     * @param output El valor de salida deseada para los inputs dados
     * @return El error medio resultante de la propagación.
     * @throws excepciones.IncorrectInputSize Cuando pones un length del input
     * diferente al del perceptrón
     * @throws excepciones.IncorrectOutputSize Cuando pones un length del output
     * diferente al del perceptrón
     *
     */
    public double backPropagation(double[] input, double[] output) {
        if (capas[0].getLength() != input.length) {
            throw new IncorrectInputSize();
        } else if (capas[capas.length - 1].getLength() != output.length) {
            throw new IncorrectOutputSize("Número incorrecto de output: tuSize:" + output.length + " correctsize:" + capas[capas.length - 1].getLength());
        }

        double[] nOutput = Ejecutar(input);
        double error;

        // Calculamos el error del output
        for (int i = 0; i < capas[capas.length - 1].getLength(); i++) {
            error = output[i] - nOutput[i];
            capas[capas.length - 1].getNeurona(i).setDelta(error * funcionTransferencia.evaluarDerivada(nOutput[i]));
        }

        /**
         * Recalcula el delta (Error cuadratico), desde el penúltimo hasta la primera capa.
         * Modificación: Para clarificar más el código se han usado varios for in.
         * 
         * La complejidad ciclomática para enseñarle a un perceptrón es 3 veces lo que cuesta ejecutarlo:
         * 
         * Complejidad = (Capas*nCaja²*nInput*nOutput)² + nCaja²*nInput*nOutput
         * 
         * Donde:
         *  - Capas   -> Número de capas sin contar la caja del ouput
         *  - nCaja   -> Número medio de neuronas que hay en cada capa de la caja negra.
         *  - nInput  -> Número de neuronas que tiene el input.
         *  - nOutput -> Número de neuronas que tiene el output.
         */
        Capa cDespues = capas[capas.length-1];
        for (int k = capas.length - 2; k >= 0; k--) {
            Capa cAhora = capas[k];
            
            // Calcula el error de la capa actual y recalcula los delta (Error cuadrático)
            for (Neurona nAhora : cAhora.getNeuronas()) {
                error = 0f;
                for (Neurona nDespues : cDespues.getNeuronas()){
                    error += nDespues.getDelta() * nDespues.getEnlace(nAhora);
                }
                nAhora.setDelta(error * funcionTransferencia.evaluarDerivada(nAhora.getValor()));
            }

            // Actualiza los pesos de la siguiente capa y modifica un poco el umbral
            for (Neurona nDespues : cDespues.getNeuronas()) {
                for (Neurona nAhora : capas[k].getNeuronas()) {
                    nDespues.aumentarPesoEnlace(nAhora, rateAprendizaje * nDespues.getDelta() * nAhora.getValor());
                }
                nDespues.aumentarUmbral(rateAprendizaje * nDespues.getDelta());
            }
            
            cDespues = cAhora;
        }

        // Calculamos el error final medio (El que será devuelto)
        error = 0;

        // El error se calculará sumando la diferencia entre los valores anteriores y el nuevo
        // y dividiendolo entre el contenido
        for (int i = 0; i < output.length; i++) {
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
            throw new NotImplementedYed("No implementado aún");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            return false;
        }

        //return true;
    }

    /**
     *
     * Carga la red neuronal desde un archivo para poder ser utilizado. Su uso
     * es estático por lo que puede ser usado incluso sin haber instanciado la
     * variable
     *
     * @param path Fichero de entrada
     * @return El perceptrón cargado desde memoria
     *
     */
    public static Perceptron load(String path) throws PerceptronNotFoundException {
        //Si no existe ningun archivo en la ruta ignora la petición de carga.
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("El archivo " + f.getName() + " no existe");
            throw new PerceptronNotFoundException("No se ha encontrado el perceptrón en la ruta " + path);
        }

        try {
            // TODO: Cargar el contenido del perceptrón
            throw new NotImplementedYed("No implementado aún");
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            throw new PerceptronNotFoundException("Error al cargar el perceptrón: " + e);
        }
    }

    /**
     * Devuelve el rate de aprendizaje.
     */
    public double getRateAprendizaje() {
        return rateAprendizaje;
    }

    /**
     * Asigna el rate de aprendizaje. Un valor más alto hará que "aprenda" más
     * rápido pero con más errores. Contra más bajo sea más tardará en llegar al
     * valor óptimo, pero será más exacto.
     */
    public void setRateAprendizaje(double rate) {
        rateAprendizaje = rate;
    }

    /**
     * Asigna una nueva función para evaluar el programa Normalmente se utiliza
     * la función sigmoide no por ser la mejor si no por ser la más fácil
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
