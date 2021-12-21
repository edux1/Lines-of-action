package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class temate_otrave implements IPlayer, IAuto {

    private String name;
    private ElMeuStatus s;
    private int profunditat;

    public temate_otrave(String name, int profunditat) {
        this.name = name;
        this.profunditat = profunditat;
    }

    @Override
    public Move move(GameStatus gameStatus) {
        this.s = new ElMeuStatus(gameStatus);
        Map.Entry<Point, Point> millorMoviment = null;
        Point origen = new Point();
        Point desti = new Point();

//        Map.Entry<Point, Point> millorMoviment = minimax_AlfaBeta.Tria;

        try {
            millorMoviment = MinmaxIDS.start(s, 2);
            origen = millorMoviment.getKey();
            desti = millorMoviment.getValue();
        } catch (InterruptedException ignored) {}

        return new Move(origen, desti, 0, 0, SearchType.MINIMAX_IDS);
    }

    @Override
    public void timeout() {
        MinmaxIDS.timeout();
    }

    @Override
    public String getName() {
        return this.name;
    }
     
}
