package othello.white;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import othello.common.OthelloBoard;
import java.awt.Color;
import javax.swing.JButton;

public class TicAgent extends Agent {
    private TicAgentGUI ticGui;
    public OthelloBoard othello;
    boolean turn = true; // black starts first
    int step = 0;
    String lastMsg = "";

    protected void setup() {
        System.out.println("Tic-agent "+getAID().getName()+" (BLACK) is ready.");
        othello = new OthelloBoard();

        // Show the GUI to interact with the user
        ticGui = new TicAgentGUIImplementation();
        ticGui.setAgent(this);
        ticGui.showGUI();
        ticGui.updateBoardVisual(othello.board);

        // behaviors: invite & play (similar to Tic original)
        addBehaviour(new invitingBehaviour(this));
        addBehaviour(new playingBehaviour(this));
    }

    class invitingBehaviour extends CyclicBehaviour {
        ACLMessage msg = receive();
        public invitingBehaviour(Agent a) { super(a); }
        public void action() {
            if (step == 0) {
                ACLMessage m = new ACLMessage(ACLMessage.INFORM);
                m.setContent("Let's play Othello!");
                m.addReceiver(new AID("tac", AID.ISLOCALNAME));
                send(m);
                block(500);
                msg = receive();
                if (msg != null && msg.getContent().contains("Okay")) {
                    step = 1;
                    // black starts
                    if (othello.hasAnyValidMove(1)) {
                        setTurn(true);
                        ticGui.activateValidMoves(1);
                    } else {
                        // if black has no moves (rare), send PASS
                        ACLMessage pm = new ACLMessage(ACLMessage.INFORM);
                        pm.setContent("PASS");
                        pm.addReceiver(new AID("tac", AID.ISLOCALNAME));
                        send(pm);
                    }
                }
            }
        }
    }

    class playingBehaviour extends CyclicBehaviour {
        ACLMessage msg = receive();
        public playingBehaviour(Agent a) { super(a); }
        public void action() {
            if (step == 1 && !isTurn()) {
                msg = receive();
                if (msg != null && !msg.getContent().equals(lastMsg)) {
                    lastMsg = msg.getContent();
                    String content = msg.getContent();
                    if (content.equals("PASS")) {
                        // opponent passed -> our turn if we have moves
                        if (othello.hasAnyValidMove(1)) {
                            setTurn(true);
                            ticGui.activateValidMoves(1);
                        } else {
                            // both cannot move -> end
                            endGame();
                        }
                    } else {
                        int r = Character.getNumericValue(content.charAt(0)) - 1;
                        int c = Character.getNumericValue(content.charAt(1)) - 1;
                        // apply opponent move (white=0)
                        othello.applyMove(0, r, c);
                        ticGui.updateBoardVisual(othello.board);
                        if (othello.hasAnyValidMove(1)) {
                            setTurn(true);
                            ticGui.activateValidMoves(1);
                        } else {
                            // black has no moves -> send PASS back
                            ACLMessage m = new ACLMessage(ACLMessage.INFORM);
                            m.setContent("PASS");
                            m.addReceiver(new AID("tac", AID.ISLOCALNAME));
                            send(m);
                        }
                    }
                }
            }
        }
    }

    boolean isTurn(){ return turn; }
    void setTurn(boolean b){ turn = b; }

    // called on button click in GUI
    void updateBoard(String content) {
        String s = content;
        if (s.length() > 2) {
            s = "" + s.charAt(s.length()-2) + s.charAt(s.length()-1);
        }
        int r = Character.getNumericValue(s.charAt(0)) - 1;
        int c = Character.getNumericValue(s.charAt(1)) - 1;
        // apply black (1)
        othello.applyMove(1, r, c);
        ticGui.updateBoardVisual(othello.board);
        setTurn(false);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("" + (r+1) + (c+1));
        msg.addReceiver(new AID("tac", AID.ISLOCALNAME));
        send(msg);

        ticGui.deactivateAll();
    }

    void endGame(){
        int[] cnt = othello.countPieces();
        System.out.println("Game over. Black: " + cnt[0] + " White: " + cnt[1]);
        ticGui.notifyUser("Game over. Black: "+cnt[0]+" White: "+cnt[1]);
    }
}