/*
 * Yahtzee GUI-based game that supports multiple human and AI players, and a
 * high score system for recording and viewing past top score. Official
 * game rules from http://www.hasbro.com/common/instruct/Yahtzee.pdf are 
 * followed.
 * Class: CSC421
 * Project 2
 * Professor: Dr. Spiegels
 * 
 * @author: Ken Zyma
 * @version: 1.0.0
 * @since: 1.6
 */ 

import javax.swing.JPanel;

/**
* PlayerController acts on PlayerModel and PlayerView.
* <p>Following Model-View-Controller pattern: Player, PlayerController, 
* PlayerView
*/

public interface PlayerController {
    /**
     * The player should be responsible for his or her own actions, this is the
     * only entry point for the game to 'talk' to a player.
     */
    public void decideAction();
    
    /**
     * Call if turn is over. Activates callback routine to set next player's
     * view.
     */
    public void turnOver();
    
    /**
     * Roll the game Dice.
     */
    public void rollDice();
    
    /**
     * A Players possible choices change throughout the turn. newTurn()
     * returns players state to first move of a turn.
     */
    public void newTurn();
    
    /**
     * @return: true if player is finished playing (scorecard is full), and
     *  false otherwise.
     */ 
    public boolean donePlaying();
    
    /**
     * @return: Returns Players scorecard.
     */
    public Scorecard getScorecard();
    
    /**
     * @return playersName
     */
    public String getName();
    
    /**
     * Set model's name
     */
    public void setName(String name);
    
    /**
     * Get Panel Reference for a players scorecard.
     */
    public JPanel getScoreView();
    
    /**
     * Get reference to global controller view.
     */
    public JPanel getGlobalControllerView();
    
    /**
     * Reset current player's scorecard and bet.
     */
    public void reset();
    
    /**
     * @return string representing model name, and scorecard.
     */
    @Override
    public String toString();
    
     /**
     * Listen for player's turn to be over.
     */
    public void addturnOverListener(CallbackEvent l);
    
}
