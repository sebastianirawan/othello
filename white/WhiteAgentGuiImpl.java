package othello.white;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WhiteAgentGuiImpl extends JFrame implements WhiteAgentGui {
    private WhiteAgent myAgent;
    private JButton[][] buttons = new JButton[8][8];

    public WhiteAgentGuiImpl() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        JPanel boardPanel = new JPanel(new GridLayout(8,8,2,2));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(48,48));
                b.setName("B" + (r+1) + (c+1)); // e.g. B11
                b.setOpaque(true);
                b.setContentAreaFilled(true);
                b.setBackground(Color.WHITE);
                b.setEnabled(false);
                final int rr = r, cc = c;
                b.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e) {
                        btnActionPerformed(b, rr, cc);
                    }
                });
                buttons[r][c] = b;
                boardPanel.add(b);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void btnActionPerformed(JButton btn, int r, int c) {
        if (btn.isEnabled() && myAgent.isTurn()) {
            // send move to agent (agent will apply move and send to opponent)
            btn.setBackground(Color.GREEN); // white agent uses GREEN
            myAgent.updateBoard(btn.getName());
            deactivateAll();
            notifyUser(btn.getName() + " pressed.");
        }
    }

    @Override
    public void setAgent(WhiteAgent a) {
        myAgent = a;
        setTitle(myAgent.getName());
    }

    @Override
    public void showGUI() {
        super.setVisible(true);  // pakai super untuk memastikan tidak rekursi
    }
    
    @Override
    public void notifyUser(String message) {
        System.out.println("White GUI: " + message);
    }

    @Override
    public void updateBoardVisual(int[][] board) {
        // board is 8x8 with -1,1,0
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                JButton b = buttons[r][c];
                if (board[r][c] == 1) { // black -> BLUE
                    b.setOpaque(true);
                    b.setContentAreaFilled(true);
                    b.setBackground(Color.BLUE);
                    b.setEnabled(false);
                } else if (board[r][c] == 0) { // white -> GREEN
                    b.setOpaque(true);
                    b.setContentAreaFilled(true);
                    b.setBackground(Color.GREEN);
                    b.setEnabled(false);
                } else {
                    b.setBackground(Color.WHITE);
                    b.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void activateValidMoves(int player) {
        // highlight valid moves for player: enable buttons where move valid
        othello.common.OthelloBoard tmp = myAgent.othello;
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                JButton b = buttons[r][c];
                if (tmp.board[r][c] == -1 && tmp.isValidMove(player, r, c)) {
                    b.setEnabled(true);
                    // optional highlight as light gray
                    b.setBackground(Color.LIGHT_GRAY);
                } else {
                    b.setEnabled(false);
                    // if empty keep white
                    if (tmp.board[r][c] == -1) b.setBackground(Color.WHITE);
                }
            }
        }
    }

    @Override
    public void deactivateAll() {
        for (int r=0;r<8;r++){
            for (int c=0;c<8;c++){
                buttons[r][c].setEnabled(false);
                // if empty keep white
                if (myAgent != null && myAgent.othello != null && myAgent.othello.board[r][c] == -1) {
                    buttons[r][c].setBackground(Color.WHITE);
                }
            }
        }
    }
}