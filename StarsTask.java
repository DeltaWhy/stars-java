/**
 * When the timer ticks, call Game.tick()
 */
import java.util.TimerTask;
public class StarsTask extends TimerTask
{
    private Game g;
    public StarsTask(Game g)
    {
        super();
        this.g = g;
    }
    public void run()
    {
        g.tick();
    }
}