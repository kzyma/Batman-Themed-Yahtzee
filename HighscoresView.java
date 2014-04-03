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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
*  GUI View for highscoresController.
* <p>Follows Model-View-Controller pattern.
*/
public class HighscoresView extends JPanel {
    private Image backgroundImg;
    
    //hold each highscore
    private JPanel p_masterContainer;
    private CardLayout master_Layout;
    private JPanel p_center;
    
    private JButton b_toMainMenu;
    private HighScoreButton[] highScores;
    
    /**
     * Construct HighscoresView object.
     */
    public HighscoresView(){
        //load background image
         try {
            Image image = ImageIO.read(new File("./graphics/HighScoreCard.jpg"));
            this.backgroundImg = image.getScaledInstance(600,434,java.awt.Image.SCALE_SMOOTH ); 

        } catch(IOException e) {
            e.printStackTrace();
        }
         
         //p_center used for simple centering of master container.
         p_center = new JPanel(new GridBagLayout());
         master_Layout = new CardLayout();
         p_masterContainer = new JPanel(master_Layout);
         p_masterContainer.setBackground(Color.BLACK);
         b_toMainMenu = new JButton("Main Menu");
         setLayout(new GridBagLayout());
    }
    
    /**
     * Add callback when main menu button is pressed. 
     */
    public void addMainMenuListener(ActionListener l){
        b_toMainMenu.addActionListener(l);
    }
    
    /**
     * Update view to represent current data in Highscores model.
     * @param records 
     */
    public void update(Highscores records){ 
        //init Menu Panel
        highScores = new HighScoreButton[records.length()];
   
        JPanel menuPanel = new JPanel(new GridBagLayout());
        menuPanel.setPreferredSize(new Dimension(300,(records.length()*30)));
        menuPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,0);

        for(int i=0;i<records.length();i++){
            this.highScores[i] = new HighScoreButton(records.getName(i),
                    records.getScore(i),new selectHighscoreListener());
                    menuPanel.add(this.highScores[i],c);
                    c.gridy++;
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        menuPanel.add(b_toMainMenu,c);
        p_masterContainer.add(menuPanel,"Menu"); 
        
        
        /*
         * init panel for each player
         */
        JPanel scoreCardPanel[] = new JPanel[records.length()];
        GridBagConstraints p = new GridBagConstraints();
        p.fill = GridBagConstraints.BOTH;
        p.weightx = 1.0;
        p.weighty = 1.0;
        p.gridx = 0;
        p.gridy = 0;
        p.insets = new Insets(0,0,0,0);
        for(int i=0;i<scoreCardPanel.length;i++){
            scoreCardPanel[i] = new JPanel(new GridBagLayout());
            scoreCardPanel[i].setPreferredSize(new Dimension(300,((13*10)+20)));
            JTextArea temp = new JTextArea(records.getScoreCardString(i));
            temp.setBackground(Color.BLACK);
            temp.setForeground(Color.WHITE);
            temp.setEditable(false);
            
            JButton b_back = new JButton("Back To HighScores");
            
            b_back.addActionListener(new ActionListener(){
            @Override
                public void actionPerformed(ActionEvent e) {
                master_Layout.show(p_masterContainer,"Menu");
            }
            });
            p.gridy = 0;
            scoreCardPanel[i].add(temp,p);
            p.gridy = 1;
            scoreCardPanel[i].add(b_back,p);
            p_masterContainer.add(scoreCardPanel[i],i+"ScoreCard");
         }
         
         
         p_center.add(p_masterContainer);
         this.master_Layout.show(p_masterContainer, "Menu");
         add(p_center);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //workaround for poor photoshop work, (-20) will center the image.
        g.drawImage(backgroundImg,-20,0,620,434,null);
    }
    
    /**
     * Show scorecard for player, corresponding to which 'highscore button' 
     * fired the event.
     */
    private class selectHighscoreListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            int index = -1;
            //get index for button pressed
            for(int i=0;i<highScores.length;i++){
                if (highScores[i].getComponent() == e.getSource()){
                    index = i;
                }
            }
            
            master_Layout.show(p_masterContainer,index+"ScoreCard");
        }
    }
    
    /**
     * Holds player name and score in a formatted JPanel. 
     */
    private class HighScoreButton extends JPanel{
        JButton b_name;
        
        public HighScoreButton(String name,int score,ActionListener l){
            setLayout(new GridBagLayout());
            this.setPreferredSize(new Dimension(200,5));
            GridBagConstraints cs_GG = new GridBagConstraints();
            cs_GG.fill = GridBagConstraints.BOTH;
            cs_GG.weightx = 1.0;
            cs_GG.weighty = 1.0;
            cs_GG.gridx = 0;
            cs_GG.gridy = 0;
            cs_GG.insets = new Insets(0,0,0,0);

            b_name = new JButton(name);
            b_name.setHorizontalAlignment(SwingConstants.LEFT);
            b_name.setBorderPainted(false); 
            b_name.setContentAreaFilled(false); 
            b_name.setFocusPainted(false); 
            b_name.setOpaque(false);
            b_name.setForeground(Color.WHITE);
            b_name.setMargin(new Insets(0, 0, 0, 0));
            b_name.setBorder(new EmptyBorder(0, 6, 0, 0));

            b_name.addActionListener(l);
            
            JLabel l_score = new JLabel(new Integer(score).toString());
            l_score.setHorizontalAlignment(SwingConstants.RIGHT);
            l_score.setOpaque(false);
            l_score.setForeground(Color.WHITE);
            l_score.setBorder(new EmptyBorder(0, 0, 0, 6));

            setBackground(Color.BLACK);
            add(b_name,cs_GG);
            cs_GG.gridx = 1;
            cs_GG.weightx = .2;
            setBorder(BorderFactory.createLineBorder(new Color(248,248,255,100),1));
            add(l_score,cs_GG);
        }
        
        /**
         * Return model from which the action event was fired.
         * <p>Use to figure out which HighScoresButton was pressed.
         */
        public JButton getComponent(){
            return this.b_name;
        }
    }
    
}
