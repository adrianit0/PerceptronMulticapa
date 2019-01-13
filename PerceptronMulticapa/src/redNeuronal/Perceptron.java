package redNeuronal;

import excepciones.*;
import funciones.*;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import util.Posicion;

/**
 *
 * @author Adrián
 */
public class Perceptron {

    private double rateAprendizaje = 0.6;
    private ArrayList<Capa> capas;
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

        this.capas = new ArrayList<>(valorCapas.length);

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

        this.capas = new ArrayList<>(valorCapas.length);

        crearCapas(valorCapas);
    }
    
    public Perceptron(int[] valorCapas, boolean crearEnlaces) {
        this.rateAprendizaje = 0.6;
        this.funcionTransferencia = new Sigmoide();

        this.capas = new ArrayList<>(valorCapas.length);

        crearCapas(valorCapas, crearEnlaces);
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

        this.capas = new ArrayList<>();

        crearCapas(capasPorDefecto);
    }

    private void crearCapas(int[] valorCapas, boolean crearEnlaces) {
        for (int i = 0; i < valorCapas.length; i++) {
            if (i != 0 && crearEnlaces) {
                this.capas.add(new Capa(i, valorCapas[i], this.capas.get(i - 1)));
            } else {
                this.capas.add(new Capa(i, valorCapas[i]));
            }
        }
    }
    
    private void crearCapas(int[] valorCapas) {
        crearCapas (valorCapas, true);
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
    *
    * @param input El input, su tamaño debe coincidir con el valor inicial de entradas dadas.
    * @throws excepciones.IncorrectInputSize
    *
    */
    public double[] Ejecutar(double[] input) {
        // Comprobamos si el input dado es del mismo tamaño que el input real del perceptrón
        int size = getInputLayerSize();
        if (size != input.length) {
            throw new IncorrectInputSize();
        }
        
        // Inicializamos todas las variables a usar:
        double[] output = new double[getOutputLayerSize()];
        Capa capa = capas.get(0);
        double nuevoValor;

        // Añadimos los valores al input
        for (int i = 0; i < size; i++) {
            capa.getNeurona(i).setValor(input[i]);
        }
        
        // Modificamos la caja negra, incluyendo el output
        size=capas.size();
        for (int k = 1; k < size; k++) {
            capa = capas.get(k);
            for  (Neurona neurona : capa.getNeuronas()) {
                // Calculamos de la neurona
                // (Multiplicación del valor de cada neurona conectada a esta multiplicado por el valor del enlace)
                nuevoValor=neurona.calcularValorEjecucion();
                // Añadimos el valor umbral de esta neurona
                nuevoValor+=neurona.getUmbral();
                // Ponemos el valor, calculandolo usando nuestra función de transferencia
                neurona.setValor(funcionTransferencia.evaluar(nuevoValor));
            }
        }

        // Devolvemos el output
        capa = capas.get(capas.size()-1);
        size=getOutputLayerSize();
        for (int i = 0; i < size; i++) {
            output[i] = capa.getNeurona(i).getValor();
        }

        return output;
    }

    /**
     *
     * Algoritmo de Back propagation o propagación trasera usando el descenso por gradiente. 
     * Es el usado para que la red neuronal "aprenda" a base de valores correctos
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
     * @throws excepciones.IncorrectInputSize Cuando pones un length del input diferente al del perceptrón
     * @throws excepciones.IncorrectOutputSize Cuando pones un length del output diferente al del perceptrón
     *
     */
    public double backPropagation(double[] input, double[] output) {
        int inputLayerSize = getInputLayerSize();
        int outputLayerSize = getOutputLayerSize();
        
        if (inputLayerSize != input.length) {
            throw new IncorrectInputSize("Número incorrecto de input: tuSize:" + input.length + " correctSize:" + inputLayerSize);
        } else if (outputLayerSize != output.length) {
            throw new IncorrectOutputSize("Número incorrecto de output: tuSize:" + output.length + " correctSize:" + outputLayerSize);
        }

        double[] outputEjecutado = Ejecutar(input);
        double error;

        /**
         * Calculamos el error de los outputs.
         * 
         * La manera de calcularlo es ver la diferencia entre el valor real y el output que se dió al ejecutarse
         * Y multiplicarlo por la derivada de la función de trasferencia dando como valor la salida ejecutada.
         * 
         * Esta manera de hacerlo se llama "Descenso por gradiente", y busca encontrar el mínimo aceptable a partir de pequeños ajustes.
         */
        Capa cAhora = capas.get(capas.size()-1);
        for (int i = 0; i < outputLayerSize; i++) {
            error = output[i] - outputEjecutado[i];
            cAhora.getNeurona(i).setDelta(error * funcionTransferencia.evaluarDerivada(outputEjecutado[i]));
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
        Capa cDespues = capas.get(capas.size()-1);
        for (int k = capas.size() - 2; k >= 0; k--) {
            cAhora = capas.get(k);
            
            // Calcula el error de la capa actual y recalcula los delta (Error cuadrático)
            for (Neurona neurona : cAhora.getNeuronas()) {
                // Calcula el error de las neuronas salientes
                error = neurona.calcularErrorBackPropagation();
                // Realiza la multiplicación
                neurona.setDelta(error * funcionTransferencia.evaluarDerivada(neurona.getValor()));
            }

            // Actualiza los pesos de la capa posterior a esta y modifica un poco el umbral
            for (Neurona nDespues : cDespues.getNeuronas()) {
                // Calcula los pesos de todas las neuronas.
                nDespues.calcularPesosBackPropagation(rateAprendizaje);
                // Modifica un poco el umbral partiendo del error cuadrático y el rate
                nDespues.aumentarUmbral(rateAprendizaje * nDespues.getDelta());
            }
            
            cDespues = cAhora;
        }

        // Calculamos el error final medio (El que será devuelto)
        error = 0;

        // El error se calculará sumando la diferencia entre los valores anteriores y el nuevo
        // y dividiendolo entre el contenido para así conseguir la media
        for (int i = 0; i < output.length; i++) {
            error += Math.abs(outputEjecutado[i] - output[i]);
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
            throw new NotImplementedException("No implementado aún");
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
            throw new NotImplementedException("No implementado aún");
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
     * Asigna una nueva función de transferencia para evaluar el programa 
     * Normalmente se utiliza la función sigmoide no por ser la mejor si no por ser la más fácil
     */
    public void setTransferFunction(Funciones funcion) {
        funcionTransferencia = funcion;
    }

    /**
     * Devuelve el tamaño del input.
     */
    public int getInputLayerSize() {
        return capas.get(0).getLength();
    }

    /**
     * Devuelve el tamaño del output.
     */
    public int getOutputLayerSize() {
        return capas.get(capas.size() - 1).getLength();
    }

    /**
     * Devuelve el número de capas que tiene el perceptrón.
     */
    public int getCapaLength() {
        return capas.size();
    }

    /**
     * Devuelve la cantidad de neuronas que tiene una capa.
     */
    public int getCantidadNeuronasDesdeCapa(int capa) {
        return capas.get(capa).getLength();
    }

    /**
     * Devuelve una neurona a partir de una capa y un índice.
     */
    public Neurona getNeuronaDesdeCapa(int capa, int index) {
        return capas.get(capa).getNeurona(index);
    }
    
    public boolean existeNeurona (int capa, int neurona) {
        if (capas==null||capa<0||capa>=capas.size())
            return false;
        
        int size = capas.get(capa).getLength();
        if (neurona<0||neurona>=size)
            return false;
        
        return true;
    }
    
    // FUNCIONES DE CONFIGURACION: No usar si no se sabe usar correctamente
    
    /**
     * Añade todos los enlaces en todas las neuronas de una capa.
     */
    public void addTodosLosEnlacesEnCapa (int capa) {
        if (capa<=0 || capa>=capas.size()) {
            // No puede añadir los enlaces ya que tiene que ser la capa 1 o superior
            return;
        }
        
        Neurona[] neuronasEntrantes = capas.get(capa).getNeuronas();
        Neurona[] neuronasSalientes = capas.get(capa-1).getNeuronas();
        
        // Añadimos todas las neuronas de entrada
        for (Neurona nAhora : neuronasEntrantes) {
            for (Neurona nAntes : neuronasSalientes) {
                nAhora.addEnlace(nAntes);
            }
        }
    }
    /**
     * Añade todos los enlaces en una neurona.
     * 
     * TODO: Hacer
     */
    public void addTodosLosEnlacesEnNeurona (int capa, int neurona) {
        if (capa<=0 || capa>=capas.size()) {
            // No puede añadir los enlaces ya que tiene que ser la capa 1 o superior
            return;
        }
        
        throw new NotImplementedException ();
    }
    
    public void addEnlaceToNeurona(Posicion entrada, Posicion... salidas) {
        if (!existeNeurona(entrada.getKey(), entrada.getValue())) {
            // No existe la neurona
            return;
        }
        
        Neurona neuronaEntrante = capas.get(entrada.getKey()).getNeurona(entrada.getValue());
        Neurona neuronaSaliente;
        for (Posicion p : salidas) {
            if (!existeNeurona(p.getKey(), p.getValue())) 
                continue; // No existe salida
            
            neuronaSaliente = capas.get(p.getKey()).getNeurona(p.getValue());
            
            if (neuronaEntrante.getCapaID()<=neuronaSaliente.getCapaID()) {
                System.out.println("NO SE PUEDE PONER EL ENLACE");
                continue; // No se suele poner una capa
            }
                
            
            neuronaEntrante.addEnlace(neuronaSaliente);
        }
    }
    
    // Testeo
    public void comprobarEnlaces (){
        int indexC = 0;
        int indexN = 0;
        for (Capa c : capas){
            System.out.println("CAPA " + indexC+":");
            for (Neurona n : c.getNeuronas()) {
                System.out.println("\t- Neurona "+indexN+": Entrantes: "+n.getLengthEnlace()+" Salientes: "+n.getLengthEnlaceSalientes());
                
                indexN++;
            }
            indexC++;
            indexN=0;
        }
    }
    
    /**
     *  Comprueba todos los valores de una red neuronal, para observar, mediante texto si todo
     *  funciona como se desea.
     * 
     * TODO: Mirar si no tiene ninguna conexión de entrada y de salida.
     * 
     */
    public void comprobarValores () {
        int indexC = 0;
        int indexN = 0;
        DecimalFormat formater = new DecimalFormat("0.00");
        for (Capa c : capas){
            System.out.println("CAPA " + indexC+":");
            for (Neurona n : c.getNeuronas()) {
                System.out.println("    - Neurona " +indexN+":");
                System.out.println("         Valor: "+formater.format(n.getValor())+
                        "\n         Umbral: "+formater.format(n.getUmbral()) + 
                        "\n         Error: "+formater.format(n.getDelta())+": ");
                
                System.out.println("         Salientes: " +n.getLengthEnlaceSalientes());
                System.out.println("         Entrantes: "+ n.getLengthEnlace());
                if (indexC>0) {
                    HashMap<Neurona, Double> enlaces = n.getEnlacesEntrantes();
                    for (Map.Entry<Neurona, Double> v : enlaces.entrySet()) {
                        System.out.println("           " +v.getKey() + " > "+ formater.format(v.getValue()));
                    }
                }
                
                indexN++;
            }
            indexC++;
            indexN=0;
        }
    }
}
