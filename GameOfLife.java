package src;

import java.awt.Color;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

//Classe qui fait office de main et d'interface graphique en méme temp, qui nous permet de voir et de gerer la simulation

public class GameOfLife {
	public static void main(String[] args) {
		
		//generation d'un noeud
		QuadTree quadtree = QuadTree.generateUniverse(true);
		quadtree.erase(quadtree);

		//instanciation de la classe qui gere l'affichage du noeud
		GridDisplay grid = new GridDisplay(quadtree);
		grid.setOpaque(true);
		grid.setBounds(0, 0, 1920, 1080);
		
		//ajout des divers elements de l'interface

		String[] optionsToChoose = { "             TODO WORK             " };

		String[] availableSize = { "512x512        ", "1x1", "2x2", "4x4", "8x8",
				"16x16", "64x64",
				"256x256", "512x512", "1024x1024", "2096x2096" };

		JComboBox<String> jComboBox = new JComboBox<>(optionsToChoose);
		jComboBox.setBounds(80, 50, 140, 20);

		JComboBox<String> jComboBox2 = new JComboBox<>(availableSize);
		jComboBox.setBounds(80, 100, 140, 40);

		JButton generate = new GenerateButton(jComboBox2, grid);
		JButton erase = new EraseButton(grid);
		JButton start = new StartButton(grid, erase, generate);
		Border options = BorderFactory.createTitledBorder("Options");
		Border pattern = BorderFactory.createTitledBorder("Pattern");
		Border sizeChoice = BorderFactory.createTitledBorder("Random generation");

		JPanel menu = new JPanel();
		menu.setOpaque(true);
		menu.setBackground(Color.white);
		menu.setBounds(1220, 20, 300, 200);
		menu.add(start);
		menu.add(erase);

		JPanel sizeSelection = new JPanel();
		sizeSelection.setBorder(sizeChoice);
		sizeSelection.add(jComboBox2);
		sizeSelection.add(generate);
		menu.add(sizeSelection);

		jComboBox.setBorder(pattern);
		menu.add(jComboBox);
		menu.setBorder(options);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(0, 0, 1920, 1080);
		layeredPane.add(menu, Integer.valueOf(1));
		layeredPane.add(grid, Integer.valueOf(0));

		JFrame frame = new JFrame("Quadtree Grid");
		frame.getContentPane().setBackground(Color.black);
		frame.add(layeredPane);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1920, 1080);
		frame.setLocationRelativeTo(null);
		frame.setLayout(null);
		frame.setVisible(true);

		
		//On as décider d'utiliser un module qui nous donne accés a des fonctions qui remplace la while traditionnel
		//car on as voulu faire les evolution de facon period en 60 images par secondes ce qui est un casse téte avec une boucle while
		JOptionPane.showMessageDialog(frame, "Welcome to GoL, press RIGHT CLICK to add or delete cells, you can move or zoom with the LEFT CLICK");
		ScheduledExecutorService period = Executors.newSingleThreadScheduledExecutor();

		
		//on crée un parent de depth +1 a notre quadtree ensuite on lui applique l'evolution
		period.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (!Propreties.pause) {
					QuadTree gridWithBorders = quadtree.addBorder(grid.quadtree);
					grid.quadtree = grid.quadtree.evolve(gridWithBorders);
				}
				// on reinitialise la table de hashage chaque fois qu'elle depasse 2500000 pour ne pas utiliser toutes la mémoire du pc
				if (Propreties.calculatedQuads.size() >= 2500000) {
					Propreties.calculatedQuads.clear();
				}
				grid.repaint();

			}
		}, 0, 17, TimeUnit.MILLISECONDS);

	}

}
