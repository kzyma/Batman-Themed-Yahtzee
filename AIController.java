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
import javax.swing.Timer;

/**
* AIController provides the concrete class to handle model and view,
* AI and AIView.
*/

public class AIController implements PlayerController {
    private AI model;
    private AIView view;
    private int rollCounter;
    private char lastTurn;
    private CallbackEvent turnOverListener;
    private Timer actionTimer;
    
    /**
     * Construct AIController object. 
     */
    public AIController(AI model, AIView view){
        this.model = model;
        this.view = view;
        this.rollCounter = 0;
        
        actionTimer = new Timer(1000,new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                AIController.this.action();
            }
        });
        actionTimer.stop();
    }
    
    /**
     * Add callback routine to be fired when player's turn is over.
     * @param l 
     */
    @Override
    public void addturnOverListener(CallbackEvent l){
        turnOverListener = l;
    }
    
    /**
     * Fire turnover event.
     */
    @Override
    public void turnOver(){
        //turn timer off
        this.actionTimer.stop();
        
        //update group score card.
        GroupTotalScoreView totalScore = 
                GroupTotalScoreView.getGroupTotalScoreInstance();
        totalScore.updateScore(this.model.getPlayerNumber(),this.getScore());
        
        turnOverListener.eventPerformed();
    }
    
    /**
     * Return current score. 
     */
    public int getScore(){
        return ((this.getScorecard()).getTotalScore());
    }
    
     /**
     * AI decideAction plays an entire turn.
     * <p>Uses helper action to decide the action for each 'turn'.
     */
    @Override
    public void decideAction(){
        actionTimer.start();
    }
    
    /**
     * Helper Function for decideAction.
     * <p>Use for the AI to decide one action, opposed to decideAction which
     * will continue until a score is made.
     *          Actions choosen:
     *          r:roll,h:hold,l:release,s:score.
     */
    private void action(){
        char action =  this.model.decideAction(this.rollCounter);
        if((action=='h')&&(this.lastTurn=='h')){
            (this.view.get_RollButton()).doClick();
            action='r';
        }
        this.lastTurn= action;
        if(action=='r'){
            (this.view.get_RollButton()).doClick();
        }
        
        if(action == 's'){
            this.turnOver();
        }
    }
    
    /**
     * An AI's possible choices change throughout the turn. newTurn()
     * returns AI state to first move of a turn.
     */
    @Override
    public void newTurn(){
        Dice dice = Dice.getDiceInstance();
        dice.releaseAll();
        
        this.rollDice();
        this.rollCounter = 1; 
        this.lastTurn = 'r';
    }
    
     /**
     * Roll the game Dice.
     */
    @Override
    public void rollDice(){
        model.roll();
        this.rollCounter++;
    }
    
    /**
     * @return: true if player is finished playing (scorecard is full), and
     *  false otherwise.
     */
    @Override
    public boolean donePlaying(){
        return model.getScorecard().allCatagoriesComplete();
    }
    
     /**
     * @return: Returns Players scorecard.
     */
    @Override
    public Scorecard getScorecard(){
        return this.model.getScorecard();
    }
    
    /**
     * @return Returns Players model's name.
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
     * @return string representing model name, and scorecard.
     */
    @Override
    public String toString(){
        StringBuilder humanString = new StringBuilder();
        humanString.append((this.model.getName()+"\n"));
        humanString.append(this.model.getScorecard().toString());
        return humanString.toString(); 
    }
    
    /**
     * Reset player's attributes between games.
     */
    @Override
    public void reset(){
        (this.model.getScorecard()).reset();
        //this.view.update_GlobalControllerView();
    }
    
    /**
     * Return globalControllerView. Panel contains roll button, and desposit/
     * bet buttons.
     */
    @Override
    public JPanel getGlobalControllerView(){
        return view.get_GlobalControllerView(new RollAction());
    }
    
    /**
     * Listen for 'roll' button press. Calls this.rollDice() if event fired and
     * the dice are in a rollable state.
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
}

