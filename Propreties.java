package src;

import java.util.HashMap;


//Classe qui defini les variables importants et la table de hashage
public class Propreties {
	public static int gridSize = 9; //taille de la grille ou actuelle
	public static int universeSize = 11; //taille de l'univers
	public static HashMap<QuadTree, QuadTree> calculatedQuads = new HashMap<QuadTree, QuadTree>(); //table de hashage
	public static QuadTree liveCell = new QuadTree(true);
	public static QuadTree deadCell = new QuadTree(false);
	public static QuadTree border = QuadTree.initQuadTree((int) (Math.pow(4, universeSize - 1)), false);
	public static boolean pause = true;
	public static boolean del = true; //boolean qui permet de modifier ou non la grille avec la souris
	public static boolean firstSimulation = true;
}
