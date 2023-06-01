package src;

import javax.swing.*;
import java.awt.event.*;
//Bouton qui nous permet de changer la taille de la grille et de generer par la suite un carre qui contient des cellules aleatoires (vivante ou morte)
public class GenerateButton extends JButton implements ActionListener {

    protected GridDisplay grid;
    protected JComboBox selectedSize;

    public GenerateButton(JComboBox selectedSize, GridDisplay grid) {
        this.selectedSize = selectedSize;
        this.grid = grid;
        this.setText("Generate");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String choice = selectedSize.getSelectedItem().toString(); //recupére le choix de la taille dans la JComboBox
        String[] strNum = choice.split("x");
        int num = Integer.parseInt(strNum[0]);
        Propreties.gridSize = (int) (Math.log(num * num) / Math.log(4)); //recupére l'entier de la String et calcul la Depth du noeud a generer
        this.grid.quadtree.terminate(this.grid.quadtree);
        this.grid.quadtree = QuadTree.generateUniverse(true); //generation du noeud
    }

}
