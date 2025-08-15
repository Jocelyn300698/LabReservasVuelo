/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package juego;

/**
 *
 * @author Admin
 */
public class TableroBuscaminas {
    Casilla [][] casillas;
    int numeroFilas;
    int numerColumnas;
    int numeroMinas;

    public TableroBuscaminas(int numeroFilas, int numerColumnas, int numeroMinas) {
        this.numeroFilas = numeroFilas;
        this.numerColumnas = numerColumnas;
        this. numeroMinas=numeroMinas;
        inicializarCasillas();
        generarMinas();
    }
    public void inicializarCasillas(){
        casillas=new Casilla[this.numeroFilas][this.numerColumnas];
        for(int i=0; i<casillas.length;i++){
            for(int j=0; j<casillas[i].length; j++){
                casillas[i][j]=new Casilla(i,j);
            }
        }
    }
    //Método para generar la cantidad de minas
    private void generarMinas(){
        int minasGeneradas=0;
        while(minasGeneradas!= numeroMinas){
            int posTempoFila=(int) (Math.random()*casillas.length);
            int posTempoColumna=(int)(Math.random()*casillas[0].length);
            if (casillas[posTempoFila][posTempoColumna].isMina()){
                casillas[posTempoFila][posTempoColumna].setMina(true);
                minasGeneradas++;
            }
        }
    }
    private void imprimirTablero(){
        for(int i=0; i<casillas.length;i++){
            for(int j=0; j<casillas[i].length; j++){
                System.out.print(casillas[i][j].isMina()?"*":"0");
            }
            System.out.println("");
        }
    }

}
