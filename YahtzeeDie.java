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
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
* Holds one of 6 possible values [1-6], that can be rolled to 
* hold a new random value. YahtzeeDie also contains the view
* for this class.
*
*/
public class YahtzeeDie {
    private int value;
    private RandomGen dieRandomValGenerator;
    
    /************* GUI 'VIEW' COMPONENTS **************/
    
    JButton b_die;
    Image img[];
    Image backgroundImg[];
    ImageIcon icon[];
   
    /************* GUI 'VIEW' COMPONENTS **************/
    
    /**
     * Construct YahtzeeDie object with random initial value.
     */
    public YahtzeeDie(){
        this.dieRandomValGenerator = new RandomGen(1,7);
        this.value=this.dieRandomValGenerator.nextInt();
        
        //GUI 'View' Components
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;

        b_die = new JButton();
        b_die.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
        
        img = new Image[6];
        backgroundImg = new Image[6];
        icon = new ImageIcon[6];
        
        /*init ALL images */
         try {
             for(int i=0;i<backgroundImg.length;i++){                
                img[i] = ImageIO.read(getClass()
                        .getResource("./graphics/die_"+(i+1)+".jpg"));
                backgroundImg[i] = img[i]
                        .getScaledInstance( 120, 120,java.awt.Image.SCALE_SMOOTH ); 
                icon[i] = new ImageIcon(backgroundImg[i]);
             }
            //set default image.
            b_die.setIcon(icon[0]);
           
        }catch (IOException ex) {
            System.out.println("Photo not found:"+ex);
        }
    }
    
     /**
     * Construct YahtzeeDie object with predictable pseudo-random sequence.
     */
    public YahtzeeDie(int seed){
        this.dieRandomValGenerator = new RandomGen(seed,1,7);
        this.value=this.dieRandomValGenerator.nextInt();
    }
    
    /**
     * roll Yahtzee die to set a new random value.
     */
    public void roll(){
        this.value=this.dieRandomValGenerator.nextInt();
        this.setValue(this.value);
    }
    
    /**
     * Return current value of YahtzeeDie.
     * @return value of YahtzeeDie will be from domain [1,2,3,4,5,6].
     */
    public int getValue(){
        return this.value;
    }
    
    /**
     * Set value of YahtzeeDie
     * @param value: must be between 1 and 6, inclusive.
     */
    public void setValue(int value) throws IllegalArgumentException{
        //validate input, caller should handle exception.
        if((value<1)||(value>6)){
            throw new IllegalArgumentException(Integer.toString(value));
        }
        this.value=value;
        this.updateView();
    }
    
    /**
     * Return view for YahtzeeDie.
     */
    public JButton getView(){        
        return b_die;
    }
    
    /**
     * Update values on view.
     */
    public void updateView(){
        this.getView().setIcon(icon[(this.value-1)]);
        this.getView().repaint();
    } 
}
