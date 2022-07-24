/*
*Vamos a validar los numeros y las letras
 */
package Controlador;

import java.awt.event.KeyEvent;

/**
 * Esta parte vamos a validar los numeros con coma decimal y las letras
 */
public class ValidarLetrasyNumeros {

    /*Esta funcion valida numeros reales, es decir con un punto decimal*/
    public static void numerosReales(int numascii, String numdec, KeyEvent evt) {
        int tamaño = numdec.length();
        if (numascii >= 46 && numascii <= 57) {
            if (numascii == 46) {
                for (int i = 0; i <= tamaño; i++) {
                    //Aqui solo va a permitir un solo punto decimal
                    if (numdec.contains(".")) {
                        evt.setKeyChar((char) KeyEvent.VK_CLEAR);
                    }
                }
            }

            //no ingresará la barra divisoria /
            if (numascii == 47) {
                evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            }

        } else {
            //JOptionPane.showMessageDialog(null, "Ingrese numeros");
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            evt.consume();
        }
    }

    public static void numerosEnteros(int numascii, String numEnt, KeyEvent evt) {
        if (numascii >= 46 && numascii <= 57) {
            //No va a permitir el punto decimal
            if (numascii == 46) {
                evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            }
            //no ingresará la barra divisoria /
            if (numascii == 47) {
                evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            }

        } else {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            evt.consume();
        }
    }

    public static void letraSoloMayuscula(KeyEvent evt) {
        char caracter = evt.getKeyChar();
        if (Character.isLowerCase(caracter)) {
            evt.setKeyChar(Character.toUpperCase(caracter));
        }
    }
}
