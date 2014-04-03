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

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * View for the main menu routine. Contains main menu screen, number of players
 * screen, and character selection screen.
 * <p>Use of this class requires using callback routines to listen remotely
 * for the start of the game, character selection and number of player selection.
 * It's important to note this class should not be responsible for storing the 
 * data mentioned previously and only represents the UI.
 */
public class MainMenuView extends JPanel {
        private JButton b_newGame;
        private JButton b_highscores;
        private JButton b_demo;
        private JButton b_exit;
        
        private JButton b_enterPlayerNumber;
        private JButton b_back;
        
        private int currentPlayer;
        private int p;
        private ChooseCharacterMenu_JPanel p_characterContainer[];
        private JPanel p_characterSelectGUI[];
        private JButton b_backButton[];
        private PlayerName_Textbox pName[];
        private JButton b_batman[];
        private JButton b_robin[];
        private JButton b_catWoman[];
        
        private JButton b_startGame;
                
        //panel that holds 'clickable' GUI buttons for menu.
        private JPanel p_MainMenu;
        private  MainMenu_JPanel p_MainMenuBackground;
        
        private PlayerMenu_JPanel p_NumOfPlayersBackground;
        private JPanel p_NumOfPlayers;
        
        private JTextField tf_numOfPlayers;
        
        private CardLayout cardLayout;
        private JPanel p_mainMenuContainer;
        
        private String playerNames[];
        private String playerCharacters[];
              
    /**
     * Construct a MainMenuView object.
     */
    public MainMenuView(){  
        cardLayout = new CardLayout();
        
        //main menu prompt
        //p_mainMenuContainer = new JPanel(cardLayout);
        setLayout(cardLayout);
        
        p_MainMenu = new JPanel(new GridBagLayout());
        p_MainMenu.setPreferredSize(new Dimension(300,150));
        
        p_MainMenuBackground = new  MainMenu_JPanel();
        p_MainMenuBackground.setLayout(new GridBagLayout());
        p_MainMenuBackground.setPreferredSize(new Dimension(600, 434));
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0,0,0,0);
        
        b_newGame = new JButton("New Game");
        b_highscores = new JButton("View Highscores");
        b_demo = new JButton("AI Demo");
        b_exit = new JButton("Exit");
        
        c.gridx = 0;
        c.gridy = 0;
        p_MainMenu.add(b_newGame,c);
        c.gridy++;
        c.insets = new Insets(5,0,0,0);
        p_MainMenu.add(b_highscores,c);
        c.gridy++;
        p_MainMenu.add(b_demo,c);
        c.gridy++;
        p_MainMenu.add(b_exit,c);
        c.insets = new Insets(0,0,0,0);
        p_MainMenu.setOpaque(false);
        
        //center vertically/horizontally with empty constraints
        p_MainMenuBackground.add(p_MainMenu,new GridBagConstraints());
        
        //number of players prompt
        p_NumOfPlayersBackground = new PlayerMenu_JPanel();
        p_NumOfPlayersBackground.setLayout(null);
        
        p_NumOfPlayers = new JPanel();
        p_NumOfPlayers.setBounds(140,167,420,200);
        p_NumOfPlayers.setOpaque(false);

        b_back = new JButton();
        try {
                Image img = ImageIO.read(getClass().getResource("./graphics/Back_Button.jpg"));
                Image newimg = img.getScaledInstance( 100, 100,java.awt.Image.SCALE_SMOOTH );  
                b_back.setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
        }
        
        tf_numOfPlayers = new JTextField(1);
        //set font, default text and center align 
        Font gothamFont =  new Font("Segoe UI", Font.BOLD,82);
        tf_numOfPlayers.setText("1");
        tf_numOfPlayers.setHorizontalAlignment(JTextField.CENTER);
        tf_numOfPlayers.setFont(gothamFont);
        
