/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import juego.Casilla;
import juego.TableroBuscaminas;



/**
 * VentanaBuscamina esta es la clase principal de la interfaz gráfica del juego Buscaminas.
 * Contiene el tablero, botones, estadísticas y manejo de eventos del usuario.
 *  @author Tatiana y Jocelyn
 */
public class VentanaBuscaminas extends javax.swing.JFrame {
    private TableroBuscaminas tablero; // Nuestra lógica del juego
    private JButton[][] botones;        // Matriz de botones que representa las casillas
    private int L;                  // Tamaño del tablero (LxL)
    private int minasTotales;           // 2 * L
    private int juegosGanados = 0;  //Contador de juegos ganados
    private int juegosPerdidos = 0; //Contador de juegos perdidos
    private int juegosJugados = 0;  //Contador de juegos jugados

    /**
     * Cpnstructor de la ventana
     */
    public VentanaBuscaminas() {
        initComponents(); 
        
       // Botón Nuevo Juego inicia un juego desde cero
        btnNuevoJuego.addActionListener(e -> iniciarNuevoJuego());
        
        // Botón Salir cierra la aplicación
        btnSalir.addActionListener(e -> System.exit(0));
        
        // Iniciar un primer juego automáticamente al abrir la ventana
        iniciarNuevoJuego();

    }
    /**
     * Método para pedir al usuario el tamaño del tablero
     */
    private int pedirTamañoTablero() {
        int L = 0;
        do {
            String input = JOptionPane.showInputDialog(this, "Ingrese el tamaño del tablero (L>2):", "Tamaño del tablero", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return 8; // Cancel → valor por defecto
            try {
                L = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                L = 0;
            }
        } while (L <= 2);
        return L;
    }

    /**
     * Método para iniciar un nuevo juego
     */
    private void iniciarNuevoJuego() {
        L = pedirTamañoTablero();
        minasTotales = 2 * L;   // Calcula minas según el  tamaño del tablero
        tablero = new TableroBuscaminas(L);  // Se crea la lógica del tablero
        juegosJugados++;  // Esto aumenta el contador de juegos jugados

        // Definir tamaño y disposición del panel de botones
        int tamBoton = 50;  // Tamaño fijo de cada botón
        panelTablero.setPreferredSize(new Dimension(L * tamBoton, L * tamBoton));
        panelTablero.setLayout(new GridLayout(L, L, 2, 2)); // Filas, columnas y separación
        panelTablero.removeAll(); // Limpiar cualquier botón anterior

        botones = new JButton[L][L]; // Se crea la matriz de botones

        // Crear cada botón del tablero
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 16)); 
                btn.setFocusPainted(false); // Quitar borde de selección por defecto
                botones[i][j] = btn;

                final int fila = i; // Necesario para usar dentro de lambdas, esto lo que hace es, cuando hagan click en ese boton, ejecuta manejarClick(fila, col)
                final int col = j;

                // Click izquierdo → destapar casilla
                btn.addActionListener(e -> manejarClick(fila, col));

