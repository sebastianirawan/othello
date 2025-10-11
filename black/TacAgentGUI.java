package othello.black;

public interface TacAgentGUI {
    void setAgent(TacAgent a);
    void showGUI();
    void notifyUser(String message);
    void updateBoardVisual(int[][] board);
    void activateValidMoves(int player); // player: 0=white,1=black
    void deactivateAll();
}