package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;
import java.util.Map.Entry;

public class Minmax {
    
    
    public static Entry<Point, Point> Tria_Moviment(ElMeuStatus estat, int profunditat) {
        int valor = Integer.MIN_VALUE;
        Entry<Point, Point> millorMoviment = Map.entry(new Point(),new Point());
        
        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);
                
                //Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == estat.getCurrentPlayer())
                    return Map.entry(posAct, pos);
                                
                int min = minvalor(aux, profunditat-1);
                if (min >= valor) {
                    valor = min;
                    millorMoviment = Map.entry(posAct, pos);
                }
            }
        }
        return millorMoviment;
    }
    

    
    public static int maxvalor(ElMeuStatus estat, int profunditat) {
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(estat, CellType.opposite(estat.getCurrentPlayer())) ;
        }

        int valor = Integer.MIN_VALUE;
        
        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);
                
                // Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == estat.getCurrentPlayer())
                    return Integer.MAX_VALUE;
                
                valor = Math.max(valor, minvalor(aux, profunditat - 1));
            }
        }
        return valor;
    }


    public static int minvalor(ElMeuStatus estat, int profunditat) {
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(estat, CellType.opposite(estat.getCurrentPlayer()));
        }

        int valor = Integer.MAX_VALUE;

        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);

                // Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == estat.getCurrentPlayer())
                    return Integer.MIN_VALUE;
                    
                valor = Math.min(valor, maxvalor(aux, profunditat - 1));
            }
        }
        return valor;
    }

}
   