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
    
    private static final int[][] mat_score = new int[][] {
            {-50,-30,-30,-30,-30,-30,-30,-50},
            {-30,-20,-20,-20,-20,-20,-20,-30},
            {-30,-20,-10,-10,-10,-10,-20,-30},
            {-30,-20,-10,+00,+00,-10,-20,-30},
            {-30,-20,-10,+00,+00,-10,-20,-30},
            {-30,-20,-10,-10,-10,-10,-20,-30},
            {-30,-20,-20,-20,-20,-20,-20,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}
    };
    
    public static int calcula(ElMeuStatus estat, CellType jugador) {
        return heuristica_1(estat, jugador) - heuristica_1(estat, CellType.opposite(jugador));
        //return uristica(estat, jugador) - uristica(estat, CellType.opposite(jugador));
    }

    
    public static int heuristica_1(ElMeuStatus s, CellType jugador) {
        int grup_max = 1;

        ArrayList<Point> fitxes = new ArrayList<>();

        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }

        grup_max = search_best_group(fitxes);
        
        // Retornem el valor de l'heuristica
        return (int) ( ( (float) grup_max / s.getNumberOfPiecesPerColor(jugador)) * 12);
    }

    private static int search_best_group(ArrayList<Point> fitxes) {
        ArrayList<Boolean> visitades = new ArrayList<>();
        for (int i = 0; i < fitxes.size(); i++) {
            visitades.add(false);
        }
        int grup_max = 1;
        for (int i = 0; i < fitxes.size(); i++) {
            if(!visitades.get(i)) {
                grup_max = Math.max(grup_max, suma_veines(i, fitxes, visitades));
            }
            visitades.set(i,true);
        }
        return grup_max;
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
        ArrayList<ArrayList<Point>> groups;
        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Point> group;
        int score;
        
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }

        groups = create_groups(s, jugador, fitxes);
        score = distancies(groups, fitxes);
        //score = search_best_group(fitxes);
        //group = get_best_group(fitxes);
        //score += fitxes_restants(fitxes);
        score += bloquejades(s, fitxes);

        // Retornem el valor de l'heuristica
        return score;
    }
    
    public static int heuristica_3(ElMeuStatus s, CellType jugador) {
        //int grup_max = 1;
        ArrayList<ArrayList<Point>> groups;
        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Point> group;
        int score;
        
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }

        //groups = create_groups(s, jugador, fitxes);
        //score = distancies(groups, fitxes);
        score = search_best_group(fitxes);
        //group = get_best_group(fitxes);
        score += fitxes_restants(fitxes);
        score += bloquejades(s, fitxes);
        
        // Retornem el valor de l'heuristica
        return score;
    }
    
    public static int uristica(ElMeuStatus s, CellType jugador) {
        int score = 0;
        ArrayList<Point> fitxes = new ArrayList<>();
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }
        //Point p = get_center_of_mass(fitxes);
        //score += distances_from_center(p, fitxes);
        score += center_distances(fitxes);
        score += puntua_veines(fitxes);
        //score += fitxes_restants(fitxes);
        return score;
    }
    
    public static Point get_center_of_mass(ArrayList<Point> fitxes) {
        double x = 0, y = 0;
        for(Point p: fitxes) {
            x += p.x;
            y += p.y;
        }
        x = Math.round(x/fitxes.size());
        y = Math.round(y/fitxes.size());
        return new Point((int)x, (int)y);
    }
    
    public static int distances_from_center(Point center, ArrayList<Point> fitxes) {
        int score = 0;
        for(Point p: fitxes) {
            score += distance(center, p) * (-10);
        }
        return score;
    }
    
    public static int puntua_veines(ArrayList<Point> fitxes) {
        int score = 0;
        int it = 1;
        for(Point p: fitxes) {
            for(int i = it; i < fitxes.size(); i++) {
                score += puntua_veina(p, fitxes.get(i));
            }
            it++;
        }
        return score;
    }
    
    //not veina score = 0 pts
    //veina diagonal = 15 pts
    //veina not diagonal = 5 pts
    public static int puntua_veina(Point a, Point b) {
        int score = 0;
        int x = Math.abs(a.x - b.x);
        int y = Math.abs(a.y - b.y);
        if(x <= 0 && y <= 0) {
            if(x == 0 || y == 0)
                score = 5; //recta
            else
                score = 15; //diagonal
        }
        return score;
    }
    
    public static int center_distances(ArrayList<Point> fitxes) {
        int score = 0;
        for(Point p: fitxes) {
            score += mat_score[p.x][p.y];
        }
        return score;
    }
    
    public static boolean esVeina(Point a, Point b) {
        double x = Math.abs(a.x - b.x);
        double y = Math.abs(a.y - b.y);
        return (x <= 1) && (y <= 1);
    }

    private static int distance(Point a, Point b) {
        int x =  Math.abs(b.x - a.x);
        int y =  Math.abs(b.y - a.y);

        /*int min =  Math.min(dx, dy);
        int max =  Math.max(dx, dy);

        int diagonalSteps = min;
        int straightSteps = max - min;*/

        return Math.max(x, y);
    }

    private static int distancies(ArrayList<ArrayList<Point>> grups, ArrayList<Point> fitxes) {
        // Recorremos los grupos y miramos a que distancia estan las otras fichas que no son del grupo
        int score = Integer.MIN_VALUE;
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

            if (puntuacio > score)
                score = puntuacio;
        }
        return score;
    }
    
    private static ArrayList<ArrayList<Point>> create_groups(ElMeuStatus s, CellType jugador, ArrayList<Point> fitxes) {
        ArrayList<ArrayList<Point>> grups = new ArrayList<>();

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
        return grups;
    }
    
    private static int bloquejades(ElMeuStatus s, ArrayList<Point> fitxes) {
        int score = 0;
        for(Point p: fitxes) {
            if( (s.getMoves(p)).isEmpty() )
                score -= 15;
        }
        return score;
    }
    
    private static int fitxes_restants(ArrayList<Point> fitxes) {
        int score = 0;
        for(Point p: fitxes) {
            if(!fitxes.contains(p))
                score = -10;
        }
        return score;
    }
    
    private static ArrayList<Point> get_best_group(ArrayList<Point> fitxes) {
        ArrayList<Boolean> visitades = new ArrayList<>();
        ArrayList<Point> group = new ArrayList<>();
        for (int i = 0; i < fitxes.size(); i++) {
            visitades.add(false);
        }
        int grup_max = 1;
        for (int i = 0; i < fitxes.size(); i++) {
            if(!visitades.get(i)) {
                grup_max = suma_veines(i, fitxes, visitades);
                group.add(fitxes.get(i));
            }

            if(grup_max >= (fitxes.size() - i))
                break;

            visitades.set(i,true);
        }
        return group;
    }
}
