package edu.upc.epsevg.prop.loa;

import edu.upc.epsevg.prop.loa.players.*;

import javax.swing.SwingUtilities;

/**
 * Lines Of Action: el joc de taula.
 * @author bernat
 */
public class Game {
        /**
     * @param args
     */
    public static void main(String[] args) {

        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                // IPlayer player1 = new RandomPlayer("Random alejandro");
                //IPlayer player1 = new BuckyPlayer(4);
                IPlayer player1 = new temate_otrave("Temate Otrave", 6, 2, HeuristicaEnum.HEURISTICA_2, MinmaxEnum.MINMAX_IDS);
                //IPlayer player2 = new temate_otrave("Temate Otrave", 4, 2, HeuristicaEnum.HEURISTICA_2, MinmaxEnum.MINMAX_ALFABETA);
                IPlayer player2 = new MCCloudPlayer();

                new Board(player1 , player2, 10, Level.DIFFICULT);
             }
        });
    }
}
