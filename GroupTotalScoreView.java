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
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
* Hold scores for all players in one game instance.
* <p>Follows singleton pattern.
*/
public class GroupTotalScoreView {
   private volatile static GroupTotalScoreView groupViewInstance;
   private JPanel p_GroupScoreContainer;
   private JLabel l_playerNames[];
   private JLabel l_playerTotals[];
   private int playerTotals[];
   private JPanel container[];
   private int numPlayers;
   private String playerNames[];
    
   /**
    * Construct GroupTotalScoreView Object.
    */
   private GroupTotalScoreView(int numPlayers,String[] playerNames){
        
        this.numPlayers = numPlayers;
        this.playerNames = new String[numPlayers];
        this.playerTotals = new int[numPlayers];
        for(int i=0;i<playerNames.length;i++){
            this.playerNames[i] = playerNames[i];
            this.playerTotals[i] = 0;
        }
        
        p_GroupScoreContainer = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,0,5,0);
        
        p_GroupScoreContainer.setPreferredSize(new Dimension(210,(numPlayers*40)));
        l_playerNames = new JLabel[numPlayers];
        l_playerTotals = new JLabel[numPlayers];
        container = new JPanel[numPlayers];
        
        for(int i=0;i<numPlayers;i++){
            container[i] = new JPanel(new GridBagLayout());
            container[i].setBorder(BorderFactory.createLineBorder
                    (new Color(248,248,255,150),1));
            container[i].setOpaque(false);
            
            GridBagConstraints p = new GridBagConstraints();
            p.weightx = 1.0;
            p.weighty = 1.0;
            p.gridx = 0;
            p.gridy = 0;
            p.fill = GridBagConstraints.BOTH;
            
            l_playerNames[i] = new JLabel(playerNames[i]);
            l_playerNames[i].setHorizontalAlignment(SwingConstants.LEFT);
            l_playerNames[i].setForeground(Color.WHITE);
            container[i].add(l_playerNames[i],p);
            
            p.gridx = 1;
            p.weightx = .3;
            l_playerTotals[i] = new JLabel("0");
            l_playerTotals[i].setHorizontalAlignment(SwingConstants.RIGHT);
            l_playerTotals[i].setForeground(Color.WHITE);
            container[i].add(l_playerTotals[i],p);
        }
        
        for(int i=0;i<numPlayers;i++){
            c.gridy++;
            p_GroupScoreContainer.add(container[i],c);
        }
   
        p_GroupScoreContainer.setOpaque(false);
    }
    
   /**
    * Get singleton instance of object.
    */
    public static GroupTotalScoreView getGroupTotalScoreInstance(int numPlayers){
    if(groupViewInstance==null){
        /*synchronized provides locks to keep this singleton object
         * thread-safe. It also ensures the resulting value is 
         * visible to all threads. */
        synchronized (Dice.class){
            if(groupViewInstance==null){
                groupViewInstance = new GroupTotalScoreView(numPlayers
                        ,new String[numPlayers]);
            }
        }
    }
    return groupViewInstance;
    }
 
    /**
     * Get singleton instance of object.
     */
    public static GroupTotalScoreView getGroupTotalScoreInstance(int numPlayers,String[] playerNames){
    if(groupViewInstance==null){
        /*synchronized provides locks to keep this singleton object
         * thread-safe. It also ensures the resulting value is 
         * visible to all threads. */
        synchronized (Dice.class){
            if(groupViewInstance==null){
                groupViewInstance = new GroupTotalScoreView(numPlayers,playerNames);
            }
        }
    }
    return groupViewInstance;
    }
    /**
     * Get singleton instance of object. 
     */
    public static GroupTotalScoreView getGroupTotalScoreInstance(){
    if(groupViewInstance==null){
        /*synchronized provides locks to keep this singleton object
         * thread-safe. It also ensures the resulting value is 
         * visible to all threads. */
        synchronized (Dice.class){
            if(groupViewInstance==null){
                System.err.println("Must initialize "
                        + "GroupTotalScoreView(numofplayers) before using"
                        + " getGroupTotalScoreInstance.");
                //groupViewInstance = new GroupTotalScoreView();
            }
        }
    }
    return groupViewInstance;   
    }
    
    /**
     * Return view.
     */
    public JPanel getView(){
        return p_GroupScoreContainer;
    }
    
    /**
     * Update score data for a player.
     */
    public void updateScore(int playerIndex,int newScore){
        this.playerTotals[playerIndex] = newScore;
        this.l_playerTotals[playerIndex]
                .setText(new Integer(newScore).toString());
        
        p_GroupScoreContainer.removeAll();
        
        //figure out the order each players should be
        //displayed based on score->>high to low.
        int scoreOrder[] = new int[this.numPlayers];
        boolean picked[] = new boolean[this.numPlayers];
        
        for(int i=0;i<scoreOrder.length;i++){
            picked[i] = false;
        }
        
        for(int i=0;i<scoreOrder.length;i++){
            //get first non-'picked' element
            int low = 0;
            for(int j=0;j<scoreOrder.length;j++){
                if(picked[j]==false){
                    low = j;
                    break;
                }
            }
            //find lowest scored non-'picked' element
            for(int j=0;j<scoreOrder.length;j++){
                if((playerTotals[low]>=playerTotals[j])&&(picked[j]==false)){
                    low=j;
                }
            }
            scoreOrder[i] = low;
            picked[low] = true;
        }
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,0,5,0);
        for(int i=(numPlayers-1);i>=0;i--){
            c.gridy++;
            p_GroupScoreContainer.add(container[scoreOrder[i]],c);
        }  
    }
  
}
