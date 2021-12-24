package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

public class MinmaxIDSZobrist {

    private static volatile boolean timeout;

    public static CustomInfo start(ElMeuStatus estat, HeuristicaEnum heuristicaSeleccionada, int profunditatInicial) throws InterruptedException {
        timeout = false;

        MinmaxIDSZobristRunnable ids = new MinmaxIDSZobristRunnable(estat, heuristicaSeleccionada, profunditatInicial);
        Thread thread = new Thread(ids);
        thread.start();

        // Espera hasta el timeout
        while (!timeout) {
            Thread.onSpinWait();
        }

        CustomInfo info = ids.getInfo();
        thread.stop();

        return info;
    }

    public static void timeout() {
        timeout = true;
    }

}

class MinmaxIDSZobristRunnable implements Runnable {

    private ElMeuStatus estat;
    private volatile Map.Entry<Point, Point> moviment;
    private static HeuristicaEnum heuristicaSeleccionada;
    private int profunditat;
    private static int nodes_explorats;
    private static int nodes_explorats_total;

    public MinmaxIDSZobristRunnable(ElMeuStatus estat, HeuristicaEnum heuristica, int profunditat) {
        this.estat = estat;
        this.profunditat = profunditat;
        this.moviment = null;
        heuristicaSeleccionada = heuristica;
    }

    @Override
    public void run() {
        while (true) {
            moviment = ( Tria_Moviment(estat, profunditat) );
            profunditat++;
        }
    }

    public static Map.Entry<Point, Point> Tria_Moviment(ElMeuStatus estat, int profunditat) {
        int valor = Integer.MIN_VALUE;
        Map.Entry<Point, Point> millorMoviment = Map.entry(new Point(),new Point());

        // Incrementem els nodes explorats
        nodes_explorats++;

        int alfa = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        CellType jugador = estat.getCurrentPlayer();

        for (int i = 0; i < estat.getNumberOfPiecesPerColor(jugador); i++) {
            Point posAct = estat.getPiece(jugador, i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);

                aux.movePiece(posAct, pos);

                //Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == jugador) {
                    estat.put_transposicio(Map.entry(posAct, pos), valor, profunditat);
                    return Map.entry(posAct, pos);
                }

                else if (!aux.isGameOver()) {
                    int min = minvalor(aux, profunditat-1, alfa, beta, jugador);
                    if (min >= valor) {
                        valor = min;
                        millorMoviment = Map.entry(posAct, pos);
                    }
                }
            }
        }

        System.out.println("\n========== Profunditat " + profunditat + " ==========");
        System.out.println("Nodes explorats: " + nodes_explorats);
        System.out.println("Nodes explorats totals: " + nodes_explorats_total);
        nodes_explorats = 0;

        estat.put_transposicio(millorMoviment, profunditat, valor);
        return millorMoviment;
    }


    public static int maxvalor(ElMeuStatus estat, int profunditat, int alfa , int beta, CellType jugador) {
        // Incrementem els nodes explorats
        nodes_explorats++;

        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            int heu = Heuristica.calcula(heuristicaSeleccionada, estat, jugador);
            return heu;
        }

        //System.out.println(estat);

        int valor = Integer.MIN_VALUE;

        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            int millorInici = i;

            // Zobrist per comencar per la millor posicio
            Transposition t = estat.get_transposicio(profunditat);
            if (t != null) {
                Point millorFitxa = t.getMillorMoviment().getKey();
                millorInici = (i + estat.findPiece( millorFitxa , estat.getCurrentPlayer())) % estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer());
            }

            Point posAct = estat.getPiece(estat.getCurrentPlayer(), millorInici);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);

                // Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == jugador) {
                    // estat.guarda_hash(Map.entry(posAct, pos), valor, profunditat);
                    return Integer.MAX_VALUE;
                } else if (!aux.isGameOver())
                    valor = Math.max(valor, minvalor(aux, profunditat - 1, alfa,beta, jugador));

                // Poda alfa beta
                if (beta <= valor) {
                    // estat.guarda_hash(Map.entry(posAct, pos), valor, profunditat);
                    return valor;
                }
                alfa = Math.max(valor,alfa);

            }
        }

//        estat.guarda_hash();
        return valor;
    }


    public static int minvalor(ElMeuStatus estat, int profunditat, int alfa , int beta, CellType jugador) {
        // Incrementem els nodes explorats
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
                if(aux.isGameOver() && aux.GetWinner() == CellType.opposite(jugador))
                    return Integer.MIN_VALUE;
                else if (!aux.isGameOver())
                    valor = Math.min(valor, maxvalor(aux, profunditat - 1, alfa, beta, jugador));

                // Poda alfa beta
                if (valor <= alfa)
                    return valor;
                beta = Math.min(valor,beta);

            }
        }
        return valor;
    }

    public CustomInfo getInfo() {
        return new CustomInfo(moviment, profunditat - 1, nodes_explorats, nodes_explorats_total);
    }
}