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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Holds player's current Yahtzee score. Class is responsible
 * for computing totals and keeping track of what catagories are already scored.
*/

public class Scorecard {
    
    private ScoreCatagory[] scoreData;
    private int allBonus;
    //1 bonusChip == 100 pt bonus
    private int bonusChip;
    private int totalScore;
    private Dice playersDice;
    //see hasbro manual page for joker rules.
    private boolean jokerRules;
    private String characterName;
    
    ScoreCard_JPanel p_scoreCardContainer;
    
    /**
     * Construct a Scorecard Object. Initializes all scores to -1 and 
     * sets up the view.
     */
    public Scorecard(String CharacterType){
        characterName = "";
        scoreData = new ScoreCatagory[13];
        
        //fill scorecard with names of scoring catagories.
        String[] catagoryNames= {
            "Aces(Ones)","Twos","Threes","Fours",
            "Fives","Sixes","3 of a Kind","4 of a Kind",
            "Full House","Small Straight","Large Straight","YAHTZEE","Chance"
        };
        
        
        for (int i=0;i<this.scoreData.length;i++){
            scoreData[i] = new ScoreCatagory(catagoryNames[i],-1);
        }
         
        this.totalScore = 0;
        this.bonusChip=0;
        this.allBonus=0;
        this.playersDice = Dice.getDiceInstance();
        this.jokerRules=false;
        
        /************* GUI VIEW COMPONENTS *********************/
        
        p_scoreCardContainer = new ScoreCard_JPanel(CharacterType+"_ScoreCard");
        p_scoreCardContainer.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,0,0,0);
        c.fill = GridBagConstraints.VERTICAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        for(int i=0;i<this.scoreData.length;i++){
            p_scoreCardContainer.add(scoreData[i].getView(),c);
            c.gridy += 1;
        }        
        
