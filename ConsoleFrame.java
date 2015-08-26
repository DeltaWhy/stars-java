/**
 * Holds the console. Does nothing else.
 */
import java.awt.*;
import javax.swing.*;
public class ConsoleFrame extends JFrame
{
    public Console console;
    public ConsoleFrame()
    {
        this("");
    }
    public ConsoleFrame(String title)
    {
        this(title, 26, 80);
    }
    public ConsoleFrame(String title, int rows, int cols)
    {
        super();
        console = new Console(rows, cols);
        Container c = getContentPane();
        c.add(console);
        setBounds(300,300,0,0);
        setSize(console.getPreferredSize());
        setTitle(title);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}