/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juego;

/**
 *
 * @author Jocelyn y Tatiana
 */

public class TableroBuscaminas {
    private Casilla[][] casillas; // Matriz que guarda todas las casillas del tablero
    private int numeroFilas;
    private int numeroColumnas;
    private int numeroMinas;

    public TableroBuscaminas(int numeroFilas, int numeroColumnas, int numeroMinas) {
        this.numeroFilas = numeroFilas;
        this.numeroColumnas = numeroColumnas;
        this.numeroMinas = numeroMinas;
        inicializarCasillas();   // Creamos las casillas vacías
        generarMinas();          // Colocamos las minas en posiciones aleatorias
        contarMinasCercanas();   // Calculamos cuántas minas tiene cada casilla alrededor
    }

    // Crea las casillas del tablero con sus coordenadas
    private void inicializarCasillas() {
        casillas = new Casilla[this.numeroFilas][this.numeroColumnas];
        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j < numeroColumnas; j++) {
                casillas[i][j] = new Casilla(i, j);
            }
        }
    }

    // Coloca las minas en posiciones aleatorias sin repetir
    private void generarMinas() {
        int minasGeneradas = 0;
        while (minasGeneradas < numeroMinas) {
            int fila = (int) (Math.random() * numeroFilas);
            int col = (int) (Math.random() * numeroColumnas);
            // Si la casilla todavía no tiene mina, la ponemos
            if (!casillas[fila][col].isMina()) {
                casillas[fila][col].setMina(true);
                minasGeneradas++;
            }
        }
    }

    // Recorre todo el tablero y cuenta las minas adyacentes para cada casilla
    private void contarMinasCercanas() {
        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j < numeroColumnas; j++) {
                if (!casillas[i][j].isMina()) {
                    int contador = 0;
                    // Revisar las 8 direcciones alrededor
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            int nuevaFila = i + x;
                            int nuevaCol = j + y;
                            // Verificamos que esté dentro del tablero
                            if (nuevaFila >= 0 && nuevaFila < numeroFilas &&
                                nuevaCol >= 0 && nuevaCol < numeroColumnas &&
                                casillas[nuevaFila][nuevaCol].isMina()) {
                                contador++;
                            }
                        }
                    }
                    casillas[i][j].setMinasCercanas(contador);
                }
            }
        }
    }

    // Marca una casilla con bandera (X)
    public void marcar(int fila, int col) {
        Casilla c = casillas[fila][col];
        if (!c.isRevelada()) { // No podemos marcar una casilla destapada
            c.setMarcada(!c.isMarcada()); // Alterna entre marcada y desmarcada
        }
    }

    // Destapa una casilla. Devuelve true si es mina (para saber si se pierde).
    public boolean destapar(int fila, int col) {
        Casilla c = casillas[fila][col];
        // Si ya está revelada o marcada, no hacemos nada
        if (c.isRevelada() || c.isMarcada()) {
            return false;
        }
        c.setRevelada(true);

        // Si es mina, se pierde
        if (c.isMina()) {
            return true;
        }

        // Si no hay minas cercanas, destapamos alrededor (efecto "vacío")
        if (c.getMinasCercanas() == 0) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    int nuevaFila = fila + x;
                    int nuevaCol = col + y;
                    if (nuevaFila >= 0 && nuevaFila < numeroFilas &&
                        nuevaCol >= 0 && nuevaCol < numeroColumnas) {
                        destapar(nuevaFila, nuevaCol);
                    }
                }
            }
        }
        return false; // No era mina
    }

    // Revela todas las minas (se usa al perder)
    public void revelarTodasLasMinas() {
        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j < numeroColumnas; j++) {
                if (casillas[i][j].isMina()) {
                    casillas[i][j].setRevelada(true);
                }
            }
        }
    }

    // Verifica si todas las minas están correctamente marcadas
    public boolean todasMinasMarcadas() {
        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j < numeroColumnas; j++) {
                Casilla c = casillas[i][j];
                if (c.isMina() && !c.isMarcada()) {
                    return false; // Hay una mina sin marcar
                }
                if (!c.isMina() && c.isMarcada()) {
                    return false; // Hay una marca incorrecta
                }
            }
        }
        return true;
    }

    // Método de depuración: imprime el tablero en consola
    public void imprimirTableroDebug() {
        for (int i = 0; i < numeroFilas; i++) {
            for (int j = 0; j < numeroColumnas; j++) {
                if (casillas[i][j].isMina()) {
                    System.out.print("* ");
                } else {
                    System.out.print(casillas[i][j].getMinasCercanas() + " ");
                }
            }
            System.out.println();
        }
    }

    public Casilla getCasilla(int fila, int col) {
        return casillas[fila][col];
    }
}
