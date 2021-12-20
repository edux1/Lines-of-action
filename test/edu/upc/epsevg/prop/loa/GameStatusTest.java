/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.upc.epsevg.prop.loa;

import edu.upc.epsevg.prop.loa.GameStatus;
import edu.upc.epsevg.prop.loa.CellType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Usuari
 */
public class GameStatusTest {
    
    public GameStatusTest() {
    }

    
    @Test
    public void testGetHeuristic() {
        
        int matrix[][] = new int[][] {
            {+0,-1,-1,-1, 0,-1,-1,+0},
            {+1,+0,+0,+0,+0,+0,+0,+1},
            {+1,+0,+0,+0,+0,+0,+0,+1},
            {0,+0,+0,+1,+1,+0,+0,0},
            {0,+0,+0,+1,+1,+0,+0,0},
            {+1,+0,+0,+0,+0,+0,+0,+1},
            {+1,+0,+0,+0,+0,+0,+0,+1},
            {+0,-1,-1,-1,-1,-1,-1,+0}
        };
        ElMeuStatus gs = new ElMeuStatus(matrix);

        // Imprime tablero
        System.out.println(gs.toString());

        // Imprime heuristica
        System.out.println("Heuristica blancas: " + Heuristica.calcula(gs, CellType.PLAYER1));
        System.out.println("Heuristica negras: " + Heuristica.calcula(gs, CellType.PLAYER2));

        System.out.println("=========================================================");

    }

    
}
