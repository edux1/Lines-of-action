package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

public class temate_otrave implements IPlayer, IAuto {

    private String name;
    private ElMeuStatus s;
    private int profunditat;

    // Taula Zobrist Hashing
    private final int[][][] zobristTable;

    public temate_otrave(String name, int profunditat) {
        this.name = name;
        this.profunditat = profunditat;
        this.zobristTable = new int[8][8][2];

        fill_Matrix();
    }

    @Override
    public Move move(GameStatus gameStatus) {
        this.s = new ElMeuStatus(gameStatus, this.zobristTable);

        Map.Entry<Point, Point> millorMoviment;
        Point origen = new Point();
        Point desti = new Point();

        // Minmax base
        // Map.Entry<Point, Point> millorMoviment = minimax.Tria_Moviment;

        // Alfa Beta
        // Map.Entry<Point, Point> millorMoviment = minimax_AlfaBeta.Tria_Moviment;

        // IDS
        //try {
        //    millorMoviment = MinmaxIDS.start(s, 2);
        //    origen = millorMoviment.getKey();
        //    desti = millorMoviment.getValue();
        //} catch (InterruptedException ignored) {}

        // Zobrist
        // Pendiente:
        // Implementarlo en max.
        // Hacer tabla con millormoviment, profundidad y heu.
        // No calcular de 0 cada estado, usar el anterior
        try {
            Hashtable<Long, Transposition> transpositionHashtable = new Hashtable<>();

            Transposition t = MinmaxIDSZobrist.start(s, 2);

            millorMoviment = t.getMillorMoviment();
            origen = millorMoviment.getKey();
            desti = millorMoviment.getValue();

            System.out.println("Heuristica: " + t.getHeuristica());
            System.out.println("Profunditat: " + t.getProfunditat());

            transpositionHashtable.put(s.getHash(), t);
        } catch (InterruptedException ignored) {}

        return new Move(origen, desti, 0, 0, SearchType.MINIMAX_IDS);
    }

    @Override
    public void timeout() {
//        MinmaxIDS.timeout();
        MinmaxIDSZobrist.timeout();
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void fill_Matrix() {
        for (int i = 0; i < zobristTable.length; i++) {
            for (int j = 0; j < zobristTable[0].length; j++) {
                for (int k = 0; k < 2; k++) {
                    zobristTable[i][j][k] = new Random().nextInt(420); // Nice
                }
            }
        }
    }
     
}
