package uk.ac.bris.cs.scotlandyard.ui.ai;

import java.util.Set;
import java.util.function.Consumer;

import uk.ac.bris.cs.scotlandyard.ai.ManagedAI;
import uk.ac.bris.cs.scotlandyard.ai.PlayerFactory;
import uk.ac.bris.cs.scotlandyard.model.*;
import uk.ac.bris.cs.scotlandyard.model.Player;

// TODO name the AI
@ManagedAI("Neloo")
public class MyAI implements PlayerFactory {


    // TODO create a new player here
    @Override
    public Player createPlayer(Colour colour) {
        return new MyPlayer();
    }

    // TODO A sample player that selects a random move
    private static class MyPlayer implements Player {

        //private final Random random = new Random();

        Move bestMove;

        @Override
        public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                             Consumer<Move> callback) {
            // TODO do something interesting here; find the best move
            // picks a random move

            ScotMask mask = new ScotMask(view);
            mask.setXLocation(location);
            minimax(2,true, mask, Integer.MIN_VALUE, Integer.MAX_VALUE, moves, location);
            callback.accept(bestMove);

        }

        /*private Move getBestMove(Set<Move> moves, ScotlandYardView view) {
            Move best = new PassMove(Colour.BLACK);
            int bestScore = 0;
            for (Move move : moves) {
                ScotMask mask = new ScotMask(view);
                if (move instanceof TicketMove) {
                    mask.makeMove(move, Colour.BLACK);
                    int score = new Score().getMrXscore(mask);
                    if (score > bestScore) {
                        bestScore = score;
                        best = move;
                    }
                }
            }
            return best;
        }*/

        private Move getDetectiveBestMove(Set<Move> moves, ScotlandYardView view, Colour detective, int Xloc) {
            Move best = new PassMove(detective);
            int bestScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                ScotMask mask = new ScotMask(view);

                mask.makeMove(move, detective);
                int score = new Score().getDetectiveScore(mask, Xloc, detective);

                if (score < bestScore) {
                    bestScore = score;
                    best = move;
                }

            }
            return best;
        }


        private int minimax(int depth, boolean MrXTurns, ScotlandYardView view, int alpha, int beta, Set<Move> nextMovesfromNode, int Xloc) {
                Score score= new Score();
                ScotMask mask = new ScotMask(view);
                mask.setCurrentPlayer(MrXTurns);
                int eval;
                if(depth==0 || view.isGameOver())
                    return score.getMrXscore(mask);
                if(MrXTurns) {
                    int maxEval = Integer.MIN_VALUE;
                    for (Move move : nextMovesfromNode) {
                        mask.makeMove(move, Colour.BLACK);
                        eval = minimax(depth - 1, false, mask, alpha, beta, null, mask.getMrX().getLocation());
                        maxEval = Integer.max(maxEval, eval);
                        if (eval > alpha) {
                            alpha = eval;
                            bestMove = move;
                        }
                        if (beta <= alpha)
                            break;
                    }
                    return alpha;
                }
                else {
                    int minEval = 0;
                    for(uk.ac.bris.cs.scotlandyard.ui.ai.Player p : mask.getDetectives()) {

                        mask.makeMove(getDetectiveBestMove(mask.getValidMoves(p.getColour()), mask, p.getColour(), Xloc), p.getColour());
                        minEval += new Score().getDetectiveScore(mask, Xloc, p.getColour());

                    }
                    if(minEval < beta)
                        beta = minEval;
                    return minimax(depth - 1, true, mask, alpha, beta, mask.getValidMoves(Colour.BLACK), mask.getMrX().getLocation());
                }
        }

    }
}
