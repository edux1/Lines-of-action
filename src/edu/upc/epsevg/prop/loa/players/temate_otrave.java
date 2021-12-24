package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.*;
import java.awt.*;
import java.util.Map;
import java.util.Random;

/**
 * Jugador temate_otrave
 */
public class temate_otrave implements IPlayer, IAuto {

    private final String name;
    private ElMeuStatus s;
    private final int profunditatMaxima;
    private final int profunditatInicial;
    private final HeuristicaEnum heuristicaSeleccionada;
    private final MinmaxEnum minmaxSeleccionat;

    // Taula Zobrist Hashing (Nomes ha de calcularse un cop en tota la partida)
    private int[][][] zobristTable;

    /**
     * Constructor del jugador
     * @param name Nom del jugador
     * @param profunditatMaxima En cas de fer servir Minmax i alfabeta, serveix per indicar fins a quina profunditat màxima arribarà
     * @param profunditatInicial En cas de fer servir IDS i Zobrist, serveix per indicar a quina profunditat començarà a explorar
     * @param heuristicaSeleccionada Indica quina heuristica fara servir
     * @param minmaxSeleccionat Indica quina implementació de minmax fara servir
     */
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

    /**
     * Realitza un moviment al tauler de joc
     * @param gameStatus Dona informació del tauler de joc
     * @return un objecte Move
     */
    @Override
    public Move move(GameStatus gameStatus) {

        if (this.minmaxSeleccionat.equals(MinmaxEnum.MINMAX_ZOBRIST))
            this.s = new ElMeuStatus(gameStatus, this.zobristTable);
        else
            this.s = new ElMeuStatus(gameStatus);

        CustomInfo info = new CustomInfo();
        Map.Entry<Point, Point> millorMoviment;
        Point origen = new Point();
        Point desti = new Point();

        switch (this.minmaxSeleccionat) {

            // Minmax basic
            case MINMAX:
                info = Minmax.Tria_Moviment(this.s, this.heuristicaSeleccionada, this.profunditatMaxima);
                origen = info.getMillorMoviment().getKey();
                desti = info.getMillorMoviment().getValue();
                return new Move(origen, desti, info.getNodes_explorats(), this.profunditatMaxima, SearchType.MINIMAX);

            // Minmax Poda Alfa Beta
            case MINMAX_ALFABETA:
                info = Minmax_AlfaBeta.Tria_Moviment(this.s, this.heuristicaSeleccionada, this.profunditatMaxima);
                origen = info.getMillorMoviment().getKey();
                desti = info.getMillorMoviment().getValue();
                return new Move(origen, desti, info.getNodes_explorats(), this.profunditatMaxima, SearchType.MINIMAX);

            // Minmax IDS
            case MINMAX_IDS:
                try {
                    info = MinmaxIDS.start(this.s, this.heuristicaSeleccionada, this.profunditatInicial);
                    origen = info.getMillorMoviment().getKey();
                    desti = info.getMillorMoviment().getValue();
                } catch (InterruptedException ignored) {}
                return new Move(origen, desti, info.getNodes_explorats(), info.getProfunditat(), SearchType.MINIMAX_IDS);

            // Minmax Zobrist
            case MINMAX_ZOBRIST:
                try {
                    info = MinmaxIDSZobrist.start(this.s, this.heuristicaSeleccionada, this.profunditatInicial);
                    origen = info.getMillorMoviment().getKey();
                    desti = info.getMillorMoviment().getValue();
                } catch (InterruptedException ignored) {}
                return new Move(origen, desti, info.getNodes_explorats(), info.getProfunditat(), SearchType.MINIMAX_IDS);
        }

        System.out.println("No deberias estar aqui");
        return new Move(origen, desti, 0, 0, SearchType.RANDOM);
    }

    /**
     * Mètode que es crida quan arribem al timeout
     */
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

    /**
     * Retorna el nom del jugador
     * @return el nom del jugador
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Omple la taula de zobrist de numeros aleatoris
     */
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
