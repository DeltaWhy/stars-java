/**
 * Stars - a simple but addictive game
 * This class simply creates the objects and sets up the game.
 * 
 * @author Michael Limiero
 * @version 1
 */
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.URL;
public class StarsApplet extends JApplet
{
    public void init()
    {
        Console console = new Console(12,20);
        console.setEditable(false);
        add(console);
        
        ConsoleView view = new ConsoleView(console);
        KeyController controller = new KeyController();
        console.addKeyListener(controller);
        Game game = new Game(controller, view);
        game.startGame();
    }
}