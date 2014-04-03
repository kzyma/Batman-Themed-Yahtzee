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
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
* ScoreCatagory 'record' holds a single score, composed of a name and value pair.
*/

public class ScoreCatagory {
        private String name;
        private int score;
        
        private ScoreCatagoryGUI b_scoreCat;
        
        /**
         * Construct empty ScoreCatagory. 
         * <p>Note that -1 denotes an un-used score.
         */
        public ScoreCatagory(){
            this.name="";
            this.score=-1;
            
            b_scoreCat = new ScoreCatagoryGUI(this.getName());
        }
        /**
         * Construct ScoreCatagory with name and score.
         */
        public ScoreCatagory(String name,int score){
            this.name = name;
            this.score = score;
            
            b_scoreCat = new ScoreCatagoryGUI(this.getName());
        }
        
        /**
         * Set catagory Name.
         */
        public void setName(String name){
            this.name = name;
            updateView();
        }
        
        /**
         * Set catagory Score.
         */
        public void setScore(int score){
            this.score = score;
            updateView();
        }
        
        /**
         * Get catagory Name.
         */
        public String getName(){
            return this.name;
        }
        
        /**
         * Get catagory Score.
         */
        public int getScore(){
            return this.score;
        }
        /**
         * Return ScoreCatagory View.
         */
        public ScoreCatagoryGUI getView(){
            return b_scoreCat;
        }
        
        /**
         * Update ScoreCatagory view.
         */
        public void updateView(){
            b_scoreCat.setCatagory(this.getName());
            b_scoreCat.setScore(this.getScore());
        }
        
        /**
         * Formatted 'button' like container.
         * <p>Note that listening for an action from this component should be
         * done using a callback method added to this from addActionListener().
         */
        public class ScoreCatagoryGUI extends JPanel{
            private JButton b_scoreCat;
            private JLabel l_score;
            
            /**
             * Construct ScoreCatagoryGUI object.
             * @param str the score catagory.
             */
            public ScoreCatagoryGUI(String str){
                super();
                setLayout(new GridBagLayout());
                this.setPreferredSize(new Dimension(140,5));
                GridBagConstraints cs_GG = new GridBagConstraints();
                cs_GG.fill = GridBagConstraints.BOTH;
                cs_GG.weightx = 1.0;
                cs_GG.weighty = 1.0;
                cs_GG.gridx = 0;
                cs_GG.gridy = 0;
                cs_GG.insets = new Insets(0,0,0,0);
                
                b_scoreCat = new JButton(str);
                b_scoreCat.setHorizontalAlignment(SwingConstants.LEFT);
                b_scoreCat.setBorderPainted(false); 
                b_scoreCat.setContentAreaFilled(false); 
                b_scoreCat.setFocusPainted(false); 
                b_scoreCat.setOpaque(false);
                b_scoreCat.setForeground(Color.WHITE);
                b_scoreCat.setMargin(new Insets(0, 0, 0, 0));
                b_scoreCat.setBorder(new EmptyBorder(0, 6, 0, 0));
                
                l_score = new JLabel(" ");
                l_score.setHorizontalAlignment(SwingConstants.RIGHT);
                l_score.setOpaque(false);
                l_score.setForeground(Color.WHITE);
                l_score.setBorder(new EmptyBorder(0, 0, 0, 6));
                
                setOpaque(false);
                add(b_scoreCat,cs_GG);
                cs_GG.gridx = 1;
                cs_GG.weightx = .2;
                setBorder(BorderFactory.createLineBorder(new Color(248,248,255,100),1));
                add(l_score,cs_GG);
            }
            
            /**
             * Set score catagory.
             */
            public void setCatagory(String str){
                this.b_scoreCat.setText(str);
            }
            
            /**
             * Get score catagory.
             */
            public String getCatagory(){
                return this.b_scoreCat.getText();
            }
            
            /**
             * Get reference to the score catagory button.
             * <p>Used at a higher level to figure out which ScoreCatagory fired
             * an event.
             */
            public JButton getCatagoryButton(){
                return this.b_scoreCat;
            }
            
            /**
             * Set score for this catagory. 
             */
            public void setScore(int score){
                if(score==-1){
                    this.l_score.setText("");
                    setBorder(BorderFactory.createLineBorder(new Color(248,248,255,100),1));
                }else{
                    this.l_score.setText((new Integer(score).toString()));
                    setBorder(BorderFactory.createLineBorder(new Color(248,248,255,255),1));
                }
            }
            
            /**
             * Listen for button press.
             */
            public void addActionListener(ActionListener e){
                this.b_scoreCat.addActionListener(e);
            }
            
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                
            }
        }
        
}

