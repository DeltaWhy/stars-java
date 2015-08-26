/**
 * Keeps track of Actors and displays them (and score)
 */
public interface View
{
    //View has no knowledge of Game... it doesn't need to
    void startGame();
    void endGame();
    void addActor(Game.Actor a, int x, int y);
    void addRandom(Game.Actor a);
    void updateScore(int score);
    void showHelp(int page);
    Game.MoveResult move(int direction);
}
