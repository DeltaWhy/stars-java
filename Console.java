/**
 * A modified JTextArea that sets the proper size/font options
 */
import java.awt.*;
import javax.swing.*;
public class Console extends JTextArea
{   
    public Console()
    {
        this(26,80);
    }
    public Console(int rows, int columns)
    {
        super(rows, columns);
        Font x = new Font("Monospaced", Font.PLAIN, 24);
        setFont(x);
        setLineWrap(true);
    }
}

