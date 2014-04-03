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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;


/**
* Responsible for dispatching requests to the correct
* handler from Yahtzee.
* <p>Follows front-controller pattern. See Yahtzee and
* YahtzeeView.
*/
public class YahtzeeDispatcher {
    private YahtzeeGame game;
    private int numberOfPlayers;
    private PlayerController[] players;
    private MainMenuView menuView;
    private YahtzeeView view;
    private HighscoresController highscores;
    
    /**
     * Construct a new YahtzeeDispatcher object.
     * @param view An Instance of YahtzeeView.
     */
    public YahtzeeDispatcher(YahtzeeView view){
        this.view = view;
    }  
    
    /**
     * Initialize data for the main menu.
     * @param menuListener Callback routine for main menu.
     */
    public void initMainMenu(ActionListener menuListener){
        menuView = new MainMenuView();
        menuView.addMenuListener(menuListener);
        menuView.addSetPlayersListener(new NumOfPlayersListener());
        view.addPage_MainContainer(menuView,"menuView");
    }
    
    /**
     * Initialize Player data.
     * <p>The default is to initialize one human player and one AI player.
     */
    public void initPlayers(){
        players = new PlayerController[numberOfPlayers];
        
        //BANE is the only AI character
        for(int i=0;i<(this.numberOfPlayers-1);i++){
            HumanView h_view = new HumanView();
            Human model = new Human(menuView.getPlayerName(i),
                    i,menuView.getPlayerCharacter(i));
            players[i] = new HumanController(model,h_view);
        }
        
        AIView view = new AIView();
        AI model = new AI((numberOfPlayers-1),"Bane");
        players[numberOfPlayers-1] = new AIController(model,view);
        
        //get all player information from the view.
        for(int i=0;i<(numberOfPlayers-1);i++){
            players[i].setName(menuView.getPlayerName(i));
        }
        players[(numberOfPlayers-1)].setName("Bane"); 
    }
    
    /**
     * Show main menu on the view.
     */
    public void dispatchMainMenu(){
        menuView.showMainMenu();
    }
    
    /**
     * Show 'Number Of Players' screen on the view.
     */
    public void launchGetNumberOfPlayersPrompt(){
        //open dialog to get number of players
        menuView.showNumberOfPlayersDialog();
        menuView.addStartGameListener(new startGameListener());
    }
    
    /**
     * Initialize a new game and show game screen on the view.
     */
    public void launchNewGame(){
        initPlayers();
        //start a new game
        game = new YahtzeeGame(view,players);
        //reset main menu view when new game is launched.
        this.menuView.showMainMenu();
        game.initGame();
    }
    
    /**
     * Show 'Choose Character' screen on the view.
     */
    public void launchGetPlayerCharactersPrompt(){
        menuView.showPlayerCharactersDialog((this.numberOfPlayers-1));
    }
    
    /**
     *  Initialize an AI Demo and show game screen on the view
     * <p>An AI demo is one 1vs1 game, where both players are AI.
     */
    public void launchAIDemo(){
        //init two AI players
        players = new PlayerController[2];
        numberOfPlayers = 2;
        
        AIView view1 = new AIView();
        AI model1 = new AI(0,"Batman");
        players[0] = new AIController(model1,view1);
        players[0].setName("Batman");
        
        AIView view2 = new AIView();
        AI model2 = new AI(1,"Bane");
        players[1] = new AIController(model2,view2);
        players[1].setName("Bane");
        
        game = new YahtzeeGame(view,players);
        //reset main menu view when new game is launched.
        this.menuView.showMainMenu();
        game.initGame();
    }
    
    /**
     * Initialize HighScores menu.
     */
    public void init_HighScores(){
        try{
            highscores = HighscoresController.getHighscoreInstance();
        }catch(IOException e){
            System.err.println("Unable to open highscores data: "+e);
        }
        highscores.addBackToMenuListener(new backToMenuListener());
        view.addPage_MainContainer((highscores.getView()),"HighScoresView");
    }
    
    /**
     * Show HighScores screen on view.
     */
    public void launchHighScores(){
        this.highscores.getView();
        view.showPage_MainContainer("HighScoresView");
    }
    
    /**
     * Callback routine to get number of players from YahtzeeView
     *  getNumberOfPlayersInput() and set numberOfPlayers in YahtzeeDispatcher.
     */
    private class NumOfPlayersListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Object actionSource = e.getSource();
            JButton source = null;

            if(actionSource instanceof JButton){
                source = (JButton)actionSource;
                numberOfPlayers = menuView.getNumberOfPlayersInput();
                //'+1' includes AI player BANE.
                numberOfPlayers += 1;
                launchGetPlayerCharactersPrompt();
            }else{
                System.err.println("Action not recognized from:"+
                        e.toString());
            }
        }
    }
 
    /**
     * Callback routine to launchNewGame when action is fired.
     */
    private class startGameListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            Object actionSource = e.getSource();
            JButton source = null;

            if(actionSource instanceof JButton){
                launchNewGame();
            }else{
                System.err.println("Action not recognized from:"+
                        e.toString());
            }
        }
    }
     
    /**
     * Callback routine to show the main menu when action is fired.
     */
    private class backToMenuListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            view.showPage_MainContainer("menuView");
        }
    }
    
}
