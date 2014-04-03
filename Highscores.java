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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
*  Highscores data structure manages high score records for Yahtzee. Pulls
* data from HighscoreChart.txt for updating. Follows Model-View-Controller
* design pattern.
*/

public class Highscores {
    /**
     * A single 'player's record.
     */
    private class HighscoreRecord{
        private String name;
        private int totalScore;
        private String scoreCardData;
        
        /**
        * Construct HighscoreRecord object.
        */
        public HighscoreRecord(String name,int totalScore,String scoreCardData){
            this.name = name;
            this.totalScore = totalScore;
            this.scoreCardData = scoreCardData;
        }
    }
    
    //Highscores members
    private HighscoreRecord[] record;
    private int length;
    private int capacity;
    //is this structure representing the most current information?
    private boolean isUpdated;
    
    /**
     * Construct empty highscores structure. Must run readHighscores to fill the
     * structure.
     */
    public Highscores(){
        length = 10;
        record = new HighscoreRecord[length];
        for(int i=0;i<record.length;i++){
            record[i]=new HighscoreRecord("",-1,"");
        }
        //data has not been read to data structure.
        isUpdated = false;
    }
    
    /**
     * Read high score data from file "HighscoreChart.txt" to update object.
     * @throws IOException 
     */
    public void readHighscores() throws IOException{
        BufferedReader in = null;
        try{
            FileReader fr = new FileReader("HighscoreChart.txt");
            in = new BufferedReader(fr);
            for(int i=0;i<this.record.length;i++){
                String name = in.readLine();
                if(name==null){name="";}
                String scoreStr = in.readLine();
                if(scoreStr==null){
                    scoreStr="-1";
                }else{
                    scoreStr = scoreStr.replaceAll("[\n]","");
                }
                int score = new Integer(scoreStr);
                int scoresheetLines = 17;
                StringBuilder scorecardBuilder = new StringBuilder();
                for(int j=0;j<scoresheetLines; j++){
                    String line = in.readLine();
                    if(line==null){line="";}
                    scorecardBuilder.append(line);
                    scorecardBuilder.append("\n");
                }
                String scorecardStr = scorecardBuilder.toString();
                //update this model entry
                this.record[i] = new HighscoreRecord(name,score,scorecardStr);
            }
            isUpdated = true;
        }finally{
            in.close();
        }
    }
    
    /**
     * Write highscore data to data file.
     * @return status of write:
     *      0: success
     *      1: write failed because data structure has not been synced. Run 
     *         readHighscores() first.
     */
    public int writeHighScores() throws IOException{
        if (isUpdated == false){return 1;}
        PrintWriter out = null;
        try{
            out = new PrintWriter("HighscoreChart.txt");
            StringBuilder recordStr = new StringBuilder();
            for (int i=0;i<this.record.length;i++){
                recordStr.append((this.record[i].name+"\n"));
                recordStr.append((this.record[i].totalScore+"\n"));
                if(this.record[i].totalScore==-1){
                    recordStr.append("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
                }else{
                    recordStr.append((record[i].scoreCardData.toString()));
                }
            }
            out.write(recordStr.toString());
        }finally{
            out.close();
        }
        
        return 0;
    }
    
    /**
     * Replace a record for a new highscore record. All below shift down.
     * @param: index. The record index you are replacing
     * All other parameters correspond to the highscore record data you wish
     * to save.
     */
    public void replaceRecord(int index,String name, int score, String scorecard){
        //shift all below index and get rid of last highscore
        for(int i=(this.record.length-1);i>index;i--){
            this.record[i]=this.record[i-1];
        }
        this.record[index] = new HighscoreRecord(name,score,scorecard);
    }
    
    /**
     * Return name of player at index.
     */
    public String getName(int index){
        return this.record[index].name;
    }
    
    /**
     * Return score of player at index. 
     */
    public int getScore(int index){
        return this.record[index].totalScore;
    }
    
    /**
     * Return length of highscore data. 
     */
    public int length(){
        return this.length;
    }
    
    /**
     *  Return string representation of a score card for a highscore record.
     */
    public String getScoreCardString(int index){
        return this.record[index].scoreCardData;
    }
}
