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
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * View that displays summary of game results.
 */
public class EndGameView extends JPanel {
        private Image backgroundImg;
        private JPanel p_playerScores;
        private JLabel[] l_playerNames;
        private JLabel[] l_playerTotals;
        private JPanel[] container;
        private JButton b_newGame;
        private JButton b_toMainMenu;
        
        private int numPlayers;
        private String[] playerNames;
        private int[] playerTotals;
        
    /**
     * Construct EndGameView object.
     */
    public EndGameView(int numPlayers,String[] playerNames){
        //load background image
        try {
            Image image = ImageIO.read(new File("./graphics/defaultScoreSheet.jpg"));
            this.backgroundImg = image.getScaledInstance(600,434,java.awt.Image.SCALE_SMOOTH ); 

        } catch(IOException e) {}
        
        this.numPlayers = numPlayers;
        this.playerNames = new String[numPlayers];
        this.playerTotals = new int[numPlayers];
        for(int i=0;i<playerNames.length;i++){
            this.playerNames[i] = playerNames[i];
            this.playerTotals[i] = 0;
        }
        
        b_toMainMenu = new JButton("Cash Out");
        b_newGame = new JButton("Play Again");
        
        p_playerScores = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(5,0,5,0);
        
        p_playerScores.setPreferredSize(new Dimension(210,(numPlayers*40+(2*40))));
        l_playerNames = new JLabel[numPlayers];
        l_playerTotals = new JLabel[numPlayers];
        container = new JPanel[numPlayers];
        
        for(int i=0;i<numPlayers;i++){
            container[i] = new JPanel(new GridBagLayout());
            GridBagConstraints p = new GridBagConstraints();
            container[i].setBorder(BorderFactory.createLineBorder
                    (new Color(248,248,255,150),1));
            container[i].setOpaque(false);
            
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
            p_playerScores.add(container[i],c);
        }
        
        c.weighty = .2;
        c.insets = new Insets(1,0,1,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        p_playerScores.add(b_newGame,c);
        c.gridy++;
        p_playerScores.add(b_toMainMenu,c);
   
        p_playerScores.setOpaque(false);
        //temporary container to center the scoresheet
        JPanel center = new JPanel(new GridBagLayout());
        center.add(p_playerScores);
        center.setOpaque(false);
        center.setPreferredSize(new Dimension(600,434));
        
        add(center);
        
    }
   
    /**
     * Add callback routine to fire when "main menu" Button is pressed.
     */
    public void add_BackToMainListener(ActionListener l){
        b_toMainMenu.addActionListener(l);
    }
    
    /**
     * Add callback routine to fire when "new game" Button is Pressed. 
     */
    public void add_newGameListener(ActionListener l){
        this.b_newGame.addActionListener(l);
    }
    
    /**
     * Update scores and game results based on player's current scores.
     */
    public void update(int playerScores[]){
        for(int i=0;i<playerScores.length;i++){
            this.playerTotals[i] = playerScores[i];
            this.l_playerTotals[i]
                .setText(new Integer(playerScores[i]).toString());   
        }
        
        p_playerScores.removeAll();
        
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
            p_playerScores.add(container[scoreOrder[i]],c);
        }
        
        c.weighty = .2;
        c.insets = new Insets(1,0,1,0);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        p_playerScores.add(b_newGame,c);
        c.gridy++;
        p_playerScores.add(b_toMainMenu,c);
    }
    
    @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImg,0,0,null);
    }
  
}
