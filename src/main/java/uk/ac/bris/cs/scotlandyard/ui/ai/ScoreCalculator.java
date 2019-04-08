package uk.ac.bris.cs.scotlandyard.ui.ai;

import uk.ac.bris.cs.scotlandyard.model.ScotlandYardView;

public interface ScoreCalculator {

    public int getMrXscore(ScotMask mask, int mrXLocation);

    public int getDetectiveScore(ScotMask mask);
}
