************************ Readme.txt ************************
author: Ken Zyma
Date: March 2014
Assignment: Project 2
Class: CSC421

*********************** OverView ***************************

Yahtzee GUI-based game that supports multiple human and AI players, and a
high score system for recording and viewing past top score. Official
game rules from http://www.hasbro.com/common/instruct/Yahtzee.pdf are 
followed.

*********************** How to run *************************
Web Application:

Located at:
http://acad.kutztown.edu/~kzyma650/index.html

Javdoc located at:
http://acad.kutztown.edu/~kzyma650/javadoc

Application:
Step 1: compile all included files to java byte code.
	example : javac *.java
Step 2: Run main from Yahtzee.java
	example: java Yahtzee

Applet:
Step 1: compile all included files to java byte code.
	example : javac *.java
Step 2: Lunch index.html in browser or with AppletViewer.
	example: appletviewer index.html

The program flow should be pretty obvious. On start a
main menu is displayed allowing you to choose one of
four options:
1)New Game: Start a new Yahtzee game.
	You may choose as many players as you like. All ‘players’
	are human and play against a single AI ‘bad guy’.
	To keep with the Batman theme across the game, 
	players character’s may be Batman,Robin, or Catwoman and
	the AI character is Bane.
2)View Highscores: View the top 10 highscores of all time.
3)View AI Demo: see a AI vs AI demo match.
4)Exit: exits program (Application only).

*note2* If a yahtzee has been already scored and you get a second
yahtzee, you will see “Joker rules are in effect”. You may now
score the current turn according to “Joker Rules” (see hasbro link
above for any questions). Additionally a 100 point bonus is awarded if 
you previously scored ’50’ in the Yahtzee category. This is the
method in hasbro’s yahtzee rules and I stuck to them for this project.

*note3* The cheat to change dice values for the game can be entered
at any time through a player’s turn. Press ‘c’ and being entering
values [1-6] for the dice from left to right.

******************* Project Design **********************

  The difficulty in this design came from the brute amount of work it
took creating graphics for the entire game. The second speed bump with
this iteration of Yahtzee was trying to figure out how to listen for
button clicks that were going on in various class’s. The second problem
was addressed using a series of ‘callback’ routines, effectively
function pointers wrapped in an ActionEvent class, to distribute my
listening to remote classes. This worked really well and provided a simple
method for this.

  The Model-View-Controller pattern used on players, highscores, and Yahtzee 
made updating code from the text-based game to a GUI Swing based game easier,
as most of my display code was already in the ‘view. The MVC pattern also
allowed me to simplify my Players (human and AI) classes regarding how they 
interact with the rest of the program. It gave a simple interface (PlayerConroller)
to code to. 

  You will notice my project has an arsonal of various container’s for everything. 
This was a design decision based on not being completely sure what kind of 
Animations I am going to add for the next project, but to make additions easy 
with very little change to current source code. For example, I am using a
container for the entire slot machine ‘screen’ with CardLayout, this will allow
me to easily place an animation on the screen between turns/dice rolls/ect.

Container High Level Design:

Yahtzee(Applet)/JFrame
|
 ->Yahtzee View
   |
   |->YahtzeeMainMenuView(CardLayout)
   |   |
   |   |->p_MainMenuBackground
   |   |
   |   |->p_Num_OfPlayersBackground
   |   |
   |   |->p_CharacterSelectionGUIs[]
   |
   |->YahtzeeGameView/p_MasterGraphics (CardLayout)
   |   |
   |   |->p_GameGraphics
   |      |
   |      |->p_UpperGraphics (CardLayout)
   |      |  |
   |      |   ->p_StandardScoreUpper
   |      |     |
   |      |     |->p_CurrentScoreCard (CarLayout)
   |      |     |
   |      |      ->p_GroupScoreCard
   |      |
   |      |->p_LowerGraphics
   |
    ->YahtzeeEndGameView
       |
        ->p_PlayerScores
   
***********************   Bugs ******************************

I felt implementing the slot machine mechanics such as depositing
money, ‘cashing out’ ext was beyond the scope of what had to be
completed for this project, so I left it out (although it will be
present in the final iteration). Implementing this functionality
will have a degree of difference between this, and project 3, thus
is left to only implement once.

********************* References ****************************

  I believe my work to follow the U.S Copyright Office ‘Fair Use’ Policy, and
more specifically chapter 9’s ‘Educational Use’ Policy. Although a court decision
is the only true test of correct use of copyrighted work, the following supports
my decision to use all material included:
  Factor 1: Purpose and Character of Use
    1) preference favors educational, non-profit use.
    2) purpose of use is scholorship.
    3) Degree of transformation of work:Each re-purposed to various degree’s
          , they do however all alter the intended message (I believe) of the
	  original artist(s).
  Factor 2: Nature of Copyrighted Work
    -
  Factor 3: Relative Amount
    I believe no more material was taken than needed to make my point in this
    work.
  Factor 4: Effect Upon Potential Market
    There will be no effect on the market of any material I have used. I believe
    no harm was done to original work.

All Material Not Owned By Ken Zyma:

-Bane_Icon.jpg:

1) https://static.squarespace.com/static/51b3dc8ee4b051b96ceb10de/51ce6099e4b0d911b4489b79/51ce61d7e4b0d911b44a31f5/1344362555427/1000w/bane-tom-hardy-the-dark-k862012night-rises-mask-wallpaper.jpeg  

-Bane_ScoreCard.jpg

1)http://img1.wikia.nocookie.net/__cb20120713050100/villains/images/2/25/BaneTDKR.jpg

-Batman_Icon.jpg

1)http://www.motleyhealth.com/images/Batman_Christian_Bale2.jpg

-Batman_ScoreCard.jpg

1)http://hqwide.com/batman-movies-rain-the-dark-knight-rises-wallpaper-9873/

-CatWoman_Icon.jpg

1)http://the-suit.com/wp-content/uploads/2012/07/anne_hathaway_catwoman.jpg

-CatWoman_ScoreCard.jpg

1)http://th08.deviantart.net/fs71/PRE/f/2012/145/a/d/the_dark_knight_rises_catwoman_batman_bane_by_titiuchiha-d514b13.jpg

-Choose_Character_Background.jpg

1)http://img1.wikia.nocookie.net/__cb20131024182929/playstationallstarsbattleroyale/images/7/72/Gotham-City-batman-24242266-1131-707.jpg

-MainMenu_BackGround.jpg

1)http://th08.deviantart.net/fs71/PRE/f/2012/145/a/d/the_dark_knight_rises_catwoman_batman_bane_by_titiuchiha-d514b13.jpg

-Num_Players_BackGround.jpg

1)http://img1.wikia.nocookie.net/__cb20131024182929/playstationallstarsbattleroyale/images/7/72/Gotham-City-batman-24242266-1131-707.jpg

-Robin_Icon.jpg

1)http://2.bp.blogspot.com/-eskKQthvkt0/T4fGeTZVW0I/AAAAAAAAAwQ/2H841zNcSd0/s640/Photo+23.jpg

-Robin_ScoreCard.jpg

1)http://24.media.tumblr.com/tumblr_m85ex6tAku1qli6yqo1_500.jpg   




