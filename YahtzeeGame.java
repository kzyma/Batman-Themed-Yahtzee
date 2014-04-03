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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JPanel;

/**
* YahtzeeGame is responsible for the logical flow of a single Yahtzee game.
*/

public class YahtzeeGame {
    private PlayerController[] players;
    private Dice dice;
    private YahtzeeView view;
    private int currentPlayerPlaying;
    
    private FixSizeCardLayout cardlayout_Master;
    private JPanel p_masterGraphicsContainer;
    private String currentPage_MasterGraphicsContainer;
    
    private JPanel p_gameGraphicsContainer;
    private GridBagConstraints constraints_gameGraphicsContainer;
    private String currentPage_gameGraphicsContainer; 
    
    private JPanel p_upperGraphicsContainer;
    private CardLayout cardlayout_UpperGraphics;
    private JPanel p_lowerGraphicsContainer;
    
    //gridlayout that holds both p_groupScoreContainer and p_currentScoreCardC...
    private JPanel p_standardScoreUpperContainer;
    //hold current Player playing scorecard (right side of upper)
    private JPanel p_currentScoreCardContainer;
    private CardLayout cardlayout_CurrentScoreCard;
    private String currenPage_currentScoreCard;
    //hold score for entire group playing (left side of upper)
    private JPanel p_groupScoreContainer;
    
    private EndGameView p_endView;
    
    /**
     * Construct YahtzeeGame with a variable number of players via an array of
     * user defined type PlayerController.
     * @param players: players array for the instance of Yahtzee game.
     */
    public YahtzeeGame(YahtzeeView view,PlayerController[] players){
        this.currentPage_gameGraphicsContainer = "";
        this.currentPage_MasterGraphicsContainer = "";
        this.currenPage_currentScoreCard = "";
        this.currentPlayerPlaying = 0;
        this.cardlayout_UpperGraphics = new CardLayout();
        this.cardlayout_CurrentScoreCard = new CardLayout();
        
        this.view = view;
        this.players = new PlayerController[players.length];
        this.dice = Dice.getDiceInstance();
        
        //manual array copy.
        for(int i=0;i<this.players.length;i++){
            this.players[i] = players[i];
        }
        
        init_AllGraphics();
    }
    
    /**
     *  Load all resources for a game of Yahtzee.
     */
    private void init_AllGraphics(){
        init_GameGraphicsContainer();
        init_DiceGraphics();
        init_GroupScore();
        init_PlayerGraphics();
        init_GlobalControllerGraphics();
        init_endGameGraphics();
    }
    
    /**
     * Load game graphics container's.
     */
    private void init_GameGraphicsContainer(){
        /*
         * --LAYOUT FOR EACH CONTAINER (in order)--
         * MasterGraphics: CardLayout
         * GameGraphics: GridBagLayout
         * UpperGraphics: CardLayout
         * LowerGraphics: None
         * StandardScoreUpper: Gridlayout
         * currentScoreCard: CardLayout
         * groupScore: None
         */
        
        //master is used to whipe the whole screen in the event I 
        //add whole screen animations.
        cardlayout_Master = new FixSizeCardLayout();
        p_masterGraphicsContainer = new JPanel(cardlayout_Master);
        
        p_gameGraphicsContainer = new JPanel(new GridBagLayout());
        constraints_gameGraphicsContainer = new GridBagConstraints();
        //cs_GG used to decrease typing for me in this code block
        GridBagConstraints cs_GG = constraints_gameGraphicsContainer;
        cs_GG.fill = GridBagConstraints.BOTH;
        cs_GG.weightx = 1.0;
        cs_GG.weighty = 1.0;
        cs_GG.insets = new Insets(0,0,0,0);
        
        p_upperGraphicsContainer = new JPanel(cardlayout_UpperGraphics);
        p_lowerGraphicsContainer = new JPanel();
        
        //upperPanel
        p_standardScoreUpperContainer = new JPanel(new GridLayout(1,2));
        p_currentScoreCardContainer = new JPanel(cardlayout_CurrentScoreCard);
        //gridbaglayout will auto-center elements inside, which is why
        //it is used here.
        p_groupScoreContainer = new JPanel(new GridBagLayout());
        p_standardScoreUpperContainer.add(p_groupScoreContainer);
        p_standardScoreUpperContainer.add(p_currentScoreCardContainer);
        
        //master & gameGraphics Container
        cs_GG.gridx = 0;
        cs_GG.gridy = 0;
        p_gameGraphicsContainer.add(p_upperGraphicsContainer,cs_GG);
        cs_GG.insets = new Insets(10,0,0,0);
        cs_GG.weighty = 0.02;
        cs_GG.gridy = 1;
        p_gameGraphicsContainer.add(p_lowerGraphicsContainer,cs_GG);
        p_masterGraphicsContainer.add(p_gameGraphicsContainer,"GameGraphics");
        
        //*******temporary for easier setting of dimensions**********
        p_upperGraphicsContainer.setBackground(Color.BLACK);
        p_lowerGraphicsContainer.setBackground(Color.BLACK);
        p_gameGraphicsContainer.setBackground(Color.BLACK);
        p_groupScoreContainer.setBackground(Color.BLACK);
        /*********************************************************/
        
        this.addPage_UpperGraphicPage(p_standardScoreUpperContainer, 
                "Standard Play Graphic");
        this.view.addPage_MainContainer(p_masterGraphicsContainer,"Game Running");
        
        /* init player's listeners */
        for(int i=0;i<players.length;i++){
            players[i].addturnOverListener(new turnOverListener());
        }      
    }
    
