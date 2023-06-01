package src;

import java.util.ArrayList;

public class QuadTree {

	protected QuadTree sw;
	protected QuadTree se;
	protected QuadTree nw;
	protected QuadTree ne;
	protected boolean isAlive;
	protected int depth;
	protected int[] posX = new int[4];
	protected int[] posY = new int[4];
	
	//Declaration de la structure qu'on va utiliser dans ce cas la, Les QuadTrees.
	//Dans n'importe quel operation qu'on fera sur nos QuadTree, on commancera toujours par l'enfant SW,SE,NW,NE;

	public QuadTree(QuadTree sw, QuadTree se, QuadTree nw, QuadTree ne, int depth) { //Structure pour un noeud qui n'est pas une feuille
		this.sw = sw;
		this.se = se;
		this.nw = nw;
		this.ne = ne;
		this.isAlive = this.sw.isAlive || this.se.isAlive || this.nw.isAlive || this.ne.isAlive; //un quadtree est vivant si il contient des enfants vivant
		this.depth = depth; //E.G: Un quadtree de taille 2^depth * 2^depth
	}

	public QuadTree(boolean alive) {//Structure pour une feuille
		this.sw = null;
		this.se = null;
		this.nw = null;
		this.ne = null;
		this.isAlive = alive;
		this.depth = 0;
	}

	
	//fonctions getteurs
	
	public QuadTree getNw() {
		return this.nw;
	}

	public QuadTree getNe() {
		return this.ne;

	}

	public QuadTree getSw() {
		return this.sw;

	}

	public QuadTree getSe() {
		return this.se;

	}

	public int getDepth() {
		return this.depth;
	}
	
	

	public QuadTree getCenter(QuadTree node) { //fonction qui retourne le centre d'un quadtree, qui as une depth de depth-1 par rapport au QuadTree mis en parametre
		if (node.depth > 1) {
			return new QuadTree((node.getSw()).getNe(), (node.getSe()).getNw(), (node.getNw()).getSe(),
					(node.getNe()).getSw(), node.depth - 1);
		}
		return null;
	}
	
	//Fonction pour calculer le nombre de voisions du sous noeud NW.SE d'un noeud de depth 2.

	public int nwNeighbors(QuadTree node) {
		int voisins = 0;
		if (node.getNw().getSw().isAlive)
			voisins++;
		if (node.getNw().getNw().isAlive)
			voisins++;
		if (node.getNw().getNe().isAlive)
			voisins++;
		if (node.getNe().getNw().isAlive)
			voisins++;
		if (node.getNe().getSw().isAlive)
			voisins++;
		if (node.getSw().getNw().isAlive)
			voisins++;
		if (node.getSw().getNe().isAlive)
			voisins++;
		if (node.getSe().getNw().isAlive)
			voisins++;
		return voisins;
	}

	
	//Fonction pour calculer le nombre de voisions du sous noeud NE.SW d'un noeud de depth 2.
	public int neNeighbors(QuadTree node) {
		int voisins = 0;
		if (node.getNw().getSe().isAlive)
			voisins++;
		if (node.getNw().getNe().isAlive)
			voisins++;
		if (node.getNe().getNw().isAlive)
			voisins++;
		if (node.getNe().getSe().isAlive)
			voisins++;
		if (node.getNe().getNe().isAlive)
			voisins++;
		if (node.getSe().getNw().isAlive)
			voisins++;
		if (node.getSe().getNe().isAlive)
			voisins++;
		if (node.getSw().getNe().isAlive)
			voisins++;
		return voisins;
	}

	//Fonction pour calculer le nombre de voisions du sous noeud SW.NE d'un noeud de depth 2.
	public int swNeighbors(QuadTree node) {
		int voisins = 0;
		if (node.getSw().getSw().isAlive)
			voisins++;
		if (node.getSw().getSe().isAlive)
			voisins++;
		if (node.getSw().getNw().isAlive)
			voisins++;
		if (node.getSe().getNw().isAlive)
			voisins++;
		if (node.getSe().getSw().isAlive)
			voisins++;
		if (node.getNw().getSw().isAlive)
			voisins++;
		if (node.getNw().getSe().isAlive)
			voisins++;
		if (node.getNe().getSw().isAlive)
			voisins++;
		return voisins;
	}

	//Fonction pour calculer le nombre de voisions du sous noeud SE.NW d'un noeud de depth 2.
	public int seNeighbors(QuadTree node) {
		int voisins = 0;
		if (node.getSe().getSw().isAlive)
			voisins++;
		if (node.getSe().getNe().isAlive)
			voisins++;
		if (node.getSe().getSe().isAlive)
			voisins++;
		if (node.getSw().getNe().isAlive)
			voisins++;
		if (node.getSw().getSe().isAlive)
			voisins++;
		if (node.getNe().getSw().isAlive)
			voisins++;
		if (node.getNe().getSe().isAlive)
			voisins++;
		if (node.getNw().getSe().isAlive)
			voisins++;
		return voisins;
	}

