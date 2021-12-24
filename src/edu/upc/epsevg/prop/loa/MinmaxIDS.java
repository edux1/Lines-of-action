package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.Map;

/**
 * Implementa el minmax IDS amb un timeout
 */
public class MinmaxIDS {

    private static volatile boolean timeout;

    /**
     * Mètode principal que gestiona tota l'implementació del minimax IDS amb timeout
     * @param estat L'estat actual de la partida
     * @param heuristicaSeleccionada La heuristica que farem servir, de les opcions que tenim
     * @param profunditatInicial La profunditat per on volem començar a explorar
     * @return Retorna un objecte CustomInfo amb informació sobre el millor moviment, la heuristica, profunditat i nodes explorats
     * @throws InterruptedException
     */
    public static CustomInfo start(ElMeuStatus estat, HeuristicaEnum heuristicaSeleccionada, int profunditatInicial) throws InterruptedException {
        timeout = false;

        MinmaxIDSRunnable ids = new MinmaxIDSRunnable(estat, heuristicaSeleccionada, profunditatInicial);
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

/**
 * Implementa el minmax IDS amb un timeout com a Runnable per poder executar-lo en un thread
 */
class MinmaxIDSRunnable implements Runnable {

    private final ElMeuStatus estat;
    private volatile Map.Entry<Point, Point> moviment;
    private static HeuristicaEnum heuristicaSeleccionada;
    private int profunditat;
    private static int nodes_explorats;
    private static int nodes_explorats_total;

    /**
     * Constructor principal del minmax Zobrist
     * @param estat L'estat actual de la partida
     * @param heuristica La heuristica que farem servir, de les opcions que tenim
     * @param profunditat La profunditat màxima que volem explorar
     */
    public MinmaxIDSRunnable(ElMeuStatus estat, HeuristicaEnum heuristica, int profunditat) {
        this.estat = estat;
        this.profunditat = profunditat;
        this.moviment = null;
        heuristicaSeleccionada = heuristica;
    }

    /**
     * És el bucle principal que executa la lògica del minmax IDS
     */
    @Override
    public void run() {
        while (true) {
            moviment = Tria_Moviment(estat, profunditat);
            profunditat++;
        }
    }

    /**
     * Mètode principal que gestiona el minmax que retorna la millor jugada per al jugador indicat
     * @param estat L'estat actual de la partida
     * @param profunditat La profunditat màxima que volem explorar
     * @return Retorna un objecte amb informació sobre el millor moviment
     */
    public static Map.Entry<Point, Point> Tria_Moviment(ElMeuStatus estat, int profunditat) {
        int valor = Integer.MIN_VALUE;
        Map.Entry<Point, Point> millorMoviment = Map.entry(new Point(),new Point());

        // Incrementem els nodes explorats
        nodes_explorats = 0;
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
                if(aux.isGameOver() && aux.GetWinner() == jugador)
                    return Map.entry(posAct, pos);
                else if(!aux.isGameOver()) {
                    int min = minvalor(aux, profunditat-1, alfa, beta, jugador);
                    if (min >= valor) {
                        valor = min;
                        millorMoviment = Map.entry(posAct, pos);
                    }
                }
            }
        }

        nodes_explorats_total += nodes_explorats;
        System.out.println("\n========== Profunditat " + profunditat + " ==========");
        System.out.println("Nodes explorats: " + nodes_explorats);
        System.out.println("Nodes explorats totals: " + nodes_explorats_total);
        return millorMoviment;
    }


    /**
     * Retorna la heuristica màxima de les possibles jugades del nostre tauler
     * @param estat Estat del tauler de la jugada actual
     * @param profunditat La profunditat màxima que volem explorar
     * @param alfa El valor alfa de la poda
     * @param beta El valor beta de la poda
     * @param jugador El jugador (Fitxa blanca o negra)
     * @return
     */
    public static int maxvalor(ElMeuStatus estat, int profunditat, int alfa, int beta, CellType jugador) {
        // Incrementem els nodes explorats
        nodes_explorats++;

        // No podemos seguir o llegado a la hoja
        if (estat.checkGameOver() || profunditat == 0) {
            return Heuristica.calcula(heuristicaSeleccionada, estat, jugador);
        }

        int valor = Integer.MIN_VALUE;

        for (int i = 0; i < estat.getNumberOfPiecesPerColor(estat.getCurrentPlayer()); i++) {
            Point posAct = estat.getPiece(estat.getCurrentPlayer(), i);
            for (Point pos : estat.getMoves(posAct)) {
                ElMeuStatus aux = new ElMeuStatus(estat);
                aux.movePiece(posAct, pos);

                // Caso ganador
                if(aux.isGameOver() && aux.GetWinner() == jugador)
                    return Integer.MAX_VALUE;
                else if (!aux.isGameOver())
                    valor = Math.max(valor, minvalor(aux, profunditat - 1, alfa,beta, jugador));

                // Poda alfa beta
                if (beta <= valor)
                    return valor;
                alfa = Math.max(valor,alfa);

            }
        }
        return valor;
    }


    /**
     * Retorna la heuristica minima de les possibles jugades del nostre tauler
     * @param estat Estat del tauler de la jugada actual
     * @param profunditat La profunditat màxima que volem explorar
     * @param alfa El valor alfa de la poda
     * @param beta El valor beta de la poda
     * @param jugador El jugador (Fitxa blanca o negra)
     * @return La heuristica minimitzadora
     */
    public static int minvalor(ElMeuStatus estat, int profunditat, int alfa, int beta, CellType jugador) {
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
                    valor = Math.min(valor, maxvalor(aux, profunditat - 1, alfa,beta, jugador));

                // Poda alfa beta
                if (valor <= alfa)
                    return valor;
                beta = Math.min(valor,beta);

            }
        }
        return valor;
    }

    /**
     * Mètode per obtenir el resultat del fil que esta executant el minmax i enviar-lo al programa principal
     * @return Retorna un objecte CustomInfo amb informació sobre el millor moviment, la heuristica, profunditat i nodes explorats
     */
    public CustomInfo getInfo() {
        return new CustomInfo(moviment, profunditat - 1, nodes_explorats, nodes_explorats_total);
    }
}