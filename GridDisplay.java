package src;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
//Classe qui gére l'affichage dynamique du QuadTree.
public class GridDisplay extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	public QuadTree quadtree;
	private int offSetX = -15414;
	private int offSetY = -16496;
	private int width = 32768;
	private int height = 32768;
	private int lastX;
	private int lastY;
	private boolean panning = false;
	private int cellGap = 3;
	private int cellSize;
	private boolean modifyCell = false;

	public GridDisplay(QuadTree quadtree) {
		this.quadtree = quadtree;
		this.setBackground(Color.black);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);

	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(offSetX, offSetY);
		g2d.setColor(Color.white);
		drawNode(g2d, this.quadtree, 0, 0, width, height);

	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {//quand le bouton 1 de la souris est pressé, on peut faire bouger l'affichage
		if (e.getButton() == MouseEvent.BUTTON1) {
			panning = true;
			lastX = e.getX();
			lastY = e.getY();
			this.repaint();
		} else {
			if(!Propreties.firstSimulation) {
				JOptionPane.showMessageDialog(this, "You can't modify the grid after starting the simulation, please relaunch the program or click on the clear button.");
			}
			modifyCell = true;
			int mouseX = e.getX() - offSetX;
			int mouseY = e.getY() - offSetY;
			this.quadtree.isCellAlive(quadtree, cellSize, mouseX, mouseY);
			this.quadtree.modifyCell(quadtree, cellSize, mouseX, mouseY);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			panning = false;
		}

		else {
			modifyCell = false;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		if (panning) {
			int dx = e.getX() - lastX;
			int dy = e.getY() - lastY;
			offSetX += dx;
			offSetY += dy;
			lastX = e.getX();
			lastY = e.getY();
			this.repaint();
		}
		if (modifyCell) {
			System.out.println("test");
			int mouseX = e.getX() - offSetX;
			int mouseY = e.getY() - offSetY;
			this.quadtree.modifyCell(quadtree, cellSize, mouseX, mouseY);
			this.repaint();
		}
		
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int zooming = e.getWheelRotation();
		if (zooming < 0) {
			int mouseX = e.getX() - offSetX;
			int mouseY = e.getY() - offSetY;
			int lastWidth = width;
			int lastHeight = height;
			
			if (width == 0 && height == 0) {
				width += 1;
				height += 1;
			} else {
				width *= 2;
				height *= 2;
			}
			offSetX = e.getX() - (int) (mouseX * ((double) width / lastWidth));
			offSetY = e.getY() - (int) (mouseY * ((double) height / lastHeight));
			this.repaint();
		}

		else if (zooming > 0) {
			
			int mouseX = e.getX() - offSetX;
			int mouseY = e.getY() - offSetY;
			int lastWidth = width;
			int lastHeight = height;
			
			if (width == 0 && height == 0) {
				width += 1;
				height += 1;
			}
			
			if (width >= 2 && height >= 2) {
				width /= 2;
				height /= 2;
				offSetX = e.getX() - (int) (mouseX * ((double) width / lastWidth));
				offSetY = e.getY() - (int) (mouseY * ((double) height / lastHeight));
			}
			this.repaint();
		}
	}
	
	public void center() {//méthode qui nous permet de centrer l'affichage de sorte que le noeud soit au milieu de l'ecran
		this.offSetX = -15414;
		this.offSetY = -16496;
		this.width = 32768;
		this.height = 32768;
	}

	
	//fonction qui gére l'affichage d'un quadTree de facon recursive
	private void drawNode(Graphics2D g, QuadTree node, int x, int y, int width, int height) {

		if (node.getDepth() <= 1) {
			int midX = x + width / 2;
			int midY = y + height / 2;

			cellSize = (width / 2) - 1;

			if (cellSize <= cellGap || cellSize <= cellGap) {
				cellSize = cellGap;
				cellSize = cellGap;
			}

			node.posX[0] = midX - width / 2;
			node.posY[0] = midY;

			node.posX[1] = midX;
			node.posY[1] = midY;

			node.posX[2] = midX - width / 2;
			node.posY[2] = midY - height / 2;

			node.posX[3] = midX;
			node.posY[3] = midY - height / 2;

			if (node.getSw().isAlive) {
				g.fillRect(midX - width / 2, midY, cellSize, cellSize);
			}
			if (node.getSe().isAlive) {
				g.fillRect(midX, midY, cellSize, cellSize);
			}
			if (node.getNw().isAlive) {
				g.fillRect(midX - width / 2, midY - height / 2, cellSize, cellSize);
			}
			if (node.getNe().isAlive) {
				g.fillRect(midX, midY - height / 2, cellSize, cellSize);
			}

		} else {

			int midX = x + width / 2;
			int midY = y + height / 2;

			drawNode(g, node.getSw(), x, midY, width / 2, height / 2);
			drawNode(g, node.getSe(), midX, midY, width / 2, height / 2);
			drawNode(g, node.getNw(), x, y, width / 2, height / 2);
			drawNode(g, node.getNe(), midX, y, width / 2, height / 2);

		}

	}
	
}