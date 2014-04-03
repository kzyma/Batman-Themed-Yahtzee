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
* AI represents a artificial agent in yahtzee. AI player knows how to make
* his or her own game 'moves' and scoring.
*/

public class AI implements Player{
    private Dice yahtzeeDice;
    private Scorecard myScorecard;
    private String name;
    private String character;
    //keep track of # of AI instances created, used for naming AI only.
    private static int classCounter=1;
    private boolean jokerRules;
    private int scoreCatagoryLength;
    private int playerNumber;

    
    /**
     * Construct AI model object.
     */
    public AI(int playerNumber,String characterType){
        yahtzeeDice = Dice.getDiceInstance();
        character = characterType;
        myScorecard = new Scorecard(characterType);
        //Generic name for run of Yahtzee
        name = "AI Player "+classCounter;
        classCounter++;
        jokerRules=false;
        scoreCatagoryLength=13;
        this.playerNumber = playerNumber;
    }
    
    /**
     * Return player number.
     */
    public int getPlayerNumber(){
        return this.playerNumber;
    }
    /**
     * Decide an action for AI to take. This is the 'main brain'.
     * @param: rollCounter. Different choices are avaliable based on 
     * which roll it is (ex1: roll 1: AI can only score).
     */
    public char decideAction(int rollCounter){
        /* Simple AI for first project.
         * If yahtzee, this is automatically choosen. Otherwise
         * should figure out the best likely choice, based on 
         * (chance of getting that option X pnts it will be worth).
         * Right now it just makes a slightly educated guess as 
         * a simplfied AI.
         */
        boolean decisionMade = false;
        char action='r';
        int scoreOption=0;
        int[] hold = new int[0];

        if(this.myScorecard.yahtzeeScored()&&(this.myScorecard.isYahtzee())){
            action = 's';
            this.setJokerRules(true);
            decisionMade = true;
        }
        if((this.myScorecard.isYahtzee())&&(!decisionMade)){
            action = 's';
            scoreOption = 11;
            decisionMade = true;
        }
        //if it's the last turn, must make a score. Find highest possibility
        if((!decisionMade)&&(rollCounter>2)){
            action = 's';
            scoreOption = this.findHighestScoreOption();
            decisionMade = true;
        }
        //if not last turn, hold for best probabil option.
        if(!decisionMade){
            action = 'h';
            //note: literal 10=Large Straight
            if((this.myScorecard.isStraight(4))&&
                    (this.myScorecard.isScored(10))){
                hold=holdStraight(4);
            //note: literal 9=small straight
            }else if((this.myScorecard.isStraight(3))&&
                    (this.myScorecard.isScored(9))){
                hold=holdStraight(3);
            }else if(this.myScorecard.isNumOfKind(3)){
                hold=holdOfAKind(3);
            }else if(this.myScorecard.isNumOfKind(2)){
                hold=holdOfAKind(2);
            }else{
                hold = new int[0];
            }
            //desicionMade=true;
        }
        
        switch(action){
            case 'h':
                this.hold(hold);
                break;
            case 'l':
                //this.release(release);    Unused for now.Do nothing.
                break;
            case 's':
                this.score(scoreOption);
                break;
        }
        
        return action;
    }
    
    /**
     * Helper function for decideAction. <p>Looks at current dice config and
     * decides which holds should occur.
     * param: Num is the number of dice that should be held.
     * <p>example usage: You know that 2 dice in a row are in order and 
     * would like to hold them. pass 2 into this function and
     * it will get the FIRST INSTANCE where two dice are in the same order
     * (the first instance is the best always it allows for the largest straight.
     */
    private int[] holdStraight(int num){
        int count=0;
        //start loop at 1 since we manualy get value of die 0;
        int start=1;
        int currentValue = this.yahtzeeDice.get(0).getValue();
        for (int i=start;i<this.yahtzeeDice.getSize();i++){
            if((currentValue+1)== this.yahtzeeDice.get(i).getValue()){
                count++;
                if(count>=num){
                    int[] holds = new int[num];
                    for (int j=0;j<num;j++){
                        holds[j]=i-j;
                    }
                    return holds;
                }
            }else{
                count=0;
            }
            currentValue = this.yahtzeeDice.get(i).getValue();
        }
        return new int[0];
    }
    
