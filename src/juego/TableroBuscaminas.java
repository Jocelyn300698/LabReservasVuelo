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
    private int marcasColocadas; // Contador de banderas puestas

// Constructor usando tamaño L, genera un tablero LxL con 2*L minas
    public TableroBuscaminas(int L) {
        if (L <= 2) {
            throw new IllegalArgumentException("El tamaño del tablero debe ser mayor a 2");
        }
        this.numeroFilas = L;
        this.numeroColumnas = L;
        this.numeroMinas = 2 * L;
        this.marcasColocadas = 0;
        inicializarCasillas();
        generarMinas();
        contarMinasCercanas();
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

// Método para marcar/desmarcar casilla con bandera
    public void marcar(int fila, int col) {
        Casilla c = casillas[fila][col];
        if (!c.isRevelada()) {
            if (!c.isMarcada() && marcasColocadas >= numeroMinas) {
                // Ya se alcanzó el límite de marcas
                System.out.println("No se puede colocar más marcas, número máximo de minas alcanzado");
                return;
            }
            c.setMarcada(!c.isMarcada());
            if (c.isMarcada()) {
                marcasColocadas++;
            } else {
                marcasColocadas--;
            }
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
    public int getNumeroMinas() {
        return numeroMinas;
    }

    public int getMarcasColocadas() {
        return marcasColocadas;
    }

    public int getNumeroFilas() {
        return numeroFilas;
    }

    public int getNumeroColumnas() {
        return numeroColumnas;
    }
}
