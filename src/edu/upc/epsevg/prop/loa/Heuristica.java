package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author edux
 */
public class Heuristica {
    
    public static int calcula(ElMeuStatus estat, CellType jugador) {
        int score1 = heuristica_centre(estat, jugador) - heuristica_centre(estat, CellType.opposite(jugador));
        int score2 = heuristica_1(estat, jugador) - heuristica_1(estat, CellType.opposite(jugador));
        int score = score1 + score2;

//        System.out.println("Heuristica: " + score);
        return score;
    }

    
    public static int heuristica_1(ElMeuStatus s, CellType jugador) {
        int grup_max = 0;

        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Boolean> visitades = new ArrayList<>();

        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
            visitades.add(false);
        }

        for (int i = 0; i < fitxes.size(); i++) {
            if(!visitades.get(i)) {
                grup_max = busca_veines(i, fitxes, visitades);
            }

            if(grup_max >= (fitxes.size() - i))
                break;

            visitades.set(i,true);
        }
        
        // Retornem el valor de l'heuristica
        return (int) ( ( (float) grup_max / s.getNumberOfPiecesPerColor(jugador)) * 50);
    }


    public static int heuristica_centre(ElMeuStatus s, CellType jugador) {
        int score = 0;
        int[][] puntuacions = new int[][] {
            {0,0,0,0,0,0,0,0},
            {0,1,1,1,1,1,1,0},
            {0,1,3,3,3,3,1,0},
            {0,1,3,7,7,3,1,0},
            {0,1,3,7,7,3,1,0},
            {0,1,3,3,3,3,1,0},
            {0,1,1,1,1,1,1,0},
            {0,0,0,0,0,0,0,0}
        };

        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            Point p = s.getPiece(jugador, i);
            score += puntuacions[p.x][p.y];
        }

        return score;
    }
    
    
    public static int busca_veines(int posFitxa, ArrayList<Point> fitxes, ArrayList<Boolean> visitades) {
        Point p = fitxes.get(posFitxa);
        int valor = 1;

        for (int i = posFitxa; i < fitxes.size() - 1; i++) {
            if ( (esVeina(p,fitxes.get(i))) && (fitxes.get(i) != p) && (!visitades.get(i)) ) {
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
