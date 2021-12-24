package edu.upc.epsevg.prop.loa;

import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;

public class Minmax_AlfaBeta {

    private static HeuristicaEnum heuristicaSeleccionada;
    private static int nodes_explorats;
    private static int nodes_explorats_total;

    public static Entry<Point, Point> Tria_Moviment(ElMeuStatus estat, HeuristicaEnum heuristica, int profunditat) {
        heuristicaSeleccionada = heuristica;
        int valor = Integer.MIN_VALUE;
        
        //Incrementem els nodes explorats
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
                    return Map.entry(posAct, pos);
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
        System.out.println("Nodes explorats: " + nodes_explorats);
        System.out.println("Nodes explorats totals: " + nodes_explorats_total);
        nodes_explorats = 0;
        return millorMoviment;
    }
    

    
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
   