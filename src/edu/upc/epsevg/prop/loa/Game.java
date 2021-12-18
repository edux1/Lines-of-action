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
                
                IPlayer player1 = new RandomPlayer("Octopus");
                IPlayer player2 = new temate_otrave("Sinteses");
                                
                new Board(player1 , player2, 4, Level.DIFFICULT);
             }
        });
    }
}
