/**
 * Listens to keys and calls Game's methods
 */
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class KeyController implements Controller, KeyListener
{
    Game game;
    boolean reversed;
    public KeyController()
    {
    }
    public void setGame(Game game)
    {
        this.game = game;
    }
    public void startGame()
    {
        reversed = false;
    }
    public void endGame() {} //doesn't matter to us
    public void reverseControls()
    {
        reversed = !reversed;
    }
    public void keyPressed(KeyEvent e)
    {
        int result = 0;
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_UP) result = Game.UP;
        else if (c == KeyEvent.VK_DOWN) result = Game.DOWN;
        else if (c == KeyEvent.VK_LEFT) result = Game.LEFT;
        else if (c == KeyEvent.VK_RIGHT) result = Game.RIGHT;
        
        else if (c == KeyEvent.VK_ESCAPE) game.end();
        else if (c == KeyEvent.VK_ENTER) game.next();
        
        if (result == 0) return;
        if (reversed) game.move(-result);
        else game.move(result);
    }
    public void keyTyped(KeyEvent e) {} //unused
    public void keyReleased(KeyEvent e) {} //unused
}
