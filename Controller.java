/**
 * Receives input from the player and relays it to Game
 */
public interface Controller
{
    void setGame(Game game);
    void startGame();
    void endGame();
    void reverseControls();
    //a Controller must call Game.move, Game.next, and Game.end
}