    /**
     * Helper function for decideAction with a similar use to holdStraight.
     * <p>You know there is a 3 of a kind being played so pass 3 into parameter
     * num and this function will return an array with the index's to hold
     */
    private int[] holdOfAKind(int num){
        int count[]={0,0,0,0,0,0};
        //get count for each die
        for (int i=0;i<this.yahtzeeDice.getSize();i++){
            count[this.yahtzeeDice.get(i).getValue()-1]++;
        }
        for (int i=0;i<this.yahtzeeDice.getSize();i++){
            if(count[i]>=num){
                //if count is greater, find index's to pass back for holding.
                int[] holds = new int[count[i]];
                int holdsIndex = 0;
                for(int j=0;j<this.yahtzeeDice.getSize();j++){
                    if((this.yahtzeeDice.get(j).getValue())==(i+1)){
                        holds[holdsIndex]=j;
                        holdsIndex++;
                    }
                }
                return holds;
            } 
        }
        return new int[0];
    }

    /**
     * Return the index for the highest scoring option based on currently un-scored
     * catagories. <p>This is simple and just uses the current value based on
     * current dice.
     */
    private int findHighestScoreOption(){
        int high=-1;
        int highValue=-1;
        for(int i=0;i<this.scoreCatagoryLength;i++){
            if((!this.myScorecard.isScored(i))&&
                    (currentScoreValue(i)>highValue)){
                high = i;
                highValue=currentScoreValue(i);
            }
        }
        return high;
    }
    
    /**
     * helper function for findHighestScoreOption. Calculates and returns the 
     * score for a given scoring option based on the current dice. <p>Also note,
     * if "joker Rules" are in effect scoring differs, so set joker rules in
     * the class before calling.
     */
    private int currentScoreValue(int index){
        int score;
        //if entry is 0-5, we get total of that score options dice.
        if (index<6){
            score = this.myScorecard.addDice(index+1);
        //check for 3 and 4 of a kind
        }else if(index<8){
            //note:(entry-3). So if entry==7, then 4 of a kind.
            if((this.myScorecard.isNumOfKind(index-3))||(this.jokerRules())){
                score = this.myScorecard.totalAllDice();
            }else{score = 0;}
        //full house
        }else if(index==8){
            if((this.myScorecard.isFullHouse())||(this.jokerRules())){
                score = 25;
        }else{score = 0;}
        //small straight
        }else if(index==9){
            if((this.myScorecard.isStraight(4))||(this.jokerRules())){
                score = 30;
            }else{score = 0;}
        //large straight
        }else if(index==10){
            if((this.myScorecard.isStraight(5))||(this.jokerRules())){
                score = 40;
            }else{score = 0;}
            //YAHTZEE!
        }else if(index==11){
            if(this.myScorecard.isYahtzee()){
                score = 50;
            }else{
                score = 0;
            }
        //chance
        }else{
                score = this.myScorecard.totalAllDice();
            }
        this.jokerRules = false;
        return score;
    }
    
    /**
     * roll yahtzee dice
     */
    @Override
    public void roll(){
        yahtzeeDice.roll();
    }     
    
    /**
     * 'Hold'/lock dice so they cannot be rolled.
     * @param holds array with dice index's for holding.
     */
    @Override
    public void hold(int[] holds){
        for (int i=0;i<holds.length;i++){
            this.yahtzeeDice.setHold(holds[i]);
        }
    }
    
    /**
     * Player can release holds for current Dice instance. If already 'released'
     * and able to roll, do nothing.
     */
    @Override
    public void release(int[] releases){
        for (int i=0;i<releases.length;i++){
            this.yahtzeeDice.releaseHold(releases[i]);
        }   
    }
    
    /**
     * Player can use Scorecard to log a score for the given turn based on
     * current Dice. Also sets the joker rule for Scorecard if applicable.
     */
    @Override
    public boolean score(int index){
        if(this.myScorecard.yahtzeeScored()&&(this.myScorecard.isYahtzee())){
            myScorecard.setJokerRules(true);
            System.out.println("Yahtzee! Joker Scoring Rules in Effect.");
        }
        this.myScorecard.chooseScore(index); 
        return true;
    }
    
    /**
     * @return players Scorecard object.
     */  
    @Override
    public Scorecard getScorecard(){
        return this.myScorecard;
    }

    /**
     * @return players Name
     */
    @Override
    public String getName(){
        return this.name;
    }   
   
     /**
     * @return current state of joker rules and change the state to false (reset).
     */
    private boolean jokerRules(){
        boolean temp=this.jokerRules;
        this.jokerRules=false;
        return temp;
    }
    
    /**
     * set member var jokerRules. Example usage will be when the player's dice
     * is a yahtzee, automatically set jokeRules to true to allow the 
     * player to choose any catagory. This is done in the Player's controllers.
     */
    private void setJokerRules(boolean s){
        this.jokerRules=s;
    }
    
    /**
     * Set Player's name.
     */
    @Override
    public void setName(String name){
        this.name = name;
    }
}