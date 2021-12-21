package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

public class MinmaxIDS {

    private static volatile boolean timeout;

    public static Map.Entry<Point, Point> start(ElMeuStatus estat, int profunditatInicial) throws InterruptedException {
        Map.Entry<Point, Point> moviment;
        timeout = false;

        MinmaxIDSRunnable ids = new MinmaxIDSRunnable(estat,profunditatInicial);
        Thread thread = new Thread(ids);
        thread.start();

        // Espera hasta el timeout
        while (!timeout) {
            Thread.onSpinWait();
        }

        moviment = ids.getMoviment();
        thread.interrupt();

        return moviment;
    }

    public static void timeout() {
        timeout = true;
    }

}

class MinmaxIDSRunnable implements Runnable {

    private final ElMeuStatus estat;
    private volatile Map.Entry<Point, Point> moviment;
    private int profunditat;

    public MinmaxIDSRunnable(ElMeuStatus estat, int profunditat) {
        this.estat = estat;
        this.profunditat = profunditat;
        this.moviment = null;
    }

    @Override
    public void run() {
        while (true) {
            moviment = Tria_Moviment(estat, profunditat);
            profunditat++;
        }
    }

    public static Map.Entry<Point, Point> Tria_Moviment(ElMeuStatus estat, int profunditat) {
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
                    return Map.entry(posAct, pos);

                int min = minvalor(aux, profunditat-1, alfa, beta);
                if (min >= valor) {
                    valor = min;
                    millorMoviment = Map.entry(posAct, pos);
                }
            }
        }
        return millorMoviment;
    }



    public static int maxvalor(ElMeuStatus estat, int profunditat, int alfa , int beta) {
        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(estat, estat.getCurrentPlayer());
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
                if (beta <= valor)
                    return valor;
                alfa = Math.max(valor,alfa);

            }
        }
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

    public Map.Entry<Point, Point> getMoviment() {
        return moviment;
    }
}