	//Une fonction qui met tout le QuadTree en null incitant de ce fait le garbageCollector a liberer l'espace qu'il occupe, ameliorant ainsi les performances du programme
	public void terminate(QuadTree node) {
		if (node.getDepth() > 1) {
			node.terminate(node.getSw());
			node.terminate(node.getSe());
			node.terminate(node.getNw());
			node.terminate(node.getNe());

		}
		node = null;
	}
	
	
	//La fonction qui fait evoluer notre grille, qui prend une grille de depth n+1 et qui renvoie son milieu qui est evoluer, et de depth n.
	//Donc a chaque fois qu'on doit faire appelle a cette fonction on doit rajouter une depth 1 au noeud qu'on veut faire evoluer.
	public QuadTree evolve(QuadTree node) {
		
		//si le noeud n'est pas vivant alors directement renvoyer son centre qui est aussi vide.
		if(!node.isAlive) {
			return node.getCenter(node);
		}

		//on vérifie dans notre table de hashage si le resultat de ce noeud existe déja
		QuadTree res = Propreties.calculatedQuads.get(node);

		//si il existe déja on le retourne sinon on fait les calculs d'evolution
		if (res != null) {
			return res;
		}

		if (node.depth == 2) {
			/* quand on arrive dans un carré de 4*4, on compare chaque cellelle de son milieu (2x2 donc 4 cellules
			 * pour les quelles on doit calculer leurs nombre de voisins chacunes
			 */

			QuadTree centerSw = node.getSw().getNe();
			QuadTree centerSe = node.getSe().getNw();
			QuadTree centerNw = node.getNw().getSe();
			QuadTree centerNe = node.getNe().getSw();

			int voisinsSw = swNeighbors(node);
			int voisinsSe = seNeighbors(node);
			int voisinsNw = nwNeighbors(node);
			int voisinsNe = neNeighbors(node);

			QuadTree resSw = ((centerSw.isAlive) ? Propreties.liveCell : Propreties.deadCell);
			QuadTree resSe = ((centerSe.isAlive) ? Propreties.liveCell : Propreties.deadCell);
			QuadTree resNw = ((centerNw.isAlive) ? Propreties.liveCell : Propreties.deadCell);
			QuadTree resNe = ((centerNe.isAlive) ? Propreties.liveCell : Propreties.deadCell);
			
			//Regles de base du Jeu de la vie, TODO : mettre les nombres 3 et 2 dans des variables afin
			//de pouvoir changer les regles facilement.
			if (!centerSw.isAlive && voisinsSw == 3) {
				resSw = Propreties.liveCell;
			} else if (centerSw.isAlive && (voisinsSw > 3 || voisinsSw < 2)) {
				resSw = Propreties.deadCell;
			}

			if (!centerSe.isAlive && voisinsSe == 3) {
				resSe = Propreties.liveCell;
			} else if (centerSe.isAlive && (voisinsSe > 3 || voisinsSe < 2)) {
				resSe = Propreties.deadCell;
			}

			if (!resNw.isAlive && voisinsNw == 3) {
				resNw = Propreties.liveCell;
			} else if (resNw.isAlive && (voisinsNw > 3 || voisinsNw < 2)) {
				resNw = Propreties.deadCell;
			}

			if (!centerNe.isAlive && voisinsNe == 3) {
				resNe = Propreties.liveCell;
			} else if (centerNe.isAlive && (voisinsNe > 3 || voisinsNe < 2)) {
				resNe = Propreties.deadCell;
			}

			res = new QuadTree(resSw, resSe, resNw, resNe, node.getDepth() - 1);

			return res;

		}

		
		/*On construit 9 sous enfant pour le noeud acutelles et on les fait evoluer recursivement a leurs tours*/
		
		QuadTree node1 = node.getSw();
		QuadTree node3 = node.getSe();
		QuadTree node7 = node.getNw();
		QuadTree node9 = node.getNe();
		QuadTree node2 = new QuadTree(node1.getSe(), node3.getSw(), node1.getNe(), node3.getNw(), node.getDepth() - 1);
		QuadTree node4 = new QuadTree(node1.getNw(), node1.getNe(), node7.getSw(), node7.getSe(), node.getDepth() - 1);
		QuadTree node5 = new QuadTree(node1.getNe(), node3.getNw(), node7.getSe(), node9.getSw(), node.getDepth() - 1);
		QuadTree node6 = new QuadTree(node3.getNw(), node3.getNe(), node9.getSw(), node9.getSe(), node.getDepth() - 1);
		QuadTree node8 = new QuadTree(node7.getSe(), node9.getSw(), node7.getNe(), node9.getNw(), node.getDepth() - 1);

		QuadTree node1Res = evolve(node1);
		QuadTree node2Res = evolve(node2);
		QuadTree node3Res = evolve(node3);
		QuadTree node4Res = evolve(node4);
		QuadTree node5Res = evolve(node5);
		QuadTree node6Res = evolve(node6);
		QuadTree node7Res = evolve(node7);
		QuadTree node8Res = evolve(node8);
		QuadTree node9Res = evolve(node9);

		
		QuadTree intermediateNode1 = new QuadTree(node1Res, node2Res, node4Res, node5Res, node.getDepth() - 1);
		QuadTree intermediateNode2 = new QuadTree(node2Res, node3Res, node5Res, node6Res, node.getDepth() - 1);
		QuadTree intermediateNode3 = new QuadTree(node4Res, node5Res, node7Res, node8Res, node.getDepth() - 1);
		QuadTree intermediateNode4 = new QuadTree(node5Res, node6Res, node8Res, node9Res, node.getDepth() - 1);

		res = new QuadTree(getCenter(intermediateNode1), getCenter(intermediateNode2),
				getCenter(intermediateNode3), getCenter(intermediateNode4), node.getDepth() - 1);
		
		//On rajoute le noeud actuelle et son resultat dans la table de hachage
		Propreties.calculatedQuads.putIfAbsent(node, res);

		return res;

	}

	
	//Fonction pour generer un quadTree de taille size (nombre de cellules), rand est une booleane qui determine si
	//on va faire une generation avec des cellules aléatoires ou bien si on doit generer une grille morte.
	public static QuadTree initQuadTree(int size, boolean rand) {

		ArrayList<QuadTree> res = new ArrayList<QuadTree>();
		int tmp = size;
		int levels = size / 4; //la depths max du noeud
		int index = 0;
		boolean alive = false;

		for (int j = 0; j <= levels; j++) {
			for (int i = 0; i < tmp; i++) {
				if (tmp == size) {

					if (rand) {

						alive = Math.random() >= 0.7;
						if (alive)
							res.add(Propreties.liveCell);
						else
							res.add(Propreties.deadCell);

					} else
						res.add(Propreties.deadCell);

				} else {

					res.add(new QuadTree(res.get(index), res.get(index + 1), res.get(index + 2), res.get(index + 3),
							j));
					index += 4;

				}
			}

			tmp /= 4;
		}

		return res.get(res.size() - 1);

	}