    /**
     * Load Dice Graphics.
     */
    public void init_DiceGraphics(){
        p_lowerGraphicsContainer.add(dice.getDiceView());
    }
    
    /**
     * Load Group Score Graphics.
     */
    public void init_GroupScore(){
        //get all players names to pass into grouptTotalScore type.
        String playerNames[] = new String[players.length];
        for(int i=0;i<playerNames.length;i++){
            playerNames[i] = new String(players[i].getName());
        }
        
        GroupTotalScoreView totalScore = 
                GroupTotalScoreView.getGroupTotalScoreInstance(players.length,playerNames);
        p_groupScoreContainer.add(totalScore.getView());
    }
    
    /**
     * Load all player's graphics.
     */
    public void init_PlayerGraphics(){
        for(int i=0;i<this.players.length;i++){
            //add each players scorecard to currentScorePanel
            addPage_currentScoreCard(players[i].getScoreView(),
                    "Player"+i+"ScoreCard");
        }
    }
    
    /**
     * Load end game view and add to master graphics container.
     */
    public void init_endGameGraphics(){
        //get all players names to pass into grouptTotalScore type.
        String playerNames[] = new String[players.length];
        for(int i=0;i<playerNames.length;i++){
            playerNames[i] = players[i].getName();
        }
        
        p_endView = new EndGameView(players.length,playerNames);
        addPage_MasterGraphicPage(p_endView,"endView");
        p_endView.add_BackToMainListener(new backToMainListener());
        p_endView.add_newGameListener(new newGameListener());
    }
    
    /**
     * Load global controller graphics.
     */
    public void init_GlobalControllerGraphics(){
        for(int i=0; i<this.players.length;i++){
            view.addPage_GameControlContainer(players[i].getGlobalControllerView(),
                "Player"+new Integer(i).toString()+"GameController");
        }
    }
    
    /**
     * Show playerGraphics view for a given player.
     */
    public void show_PlayerGraphics(int playerNumber){
        show_currentScoreCard("Player"+playerNumber+"ScoreCard");
    }
    
    /**
     * Show game graphics (at start of game).
     */
    private void show_GameGraphics(){
        this.view.showPage_MainContainer("Game Running");
    }
    
    /**
     * Show a game controller view for a given player.
     */
    public void show_GlobalControllerGraphics(int i){
        this.view.showPage_GameControlContainer
                ("Player"+new Integer(i).toString()+"GameController");
    }
    
    /**
     * Add page to CardLayout current ScoreCard.
     */
    private void addPage_currentScoreCard(JPanel newJPanel,String name){
        p_currentScoreCardContainer.add(newJPanel,name);
    }
    
    /**
     * Show page from CardLayout of current ScoreCard.
     */
    private void show_currentScoreCard(String page){
        if(!(this.currenPage_currentScoreCard.equals(page))){
            cardlayout_CurrentScoreCard.show(p_currentScoreCardContainer,page);
            currenPage_currentScoreCard = page;
        }
    }
    
    /**
     * Add page to CardLayout of upperGraphicsPage.
     */
    private void addPage_UpperGraphicPage(JPanel newJPanel,String name){
        p_upperGraphicsContainer.add(newJPanel,name);
    }
    
    /**
     * Show page of CardLayout for upperGraphicsPage.
     */
    private void show_UpperGraphicPage(String page){
        if(!(this.currentPage_gameGraphicsContainer.equals(page))){
            cardlayout_UpperGraphics.show(p_upperGraphicsContainer,page);
            currentPage_gameGraphicsContainer = page;
        }
    }
    
    /**
     * Add page to CardLayout of masterGraphicPage. MasterGraphicPage holds
     * both upper and lower graphic container's.
     * <p>Note this panel is mostly used to flush entire screen for a 'cutscene'.
     */
    private void addPage_MasterGraphicPage(JPanel newJPanel,String name){
        p_masterGraphicsContainer.add(newJPanel,name);
    }
    
