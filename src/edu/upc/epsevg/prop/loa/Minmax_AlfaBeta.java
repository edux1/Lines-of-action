package edu.upc.epsevg.prop.loa;

import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;

public class Minmax_AlfaBeta {

    private static HeuristicaEnum heuristicaSeleccionada;
    private static int nodes_explorats;
    private static int nodes_explorats_total;

    /**
     * Mètode principal que gestiona el minmax que retorna la millor jugada per al jugador indicat.
     * @param estat Estat actual de la partida
     * @param heuristica Heurística que s'utilitzará per avaluar el tauler.
     * @param profunditat Profunditat màxima que volem explorar
     * @return La posició de la fitxa ha moure, i la posició destí de la fitxa.
     */
    public static CustomInfo Tria_Moviment(ElMeuStatus estat, HeuristicaEnum heuristica, int profunditat) {
        heuristicaSeleccionada = heuristica;
        int valor = Integer.MIN_VALUE;
        
        //Incrementem els nodes explorats
        nodes_explorats = 0;
        nodes_explorats++;
        
        Entry<Point, Point> millorMoviment = Map.entry(new Point(),new Point());
        
        int alfa = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        CellType jugador = estat.getCurrentPlayer();
        
        for (int i = 0; i < estat.getNumberOfPiecesPerColor(jugador); i++) {
            Point posAct = estat.getPiece(jugador, i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);
                
                //Caso ganador
                if (aux.isGameOver() && aux.GetWinner() == jugador)
                    return new CustomInfo(Map.entry(posAct, pos), profunditat, nodes_explorats, nodes_explorats_total);
                else if(!aux.isGameOver()) {
                    int min = minvalor(aux, profunditat-1, alfa, beta, jugador);
                    if (min >= valor) {
                        valor = min;
                        millorMoviment = Map.entry(posAct, pos);
                    }
                }
            }
        }
        nodes_explorats_total += nodes_explorats;
        System.out.println("\n========== Profunditat " + profunditat + " ==========");
        System.out.println("Nodes explorats: " + nodes_explorats);
        System.out.println("Nodes explorats totals: " + nodes_explorats_total);
        return new CustomInfo(millorMoviment, profunditat, nodes_explorats, nodes_explorats_total);
    }
    

    /**
     * Retorna la millor jugada d'un jugador.
     * @param estat Estat de la partida.
     * @param profunditat Profunditat que queda per explorar.
     * @param alfa Màxim vigent exigible al node pare.
     * @param beta Minim vigent exigible al node pare.
     * @param jugador Jugador a evaluar.
     * @return La posició de la fitxa ha moure, i la posició destí de la fitxa.
     */
    public static int maxvalor(ElMeuStatus estat, int profunditat, int alfa , int beta, CellType jugador) {
        //Incrementem els nodes explorats
        nodes_explorats++;
        
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(heuristicaSeleccionada, estat, jugador);
        }

        int valor = Integer.MIN_VALUE;
        
        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);
                
                // Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == jugador)
                    return Integer.MAX_VALUE;
                else if (!aux.isGameOver())
                    valor = Math.max(valor, minvalor(aux, profunditat - 1, alfa,beta, jugador));

                // Poda alfa beta
                if (beta <= valor) 
                    return valor;
                alfa = Math.max(valor,alfa);
                
            }
        }
        return valor;
    }

    /**
     * Simula la millor jugada del rival, retorna la millor jugada del jugador.
     * @param estat Estat de la partida.
     * @param profunditat Profunditat que queda per explorar.
     * @param alfa Màxim vigent exigible al node pare.
     * @param beta Minim vigent exigible al node pare.
     * @param jugador Jugador a evaluar.
     * @return La posició de la fitxa ha moure, i la posició destí de la fitxa.
     */
    public static int minvalor(ElMeuStatus estat, int profunditat, int alfa , int beta, CellType jugador) {
        //Incrementem els nodes explorats
        nodes_explorats++;
        
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(heuristicaSeleccionada, estat, jugador);
        }

        int valor = Integer.MAX_VALUE;

        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);

                // Caso ganador
                if (aux.isGameOver() && aux.GetWinner() == CellType.opposite(jugador))
                    return Integer.MIN_VALUE;
                else if (!aux.isGameOver())
                    valor = Math.min(valor, maxvalor(aux, profunditat - 1, alfa,beta, jugador));
                
                // Poda alfa beta
                if (valor <= alfa) 
                    return valor;
                beta = Math.min(valor,beta);
                
            }
        }
        return valor;
    }

}
   