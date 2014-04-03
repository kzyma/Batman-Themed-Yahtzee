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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
* Represents the UI visualization of the data that Player
* contains.
* <p>Following Model-View-Controller pattern: Player, PlayerController, 
* PlayerView
*/
public abstract class PlayerView {
    
    private JButton b_roll;
    private JButton b_bet5;
    private JButton b_bet10;
    private JButton b_deposit;
    
    /**
     * Construct and return view for scorecard.
     */
    public JPanel getScoreCardView(String playerName, Scorecard card){
        JPanel scoreCardContainer = new JPanel(new GridBagLayout());
        JPanel headerContainer = new JPanel();
        JLabel l_playerName = new JLabel(playerName);
        l_playerName.setForeground(Color.WHITE);
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0,0,0,0);
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = .05;
        c.gridx = 0;
        c.gridy = 0;
        headerContainer.add(l_playerName);
        scoreCardContainer.add(headerContainer,c);
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1.0;
        c.weighty = .95;
        c.gridy = 1;
        
        headerContainer.setBackground(Color.BLACK);
        headerContainer.setBorder(BorderFactory.createLineBorder(new Color(248,248,255,200),2));
        
        scoreCardContainer.setBackground(Color.BLACK);
        scoreCardContainer.add(card.getView(),c);
        
        return scoreCardContainer;
    }
    
    /**
     * Construct and return global controller view. This view contains buttons 
     * roll and buttons to deposit money into machine/game.
     */
    public JPanel get_GlobalControllerView(ActionListener rollAction){
        JPanel p_globalControls = new JPanel(new GridBagLayout());
        p_globalControls.setOpaque(false);
        
        GridBagConstraints c = new GridBagConstraints();

        //init
        b_roll = new JButton();
        b_bet5 = new JButton();
        b_bet10 = new JButton();
        b_deposit = new JButton();
        
        //add action listeners
        b_roll.addActionListener(rollAction);
        
        //format
        b_roll.setOpaque(false);
        b_roll.setMargin(new Insets(0, 0, 0, 0));
        b_roll.setBorder(new EmptyBorder(0, 0, 0, 0));
        try {
                Image img = ImageIO.read(getClass().getResource("./graphics/button_Roll.png"));
                Image newimg = img.getScaledInstance(100,50,java.awt.Image.SCALE_SMOOTH );  
                b_roll.setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
        }
        b_bet5.setOpaque(false);
        b_bet5.setMargin(new Insets(0, 0, 0, 0));
        b_bet5.setBorder(new EmptyBorder(0, 0, 0, 0));
        try {
                Image img = ImageIO.read(getClass().getResource("./graphics/button_Bet5.png"));
                Image newimg = img.getScaledInstance(100,50,java.awt.Image.SCALE_SMOOTH );  
                b_bet5.setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
        }
        b_bet10.setOpaque(false);
        b_bet10.setMargin(new Insets(0, 0, 0, 0));
        b_bet10.setBorder(new EmptyBorder(0, 0, 0, 0));
        try {
                Image img = ImageIO.read(getClass().getResource("./graphics/button_Bet10.png"));
                Image newimg = img.getScaledInstance(100,50,java.awt.Image.SCALE_SMOOTH );  
                b_bet10.setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
        }
        b_deposit.setOpaque(false);
        b_deposit.setMargin(new Insets(0, 0, 0, 0));
        b_deposit.setBorder(new EmptyBorder(0, 0, 0, 0));
        try {
                Image img = ImageIO.read(getClass().getResource("./graphics/button_Deposit.png"));
                Image newimg = img.getScaledInstance(145,30,java.awt.Image.SCALE_SMOOTH );  
                b_deposit.setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
        }
        
        //add to parent container
        c.anchor = GridBagConstraints.NORTHEAST;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0,0,0,0);
        c.gridx = 0;
        c.gridx = 0;
        p_globalControls.add(b_bet5,c);
        c.gridx = 1;
        p_globalControls.add(b_bet10,c);
        c.insets = new Insets(0,60,0,5);
        c.gridx = 2;
        p_globalControls.add(b_roll,c);
        c.insets = new Insets(15,0,0,0);
        c.gridx = 3;
        p_globalControls.add(b_deposit,c);
        
        return p_globalControls;
    }
    
    /**
     * Return reference to Roll Button.
     */
    public JButton get_RollButton(){
        return b_roll;
    }
    
    /**
     * Not Implemented.
     */
    public void update_GlobalControllerView(){
        //code to update deposit about, unhighlighting roll when it cannot be
        //'rolled' ect. COMING SOON!
        throw new UnsupportedOperationException();
    }
   
}