        /********************************************************/
    }
    
    /**
     * Print scorecard string to standard out.
     */
    public void printScoreCard(){
        System.out.println("Scorecard:");
        for(int i=0;i<this.scoreData.length;i++){
            String score = new Integer((this.scoreData[i].getScore())).toString();
            if(this.scoreData[i].getScore()==-1){score="";}
            System.out.printf("%-3d %-20s %s \n",i,this.scoreData[i].getName()
                    +": ",score);
        }
        //print bonus chips: represented by *
        System.out.print("Bonus Chips: ");
        for(int i=0;i<bonusChip;i++){
            System.out.print("*");
        }
        //print upper catagory bonus. i.e 0 or 35.
        System.out.println("");
        System.out.println("Bonus: "+
                (new Integer(this.allBonus)).toString());
        //print total score
        System.out.println("Total Score: "+
                (new Integer(this.totalScore)).toString());
    }
    
    /**
     * Choose a catagory to score based on the current instance of Dice.
     * @param: entry is the number scoring catagory on scorecard to mark for 
     *          current dice values.
     * @return true if scored, false otherwise (score was already 'scored').
     */
    public boolean chooseScore(int entry) throws ArrayIndexOutOfBoundsException{
        /*
         * note: this is is kind of a messey way of validating that
         * clients entry was correct and updating scoreData but other methods
         * where tested and I very much believe resulted in even harder
         * to manage and read code. Other methods tried included 1) a switch
         * statement(13 cases were too much..expecially for the 'lower score' optiong
         * that simply required one if statement below. Method 2) modified 
         * array of 'function' pointers. This requred too many classes to work
         * around the fact that java doesnt support function pointers and was
         * again, messy.
         */
        if((entry>=0)&&(entry<this.scoreData.length)){
            //if entry is 0-5, we get total of that score options dice.
            if (entry<6){
                //if the catagory has been scored already, return false.
                if(this.isScored(entry)){return false;}
                //find summation of all dice with the value.
                int total = addDice(entry+1);
                this.scoreData[entry].setScore(total);
                this.syncData();
                this.jokerRules = false;
                return true;
            //check for 3 and 4 of a kind
            }else if(entry<8){
                if(this.isScored(entry)){return false;}
                //note:(entry-3). So if entry==7, then 4 of a kind.
                if(this.isNumOfKind(entry-3)||(this.jokerRules())){
                    this.scoreData[entry].setScore(this.totalAllDice());
                }else{this.scoreData[entry].setScore(0);}
                this.syncData();
                this.jokerRules = false;
                return true;
            //full house
            }else if(entry==8){
                if(this.isScored(entry)){return false;}
                if(this.isFullHouse()||(this.jokerRules())){
                    this.scoreData[entry].setScore(25);
                }else{this.scoreData[entry].setScore(0);}
                this.syncData();
                this.jokerRules = false;
                return true;
            //small straight
            }else if(entry==9){
                if(this.isScored(entry)){return false;}
                if(this.isStraight(4)||(this.jokerRules())){
                    this.scoreData[entry].setScore(30);
                }else{this.scoreData[entry].setScore(0);}
                this.syncData();
                this.jokerRules = false;
                return true;
            //large straight
            }else if(entry==10){
                if(this.isScored(entry)){return false;}
                if(this.isStraight(5)||(this.jokerRules())){
                    this.scoreData[entry].setScore(40);
                }else{this.scoreData[entry].setScore(0);}
                this.syncData();
                this.jokerRules = false;
                return true;
            //YAHTZEE!
            }else if(entry==11){
                if(this.isScored(entry)){return false;}
                if(this.isYahtzee()){
                    //bonus chips are handled below by setting the
                    //joker rules on. If joker rules are applied that means
                    //a bonus chip is given.
                    if(!this.isScored(11)){
                        this.scoreData[entry].setScore(50);
                    }
                }else{
                    this.scoreData[entry].setScore(0);
                }
                this.syncData();
                this.jokerRules = false;
                return true;
            //chance
            }else{
                if(this.isScored(entry)){return false;}
                this.scoreData[entry].setScore(this.totalAllDice());
                this.syncData();
                this.jokerRules = false;
                return true;
            }
            
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    /** 
     * Summation of all instances of the die for current dice instance.
     * @param dieValue: die value to find and sum.
     * @return total of those die.
     */
    public int addDice(int dieValue){
        int total=0;
        for (int i=0;i<playersDice.getSize();i++){
            if (dieValue==playersDice.get(i).getValue()){
                total+=dieValue;
            }
        }
        return total;
    }
    
    /**
     * Returns summation of all dice.
     */
    public int totalAllDice(){
        int total=0;
        for (int i=0;i<playersDice.getSize();i++){
            total+=playersDice.get(i).getValue();
        }
        return total;
    }
    
    /**
     *Returns true if the dice are ___ of a kind. For instance if num==3 than 
     *function returns true when dice are 3 of a kind (or more).
     */
    public boolean isNumOfKind(int num){
        int count[]={0,0,0,0,0,0};
        //get count for each die
        for (int i=0;i<this.playersDice.getSize();i++){
            count[playersDice.get(i).getValue()-1]++;
        }
        for (int i=0;i<this.playersDice.getSize();i++){
            if(count[i]>=num){
                return true;
            } 
        }
        return false;
    }
    
    /*
     * This function is used as a helper for isFullHouse. isNumOfKind()
     * will return true if the current dice are [num] of a kind OR GREATER. This
     * function only returns true if the dice are exactly [num] of a kind
     */
    private boolean isExactlyNumOfKind(int num){
        int count[]={0,0,0,0,0,0};
        //get count for each die
        for (int i=0;i<this.playersDice.getSize();i++){
            count[playersDice.get(i).getValue()-1]++;
        }
        for (int i=0;i<this.playersDice.getSize();i++){
            if(count[i]==num){
                return true;
            } 
        }
        return false;
    }
    
    /**
     * @return: true if players dice instance is a full house, false otherwise. 
     */
    public boolean isFullHouse(){
        if((this.isExactlyNumOfKind(3))&&(this.isExactlyNumOfKind(2))){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * @return: true if current dice are a [num] straight. False otherwise.
     */
    public boolean isStraight(int num){
        int count=1;
        //start loop at 1 since we manualy get value of die 0;
        int start=1;
        int currentValue = this.playersDice.get(0).getValue();
        for (int i=start;i<this.playersDice.getSize();i++){
            if((currentValue+1)== this.playersDice.get(i).getValue()){
                count++;
            }else{
                count=1;
            }
            currentValue = this.playersDice.get(i).getValue();
        }
        if(count>=num){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * @param: catagory index that corresponds to score card data index.
     * @return true if catagory was alread scored.
     */
    public boolean isScored(int catagoryIndex){
        if((this.scoreData[catagoryIndex].getScore()) == -1){
            return false;
        }else{
            return true;
        }
    }
    
    /**
     * Return current state of joker rules and change the state to false (reset).
     */
    private boolean jokerRules(){
        boolean temp=this.jokerRules;
        this.jokerRules=false;
        return temp;
    }
    
    /**
     * set member var jokerRules. Example usage will be when the player's dice
     * is a yahtzee, automatically set jokeRules to true to allow the 
     * player to choose any catagory.
     */
    public void setJokerRules(boolean s){
        //only award bonus points if yahzee box is scored 50.
        if (this.scoreData[11].getScore()==50){this.bonusChip++;}
        this.jokerRules=s;
    }
    
     /**
     * Update total data (total, bonus, and upper scoring bonus).
     */
    private void syncData(){
        //temp sum
        int t=0;
        //temp bonus sum
        int b=0;
        int tempSum=0;
        //summation of all data
        for (int i=0;i<this.scoreData.length;i++){
            if(this.scoreData[i].getScore()!=-1){
                t+=this.scoreData[i].getScore();
            }
        }
        //check for upper bonus
        for (int i=0;i<6;i++){
            if(this.scoreData[i].getScore()!=-1){
                tempSum+=this.scoreData[i].getScore();
            }
        }
        //"...goal in the upper section is to score at least 63 points to earn 
        // the 35 point bonus.
        if (tempSum>=63){
            b+=35;
            t+=35;
        }
        //add yahtzee bonus (if applicable)
        b+=this.bonusChip*100;
        t+=this.bonusChip*100;
        
        this.totalScore = t;
        this.allBonus = b;
    }
    /**
     * Return true if all catagories have already been selected.
     */
    public boolean allCatagoriesComplete(){
        for(int i=0;i<this.scoreData.length;i++){
            if(this.scoreData[i].getScore()==-1){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Return true if currrent dice instance is a yahtzee.
     */
    public boolean isYahtzee(){
        //note: slightly more efficient than doing isNumOfKind(5).
        int v=playersDice.get(0).getValue();
        for (int i=0;i<playersDice.getSize();i++){
            if (v!=playersDice.get(i).getValue()){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns True if player already used Yahtzee. False otherwise.
     */
    public boolean yahtzeeScored(){
        if (this.scoreData[11].getScore() != -1){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * Return scorecards total. 
     */
    public int getTotalScore(){
        return this.totalScore;
    }
    
    /**
     * Return a formatted string for printing.
     */
    @Override
    public String toString(){
        StringBuilder scorecardString = new StringBuilder();
        scorecardString.append("Scorecard: \n");
        for(int i=0;i<this.scoreData.length;i++){
            String formattedStr = String.format("%-20s %s \n",
                    this.scoreData[i].getName(),
                    this.scoreData[i].getScore());
            scorecardString.append(formattedStr);
        }
        //print bonus chips: represented by *
        scorecardString.append("Bonus Chips: ");
        for(int i=0;i<bonusChip;i++){
            scorecardString.append("* ");
        }
        //print upper catagory bonus. i.e 0 or 35.
        scorecardString.append("\n");
        scorecardString.append(("Bonus: "+
                (new Integer(this.allBonus)).toString()+"\n"));
        //print total score
        scorecardString.append((("Total Score: "+
                ((new Integer(this.totalScore)).toString()))+"\n"));
        return scorecardString.toString();
    }
    
    /**
     * Return Scorecard view. 
     */
    public JPanel getView(){        
        return this.p_scoreCardContainer;
    }
        
    /**
     * Return a reference to catagory buttons on a scorecard.
     * <p>Used mainly if you want to test which button was pressed in 
     * conjunction with the callback routine (addScoreListener).
     * @param index the score catagory.
     * @return button object that fires ActionEvents when scored.
     */
    public JButton getGUIComponent(int index){
        return (scoreData[index].getView()).getCatagoryButton();
    }
    
    /**
     * Listen for player to 'score'.
     * @param l 
     */
    public void addScoreListener(ActionListener l){
        for (int i=0;i<this.scoreData.length;i++){
            (this.scoreData[i].getView()).addActionListener(l);
        }
    }
    
    /**
     * Reset the scorecard to it's default, constructed state.
     */
    public void reset(){
        this.allBonus = 0;
        this.bonusChip = 0;
        this.totalScore = 0;
        for(int i=0;i<this.scoreData.length;i++){
            this.scoreData[i].setScore(-1);
        }
    }
    
    /**
     * JPanel with ScoreCard background Image.
     */
    private class ScoreCard_JPanel extends JPanel{
        private Image backgroundImg;
        
        /**
         * Construct ScoreCard_JPanel object with a background image.
         * @param img image to be placed in the background of jpanel.
         */
         public ScoreCard_JPanel(String img){
            //load background image
             try {
                Image image = ImageIO.read(new File("./graphics/"+img+".jpg"));
                this.backgroundImg = image.getScaledInstance(300,260,java.awt.Image.SCALE_SMOOTH ); 
                
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImg,0,0,null);
            if(jokerRules==true){
                g.setColor(Color.RED);
                g.setFont(new Font("TimesRoman", Font.BOLD, 16));
                g.drawString("Joker Rules in Effect!",(this.getX()+145),
                        (this.getY()+80));
            }
        }
    }
}
