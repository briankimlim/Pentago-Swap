package student_player;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Quadrant;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

    //student ID
    public StudentPlayer() {
        super("260636766");
    }

    public Move chooseMove(PentagoBoardState boardState) {
    	//Moves first consist of an opening strategy, and then runs MiniMax with a-b pruning
    	long startTime = System.currentTimeMillis();
    	Move myMove = OpeningStrat.openingStrategy(boardState, boardState.getTurnPlayer());
    	long endTime = System.currentTimeMillis();
    	System.out.println(myMove.toPrettyString() + " Turn: "+boardState.getTurnNumber() + " Took "+(endTime - startTime)+" ms");
        
        // Return  move to be processed by the server.
        return myMove;
    }
}