                // Click derecho → marcar/desmarcar casilla
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent me) {
                        if (SwingUtilities.isRightMouseButton(me)) {
                            Casilla c = tablero.getCasilla(fila, col);
                            int marcasActuales = contarMarcasActuales();

                            if (!c.isMarcada() && marcasActuales >= minasTotales) {
                                JOptionPane.showMessageDialog(VentanaBuscaminas.this,
                                        "No puedes marcar más casillas que el número de minas.");
                                return;
                            }

                            tablero.marcar(fila, col);
                            actualizarTablero();
                        }
                    }
                });

                panelTablero.add(btn);
            }
        }

        panelTablero.revalidate();
        panelTablero.repaint();
        actualizarLabels();
        lblMensaje.setText("¡Bienvenido!");
    }
    
    /**
     * Método para manejar click izquierdo sobre una casilla
     */
    private void manejarClick(int fila, int col) {
        boolean mina = tablero.destapar(fila, col); // Destapar casilla y verificar si es mina

        if (mina) {  // Si es mina, el jugador pierde
            tablero.revelarTodasLasMinas(); // Muestra todas las minas
            actualizarTablero();
            juegosPerdidos++;
            actualizarLabels();
            lblMensaje.setText(" ¡Pisaste una mina!");
            preguntarReinicio(); // Preguntar si desea jugar otra vez
            return;
        }

        if (tablero.todasMinasMarcadas()) { // Si todas las minas están correctamente marcadas
            juegosGanados++;
            actualizarTablero();
            actualizarLabels();
            lblMensaje.setText(" SII GANASTE");
            preguntarReinicio(); // Pregunta si desea jugar otra vez
            return;
        }

        actualizarTablero(); // Actualiza el tablero si no hay fin de juego
    }

    /**
     * Método para actualizar la vista del tablero
     */
    
    private void actualizarTablero() {
        for (int i = 0; i < L; i++) {
        for (int j = 0; j < L; j++) {
            Casilla c = tablero.getCasilla(i, j); // Obtener estado de la casilla
            JButton btn = botones[i][j];

            if (c.isRevelada()) { // Si la casilla fue destapada
                if (c.isMina()) {
                    btn.setText(" x ");
                    btn.setBackground(Color.RED); // Mina destapada en rojo
                } else {
                    int valor = c.getMinasCercanas();
                    btn.setText(valor == 0 ? "" : String.valueOf(valor));
                    btn.setBackground(Color.LIGHT_GRAY); // Casilla destapada

                   // Cambiar color del número según cantidad de minas cercanas (imitando Windows)
                    switch (valor) {
                        case 1 -> btn.setForeground(Color.BLUE);
                        case 2 -> btn.setForeground(new Color(0, 128, 0)); // verde
                        case 3 -> btn.setForeground(Color.RED);
                        case 4 -> btn.setForeground(new Color(0, 0, 128)); // azul oscuro
                        case 5 -> btn.setForeground(new Color(128, 0, 0)); // rojo oscuro
                        case 6 -> btn.setForeground(new Color(64, 224, 208)); // turquesa
                        case 7 -> btn.setForeground(Color.BLACK);
                        case 8 -> btn.setForeground(Color.GRAY);
                        default -> btn.setForeground(Color.BLACK);
                    }
                }
                btn.setEnabled(false);
                } else if (c.isMarcada()) {
                    btn.setOpaque(true);
                    btn.setForeground(Color.RED);
                    btn.setText("🚩");
                    btn.setBackground(Color.LIGHT_GRAY);
                } else {
                    btn.setText("");
                    btn.setBackground(null);
                    btn.setEnabled(true);
                }
            }
        }
    }
    
    private int contarMarcasActuales() {
        int marcas = 0;
        for (int i = 0; i < L; i++) {
            for (int j = 0; j < L; j++) {
                if (tablero.getCasilla(i, j).isMarcada()) marcas++;
            }
        }
        return marcas;
    }
    
    /**
     * Método para actualizar los labels de estadísticas
     */
    
    private void actualizarLabels() {
        lblJuegosGanados.setText("Juegos Ganados: " + juegosGanados);
        lblJuegosPerdidos.setText("Juegos Perdidos: " + juegosPerdidos);
        lblJuegosJugados.setText("Juegos Jugados: " + juegosJugados);
    }
    
    /**
     * Método para preguntar al jugador si desea reiniciar
     */
    private void preguntarReinicio() {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Deseas jugar otra vez?", "Reiniciar",
                JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            iniciarNuevoJuego(); // Comienza un juego nuevo
        } else {
            System.exit(0); // Salir del juego
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblJuegosGanados = new javax.swing.JLabel();
        lblJuegosPerdidos = new javax.swing.JLabel();
        lblJuegosJugados = new javax.swing.JLabel();
        lblMensaje = new javax.swing.JLabel();
        btnNuevoJuego = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        panelTablero = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblJuegosGanados.setFont(new java.awt.Font("Segoe UI Variable", 1, 14)); // NOI18N
        lblJuegosGanados.setText("Juegos Ganados: ");
        lblJuegosGanados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 153, 255)));

        lblJuegosPerdidos.setFont(new java.awt.Font("Segoe UI Variable", 1, 14)); // NOI18N
        lblJuegosPerdidos.setText("Juegos Perdidos:");
        lblJuegosPerdidos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 204)));

        lblJuegosJugados.setFont(new java.awt.Font("Segoe UI Variable", 1, 14)); // NOI18N
        lblJuegosJugados.setText("Juegos Jugados:");
        lblJuegosJugados.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 102, 255)));

        lblMensaje.setFont(new java.awt.Font("Segoe UI Variable", 1, 14)); // NOI18N
        lblMensaje.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 51))));

        btnNuevoJuego.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        btnNuevoJuego.setForeground(new java.awt.Color(153, 0, 153));
        btnNuevoJuego.setText("Nuevo Juego");

        btnSalir.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 18)); // NOI18N
        btnSalir.setForeground(new java.awt.Color(153, 0, 153));
        btnSalir.setText("Salir");

        panelTablero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 255)));
        panelTablero.setForeground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout panelTableroLayout = new javax.swing.GroupLayout(panelTablero);
        panelTablero.setLayout(panelTableroLayout);
        panelTableroLayout.setHorizontalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 569, Short.MAX_VALUE)
        );
        panelTableroLayout.setVerticalGroup(
            panelTableroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 443, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnNuevoJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(593, 593, 593)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblJuegosJugados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblJuegosGanados, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                    .addComponent(lblJuegosPerdidos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMensaje, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(39, 39, 39))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(241, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(lblJuegosGanados, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblJuegosPerdidos, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(lblJuegosJugados, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblMensaje, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(btnNuevoJuego, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(17, 17, 17))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelTablero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(68, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Main
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaBuscaminas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaBuscaminas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnNuevoJuego;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblJuegosGanados;
    private javax.swing.JLabel lblJuegosJugados;
    private javax.swing.JLabel lblJuegosPerdidos;
    private javax.swing.JLabel lblMensaje;
    private javax.swing.JPanel panelTablero;
    // End of variables declaration//GEN-END:variables
}