	//fonction pour ajouter des bordures plus performantes sur une grande echelle que la fonction expendBorders()
	public QuadTree addBorder(QuadTree node) {
		QuadTree borderSw = new QuadTree(Propreties.border, Propreties.border, Propreties.border, node.getSw(), node.getDepth());
		QuadTree borderSe = new QuadTree(Propreties.border, Propreties.border, node.getSe(), Propreties.border, node.getDepth());
		QuadTree borderNw = new QuadTree(Propreties.border, node.getNw(), Propreties.border, Propreties.border, node.getDepth());
		QuadTree borderNe = new QuadTree(node.getNe(), Propreties.border, Propreties.border, Propreties.border, node.getDepth());
		return new QuadTree(borderSw, borderSe, borderNw, borderNe, node.getDepth() + 1);

	}

	//fonction pour ajouter des bordures lente mais nous permet d'eviter d'avoir plusieurs noeuds de l'univers d'avoir la méme référence
	public QuadTree expendBorders(QuadTree node) {
		QuadTree borderSw = new QuadTree(initQuadTree((int) Math.pow(4, node.getDepth() - 1),
				false), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
						false), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
								false), node.getSw(),
				node.getDepth());
		QuadTree borderSe = new QuadTree(initQuadTree((int) Math.pow(4, node.getDepth() - 1),
				false), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
						false), node.getSe(), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
								false),
				node.getDepth());
		QuadTree borderNw = new QuadTree(initQuadTree((int) Math.pow(4, node.getDepth() - 1),
				false), node.getNw(), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
						false), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
								false),
				node.getDepth());
		QuadTree borderNe = new QuadTree(node.getNe(), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
				false), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
						false), initQuadTree((int) Math.pow(4, node.getDepth() - 1),
								false),
				node.getDepth());
		return new QuadTree(borderSw, borderSe, borderNw, borderNe, node.getDepth() +
				1);

	}

	//fonction de hachage qui prend en compte l'état du noeud actuelle, sa Depth et le hashcode de ses enfants.
	@Override
	public int hashCode() {

		if (this.getDepth() == 0) {
			return this.isAlive ? 1257 : 1258;
		}
		int hash = 17;
		hash = 31 * hash + this.getDepth();
		hash = 31 * hash + (this.isAlive ? 1257 : 1258);

		hash = 31 * hash + (this.getSw().isAlive ? this.getSw().hashCode() + 124 : 0);
		hash = 31 * hash + (this.getSe().isAlive ? this.getSe().hashCode() + 458 : 0);
		hash = 31 * hash + (this.getNw().isAlive ? this.getNw().hashCode() + 4657 : 0);
		hash = 31 * hash + (this.getNe().isAlive ? this.getNe().hashCode() + 9745 : 0);
		return hash;
	}
	
	
	//fonction de verification d'egualité de deux noeuds qui suit la méme logique que la fonction de hachage.
	@Override
	public boolean equals(Object node) {

		if(getDepth() == 2) {
			return true;
		}
		QuadTree quad = (QuadTree) node;
		return this.hashCode() == quad.hashCode() && this.getDepth() == quad.getDepth() && this.isAlive == quad.isAlive && getSw().equals(quad.getSw()) && getSe().equals(quad.getSe()) && getNw().equals(quad.getNw()) && getNe().equals(quad.getNe());

	}

	
	//fonction pour generer l'univers qui as une depth par default de 11
	public static QuadTree generateUniverse(boolean test) {
		QuadTree res = QuadTree.initQuadTree((int) (Math.pow(4, Propreties.gridSize)), test);
		for (int i = Propreties.gridSize; i < Propreties.universeSize; i++) {
			res = res.expendBorders(res);
		}

		return res;
	}
	
	//fonction qui verifie si la cellule qui se trouve au coordonnée du click de la souris est morte ou vivante.
	public void isCellAlive(QuadTree node,int s, int x, int y) {
		if (node.getDepth() == 1) {
			if (x >= node.posX[0] && x <= node.posX[0] + s && y >= node.posY[0] && y <= node.posY[0] + s) {
				Propreties.del = node.sw.isAlive;
			}

			if (x >= node.posX[1] && x <= node.posX[1] + s && y >= node.posY[1] && y <= node.posY[1] + s) {
				Propreties.del = node.se.isAlive;
			}

			if (x >= node.posX[2] && x <= node.posX[2] + s && y >= node.posY[2] && y <= node.posY[2] + s) {
				Propreties.del = node.nw.isAlive;
			}

			if (x >= node.posX[3] && x <= node.posX[3] + s && y >= node.posY[3] && y <= node.posY[3] + s) {
				Propreties.del = node.ne.isAlive;
			}
			return;
		}
		
		isCellAlive(node.getSw(), s, x, y);
		isCellAlive(node.getSe(), s, x, y);
		isCellAlive(node.getNw(), s, x, y);
		isCellAlive(node.getNe(), s, x, y);

	}

	
	//fonction qui modifie (tue ou ravive) la cellule qui se trouve au coordonnée du click de la souris.
	public void modifyCell(QuadTree node, int s, int x, int y) {
		if (node.getDepth() == 1) {
			if (x >= node.posX[0] && x <= node.posX[0] + s && y >= node.posY[0] && y <= node.posY[0] + s) {
				if(!Propreties.del) {
					node.sw = Propreties.liveCell;
					node.isAlive = true;
				}
				else {
					node.sw = Propreties.deadCell;
				}
				
			}

			if (x >= node.posX[1] && x <= node.posX[1] + s && y >= node.posY[1] && y <= node.posY[1] + s) {
				if(!Propreties.del) {
					node.se = Propreties.liveCell;
					node.isAlive = true;
				}
				else {
					node.se = Propreties.deadCell;
				}
			}

			if (x >= node.posX[2] && x <= node.posX[2] + s && y >= node.posY[2] && y <= node.posY[2] + s) {
				if(!Propreties.del) {
					node.nw = Propreties.liveCell;
					node.isAlive = true;
				}
				else {
					node.nw = Propreties.deadCell;
				}
			}

			if (x >= node.posX[3] && x <= node.posX[3] + s && y >= node.posY[3] && y <= node.posY[3] + s) {
				if(!Propreties.del) {
					node.ne = Propreties.liveCell;
					node.isAlive = true;
				}
				else {
					node.ne = Propreties.deadCell;
				}
			}

			return;
		}

		modifyCell(node.getSw(), s, x, y);
		modifyCell(node.getSe(), s, x, y);
		modifyCell(node.getNw(), s, x, y);
		modifyCell(node.getNe(), s, x, y);

	}
	
	
	//fonction pour tuer recursivement toutes les feuilles d'un noeud
	public void erase(QuadTree node) {
		if(node.getDepth() == 1) {
			node.sw = Propreties.deadCell;
			node.se = Propreties.deadCell;
			node.nw = Propreties.deadCell;
			node.ne = Propreties.deadCell;
			
			return;
		}
		erase(node.getSw());
		erase(node.getSe());
		erase(node.getNw());
		erase(node.getNe());
	}

}
