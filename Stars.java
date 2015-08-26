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
public class Stars
{
    public static void main(String[] args)
    {
        ConsoleFrame consoleFrame = new ConsoleFrame("Stars", 12, 20);
        //actual grid size is 8x16 but that doesn't work so well
        //also need 3 extra lines for score/prompts
        
        ImageIcon icon=null;
        URL imgURL = consoleFrame.getClass().getResource("icon.gif");
        if (imgURL != null) icon = new ImageIcon(imgURL);
        if (icon != null) 
        {
            consoleFrame.setIconImage(icon.getImage());
        }
        
        Console console = consoleFrame.console;
        console.setEditable(false);
        
        ConsoleView view = new ConsoleView(console);
        KeyController controller = new KeyController();
        console.addKeyListener(controller);
        Game game = new Game(controller, view);
        game.startGame();
    }
}