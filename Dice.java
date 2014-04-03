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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
* YahtzeeDie holds a number of YahtzeeDie, default is 5. This class follows
*   the singleton pattern to ensure only one set of dice is used at a time.
*/

public class Dice {
    /*volatile tells the java compiler that diceInstance may be changed
     *in other threads, and thus to not do certain optimizations.*/
    private volatile static Dice diceInstance;
    private YahtzeeDie[] diceArray;
    private int arraySize;
    //hold array is parrelel to diceArray. 1 if held, 0 otherwise. 
    private int[] holdArray;
    //dice will not roll if false
    private boolean rollable;
    
    /********** GUI 'view' components *************/
     
    JPanel p_DiceContainer;

    /***********************************************
    
    /**
     * Constructs instance of Dice with 5 YahtzeeDice.
     */
    private Dice(){
        this.arraySize=5;
        this.diceArray = new YahtzeeDie[this.arraySize];
        this.holdArray = new int[this.arraySize];
        for(int i=0;i<this.arraySize;i++){
            diceArray[i]=new YahtzeeDie();
            holdArray[i]=0;
        }
        rollable = true;
        
        /********** GUI 'view' components *************/
        p_DiceContainer = new JPanel(new GridBagLayout());
        JPanel p_sizer = new JPanel(new GridBagLayout());
        p_sizer.setPreferredSize(new Dimension(590,120));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0,4,0,4);
        c.gridx = 0;
        c.gridy = 0;

        for(int i=0;i<diceArray.length;i++){
            //add hold/release listener to dice
            (diceArray[i].getView()).addActionListener(new HoldListener());

            p_sizer.add(diceArray[i].getView(),c);
            c.gridx++;
        }
        p_DiceContainer.setOpaque(false);
        p_sizer.setOpaque(false);
        p_DiceContainer.add(p_sizer);

