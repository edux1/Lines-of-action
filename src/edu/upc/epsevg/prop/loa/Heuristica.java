/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author edux
 */
public class Heuristica {
    
    public static int calcula(ElMeuStatus estat, CellType jugador) {
        int score = heuristica_1(estat, jugador);
        return score;
    }
    
    public static int heuristica_1(ElMeuStatus s, CellType jugador) {
        int grup1_max = 0;
        int grup2_max = 0;

        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Boolean> visitades = new ArrayList<>();
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
            visitades.add(false);
        }

        for (int i = 0; i < fitxes.size(); i++) {
            if(!visitades.get(i)) {
                busca_veines(i, fitxes, visitades);
            }

            if(grup1_max >= (fitxes.size() - i))
                break;

            visitades.set(i,true);
        }
        
        // Retornem el valor de l'heuristica
        return (grup1_max/s.getNumberOfPiecesPerColor(jugador)) - (grup2_max/s.getNumberOfPiecesPerColor(CellType.opposite(jugador)));
    }
    
    
    public static int busca_veines(int posFitxa, ArrayList<Point> fitxes, ArrayList<Boolean> visitades) {
        Point p = fitxes.get(posFitxa);
        int valor = 1;

        for (int i = posFitxa; i < fitxes.size() - 1; i++) {
            if (esVeina(p,fitxes.get(i))) {
                visitades.set(i,true);
                valor += busca_veines(posFitxa + 1, fitxes, visitades);
            }
        }

        return valor;
    }

    public static boolean esVeina(Point a, Point b) {
        double x = Math.abs(a.x - b.x);
        double y = Math.abs(a.y - b.y);
        return (x <= 1) && (y <= 1);
    }

}
