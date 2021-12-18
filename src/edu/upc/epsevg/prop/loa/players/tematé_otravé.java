package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class tematé_otravé implements IPlayer {

    private String name;
    private ElMeuStatus s;

    public tematé_otravé(String name) {
        this.name = name;
    }
    
    private boolean isInBounds(int x, int y) {
        return (x >= 0 && x < s.getSize())
                && (y >= 0 && y < s.getSize());
    }

    @Override
    public Move move(GameStatus gameStatus) {
        this.s = new ElMeuStatus(gameStatus);

        CellType color = s.getCurrentPlayer();

        Map.Entry<Point, Point> millorMoviment = minimax_AlfaBeta.Tria_Moviment(s, 2);
        Point origen = millorMoviment.getKey();
        Point desti = millorMoviment.getValue();
        s.movePiece(origen, desti);

//        int qn = s.getNumberOfPiecesPerColor(color);
//        ArrayList<Point> pendingAmazons = new ArrayList<>();
//        for (int q = 0; q < qn; q++) {
//            pendingAmazons.add(s.getPiece(color, q));
//        }

        return new Move(origen, desti, 0, 0, SearchType.MINIMAX);
    }

    @Override
    public void timeout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return this.name;
    }
     
}
