/*
 * Yahtzee GUI-based game that supports multiple human and AI players, and a
 * high score system for recording and viewing past top score. Official
 * game rules from http://www.hasbro.com/common/instruct/Yahtzee.pdf are 
 * followed.
 * Class: CSC421
 * Project 1
 * Professor: Dr. Spiegel
 * 
 * @author: Ken Zyma
 * @version: 1.0.0
 * @since: 1.6
 */ 

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
* Yahtzee and Batman-themed slot machine applet that may be run as a stand alone
* application or java applet.
* <p>Contains main method and high-level control for the menu system.
* <p>Follows front-controller pattern. See YahtzeeDispatcher and
* YahtzeeView.
*/
public class Yahtzee extends JApplet{
    private YahtzeeDispatcher dispatch;
    private YahtzeeView view;
    
    /**
     * Launch Application.
     * @param args Arguments from command-line. No effect.
     */
    public static void main(String[] args) {  
        JFrame frame = new JFrame();
        frame.setTitle("Batman Slots");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setResizable(false);

        Yahtzee yahtzee = new Yahtzee();
        
        frame.add(yahtzee.view);
        
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Launch Applet.
     */
    @Override
    public void init(){
        super.init();
        add(this.view);
    }

    /**
     * Construct a new Yahtzee Applet.
     * <p>If running Yahtzee as an application you will have to add the
     * YahtzeeView to your container (not Yahtzee). Use getView() to
     * obtain a reference to this.
     */
    public Yahtzee(){
        view = new YahtzeeView();
        dispatch = new YahtzeeDispatcher(view);
        dispatch.initMainMenu(new MenuListener());
        dispatch.init_HighScores();
        dispatch.dispatchMainMenu();
    }
    
    /**
     * Return the view for Yahtzee.
     * @return An instance of YahtzeeView.
     */
    public YahtzeeView getView(){
        return this.view;
    }
    
    /**
     * Launch the corresponding game logic and view.
     * @param menuChoice Possible values for menuChoice are 
     * ["New Game","View Highscores","AI Demo"]
     */
    public void routeMainMenuChoice(String menuChoice){
        switch(menuChoice){
            //New Game
            case "New Game":
                dispatch.launchGetNumberOfPlayersPrompt();
                break;
            //View Highscoress
            case "View Highscores":
                dispatch.launchHighScores();
                break;
            //View AI Demo
            case "AI Demo":
                dispatch.launchAIDemo();
                break;
            case "Exit":
                System.exit(0);
                break;
            default:
                System.err.println("Menu Selection not recognized:"+menuChoice);
                //do nothing
                break;
        }    
    }
        
    /**
     * Callback Routine for a menu button click to call routeMainMenuChoice.
     */
    private class MenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Object actionSource = e.getSource();
            JButton buttonSource = null;
            String buttonText = "";
            if(actionSource instanceof JButton){
                buttonSource = (JButton)actionSource;
                buttonText = buttonSource.getText();
            }else{
                System.err.println("Action Not Recognized from:"+e.toString());
            }
            
            routeMainMenuChoice(buttonText);
            
        }
    }
}
