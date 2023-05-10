package com.opencode.minikeyvault;

/**
 * class: Launcher.
 * @author Henry Navarro
 *          <u>Cambios</u>:
 *          <ul>
 *          <li>2022-01-04 Creación del proyecto.</li>
 *          </ul>
 * @version 1.0
 */
public class Launcher {

    /**
     * Se implementa esta clase para que, a modo de artificio, sea la encargada de 
     * arrancar la aplicación desde el compilado (java -jar minikeyvault.jar), ya 
     * que, si se intenta hacer esto a través de la clase 'Initializer' directamente,
     * nos dará un error ('Error: faltan los componentes de JavaFX runtime y son  
     * necesarios para ejecutar esta aplicación'). Esto se debe a que ahora la aplicación  
     * trabaja con OpenJfx11 y OpenJdk11, los cuales están orientados a trabajar con 
     * modulos en vez de JARs. 
     * 
     * @param args argumentos.
     */
    public static void main(String[] args) {
        Initializer.main(args);
    }

}
