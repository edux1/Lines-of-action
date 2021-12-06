/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.loa.players;

import edu.upc.epsevg.prop.loa.CellType;
import edu.upc.epsevg.prop.loa.ElMeuStatus;
import edu.upc.epsevg.prop.loa.GameStatus;
import edu.upc.epsevg.prop.loa.IPlayer;
import edu.upc.epsevg.prop.loa.Move;
import java.awt.Point;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author edux
 */
public class tematé_otravé implements IPlayer {

    private String name;
    private ElMeuStatus s;
    
    public tematé_otravé(String name) {
        this.name = name;
    }
    
    private boolean isInBounds(int x, int y) {
        return (x >= 0 && x < s.getSize())
                && (y >= 0 && y < s.getSize());
    }
    
    @Override
    public Move move(GameStatus arg0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void timeout() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int heuristica_1() {
        int score = 0;
        boolean visited[][] = new boolean[s.getSize()][s.getSize()];
        Arrays.fill(visited, false);
        
        for (int i = 0; i < s.getSize(); i++) {
            for(int j= 0; j < s.getSize(); j++) {
                
                if(s.getPos(i, j) != CellType.EMPTY && !visited[i][j]) {
                   // if()
                     //   busca_veines(s.getPos(i,j), s.getCurrentPlayer());
                }
                visited[i][j] = true;   
                
            }
        }
        
        // Retornem el valor de l'heuristica
        return score;
    }
    
    //To Do: Uri
    public int busca_veines() {
        return 0;
    }
    
}