    /**
     * Show page from CardLayout of masterGraphicPage. 
     */
    private void show_MasterGraphicPage(String page){
        if(!(this.currentPage_MasterGraphicsContainer.equals(page))){
            cardlayout_Master.show(p_masterGraphicsContainer,page);
            currentPage_MasterGraphicsContainer = page;
        }
    }
    
    /**
     * Start a new YahtzeeGame.
     */
    public void initGame(){
        //make sure all scorecards set to 0, first player is shown,ect.
        resetGame();
        show_GameGraphics();
        //start 1st players turn
        playTurn();
    }
    
    /**
     * Game Logic for a player's turn.
     */
    public void playTurn(){
        //if all players are done...launch results and exit game
        if(this.gameOver()){
            exitGame();
        }
        if(!(players[currentPlayerPlaying].donePlaying())){
            //get focus on dice.
            this.dice.getDiceView().requestFocus();
            players[currentPlayerPlaying].newTurn();
            show_PlayerGraphics(currentPlayerPlaying);
            show_GlobalControllerGraphics(currentPlayerPlaying);
            players[currentPlayerPlaying].decideAction();
        }else{
            setNextPlayer();
        }
    }
    
    /**
     * Rotate to next player.
     * <p>For all purposes you can think of the player structure as a
     * circular array, where calling setNextPlayer is the increment.
     */
    public void setNextPlayer(){
        if(currentPlayerPlaying == (this.players.length-1)){
            currentPlayerPlaying = 0;
        }else{
            currentPlayerPlaying++;
        }
    }
    
    /**
     * Return true if all players are done 'playing', false otherwise.
     */
    public boolean gameOver(){       
        for(int i=0;i<players.length;i++){
            if(!(players[i].donePlaying())){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Exit game in a controlled mannor.
     */
    private void exitGame(){
        //any cleanup before the current game exits i.e scoring, ect.
        //make all global control buttons in-operable.
        show_GlobalControllerGraphics(-1);
        //get all players scores
        int playersScores[] = new int[this.players.length];
        for(int i=0;i<playersScores.length;i++){
            playersScores[i] = (this.players[i].getScorecard()).getTotalScore();
        }
        
        //make sure the highscore panel is updated before showing it.
        this.p_endView.update(playersScores);
        this.checkHighScores(players);
        this.show_MasterGraphicPage("endView");
    }
    
    /**
     * Reset all game attributes, such as score.
     * <p>This resets attributes present from game to game, ones that span over
     * the lifetime of multiple plays such as a player's deposited money.
     */
    private void resetGame(){
        //reset all current game score, ect. to start a 'new game'.
        //note: does not reset the money deposit, that is done when
        //user goes to main menu
        GroupTotalScoreView groupScoreView = GroupTotalScoreView.getGroupTotalScoreInstance();
        for(int i=0;i<players.length;i++){
            this.players[i].reset();
            groupScoreView.updateScore(i,0);
        }
        //make sure first player's graphics are all shown
        this.currentPlayerPlaying = 0;
        this.show_UpperGraphicPage("Standard Play Graphic");
        this.show_GlobalControllerGraphics(currentPlayerPlaying);
        this.show_PlayerGraphics(currentPlayerPlaying);
        this.show_currentScoreCard(currenPage_currentScoreCard);
       
    }
    

    /**
     * Check and Update highscore's structure. This will also output
     * if a player made it onto the high scores chart.
     * @param players.
     */
    private void checkHighScores(PlayerController[] players){
        //check if either player's score is a new high score and update 
        //appropriatly.
        HighscoresController hsRecord = null;
        try{
        hsRecord = HighscoresController.getHighscoreInstance();
        }catch(IOException e){}
        for (int i=0;i<players.length;i++){
            if(hsRecord.isNewHighScore((players[i].getScorecard()).getTotalScore())){
                
                String name = players[i].getName();
                int score = players[i].getScorecard().getTotalScore();
                String card = players[i].getScorecard().toString();
                int place=0;
                try{
                    place = hsRecord.addNewHighScore(name,score,card);
                }catch(IOException e){}
            }
        }
    }
    
    /**
     * Callback routine for a player's turn to be over.
     */
    private class turnOverListener implements CallbackEvent{
        @Override
        public void eventPerformed(){
            setNextPlayer();
            playTurn();
        }
    }
    
    /**
     * Callback routine to exit to main menu.
     */
    private class backToMainListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            view.showPage_MainContainer("menuView");
        }
    }
    
    /**
     * Callback routine to start a new game.
     */
    private class newGameListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            initGame();
            show_MasterGraphicPage("GameGraphics");     
        }
    }
    
    /**
     * FixedSizeCardLayout is a cardlayout with a fixed size of 600x434.
     */
     private class FixSizeCardLayout extends CardLayout{
        FixSizeCardLayout(){
            super();
        }
        
        @Override
        public Dimension preferredLayoutSize(Container parent) {
            return new Dimension(600,434);
        }
    }
     
}
