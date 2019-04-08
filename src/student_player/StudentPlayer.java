package student_player;

import boardgame.Move;
import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    //student ID
    public StudentPlayer() {
        super("260636766");
    }

    public Move chooseMove(PentagoBoardState boardState) {
    	//Chosen moves first consist of an opening strategy, and then runs MiniMax with a-b pruning
    	return OpeningStrat.openingStrategy(boardState, boardState.getTurnPlayer());
    }
}