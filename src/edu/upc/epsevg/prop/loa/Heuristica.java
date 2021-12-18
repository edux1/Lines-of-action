/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.loa;

import java.util.Arrays;

/**
 *
 * @author edux
 */
public class Heuristica {
    //private boolean visited[][];
    
    public static int calcula(ElMeuStatus estat, CellType jugador, boolean visited[][]) {
        int score = heuristica_1(estat, jugador, visited);
        return score;
    }
    
    public static int heuristica_1(ElMeuStatus s, CellType jugador, boolean visited[][]) {
        int grup1_max = 0;
        int grup2_max = 0;
        visited = new boolean[s.getSize()][s.getSize()];
        Arrays.fill(visited, false);
        
        for (int i = 0; i < s.getSize(); i++) {
            for(int j= 0; j < s.getSize(); j++) {
                
                if(s.getPos(i, j) != CellType.EMPTY && !visited[i][j]) {
                    //Creo que se puede optimizar la segunda condición del if
                    if(s.getPos(i, j) == jugador && grup1_max < s.getNumberOfPiecesPerColor(jugador)/2)
                        grup1_max = Math.max(busca_veines(i, j, s.getPos(i,j), s, visited), grup1_max);
                    //Creo que se puede optimizar la segunda condición del if
                    else if(grup2_max < s.getNumberOfPiecesPerColor(CellType.opposite(jugador))/2)
                        grup2_max = Math.max(busca_veines(i, j, s.getPos(i,j), s, visited), grup2_max);
                }
                if(grup1_max >= s.getNumberOfPiecesPerColor(jugador)/2 && grup2_max >= s.getNumberOfPiecesPerColor(CellType.opposite(jugador))/2)
                    break;
                visited[i][j] = true;  //puede irse 
            }
            if(grup1_max >= s.getNumberOfPiecesPerColor(jugador)/2 && grup2_max >= s.getNumberOfPiecesPerColor(CellType.opposite(jugador))/2)
                break;
        }
        
        // Retornem el valor de l'heuristica
        return grup1_max - grup2_max;
    }
    
    
    public static int busca_veines(int fil, int col, CellType jugador, ElMeuStatus s, boolean visited[][]) {
        if (visited[fil][col]) return 0;
        int valor = 1;
        visited[fil][col] = true;
        
        for (int i = fil-1; i < fil+1 && i <= visited.length; i++) {
            if(i < 0) i = 0;
            for (int j = col-1; j < col+1 && j <= visited.length ; j++) {
                if(j < 0) j = 0;
                if (i != fil && j != col && s.getPos(i, j) == jugador) {
                    valor += busca_veines(i, j, jugador, s, visited);
                }
            }
        }
        return valor;
        
    }
}
