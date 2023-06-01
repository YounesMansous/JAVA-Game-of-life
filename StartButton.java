package src;
import  javax.swing.*;
import  java.awt.event.*;

//Button qui permet de pauser/debuter la simulation, et qui empéche les autres boutons d'étre clické si la simulation est en cours
public class StartButton extends JButton implements ActionListener{

    protected GridDisplay grid;
    protected JButton button1;
    protected JButton button2;

    public StartButton(GridDisplay grid,JButton button1, JButton button2){
        this.grid = grid;
        this.button1 = button1;
        this.button2 = button2;
        this.setText("Start");
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if(!Propreties.pause){
            Propreties.pause = true;
            this.setText("Start");
        }
        else{
            Propreties.pause = false;
            this.setText("Stop");
        }
        
        Propreties.firstSimulation = false;
        button1.setEnabled(Propreties.pause);
        button2.setEnabled(Propreties.pause);
    }


}