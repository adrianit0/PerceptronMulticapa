/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package builder;

import excepciones.NotImplementedYed;
import java.io.File;
import redNeuronal.Funciones;
import redNeuronal.Perceptron;

/**
 *
 * @author Adrián
 */
public class PerceptronBuilder {
    
    public static PerceptronBuilder INSTANCE;
    
    public static PerceptronBuilder getInstance() {
        if (INSTANCE==null)
            INSTANCE=new PerceptronBuilder();
        
        return INSTANCE;
    }
    
    public PerceptronBuilding create() {
        return new PerceptronBuilding();
    }
    
    public final class PerceptronBuilding {
        private Perceptron perceptron;
        
        // Solo se podrá crear el construir el constructor desde el builder
        private PerceptronBuilding(){
            perceptron = new Perceptron();
        }
        
        /**
         * Construye el perceptrón a partir de los valores generados
         */
        public Perceptron build() {
            return perceptron;
        }
        
        /**
         * Carga un perceptrón almacenado en memoria
         */
        public PerceptronBuilding fromFile (File file) {
            throw new NotImplementedYed("No implementado aún");
        }
        
        /**
         * Permite guardar el proyecto de construcción en un fichero al mismo tiempo
         * que construyes el perceptrón.
         */
        public PerceptronBuilding exportSchema (File file) {
            throw new NotImplementedYed("No implementado aún");
        }
        
        /**
         * Permite cargar un proyecto de construcción de perceptrón.
         */
        public PerceptronBuilding importSchema (File file) {
            throw new NotImplementedYed("No implementado aún");
        }
        
        /**
         * Cargas  un perceptrón a partir de otro.
         */
        public PerceptronBuilding clonePerceptron (Perceptron p) {
            throw new NotImplementedYed("No implementado aún");
        }
        
        /**
         * Añades una capa de abstracción en 2 inputs para que funcione el XOR
         * 
         * Lo que hará será crear 2 capas extras en la caja negra usando 3 NAND
         * 
         * ESQUEMA:
         * 
         *  INPUT    |     CAPA 1     |     CAPA 2     |  RESTO DE CAPAS >>
         * 
         * input1 ------------- NAND2
         *        \           /      \
         *         >  NAND1  <        >  NAND4 --------- OTRAS CAPAS
         *        /           \      /
         * input2 ------------- NAND3
         * 
         * Atención: El uso de este método hará que los valores sean diferente
         *          Pues en la primera capa negra pasará a tener un input menos
         * 
         * @param input1 El input 1
         * @param input2 El input 2
         */
        public PerceptronBuilding setXORCompatibility (int input1, int input2) {
            throw new NotImplementedYed("No implementado aún");
        }
        
        /**
         * Elimina toda compatibilidad creada previamente.
         * 
         * Eliminará las 2 capas de abstracción creada sin importar cuantas neuronas tuviese
         * 
         * Se recomienda hacer un "exportSchema" antes
         * 
         */
        public PerceptronBuilding removeXORCompatibily () {
            throw new NotImplementedYed("No implementado aún");
        }
        
        
        /**
         * Pones nuevas capas
         */
        public PerceptronBuilding setCapas (int[] capas) {
            throw new NotImplementedYed("No implementado aún");
        }
        
        /**
         * Configuras la función que desees
         */
        public PerceptronBuilding setFunction (Funciones f) {
            perceptron.setTransferFunction(f);
            return this;
        }
    }
}
