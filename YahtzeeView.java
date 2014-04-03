/*
 * Yahtzee GUI-based game that supports multiple human and AI players, and a
 * high score system for recording and viewing past top score. Official
 * game rules from http://www.hasbro.com/common/instruct/Yahtzee.pdf are 
 * followed.
 * Class: CSC421
 * Project 2
 * Professor: Dr. Spiegel
 * 
 * @author: Ken Zyma
 * @version: 1.0.0
 * @since: 1.6
 */ 

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

 /**
 *  View for the Yahtzee Class.
 * <p>Follows front-controller pattern. See Yahtzee and
 * YahtzeeDispatcher.
 */
public class YahtzeeView extends JPanel{
    private MainSlot_JPanel mainBackGroundPanel;
    private JPanel mainActionPanel;
    private CardLayout cardLayout_Action;
    private String currentPage_ActionPanel;
    
    private JPanel gameControlPanel;
    private FixSizeCardLayout gameCon_Layout;
    private String currentPage_ControlPanel;
    
    /**
     * Construct YahtzeeView object.
     * <p>Upon construction, the YahtzeeView's containers are set to
     * accept panels being added. Possible additions are through methods 
     * addPage_MainContainer and addPage_GameControlContainer.
     */
    public YahtzeeView(){
        setLayout(null);
        cardLayout_Action = new CardLayout();
        gameCon_Layout = new FixSizeCardLayout();
        //panel used to house the cardLayout manager.
        mainBackGroundPanel = new MainSlot_JPanel();
        mainBackGroundPanel.setLayout(null);
        
        mainActionPanel = new JPanel(cardLayout_Action); 
        mainActionPanel.setBounds(100, 48, 600, 434);
        gameControlPanel = new JPanel(gameCon_Layout);
        gameControlPanel.setBounds(120, 510, 540, 50);
        
        mainBackGroundPanel.add(mainActionPanel);
        mainBackGroundPanel.add(gameControlPanel);

        gameControlPanel.add(emptyGameControllerView(),
                "Player-1GameController");
        gameControlPanel.setOpaque(false);
        
        mainBackGroundPanel.setBounds(0,0,800,600);
        
        add(mainBackGroundPanel);
        setVisible(true);
        
        currentPage_ActionPanel = "";
        currentPage_ControlPanel = "";
    }
    
    /**
     * Add a page to the CardLayout of MainContainer. MainContainer holds the
     * screen for the slot machine.
     * @param newJPanel The panel to be added to MainContainer.
     * @param name The name of newJPanel
     */
    public void addPage_MainContainer(JPanel newJPanel,String name){
        mainActionPanel.add(newJPanel,name);
        setVisible(true);
    }
    
    /**
     * Show page from the CardLayout of MainContainer.
     * @param page The name of the panel to show. Name is case-sensative
     * and must already exist on the container.
     */
    public void showPage_MainContainer(String page){
        if(!(this.currentPage_ActionPanel.equals(page))){
            cardLayout_Action.show(mainActionPanel,page);
            currentPage_ActionPanel = page;
        }
    }
    
    /**
     * Add a page to the CardLayout of GameControlContainer. GameControlContainer
     * holds any controls that should be displayed on the slot machine's input 
     * panel.
     * @param newJPanel The panel to be added to GameControlContainer.
     * @param name The name of newJPanel
     */
    public void addPage_GameControlContainer(JPanel newJPanel,String name){
        gameControlPanel.add(newJPanel,name);
        setVisible(true);
    }
    
    /**
     * Show page from the CardLayout of GameControlContainer.
     * @param page The name of the panel to show. Name is case-sensative
     * and must already exist on the container. 
     */
    public void showPage_GameControlContainer(String page){
        if(!(this.currentPage_ControlPanel.equals(page))){
            gameCon_Layout.show(gameControlPanel,page);
            currentPage_ControlPanel = page;
        }
    }
    
    /**
     * Construct and return a 'dummy' game controller panel. This is the same
     * as the game controller panel used by player's in the Yahtzee game, however,
     * none of the buttons have listener/callback routines added.
     * @return JPanel containing empty game controller panel.
     */
    public JPanel emptyGameControllerView(){
        JPanel p_globalControls = new JPanel(new GridBagLayout());
        p_globalControls.setOpaque(false);
        
        GridBagConstraints c = new GridBagConstraints();

        //init
        JButton b_roll = new JButton();
        JButton b_bet5 = new JButton();
        JButton b_bet10 = new JButton();
        JButton b_deposit = new JButton();
        
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
     * MainSlot_JPanel is a JPanel with a background image.
     */
    private class MainSlot_JPanel extends JPanel{
        private Image backgroundImg;
        
        /**
         * Construct a new MainSlot_JPanel object.
         * <p>Loads background image.
         */
        public MainSlot_JPanel(){
            //load background image
             try {
                Image img = ImageIO.read(getClass()
                        .getResource("./graphics/slot_BackGround.jpg"));
                backgroundImg = img
                        .getScaledInstance(800,580,java.awt.Image.SCALE_SMOOTH );
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * Paint background image.
         * @param g Graphics reference.
         */
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImg,0,0,800,580,this);
        }
    }
    
    /**
     * FixSizeCardLayout is a CardLayout with a fixed size of 600x434.
     */
    private class FixSizeCardLayout extends CardLayout{
        FixSizeCardLayout(){
            super();
        }
        
        /**
         * Override default action of returning largest Container of a 
         * CardLayout and instead always returns 600x434.
         * @param parent the parent container in which to do the layout.
         * @return the preferred dimensions to lay out the subcomponents 
         * of the specified container
         */
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(600,434);
        }
    }
    
}