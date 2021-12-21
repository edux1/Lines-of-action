package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;


public class MinmaxIDSZobrist {

    private static volatile boolean timeout;
    
    public static Transposition start(ElMeuStatus estat, int profunditatInicial) throws InterruptedException {
        Transposition result;
        timeout = false;

        MinmaxIDSZobristRunnable ids = new MinmaxIDSZobristRunnable(estat,profunditatInicial);
        Thread thread = new Thread(ids);
        thread.start();

        // Espera hasta el timeout
        while (!timeout) {
            Thread.onSpinWait();
        }

        result = ids.getResult();
        thread.interrupt();

        return result;
    }

    public static void timeout() {
        timeout = true;
    }

}

class MinmaxIDSZobristRunnable implements Runnable {

    private final ElMeuStatus estat;
    private volatile Transposition result;
    private int profunditat;

    public MinmaxIDSZobristRunnable(ElMeuStatus estat, int profunditat) {
        this.estat = estat;
        this.profunditat = profunditat;
        this.result = null;
    }

    @Override
    public void run() {
        while (true) {
            result = ( Tria_Moviment(estat, profunditat) );
            result.setProfunditat(profunditat);
            profunditat++;
        }
    }

    public static Transposition Tria_Moviment(ElMeuStatus estat, int profunditat) {
        int valor = Integer.MIN_VALUE;
        Map.Entry<Point, Point> millorMoviment = Map.entry(new Point(),new Point());

        int alfa = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);

                //Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == estat.getCurrentPlayer())
                    return new Transposition(millorMoviment,0,valor, alfa, beta);

                if (!aux.isGameOver()) {
                    int min = minvalor(aux, profunditat-1, alfa, beta);
                    if (min >= valor) {
                        valor = min;
                        millorMoviment = Map.entry(posAct, pos);
                    }
                }
            }
        }

        return new Transposition(millorMoviment,0, valor, alfa, beta);
    }



    public static int maxvalor(ElMeuStatus estat, int profunditat, int alfa , int beta) {
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            int heu = Heuristica.calcula(estat, estat.getCurrentPlayer());

            // Guarda hash version 2
            // estat.guarda_hash();

            return heu;
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
                else if (aux.isGameOver() && aux.GetWinner() == CellType.opposite(estat.getCurrentPlayer()))
                    return Integer.MIN_VALUE;

                valor = Math.max(valor, minvalor(aux, profunditat - 1, alfa,beta));

                // Poda alfa beta
                if (beta <= valor) {
                    // Guarda hash version 2
                    // estat.guarda_hash();
                    return valor;
                }
                alfa = Math.max(valor,alfa);

            }
        }
        // Guarda hash version 2
        // estat.guarda_hash();
        return valor;
    }


    public static int minvalor(ElMeuStatus estat, int profunditat, int alfa , int beta) {
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(estat, estat.getCurrentPlayer());
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
                else if (aux.isGameOver() && aux.GetWinner() == CellType.opposite(estat.getCurrentPlayer()))
                    return Integer.MAX_VALUE;

                valor = Math.min(valor, maxvalor(aux, profunditat - 1, alfa,beta));

                // Poda alfa beta
                if (valor <= alfa)
                    return valor;
                beta = Math.min(valor,beta);

            }
        }
        return valor;
    }

    public Transposition getResult() {
        return this.result;
    }
}