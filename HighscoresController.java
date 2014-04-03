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

import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;

/**
*  Controller class for Highscores model and view. Follows singleton 
*  pattern to ensure only one highscores data object exists and can be
*  accessed globally.
*/

public class HighscoresController {
    private volatile static HighscoresController highscoresController;
    private Highscores model;
    private HighscoresView view;
        
    private HighscoresController() throws IOException{
        model = new Highscores();
        model.readHighscores();
        view = new HighscoresView();
    }
    
     /**
     * get singleton object reference.This is the only way to get
     * access to the class. For more see singleton design pattern.
     * @return highScoreInstance, the singleton object reference.
     */
    public static HighscoresController getHighscoreInstance()
            throws IOException{
        if(highscoresController==null){
            /*synchronized provides locks to keep this singleton object
             * thread-safe. It also ensures the resulting value is 
             * visible to all threads. */
            synchronized (HighscoresController.class){
                if(highscoresController==null){
                    //init model/view
                    highscoresController = new HighscoresController();
                }
            }
        }
        return highscoresController;
        
    }
    
    /**
     * @param challengingScore
     * @return true if score is a new highscore. False otherwise
     */
    public boolean isNewHighScore(int challengingScore){
        for(int i=0;i<this.model.length();i++){
            if(this.model.getScore(i)<challengingScore){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Add the new highscore data into Highscore file.
     * @return where the highscore placed.
     */
    public int addNewHighScore(String name,int score, String scorecard)
            throws IOException{
        for(int i=0;i<this.model.length();i++){
            if(this.model.getScore(i)<score){
                this.model.replaceRecord(i, name, score, scorecard);
                int flag = this.model.writeHighScores();
                //if flag == 1,write failed becuase Highscores is not synched
                //with file, synch and try agian.
                if(flag==1){
                    this.model.readHighscores();
                    this.model.writeHighScores();
                }
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Add callback routine to listen for 'back to main menu' button press. 
     */
    public void addBackToMenuListener(ActionListener l){
        this.view.addMainMenuListener(l);
    }
    
    /**
     * Return reference of view.
     */
    public JPanel getView(){
        this.view.update(this.model);
        return this.view;
    }
    
}
