/**
 * Not technically console, but a text-based view
 * of the actors and score.
 * @author Michael Limiero
 */
import javax.swing.JTextArea;
public class ConsoleView implements View
{
    JTextArea console;
    char[] field;
    //a 1D array to simplify printing
    // but xyToI lets it work like a 2D
    int mainX;
    int mainY;
    int score;
    boolean onSpring;
    Game.Mode mode;
    
    public ConsoleView(JTextArea console)
    {
       this.console = console;
       while (console.isEditable()) console.setEditable(false);
    }
    public void startGame()
    {
        mode = Game.getMode();
        score = 0;
        //generate the field
        String sField = "";
        String row = "";
        for (int i = 0; i < Game.WIDTH; i++)
            row += ".";
        row += "\n";
        for (int i = 0; i < Game.HEIGHT; i++)
            sField += row;
        field = sField.toCharArray();
        update();
        onSpring = false;
    }
    public void endGame()
    {
        //show the ranking
        int k = xyToI(0, Game.HEIGHT/2);
        String message = Game.getRank(score);
        int numSpaces = (Game.WIDTH - message.length()) / 2;
        for (int i = k; i < k + Game.WIDTH; i++) //loop thru the middle row
            //blank it out
            field[i] = ' ';
        char[] cMessage = message.toCharArray();
        for (int i = 0; i < cMessage.length; i++)
            //put the message onscreen
            field[i+k+numSpaces] = cMessage[i];
        update();
        console.setText(console.getText() + "\n{Enter}=New Game\n{Esc}=Quit");
    }
    
    public void addActor(Game.Actor a, int x, int y)
    {
        char c = '.';
        switch (a) 
        {
            case MAIN:
                c = '@';
                mainX = x;
                mainY = y;
                break;
            case MINE:
                c = ',';
                break;
            case SPRING:
                c = '^';
                break;
            case STAR:
                c = '*';
                break;
        }
        field[xyToI(x,y)] = c;
        update();
    }
    public void addRandom(Game.Actor a)
    {
        boolean okay = false;
        int x=-1; //java complains even though this will
        int y=-1; //always get initialized...
        while (!okay)
        {
            x = (int)(Math.random() * Game.WIDTH); //starts at 0
            y = (int)(Math.random() * Game.HEIGHT);
            if (field[xyToI(x,y)] == '.') okay = true;
        }
        addActor(a, x, y);
    }
    public void updateScore(int score)
    {
        this.score = score;
        update();
    }
    public Game.MoveResult move(int direction)
    {
        Game.MoveResult result = Game.MoveResult.NORMAL;
        
        int xDiff = 0;
        int yDiff = 0;
        if (direction == 2 || direction == -2) xDiff = direction/2;
        if (direction == 1 || direction == -1) yDiff = direction;
        //this is why I used constants instead of an enum
        
        int x = mainX;
        int y = mainY;
        x += xDiff;
        y += yDiff;
        //check for out-of-bounds
        if (x < 0 || x >= Game.WIDTH || y < 0 || y >= Game.HEIGHT)
        {
            if (mode == Game.Mode.BOUNCE)
            {
                result = Game.MoveResult.WALL;
                x = mainX - xDiff;
                y = mainY - yDiff;
            } else if (mode == Game.Mode.WRAPAROUND) {
                if (x < 0) x = Game.WIDTH-1;
                else if (x >= Game.WIDTH) x = 0;
                if (y < 0) y = Game.HEIGHT-1;
                else if (y >= Game.HEIGHT) y = 0;
            } else {
                if (onSpring) field[xyToI(mainX,mainY)] = '^';
                else field[xyToI(mainX,mainY)] = '.';
                return Game.MoveResult.DIE;
            }
        }
        //do the move
        if (onSpring) field[xyToI(mainX,mainY)] = '^';
        else field[xyToI(mainX,mainY)] = '.';
        char c = field[xyToI(x,y)];
        field[xyToI(x,y)] = '@';
        mainX = x;
        mainY = y;
        update();
        onSpring = false;
        //figure out the result
        if (c == ',') result = Game.MoveResult.DIE;
        else if (c == '^') 
        {
            if (result == Game.MoveResult.WALL) result = Game.MoveResult.WALLSPRING;
            //there is a special case when we bounce off the wall into a spring
            else result = Game.MoveResult.SPRING;
            onSpring = true;
        } else if (c == '*') result = Game.MoveResult.SCORE;
        return result;
    }
    public void showHelp(int page)
    {
        String[] lines = Game.getHelp().split("\n");
        String message = "";
        int k = Game.HEIGHT * page;
        for (int i = k; i < k + Game.HEIGHT && i < lines.length; i++)
            message += lines[i] + "\n";
        console.setText(message);
    }
    private void update()
    {
        console.setText(new String(field) + "Score: " + score);
    }
    private int xyToI(int x, int y)
    {
        //take an x/y coordinate and convert it
        // to its index in the char array
        return x + (Game.WIDTH+1)*y;
                            //+1 because of the newlines
    }
}
