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

/**
 * Player model holds all data
 * <p>Following Model-View-Controller pattern: Player, PlayerController, 
 * PlayerView
 */
public interface Player {

    /**
     * roll yahtzee dice
     */
    public void roll();
    
    /**
     * 'Hold'/lock dice so they cannot be rolled.
     * @param holds array with dice index's for holding.
     */
    public void hold(int[] holds);
    
    /**
     * Player can release holds for current Dice instance. If already 'released'
     * and able to roll, do nothing.
     */
    public void release(int[] rels);
    
    /**
     * Player can use Scorecard to log a score for the given turn based on
     * current Dice. Also sets the joker rule for Scorecard if applicable.
     */
    public boolean score(int index);
    
    /**
     * @return players Scorecard object.
     */  
    public Scorecard getScorecard();
    
    /**
     * @return players Name
     */
    public String getName();
    
    /**
     * Set the player's Name
     */
    public void setName(String name);
 
}
