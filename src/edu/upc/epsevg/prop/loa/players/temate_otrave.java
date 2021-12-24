package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.*;
import java.awt.*;
import java.util.Map;
import java.util.Random;

public class temate_otrave implements IPlayer, IAuto {

    private final String name;
    private ElMeuStatus s;
    private final int profunditatMaxima;
    private final int profunditatInicial;
    private final HeuristicaEnum heuristicaSeleccionada;
    private final MinmaxEnum minmaxSeleccionat;

    // Taula Zobrist Hashing (Nomes ha de calcularse un cop en tota la partida)
    private int[][][] zobristTable;

    public temate_otrave(String name, int profunditatMaxima, int profunditatInicial, HeuristicaEnum heuristicaSeleccionada, MinmaxEnum minmaxSeleccionat) {
        this.name = name;
        this.profunditatMaxima = profunditatMaxima;
        this.profunditatInicial = profunditatInicial;
        this.heuristicaSeleccionada = heuristicaSeleccionada;
        this.minmaxSeleccionat = minmaxSeleccionat;

        if (this.minmaxSeleccionat.equals(MinmaxEnum.MINMAX_ZOBRIST)) {
            this.zobristTable = new int[8][8][2];
            fill_Matrix();
        }
    }

    @Override
    public Move move(GameStatus gameStatus) {

        if (this.minmaxSeleccionat.equals(MinmaxEnum.MINMAX_ZOBRIST))
            this.s = new ElMeuStatus(gameStatus, this.zobristTable);
        else
            this.s = new ElMeuStatus(gameStatus);

        Map.Entry<Point, Point> millorMoviment;
        Point origen = new Point();
        Point desti = new Point();

        switch (this.minmaxSeleccionat) {

            // Minmax basic
            case MINMAX:
                millorMoviment = Minmax.Tria_Moviment(this.s, this.heuristicaSeleccionada, this.profunditatMaxima);
                return new Move(origen, desti, 0, 0, SearchType.MINIMAX);

            // Minmax Poda Alfa Beta
            case MINMAX_ALFABETA:
                millorMoviment = Minmax_AlfaBeta.Tria_Moviment(this.s, this.heuristicaSeleccionada, this.profunditatMaxima);
                return new Move(origen, desti, 0, 0, SearchType.MINIMAX);

            // Minmax IDS
            case MINMAX_IDS:
                try {
                    millorMoviment = MinmaxIDS.start(this.s, this.heuristicaSeleccionada, this.profunditatInicial);
                    origen = millorMoviment.getKey();
                    desti = millorMoviment.getValue();
                } catch (InterruptedException ignored) {}
                return new Move(origen, desti, 0, 0, SearchType.MINIMAX_IDS);

            // Minmax Zobrist
            case MINMAX_ZOBRIST:
                try {
                    millorMoviment = MinmaxIDSZobrist.start(this.s, this.heuristicaSeleccionada, this.profunditatInicial);
                    origen = millorMoviment.getKey();
                    desti = millorMoviment.getValue();
                } catch (InterruptedException ignored) {}
                return new Move(origen, desti, 0, 0, SearchType.MINIMAX_IDS);
        }

        return new Move(origen, desti, 0, 0, SearchType.RANDOM);
    }

    @Override
    public void timeout() {
        switch (this.minmaxSeleccionat) {
            case MINMAX_IDS:
                MinmaxIDS.timeout();
                break;
            case MINMAX_ZOBRIST:
                MinmaxIDSZobrist.timeout();
                break;
        }
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
