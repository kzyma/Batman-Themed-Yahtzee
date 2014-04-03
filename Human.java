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
* Human represents a human player in yahtzee. Human holds all data and basic 
* functions needed to facilitate that data be correctly stored and 
* represented. HumanController calls this as the interface for a player in
* the game. Implement player Interface to create a new Human model that will
* work with the current HumanController.
*/
public class Human implements Player{
    private Dice yahtzeeDice;
    private Scorecard myScorecard;
    private String name;
    private String character;
    private int playerCounter;

    
    /**
     * Construct Human object with default values.
     */
    public Human(int playerCounter){
        this("Human_Player",playerCounter,"");
    }
    
    /**
     * Construct Human object with set attributes.
     * @param name
     * @param playerCounter
     * @param characterType 
     */
    public Human(String name,int playerCounter,String characterType){
        this.yahtzeeDice = Dice.getDiceInstance();
        this.myScorecard = new Scorecard(characterType);
        this.playerCounter = playerCounter;
        this.character = characterType;
        this.name = name;
    }
    
    /**
     * Return player number.
     */
    public int getPlayerNumber(){
        return playerCounter;
    }
    
    /**
     * Roll yahtzee Dice.
     */
    @Override
    public void roll(){
        yahtzeeDice.roll();
    }
    
    /**
     * Return reference to player's current scorecard.
     */
    @Override
    public Scorecard getScorecard(){
        return this.myScorecard;
    }
    
     /**
     * Set the player's Name
     */
    @Override
    public void setName(String name){
        this.name = name;
    }
    
    /**
     * @return name of the player.
     */
    @Override
    public String getName(){
        return this.name;
    }
    
    /**
     * 'Hold'/lock dice so they cannot be rolled.
     * @param holds array with dice index's for holding.
     */
    @Override
    public void hold(int[] holds){
    }        
    
    /**
     * Player can release holds for current Dice instance. If already 'released'
     * and able to roll, do nothing.
     */
    @Override
    public void release(int[] rels){
    }
    
    /**
     * Player can use Scorecard to log a score for the given turn based on
     * current Dice. Also sets the joker rule flag for Scorecard if applicable.
     */
    @Override
    public boolean score(int index){
        //check if player already used yahtzee and they currently have yahtzee,
        //this enables joker rules in play.
        return this.myScorecard.chooseScore(index); 
    }
 
    /**
     * Check if joker rules are in play.
     * @return true if current dice are yahtzee and yahtzee has already been
     * scored. Setting joker rules allows for the 50 point bonus when scoring
     * if players scorecard scored 50 for yatzee. False otherwise.
     */
    public boolean isJokerRules(){
        if(this.myScorecard.yahtzeeScored()&&(this.myScorecard.isYahtzee())){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Cheat sets current dice value according to players input. Shame on you.
     * @param dieValue int array with the 5 values to set current die.
     */
    public void cheat(int[] dieValue){
        for(int i=0;i<this.yahtzeeDice.getSize();i++){
        this.yahtzeeDice.set(i, dieValue[i]);
        }
    }  
    
}
