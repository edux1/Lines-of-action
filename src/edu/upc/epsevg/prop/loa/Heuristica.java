package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author edux
 */
public class Heuristica {

    private static final int[][] puntuacions = new int[][] {
            {1,1,1,1,1,1,1,1},
            {1,4,4,4,4,4,4,1},
            {1,4,5,5,5,5,4,1},
            {1,4,5,5,5,5,4,1},
            {1,4,5,5,5,5,4,1},
            {1,4,5,5,5,5,4,1},
            {1,4,4,4,4,4,4,1},
            {1,1,1,1,1,1,1,1}
    };
    
    public static int calcula(ElMeuStatus estat, CellType jugador) {
        int score = heuristica_1(estat, jugador) - heuristica_1(estat, CellType.opposite(jugador));

//        System.out.println("Heuristica: " + score);
        return score;
    }

    
    public static int heuristica_1(ElMeuStatus s, CellType jugador) {
        int grup_max = 1;

        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Boolean> visitades = new ArrayList<>();

        for (int i = 1 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
            visitades.add(false);
        }

        for (int i = 1; i < fitxes.size(); i++) {
            if(!visitades.get(i)) {
                grup_max = suma_veines(i, fitxes, visitades);
            }

            if(grup_max >= (fitxes.size() - i))
                break;

            visitades.set(i,true);
        }
        
        // Retornem el valor de l'heuristica
        return (int) ( ( (float) grup_max / s.getNumberOfPiecesPerColor(jugador)) * 12);
    }


    public static int heuristica_centre(ElMeuStatus s, CellType jugador) {
        int score = 1;

        for (int i = 1 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            Point p = s.getPiece(jugador, i);
            score += puntuacions[p.x][p.y];
        }

        return score;
    }
    
    
    public static int suma_veines(int posFitxa, ArrayList<Point> fitxes, ArrayList<Boolean> visitades) {
        Point p = fitxes.get(posFitxa);
        int valor = puntuacions[p.x][p.y] * num_veines(posFitxa, fitxes);

        for (int i = posFitxa; i < fitxes.size() - 1; i++) {
            if ( (esVeina(p,fitxes.get(i))) && (fitxes.get(i) != p) && (!visitades.get(i)) ) {
                visitades.set(i,true);
                valor += suma_veines(posFitxa + 1, fitxes, visitades);
            }
        }

        return valor;
    }

    public static int num_veines(int posFitxa, ArrayList<Point> fitxes) {
        Point p = fitxes.get(posFitxa);
        int valor = 0;

        for (int i = 0; i < fitxes.size() - 1; i++) {
            if ( (esVeina(p,fitxes.get(i))) && (fitxes.get(i) != p) ) {
                valor++;
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
