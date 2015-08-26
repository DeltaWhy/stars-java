/**
 * The mother of all classes...
 * Handles game events, tracks score, etc.
 * @author Michael Limiero
 */
import java.util.Timer;
import java.util.Scanner;
import java.io.*;
import javax.swing.JOptionPane;
public class Game
{
    //direction constants
    public static final int UP = -1;
    public static final int DOWN = 1;
    public static final int LEFT = -2;
    public static final int RIGHT = 2;
    //these aren't enums because the values
    // make things simpler
    
    public enum MoveResult {NORMAL, WALL, SPRING, WALLSPRING, SCORE, DIE};
    public enum Actor {MAIN, MINE, SPRING, STAR};
    public enum Mode {BOUNCE, WRAPAROUND, DIE};
    
    //other constants
    public static final int HEIGHT = 8;
    public static final int WIDTH = 16;
    public static final int BLOCK_SIZE = 32;
    int delay = 200;
    
    private static final String SCORE_FILE = "scores.dat";
    
    private static int highScore;
    private int score;
    private boolean running;
    private boolean preGame;
    private int helpPage;
    private int currentDirection;
    private boolean nextSpring;
    private Controller controller;
    private View view;
    private Timer timer;
    private static Mode mode;
    public Game(Controller controller, View view)
    {
        this.controller = controller;
        controller.setGame(this);
        this.view = view;
        running = false;
        preGame = true;
        readHighScore();
    }
    public void startGame()
    {
        if (preGame) 
        {
            preGame();
            return;
        }
        score = 0;
        nextSpring = false;
        running = true;
        currentDirection = 0;
        //notify the other classes
        controller.startGame();
        view.startGame();
        //put stuff on the grid
        view.addActor(Actor.MAIN, WIDTH/4, HEIGHT/2);
        addRandoms();
        //start the timer
        timer = new Timer();
        timer.schedule(new StarsTask(this),delay,delay);
    }
    public void endGame()
    {
        running = false;
        timer.cancel();
        controller.endGame();
        view.endGame();
        if (score > highScore)
            if (delay <= 200)
            {
                highScore = score;
                JOptionPane.showMessageDialog(null, "New High Score!", "High Score",
                    JOptionPane.INFORMATION_MESSAGE);
                writeHighScore();
            }
            else JOptionPane.showMessageDialog(null, "You would have had a" +
                " high score, but your speed is too slow!", "Not High Score",
                JOptionPane.INFORMATION_MESSAGE); //can't make it too easy...
    }
    private void preGame()
    {
        helpPage = 0;
        view.showHelp(helpPage);
    }
    public void move(int direction) //called by Controller
    {
        currentDirection = direction;
    }
    public void tick() //actually makes the move
    {
        MoveResult result = view.move(currentDirection);
        //make a move
        switch (result)
        {
            case WALLSPRING:
                controller.reverseControls();
                break;
            case SPRING:
                controller.reverseControls();
                currentDirection = -currentDirection;
                break;
            case WALL:
                currentDirection = -currentDirection;
                break;
            case SCORE:
                score++;
                view.updateScore(score);
                addRandoms();
                break;
            case DIE:
                endGame();
                break;
        }
    }
    private void addRandoms()
    {
        view.addRandom(Actor.STAR);
        if (nextSpring) view.addRandom(Actor.SPRING);
        else view.addRandom(Actor.MINE);
        nextSpring = !nextSpring;
    }
    private void readHighScore()
    {
        highScore = 0;
        try
        {
            Scanner sc = new Scanner(new File(SCORE_FILE));
            highScore = sc.nextInt();
            sc.nextLine(); //consume the newline
            String md5 = sc.nextLine();
            sc.close();
            if (!md5.equals(JavaMD5Sum.computeSum(Integer.toString(highScore)))) 
            {
                File f = new File(SCORE_FILE);
                f.delete();
                JOptionPane.showMessageDialog(null, "Cheaters never prosper.", 
                    "You bad person!", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {} //do nothing, probably file doesn't exist
    }
    private void writeHighScore()
    {
        try
        {
        PrintStream p = new PrintStream(SCORE_FILE);
        p.println(highScore);
        p.println(JavaMD5Sum.computeSum(Integer.toString(highScore)));
        //I md5sum it so that nasty people can't edit the file and make
        // themselves look awesome when they actually suck
        p.flush();
        p.close();
        } catch (Exception e) { e.printStackTrace(); } //don't crash
    }

    public static String getRank(int score)
    {
        if (score == 0) return "FAIL";
        if (score <= 5) return "PWNED!";
        if (score <= 10) return "OUCH!";
        if (score <= 15) return "LOSEAGE!";
        if (score <= 20) return "NOT BAD";
        if (score <= 25) return "NIIIICE!";
        if (score <= 30) return "IMPRESSIVE";
        if (score <= 35) return "DAAAANG!";
        if (score <= 40) return "WOW.";
        if (score <= 45) return "INCREDIBLE!";
        if (score <= 50) return "THAT'S CREEPY";
        return "YOU HAVE NO LIFE";
    }
    public static String getHelp()
    {
        //I didn't want to use a constant...
        return "\n" +
               "     ST*RS\n" +
               "\n" +
               "Michael Limiero\n" +
               "\n" +
               "High Score: " + highScore + "\n\n" +
               "(Press Enter)\n" +
               
               "OBJECT: GET *'s\n" +
               "\n" +
               "YOU: @\n" +
               "MINE: , (AVOID)\n" +
               "SPRING: ^\n" +
               "  (REVERSES\n" +
               "  YOUR CONTROLS)\n" +
               "(Press Enter)\n" +
               
               "CONTROLS:\n\n" +
               "ARROWS TO MOVE\n" +
               "ESCAPE TO QUIT\n" +
               "ENTER TO START\n" +
               "\n" +
               "GOOD LUCK!\n" +
               "(YOU'LL NEED IT)";
    }
    public static Mode getMode()
    {
        return mode;
    }
    public void next()
    {
        if (running) return;
        if (!preGame) startGame();
        else //we are still at the help screen
        {
            int pages = (getHelp().split("\n").length - 1) / HEIGHT;
            //if it is 8 lines, there is only a page 0 (hence the -1)
            if (helpPage < pages)
            {
                helpPage++;
                view.showHelp(helpPage);
            } else {
                preGame = false;
                chooseSpeed();
                chooseMode();
                startGame();
            }
        }
    }
    public void end()
    {
        if (running) endGame();
        else System.exit(0);
    }
    private void chooseSpeed()
    {
        String ans = JOptionPane.showInputDialog(null, "Choose a speed:\n" +
            "1) Granny\n2) Wimp\n3) Sane\n4) Insane\n5) Speed demon",
            "Choose a speed", JOptionPane.QUESTION_MESSAGE);
        int choice = 3;
        try
        {
            choice = Integer.valueOf(ans);
        } catch (Exception e) {} //don't crash!
        if (choice == 1) delay = 275;
        else if (choice == 2) delay = 250;
        else if (choice == 3) delay = 200;
        else if (choice == 4) delay = 150;
        else if (choice == 5) delay = 125;
        else delay = 200; //in case they type something stupid
    }
    private void chooseMode()
    {
        String ans = JOptionPane.showInputDialog(null, "Choose the wall mode:\n" +
            "1) Bounce\n2) Wrap-around\n3) Die", "Choose a mode",
            JOptionPane.QUESTION_MESSAGE);
        int choice = 1;
        try
        {
            choice = Integer.valueOf(ans);
        } catch (Exception e) {}
        if (choice == 1) mode = Mode.BOUNCE;
        else if (choice == 2) mode = Mode.WRAPAROUND;
        else if (choice == 3) mode = Mode.DIE;
        else mode = Mode.BOUNCE;
    }
}