        //add listener...when user 'clicks' tf_numOfPlayers it should clear
        tf_numOfPlayers.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                tf_numOfPlayers.setText("");
            }
        });
        
        b_enterPlayerNumber = new JButton();
        try {
                Image img = ImageIO.read(getClass().getResource("./graphics/Next_Button.jpg"));
                Image newimg = img.getScaledInstance( 100, 100,java.awt.Image.SCALE_SMOOTH );  
                b_enterPlayerNumber.setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
        }
        
        p_NumOfPlayers.add(b_back);
        p_NumOfPlayers.add(tf_numOfPlayers);
        p_NumOfPlayers.add(b_enterPlayerNumber);
        p_NumOfPlayersBackground.add(p_NumOfPlayers);
        
        //add 'cars' to main container
        add(p_MainMenuBackground,"Main Menu");
        add(p_NumOfPlayersBackground,"Number of Players");
    
        cardLayout.show(this, "Main Menu");
        
        b_startGame = new JButton("Start Game>>");
        
        /* EVENT LISTENERS */
        b_back.addActionListener(new ActionListener(){
            @Override
           public void actionPerformed(ActionEvent e){
               showMainMenu();
           } 
        });
    }
    
    /**
     * Change to number of players screen in view.
     */
    public void showNumberOfPlayersDialog(){
        cardLayout.show(this, "Number of Players");
    }
    
    /**
     * Change to pick character screen in view.
     * <p>To get the type of character use method getPlayerType().
     * @param numberOfPlayers the number of times this screen should be
     * restored and shown by pressing 'next' button.
     */
    public void showPlayerCharactersDialog(int numberOfPlayers){
        this.currentPlayer = 0;
        playerNames = new String[numberOfPlayers];
        playerCharacters = new String[numberOfPlayers];
        
        p_characterContainer = new ChooseCharacterMenu_JPanel[numberOfPlayers];
        p_characterSelectGUI = new JPanel[numberOfPlayers];
        b_backButton = new JButton[numberOfPlayers];
        pName = new PlayerName_Textbox[numberOfPlayers];
        b_batman = new JButton[numberOfPlayers];
        b_robin = new JButton[numberOfPlayers];
        b_catWoman = new JButton[numberOfPlayers];
        
        for(this.p=0;p<numberOfPlayers;p++){
            p_characterContainer[p] = new ChooseCharacterMenu_JPanel();
            p_characterContainer[p].setLayout(null);
            
            p_characterSelectGUI[p] = new JPanel();
            p_characterSelectGUI[p].setLayout(null);

            p_characterSelectGUI[p].setBounds(0,150,600,125);
            
            pName[p] = new PlayerName_Textbox();
            pName[p].setText("Player"+(p+1));
            
            pName[p].addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent e){
                    if (pName[currentPlayer].getText().equals("Player"+(currentPlayer+1))){
                        pName[currentPlayer].setText("");
                    }
                }
            });
      
            b_backButton[p] = new JButton();
            try {
                Image img = ImageIO.read(getClass().getResource("./graphics/Back_Button.jpg"));
                Image newimg = img.getScaledInstance( 100, 100,java.awt.Image.SCALE_SMOOTH );  
                b_backButton[p].setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                System.out.println("Photo not found:"+ex);
            }
            
            b_backButton[p].addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                   cardLayout.previous(MainMenuView.this);
                } 
            });
            
            b_batman[p] = new JButton();
            try {
                Image img = ImageIO.read(getClass().getResource("./graphics/Batman_Icon.jpg"));
                Image newimg = img.getScaledInstance( 100, 100,java.awt.Image.SCALE_SMOOTH );  
                b_batman[p].setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
            }
           
            b_robin[p] = new JButton();
            try {
                Image img = ImageIO.read(getClass().getResource("./graphics/Robin_Icon.jpg"));
                Image newimg = img.getScaledInstance(100,100,java.awt.Image.SCALE_SMOOTH ); 
                b_robin[p].setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
            }
            b_catWoman[p] = new JButton(); 
            try {
                Image img = ImageIO.read(getClass().getResource("./graphics/CatWoman_Icon.jpg"));
                Image newimg = img.getScaledInstance(100,100,java.awt.Image.SCALE_SMOOTH ); 
                b_catWoman[p].setIcon(new ImageIcon(newimg));
            }catch (IOException ex) {
                //do nothing...
            }
            
            b_batman[p].addActionListener(new ActionListener(){
               @Override
               public void actionPerformed(ActionEvent e){
                   playerNames[currentPlayer] = pName[currentPlayer].getText();
                   playerCharacters[currentPlayer] = "Batman";
                   currentPlayer++;
                   cardLayout.next(MainMenuView.this);
               }
            });
            
            b_robin[p].addActionListener(new ActionListener(){
               @Override
               public void actionPerformed(ActionEvent e){
                    playerNames[currentPlayer] = pName[currentPlayer].getText();
                    playerCharacters[currentPlayer] = "Robin";
                    currentPlayer++;
                    cardLayout.next(MainMenuView.this);
               }
            });
                        
             b_catWoman[p].addActionListener(new ActionListener(){
               @Override
               public void actionPerformed(ActionEvent e){
                   int pNum = p;
                    playerNames[currentPlayer] = pName[currentPlayer].getText();
                    playerCharacters[currentPlayer] = "CatWoman";
                    currentPlayer++;
                    cardLayout.next(MainMenuView.this);
               }
            });
            p_characterSelectGUI[p].setOpaque(false);
             
            //placing buttons on null layout, need to set setbounds
            pName[p].setBounds(100,2,100,28);
            b_backButton[p].setBounds(70,35,100,100);
            b_batman[p].setBounds(190,35,100,100);
            b_robin[p].setBounds(310,35,100,100);
            b_catWoman[p].setBounds(430,35,100,100);
            
            p_characterSelectGUI[p].add(pName[p]);
            p_characterSelectGUI[p].add(b_backButton[p]);
            p_characterSelectGUI[p].add(b_batman[p]);
            p_characterSelectGUI[p].add(b_robin[p]);
            p_characterSelectGUI[p].add(b_catWoman[p]);
            p_characterContainer[p].add(p_characterSelectGUI[p]);

            add(p_characterContainer[p],"Player"+p);
        }
        
        //instructions card
        JPanel p_instructionsContainer  = new JPanel();
        JLabel l_instructions = new JLabel("Animated Intro Coming Soon...");
        p_instructionsContainer.add(l_instructions);
        p_instructionsContainer.add(b_startGame);
        add(p_instructionsContainer);
        
        cardLayout.show(this,"Player0");
    }  
    
    /**
     * Return player's name.
     * @param index player number to return.
     * @return player's name. Default is "Player"+playerNumber unless changed.
     */
    public String getPlayerName(int index){
        return this.playerNames[index];
    }
    
    /**
     * Return player's character.
     * @param index player number to return. Maximum value is set by number of 
     * players in game, use getNumberOfPlayersInput to get this bound.
     * @return player's character, will be one of the following: 
     * ['batman','robin','catwoman'].
     */
    public String getPlayerCharacter(int index){
        return this.playerCharacters[index];
    }
    
    /**
     * Return number of players choosen.
     * @return number of players in game.
     */
    public int getNumberOfPlayersInput(){
        return new Integer(this.tf_numOfPlayers.getText());
    }
    
    /**
     * Show main menu screen in view.
     */
    public void showMainMenu(){
        cardLayout.show(this,"Main Menu");
    }
    
    /**
     * Show the next card in view.
     * <p>Order is: [main menu, number of players,character select x n,
     * intoduction video].
     */
    public void nextCard(){
        this.cardLayout.next(this);
    }
    
    /**
     * Listen for one of the menu buttons to be pressed.
     * <p>Use getSource() to see which button fired event.
     */
    public void addMenuListener(ActionListener l){
        b_newGame.addActionListener(l);
        b_highscores.addActionListener(l);
        b_demo.addActionListener(l);
        b_exit.addActionListener(l);
    }  
   
    /**
     * Listen for 'start game' action.
     */
    public void addStartGameListener(ActionListener l){
        this.b_startGame.addActionListener(l);
    }
    
    /**
     * Listen for 'number of players' screen to be completed.
     */
    public void addSetPlayersListener(ActionListener l){
        this.b_enterPlayerNumber.addActionListener(l);
    }
    
    /**
     * Return name of view.
     */
    @Override
    public String getName(){
        return "MainMenuView";
    }
    
    /**
     * JPanel with main menu background graphics.
     */
    private class MainMenu_JPanel extends JPanel{
        private Image backgroundImg;
        
        public MainMenu_JPanel(){
            //load background image
             try {
                Image img = ImageIO.read(getClass()
                        .getResource("./graphics/MainMenu_BackGround.jpg"));
                backgroundImg = img
                        .getScaledInstance(600,434,java.awt.Image.SCALE_SMOOTH );
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImg,0,0,600,434,this);
        }
    }
    
    /**
     * Jpanel with number of players background.
     */
    private class PlayerMenu_JPanel extends JPanel{
        private Image backgroundImg;
        
        public PlayerMenu_JPanel(){
            setPreferredSize(new Dimension(600,434));
            //load background image
             try {
                Image img = ImageIO.read(getClass()
                        .getResource("./graphics/Num_Players_BackGround.jpg"));
                backgroundImg = img
                        .getScaledInstance(600,434,java.awt.Image.SCALE_SMOOTH );
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImg,0,0,600,434,this);
        }
    }
    
    /**
     * JPanel with choose character background.
     */
    private class ChooseCharacterMenu_JPanel extends JPanel{
        private Image backgroundImg;
        
        public ChooseCharacterMenu_JPanel(){
            //load background image
            setPreferredSize(new Dimension(600,434));
             try {
                Image img = ImageIO.read(getClass()
                        .getResource("./graphics/Choose_Character_BackGround.jpg"));
                backgroundImg = img
                        .getScaledInstance(600,434,java.awt.Image.SCALE_SMOOTH );
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(backgroundImg,0,0,600,434,this);
        }
    }
   
   /**
    * Customized Textbox.
    * <p>Font: Segoe UI, Bold, 12.
    */
   private class PlayerName_Textbox extends JTextField{
        public PlayerName_Textbox(){
            Font myFont = new Font("Segoe UI", Font.BOLD,12);
            setFont(myFont);
        }
        
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
        }
    }
}