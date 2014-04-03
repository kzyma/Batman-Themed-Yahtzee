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

import java.util.Random;

/**
* Class Random is a wrapper for java Random class.
*/

public class RandomGen {
    private long seed;
    private int min;
    private int max;
    Random randGen;
    /*keep count of number of RandomRen instantiated, incase two RandomGen
    * are made at the same systemTimeMillies their seed will still differ.*/
    private static int numOfInstances;
    /**
     * Construct Random object, seed is default to system time.
     * @param min: lowest random integer to generate
     * @param max: highest random integer to generate
     */
    public RandomGen(int min,int max){
        this((int)(System.currentTimeMillis()),min,max);
    }
    
    /**
     * Construct Random object.
     * @param seed: value to seed the pseudo-random number generator
     * @param low: lowest random integer to generate
     * @param high: highest random integer to generate
     */
    public RandomGen(long seed,int min,int max){
        this.numOfInstances++;
        this.seed=seed*numOfInstances;
        this.min=min;
        this.max=max;
        randGen = new Random(this.seed);
    }
    
    /**
     * Return next random integer.
     */
    public int nextInt(){
        return (this.randGen.nextInt(this.max-1)+this.min);
    }    
    
}
