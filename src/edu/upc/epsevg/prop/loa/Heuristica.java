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
    
    /**
     * Retorna el valor de l'estat del tauler, el valor es fa respecte l'heuristica que es rep per parametre i el jugador seleccionat.
     * @param heuristicaSeleccionada Heuristica que s'utilitzará per evaluar.
     * @param estat Tauler a evaluar.
     * @param jugador Jugador que s'evaluará.
     * @return El valor de l'estat del tauler.
     */
    public static int calcula(HeuristicaEnum heuristicaSeleccionada, ElMeuStatus estat, CellType jugador) {
        switch (heuristicaSeleccionada) {
            case HEURISTICA_1: return heuristica_1(estat, jugador) - heuristica_1(estat, CellType.opposite(jugador));
            case HEURISTICA_2: return heuristica_2(estat, jugador) - heuristica_2(estat, CellType.opposite(jugador));
            case HEURISTICA_3: return heuristica_3(estat, jugador) - heuristica_3(estat, CellType.opposite(jugador));
            case HEURISTICA_4: return heuristica_4(estat, jugador) - heuristica_4(estat, CellType.opposite(jugador));
        };
        return 0;
    }

    /**
     * Retorna el valor de l'estat del tauler respecte a un jugador, sense tenir en compte les fitxes del rival, dona preferencia a les fitxes al centre del tauler i prioritzar fer grups grans.
     * @param s Tauler a evaluar.
     * @param jugador Jugador que s'evaluará.
     * @return El valor de l'estat del tauler, sense tenir en compte les fitxes del rival.
     */
    public static int heuristica_1(ElMeuStatus s, CellType jugador) {
        int grup_max;
        
        // Guardem totes les fitxes de jugador a un array.
        ArrayList<Point> fitxes = new ArrayList<>();
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }
        // Evaluem el tauler segons el seu millor grup.
        grup_max = search_best_group(fitxes);
        
        // Dividim el valor entre el número de fitxes vives, ja que no es lo mateix tenir un grup de 6 fitxes
        // quan tens 12 fitxes, que un grup de 6 fitxes quan tens 8 fitxes. Fem això per equilibrar els resultats.
        return (int) ( ( (float) grup_max / s.getNumberOfPiecesPerColor(jugador)) * 12);
    }

    /**
     * Retorna el valor del tauler fent una búsqueda del millor grup i evaluant-lo.
     * @param fitxes Conjunt de fitxes d'un mateix jugador.
     * @return El valor del millor grup format per un jugador.
     */
    private static int search_best_group(ArrayList<Point> fitxes) {
        // Creem un array on guardarem quines fitxes hem visitat.
        ArrayList<Boolean> visitades = new ArrayList<>();
        for (int i = 0; i < fitxes.size(); i++) {
            visitades.add(false);
        }
        int grup_max = 1;
        for (int i = 0; i < fitxes.size(); i++) {
            // Si la fitxa no ha estat visitada, mirem quin grup forma, en cas de que el seu grup
            // tingui millor valor que l'anterior millor grup trobat, guardem el seu resultat.
            if(!visitades.get(i)) {
                grup_max = Math.max(grup_max, suma_veines(i, fitxes, visitades));
            }
            //Marquem la fitxa de la posició i com a visitada.
            visitades.set(i,true);
        }
        return grup_max;
    }
    
    /**
     * Retorna el valor del grup que forma una fitxa amb les altres fitxes no visitades.
     * @param posFitxa Posició en l'array de la fitxa que volem comprovar les seves veïnes.
     * @param fitxes Conjunt de fitxes d'un mateix jugador.
     * @param visitades Indicador de quines fitxes han estat visitades.
     * @return El valor de les fitxes que formen un grup.
     */
    public static int suma_veines(int posFitxa, ArrayList<Point> fitxes, ArrayList<Boolean> visitades) {
        Point p = fitxes.get(posFitxa);
        // Comprova el valor de la posició de la fitxa i multiplica el seu valor
        // per el número de fitxes veïnes del mateix jugador.
        int valor = puntuacions[p.x][p.y] * num_veines(posFitxa, fitxes);
        
        // Mirem si les fitxes no visitades son veïnes de la fitxa. 
        for (int i = posFitxa + 1; i < fitxes.size(); i++) {
            // En cas de ser veïna, la marquem com a visitades i mirem si aquesta
            // fitxa té més veïnes i valorem el seu subgrup.
            if ( (esVeina(p,fitxes.get(i))) && (!visitades.get(i)) ) {
                visitades.set(i,true);
                valor += suma_veines(posFitxa + 1, fitxes, visitades);
            }
        }
        return valor;
    }

    /**
     * Retorna el número de fitxes veïnes del mateix jugador.
     * @param posFitxa Posició en l'array de la fitxa que volem comprovar les seves veïnes.
     * @param fitxes Conjunt de fitxes d'un mateix jugador.
     * @return El número de fitxes veïnes del mateix jugador
     */
    public static int num_veines(int posFitxa, ArrayList<Point> fitxes) {
        Point p = fitxes.get(posFitxa);
        int valor = 1;
        
        // Comprovem amb quines fitxes es veïna.
        for (int i = 0; i < fitxes.size(); i++) {
            if ( (esVeina(p,fitxes.get(i))) && (fitxes.get(i) != p) ) {
                valor++;
            }
        }
        return valor;
    }

    /**
     * Retorna el valor de l'estat del tauler respecte a un jugador, sense tenir en compte les fitxes del rival, intenta reduir la distancia de les fitxes respecte al grup millor posicionat.
     * @param s Tauler a evaluar.
     * @param jugador Jugador que s'evaluará.
     * @return El valor de l'estat del tauler, sense tenir en compte les fitxes del rival.
     */
    public static int heuristica_2(ElMeuStatus s, CellType jugador) {
        ArrayList<ArrayList<Point>> groups;
        ArrayList<Point> fitxes = new ArrayList<>();
        ArrayList<Point> group;
        int score;
        // Guardem totes les fitxes de jugador a un array.
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }

        groups = create_groups(s, jugador, fitxes);
        score = distancies(groups, fitxes);

        // Retornem el valor de l'heuristica
        return score;
    }
    
    public static int heuristica_3(ElMeuStatus s, CellType jugador) {
        int score = 0;
        // Guardem totes les fitxes de jugador a un array.
        ArrayList<Point> fitxes = new ArrayList<>();
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }
        score += center_distances(fitxes);
        score += puntua_veines(fitxes);
        return score;
    }
    
    public static int heuristica_4(ElMeuStatus s, CellType jugador) {
        int score = 0;
        ArrayList<Point> fitxes = new ArrayList<>();
        for (int i = 0 ; i < s.getNumberOfPiecesPerColor(jugador) ; i++) {
            fitxes.add(s.getPiece(jugador, i));
        }
        Point p = get_center_of_mass(fitxes);
        score += distances_from_center(p, fitxes);
        score += puntua_veines(fitxes);
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
    
    /**
     * Diu si dues fitxes son veines o no.
     * @param a Posició d'una fitxa
     * @param b Posició d'una altra fitxa
     * @return Diu si les fitxes son veines
     */
    public static boolean esVeina(Point a, Point b) {
        double x = Math.abs(a.x - b.x);
        double y = Math.abs(a.y - b.y);
        return (x <= 1) && (y <= 1);
    }

    private static int distance(Point a, Point b) {
        int x =  Math.abs(b.x - a.x);
        int y =  Math.abs(b.y - a.y);
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
    
    /**
     * Retorne els grups que ha format un jugador sobre el tauler.
     * @param s Tauler a evaluar.
     * @param jugador Jugador a evaluar.
     * @param fitxes Conjunt de fitxes del jugador.
     * @return Els diferents grups formats.
     */
    private static ArrayList<ArrayList<Point>> create_groups(ElMeuStatus s, CellType jugador, ArrayList<Point> fitxes) {
        ArrayList<ArrayList<Point>> grups = new ArrayList<>();

        for (int i = 0; i < fitxes.size(); i++) {
            boolean afegida = false;

            // Busquem si forma part d'algun grup.
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

            // Si no hi ha grup, creem un.
            if (!afegida) {
                ArrayList<Point> g = new ArrayList<>();
                g.add(fitxes.get(i));
                grups.add(g);
            }
        }
        return grups;
    }

}
