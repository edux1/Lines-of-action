package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class temate_otrave implements IPlayer, IAuto {

    private String name;
    private ElMeuStatus s;
    private int profunditat;

    public temate_otrave(String name, int profunditat) {
        this.name = name;
        this.profunditat = profunditat;
    }

    private boolean isInBounds(int x, int y) {
        return (x >= 0 && x < s.getSize())
                && (y >= 0 && y < s.getSize());
    }


    @Override
    public Move move(GameStatus gameStatus) {
        this.s = new ElMeuStatus(gameStatus);

        Map.Entry<Point, Point> millorMoviment = minimax_AlfaBeta.Tria_Moviment(s, profunditat);
        Point origen = millorMoviment.getKey();
        Point desti = millorMoviment.getValue();

        return new Move(origen, desti, 0, 0, SearchType.MINIMAX);
    }

    @Override
    public void timeout() {
        System.out.println("timeout");
    }

    @Override
    public String getName() {
        return this.name;
    }
     
}
