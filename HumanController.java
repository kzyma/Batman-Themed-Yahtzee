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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

/**
* HumanController provides the concrete class to handle model and view,
* Human and HumanView.
* <p>Follows the model-view-controller pattern.
*/
public class HumanController implements PlayerController {
    private Human model;
    private HumanView view;
    private int rollCounter;
    private CallbackEvent turnOverListener;
    
    /**
     * Construct HumanController Object.
     */
    public HumanController(Human model, HumanView view){
        this.model = model;
        this.view = view;
        rollCounter = 0;
        
        this.getScorecard().addScoreListener(new ScoreListener());
    }
    
    /**
     * Add callback routine to listen for end of turn.
     */
    @Override
    public void addturnOverListener(CallbackEvent l){
        turnOverListener = l;
    }
    
    /**
     * Signal Human's turn is over.
     */
    @Override
    public void turnOver(){
        turnOverListener.eventPerformed();
    }
       
     /**
     * Get action input from the view and routes choice to appropriate action.
     */
    @Override
    public void decideAction(){
        //do nothing.
    }   
    
     /**
     * Player can use Scorecard to log a score for the given turn based on
     * current Dice. Also sets the joker rule flag for Scorecard if applicable.
     */
    public boolean score(int index){
        //check if player already used yahtzee and they currently have yahtzee,
        //this enables joker rules in play.
        Scorecard myScorecard = getScorecard();
        if(this.isJokerRules()){
            myScorecard.setJokerRules(true);
        }
        boolean result =  myScorecard.chooseScore(index); 
        if(result==true){
            this.turnOver();
        }
        //update groupScore with new total score.
        GroupTotalScoreView totalScore = 
                GroupTotalScoreView.getGroupTotalScoreInstance();
        totalScore.updateScore(this.model.getPlayerNumber(),this.getScore());
        
        return result;
    }
 
    /**
     * Check if joker rules are in play.
     * @return true if current dice are yahtzee and yahtzee has already been
     * scored. Setting joker rules allows for the 50 point bonus when scoring
     * if players scorecard scored 50 for yatzee. False otherwise.
     */
    public boolean isJokerRules(){
        Scorecard myScorecard = getScorecard();
        if(myScorecard.yahtzeeScored()&&(myScorecard.isYahtzee())){
            return true;
        }else{
            return false;
        }
    }
    
    
    /**
     * Roll the game Dice.
     */
    @Override
    public void rollDice(){
        if (this.rollCounter < 3){
        model.roll();
        this.rollCounter++;
        }
        
        //check for joker rules
        if(this.isJokerRules()){
            (this.model.getScorecard()).setJokerRules(true);
            this.model.getScorecard().getView().repaint();
        }
        
    }
    
    /**
     * A Human's possible choices change throughout the turn. newTurn()
     * returns humans state to first move of a turn.
     */
    @Override
    public void newTurn(){
        Dice dice = Dice.getDiceInstance();
        dice.releaseAll();
        
        this.rollDice();
        this.rollCounter = 1;
    }
    
    /**
     * Return true if player is finished playing (scorecard is full), and
     *  false otherwise.
     */
    @Override
    public boolean donePlaying(){
        return model.getScorecard().allCatagoriesComplete();
    }
    
    /**
     * Return: Returns Players scorecard.
     */
    @Override
    public Scorecard getScorecard(){
        return this.model.getScorecard();
    }
    
    /**
     * Return players currrent score.
     */
    public int getScore(){
        return ((this.getScorecard()).getTotalScore());
    }
    
     /**
     * Returns Players model's name.
     */
    @Override
    public String getName(){
        return this.model.getName();
    }
    
    /**
     * Set model's name
     */
    @Override
    public void setName(String name){
        this.model.setName(name);
    }
    
     /**
     * Get Panel Reference for a players scorecard.
     */
    @Override
    public JPanel getScoreView(){
        return this.view.getScoreCardView(this.model.getName(),
                this.model.getScorecard());
    }
    
    /**
     * Return string representing model name, and scorecard.
     */
    @Override
    public String toString(){
        StringBuilder humanString = new StringBuilder();
        humanString.append((this.model.getName()+"\n"));
        humanString.append(this.model.getScorecard().toString());
        return humanString.toString(); 
    }
    
    /**
     * Return human's global controller view. 
     */
    @Override
    public JPanel getGlobalControllerView(){
        return view.get_GlobalControllerView(new RollAction());
    }
    
    /**
     * Reset Player attributes between games, such as resetting scorecard.
     * <p>Note this does not reset player's monet desposited, name, ect.
     */
    @Override
    public void reset(){
        (this.model.getScorecard()).reset();
        //this.view.update_GlobalControllerView();
    }
    
    /**
     * Listener for dice roll.
     */
    private class RollAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Dice dice = Dice.getDiceInstance();
            if(dice.getRollable()){
                rollDice();
            }
        }
    }
    
    /**
     * Listener for a 'score catagory' is filled.
     */
    private class ScoreListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int index = -1;
            for(int i=0;i<13;i++){
                if (((getScorecard()).getGUIComponent(i)) == e.getSource()){
                    index = i;
                }
            }
         score(index);  
        }
    }
         
}
