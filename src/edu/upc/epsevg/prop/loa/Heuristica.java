package edu.upc.epsevg.prop.loa;

import java.awt.*;
import java.util.ArrayList;

/**
 *
 * @author Senpai
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
        return heuristica_2(estat, jugador) - heuristica_2(estat, CellType.opposite(jugador));
    }

    
    public static int heuristica_1(ElMeuStatus s, CellType jugador) {
        int grup_max = 1;

        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Boolean> visitades = new ArrayList<>();

        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
            visitades.add(false);
        }

        for (int i = 0; i < fitxes.size(); i++) {
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

        for (int i = posFitxa + 1; i < fitxes.size(); i++) {
            if ( (esVeina(p,fitxes.get(i))) && (!visitades.get(i)) ) {
                visitades.set(i,true);
                valor += suma_veines(posFitxa + 1, fitxes, visitades);
            }
        }

        return valor;
    }


    public static int num_veines(int posFitxa, ArrayList<Point> fitxes) {
        Point p = fitxes.get(posFitxa);
        int valor = 0;

        for (int i = 0; i < fitxes.size(); i++) {
            if ( (esVeina(p,fitxes.get(i))) && (fitxes.get(i) != p) ) {
                valor++;
            }
        }

        return valor;
    }


    public static int heuristica_2(ElMeuStatus s, CellType jugador) {
        //int grup_max = 1;
        ArrayList<ArrayList<Point>> grups = new ArrayList<>();

        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Boolean> afegides = new ArrayList<>();


        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
            afegides.add(false);
        }

        for (int i = 0; i < fitxes.size(); i++) {
            boolean afegida = false;

            // Busca si forma parte de un grupo
            for (ArrayList<Point> grup : grups) {
                for (Point p : grup) {
                    if (esVeina(fitxes.get(i), p)) {
                        grup.add(fitxes.get(i));
                        afegida = true;
                    }
                    if (afegida) break;
                }
                if (afegida) break;
            }

            // Si no hay grupo creamos uno
            if (!afegida) {
                ArrayList<Point> g = new ArrayList<>();
                g.add(fitxes.get(i));
                grups.add(g);
            }
        }

        // Recorremos los grupos y miramos a que distancia estan las otras fichas que no son del grupo
        int max = Integer.MIN_VALUE;
        for (ArrayList<Point> grup : grups) {
            int puntuacio = 0;

            for (Point p : fitxes) {
                if (!grup.contains(p)) {

                    int min = Integer.MAX_VALUE;
                    for (Point p2 : grup) {
                        int d = distance(p,p2);

                        if (d < min)
                            min = d;

                    }
                    puntuacio -= min;

                }
            }

            if (puntuacio > max)
                max = puntuacio;
        }

        // Retornem el valor de l'heuristica
        return max;
    }

    public static boolean esVeina(Point a, Point b) {
        double x = Math.abs(a.x - b.x);
        double y = Math.abs(a.y - b.y);
        return (x <= 1) && (y <= 1);
    }

    private static int distance(Point a, Point b) {
        int dx =  Math.abs(b.x - a.x);
        int dy =  Math.abs(b.y - a.y);

        int min =  Math.min(dx, dy);
        int max =  Math.max(dx, dy);

        int diagonalSteps = min;
        int straightSteps = max - min;

        return diagonalSteps + straightSteps;
    }

}
