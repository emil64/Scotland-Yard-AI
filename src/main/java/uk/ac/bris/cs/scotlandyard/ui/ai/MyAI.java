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


        @Override
        public void makeMove(ScotlandYardView view, int location, Set<Move> moves,
                             Consumer<Move> callback) {
            // TODO do something interesting here; find the best move
            // picks a random move

            ScotMask mask = new ScotMask(view);
            mask.setXLocation(location);
            minimax(5,true, mask, Integer.MIN_VALUE, Integer.MAX_VALUE, moves);
            callback.accept(getBestMove(moves, view));

        }

        private Move getBestMove(Set<Move> moves, ScotlandYardView view) {
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
        }

        private int minimax(int depth, boolean MrXTurns, ScotlandYardView view, int alpha, int beta, Set<Move> nextMovesfromNode) {
                Score score= new Score();
                ScotMask mask = new ScotMask(view);
                int eval = 0;
                if(depth==0 || view.isGameOver())
                    return score.getMrXscore(mask);
                if(MrXTurns) {
                    int maxEval = Integer.MIN_VALUE;
                    for (Move move : nextMovesfromNode) {
                        eval = minimax(depth - 1, false, mask, alpha, beta, mask.getValidMoves(Colour.BLACK));
                        maxEval = Integer.max(maxEval, eval);
                    }
                    return maxEval;
                }
                else {
                    return 0;
                    /*int minEval = Integer.MAX_VALUE;
                    //for( )
                    for (Move move : nextMovesfromNode) {
                        eval = minimax(depth - 1, true, view, alpha, beta, mask.getValidMoves(Colour.BLACK));
                        minEval = Integer.min(minEval, eval);
                    }
                    return minEval;*/
                }
        }

    }
}
