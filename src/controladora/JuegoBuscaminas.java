/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controladora;

import javax.swing.JOptionPane;
import juego.TableroBuscaminas;

/**
 *
 * @author tatia
 */
public class JuegoBuscaminas {
    private TableroBuscaminas tablero;
    private int juegosJugados;
    private int juegosGanados;
    private int juegosPerdidos;
    
    public JuegoBuscaminas() {
        this.juegosJugados = 0;
        this.juegosGanados = 0;
        this.juegosPerdidos = 0;
    }
    
    public void nuevoJuego() {
        int L;
        do {
            String input = JOptionPane.showInputDialog("Ingrese el tamaño del tablero (L > 2):");
           if (input == null) return; // Si cancela
            L = Integer.parseInt(input);
        } while (L <= 2);

        int minas = 2 * L;
        tablero = new TableroBuscaminas(L);
        juegosJugados++;
    }

    public TableroBuscaminas getTablero() {
        return tablero;
    }

    public void partidaGanada() {
        juegosGanados++;
        JOptionPane.showMessageDialog(null, "¡Felicidades! Has ganado.");
    }

    public void partidaPerdida() {
        juegosPerdidos++;
        JOptionPane.showMessageDialog(null, "Boom! Has pisado una mina.", "Has perdido", JOptionPane.ERROR_MESSAGE);
    }

    public String estadisticas() {
        return "Partidas jugadas: " + juegosJugados + "\n" +
               "Ganadas: " + juegosGanados + "\n" +
               "Perdidas: " + juegosPerdidos;
    }
}