        cheatKeyListener cheatListener = new cheatKeyListener();
        p_DiceContainer.addKeyListener(cheatListener);
        /*************************************************/
        
    }
    
     /**
     * Constructs instance of Dice with 5 YahtzeeDice, all using a predictable
     * sequence of random number generation using the given seed.
     */
    private Dice(int seed){
        this.arraySize=5;
        this.diceArray = new YahtzeeDie[this.arraySize];
        this.holdArray = new int[this.arraySize];
        for(int i=0;i<this.arraySize;i++){
            diceArray[i]=new YahtzeeDie(seed);
            holdArray[i]=0;
        }
    }
    
    /**
     * get singleton object reference.This is the only way to get
     * access to the class. For more see singleton design pattern.
     * @return diceInstance, the singleton object reference.
     */
    public static Dice getDiceInstance(){
        if(diceInstance==null){
            /*synchronized provides locks to keep this singleton object
             * thread-safe. It also ensures the resulting value is 
             * visible to all threads. */
            synchronized (Dice.class){
                if(diceInstance==null){
                    diceInstance = new Dice();
                }
            }
        }
        return diceInstance;
        
    }
    
    /**
     * get singleton object reference.This is the only way to get
     * access to the class. For more see singleton design pattern.
     * This differs from the above because it allows creation of Dice
     * using a seed for the random value generation, making die sequence values
     * predicable.
     * @return diceInstance, the singleton object reference.
     */
    public static Dice getDiceInstance(int seed){
        if(diceInstance==null){
            /*synchronized provides locks to keep this singleton object
             * thread-safe. It also ensures the resulting value is 
             * visible to all threads. */
            synchronized (Dice.class){
                if(diceInstance==null){
                    diceInstance = new Dice(seed);
                }
            }
        }
        return diceInstance;
        
    }
    
    /**
     * @return size of dice array (integer).
     */
    public int getSize(){
        return this.arraySize;
    }
    
    /**
     * Return reference to Yahtzee Die at index.
     */
    public YahtzeeDie get(int index){
        return this.diceArray[index];
    }
    
    /**
     * Set yahtzeeDie at index to value.
     */
    public void set(int index, int value){
        this.diceArray[index].setValue(value);
        this.updateDiceView();
    }
    
    /**
     * Set true if dice may be 'rolled', false otherwise. 
     */
    public void setRollable(boolean rollable){
        this.rollable = rollable;
    }
    
    public boolean getRollable(){
        return this.rollable;
    }
    
    /**
     * @return string representation of object. value0, value1, value2...valueN
     */
    @Override
    public String toString(){
        StringBuilder outputBuilder = new StringBuilder();
        for(int i=0;i<this.arraySize;i++){
            String temp = this.get(i).getValue()+" ";
            outputBuilder.append(temp);
        }
        String output = outputBuilder.toString();
        return output;
    }
    
    /**
     * set hold on Yahtzee die at index. If already set, do nothing.
     */
    public void setHold(int index) throws ArrayIndexOutOfBoundsException{
        //validate inputs
        if ((index<this.getSize()&&(index>=0))){
            this.holdArray[index]=1;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
        
        JButton tempView = diceArray[index].getView();
        tempView.setBorder(new LineBorder(Color.YELLOW, 2));
    }
    
    /**
     * Check if dice at index is 'held' (cannot be rolled).
     * @param index
     * @return true if held, false otherwise.
     * @throws ArrayIndexOutOfBoundsException 
     */
    public boolean isHeld(int index) throws ArrayIndexOutOfBoundsException{
        //validate input
        if ((index<this.getSize()&&(index>=0))){
            if(this.holdArray[index]==1){
                return true;
            }else{
                return false;
            }
        }else{
            //false
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    /**
     * if YatzeeDie at index  is held, relase the hold.
     * If die is not held then do nothing.
     */
    public void releaseHold(int index) throws ArrayIndexOutOfBoundsException{
        if ((index<this.getSize()&&(index>=0))){
            this.holdArray[index]=0;
        }else{
            throw new ArrayIndexOutOfBoundsException();
        }
        
        JButton tempView = diceArray[index].getView();
        tempView.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
    }
    
    /**
     * roll all non-held YahtzeeDie.
     */
    public void roll(){
        for(int i=0;i<this.getSize();i++){
            if(this.holdArray[i]==0){
                this.get(i).roll();
            }
        }
        updateDiceView();
    }
    
    /**
     * Release hold on all dice.
     */
    public void releaseAll(){
         for(int i=0;i<this.getSize();i++){
            this.releaseHold(i);
        }
    }
    
    /**
     * Add and return value of all dice.
     */
    public int summation(){
        int total=0;
        for(int i=0;i<this.getSize();i++){
            total+=this.diceArray[i].getValue();
        }
        return total;
    }
    
    /**
     * Return dice view.
     */
    public JPanel getDiceView(){
        return p_DiceContainer;
    }
    
    /**
     * Update Dice 'view' to represent current data.
     */
    public void updateDiceView(){
        for(int i=0;i<diceArray.length;i++){
            diceArray[i].updateView();
        }
         //get focus on dice.
         this.getDiceView().requestFocus();
    }
    
    /**
     * Listener for 'hold' button.
     */
    private class HoldListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            //find the index for the die that action was fired from
            //and hold/release that die.
            int index = -1;
            for(int i=0;i<diceArray.length;i++){
                if(e.getSource() == ((diceArray[i])).getView()){
                    index = i;
                }
            }
            
            if(isHeld(index)){
                releaseHold(index);
            }else{
                setHold(index);
            }
        }
    }
    
    /**
     * Listener for cheat keys.
     * <p>Press 'c' and then enter values [1-6] for dice going from left to
     * right as displayed.
     */
    class cheatKeyListener extends KeyAdapter{
        char lastKeyPressed;
        int diceCounter;
        
        public cheatKeyListener(){
            lastKeyPressed = 'a';
            diceCounter = 0;
        }
        
        @Override
        public void keyPressed(KeyEvent e){
            char key = e.getKeyChar();
            if ((key=='c')&&(!isValidNumeral(lastKeyPressed))){
                //start dice cheat
                this.lastKeyPressed = 'c';
            }else if((isValidNumeral(key))&&(lastKeyPressed == 'c')){
                //enter numbers to set dice values
                set(diceCounter,(new Integer(key)-48));
                diceCounter++;
                if(diceCounter == 5){
                    diceCounter = 0;
                    //exit the cheat menu
                    this.lastKeyPressed = 'a';
                }
            }else if((key=='c')&&(this.lastKeyPressed == 'c')){
                //exit cheat menu mid entering by clicking 'c' again...
                lastKeyPressed = 'a';
            }else{
                //do nothing
            }
        }
        
        private boolean isValidNumeral(char test){
            if((test=='1')||(test=='2')||(test=='3')||(test=='4')
                    ||(test=='5')||(test=='6')){
                return true;
            }else{
                return false;
            }
        }
    }
}
