package src;
import javax.swing.*;
import java.awt.event.*;


//Button pour effacer/tuer toutes les cellules, qui fait appel a la fonction erase dans la class QuadTree
public class EraseButton extends JButton implements ActionListener {

	protected GridDisplay grid;

    public EraseButton(GridDisplay grid) {
        this.grid = grid;
        this.setText("Clear");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
    	Propreties.firstSimulation = true; //nous permet a nouveau de pouvoir modifier la grille avec la souris
    	this.grid.center(); //centre la camera au centre de l'univers.
    	this.grid.quadtree.terminate(this.grid.quadtree); //mettre tout le noeud et ses sous noeuds a null, puis generer un nouveau quadtree et le vider
    	this.grid.quadtree = QuadTree.generateUniverse(true);
    	this.grid.quadtree.erase(this.grid.quadtree);
    }
}

