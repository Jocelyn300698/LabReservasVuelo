/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import ui.VentanaBuscaminas;

/**
 *
 * @author tatia
 */
public class Main {
    public static void main(String[] args) {
        // Ejecuta la interfaz gráfica en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            VentanaBuscaminas ventana = new VentanaBuscaminas();
            ventana.setVisible(true);
        });
    }
}
