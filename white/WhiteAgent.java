package othello.white;
import othello.common.OthelloBoard;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

public class WhiteAgent extends Agent {
    private WhiteAgentGui whiteGui;
    public OthelloBoard othello;
    boolean turn = false; // white starts second (black first)
    int step = 0;
    String lastMsg = "";

    protected void setup() {
        System.out.println("White-agent "+getAID().getName()+" (WHITE) is ready.");
        othello = new OthelloBoard();

        // Show the GUI to interact with the user
        whiteGui = new WhiteAgentGuiImpl();
        whiteGui.setAgent(this);
        whiteGui.showGUI();
        whiteGui.updateBoardVisual(othello.board);

        // behaviors
        addBehaviour(new waitingBehaviour(this));
        addBehaviour(new playingBehaviour(this));
    }

    class waitingBehaviour extends CyclicBehaviour {
        ACLMessage msg = receive();
        public waitingBehaviour(Agent a) { super(a); }
        public void action() {
            if (step == 0) {
                msg = receive();
                if (msg != null) {
                    lastMsg = msg.getContent();
                    // reply Okay to invitation
                    ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                    reply.setContent("Okay");
                    reply.addReceiver(new AID("black", AID.ISLOCALNAME));
                    send(reply);
                    step = 1; // enter play stage
                    // White initially not turn; black will start
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
                        // opponent passed -> now it's our turn if we have moves
                        if (othello.hasAnyValidMove(0)) {
                            setTurn(true);
                            whiteGui.activateValidMoves(0);
                        } else {
                            // both can't move -> game over
                            endGame();
                        }
                    } else {
                        // normal move: two chars "rc" 1-based
                        int r = Character.getNumericValue(content.charAt(0)) - 1;
                        int c = Character.getNumericValue(content.charAt(1)) - 1;
                        // apply opponent's move (black=1)
                        othello.applyMove(1, r, c);
                        whiteGui.updateBoardVisual(othello.board);
                        // check if we have valid moves
                        if (othello.hasAnyValidMove(0)) {
                            setTurn(true);
                            whiteGui.activateValidMoves(0);
                        } else {
                            // if white has no moves, send PASS back
                            ACLMessage m = new ACLMessage(ACLMessage.INFORM);
                            m.setContent("PASS");
                            m.addReceiver(new AID("black", AID.ISLOCALNAME));
                            send(m);
                        }
                    }
                }
            }
        }
    }

    boolean isTurn(){ return turn; }
    void setTurn(boolean b){ turn = b; }

    // called when local human clicks a valid button
    void updateBoard(String content){
        // content expected "rc" (2 chars) or button name that encodes r and c
        // we'll accept either 2-char string or "Brc"
        String s = content;
        if (s.length() > 2) {
            // assume last two chars are row/col digits e.g. B34 -> '3' '4'
            s = "" + s.charAt(s.length()-2) + s.charAt(s.length()-1);
        }
        int r = Character.getNumericValue(s.charAt(0)) - 1;
        int c = Character.getNumericValue(s.charAt(1)) - 1;
        // apply white (0)
        othello.applyMove(0, r, c);
        whiteGui.updateBoardVisual(othello.board);
        setTurn(false);

        // send move to opponent
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setContent("" + (r+1) + (c+1));
        msg.addReceiver(new AID("black", AID.ISLOCALNAME));
        send(msg);

        // after move, check whether opponent has any move. If not, they will reply PASS and we handle that.
        // disable our buttons
        whiteGui.deactivateAll();
    }

    void endGame(){
        int[] cnt = othello.countPieces();
        System.out.println("Game over. Black: " + cnt[0] + " White: " + cnt[1]);
        whiteGui.notifyUser("Game over. Black: "+cnt[0]+" White: "+cnt[1]);
    }
}