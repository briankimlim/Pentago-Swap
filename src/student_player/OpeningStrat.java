package student_player;

import boardgame.Move;

import pentago_swap.PentagoPlayer;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoBoardState.Quadrant;

public class OpeningStrat {
	
	public static Move openingStrategy(PentagoBoardState boardState, int player) {
		
	    Move firstMoveOne = new PentagoMove(1, 0, Quadrant.BL, Quadrant.BR, player);
	    Move firstMoveTwo = new PentagoMove(1, 3, Quadrant.BL, Quadrant.BR, player);
	    Move moveThree = new PentagoMove(0, 1, Quadrant.BL, Quadrant.TR, player);
	    Move moveFour = new PentagoMove(0, 4, Quadrant.TL, Quadrant.BR, player);
	    Move moveFive = new PentagoMove(3, 1, Quadrant.BL, Quadrant.TL, player);
	    Move moveSix = new PentagoMove(3, 4, Quadrant.TL, Quadrant.TR, player);
	    Move moveSeven =  new PentagoMove(2, 2, Quadrant.TL, Quadrant.TR, player);
	    Move moveEight = new PentagoMove(2, 5, Quadrant.TL, Quadrant.BR, player);
	    Move moveNine = new PentagoMove(5, 2, Quadrant.BL, Quadrant.TL, player);
	    Move moveTen = new PentagoMove(5, 5, Quadrant.TL, Quadrant.TR, player);
	    
	    boardState.getPieceAt(1, 0).toString().equals("WHITE");
	    if(boardState.getTurnNumber() == 0 || boardState.getTurnNumber() == 1) {
	        if(boardState.isLegal((PentagoMove) firstMoveOne)) {
	        	return firstMoveOne;
	        }
	        else if(boardState.isLegal((PentagoMove) firstMoveTwo)) {
	        	return firstMoveTwo;
	        }
	        else {
	        	System.out.println("random move at turn "+boardState.getTurnNumber() +".\n"); //TODO
	        	return boardState.getRandomMove();
	        }
	    }
	    else if(boardState.getTurnNumber() == 2 || boardState.getTurnNumber() == 3) {
	    	if(player==0) {
		    	if(boardState.isLegal((PentagoMove) moveThree) && boardState.getPieceAt(1, 0).toString().equals("w")) {
		        	return moveThree;
		        }
		    	if(boardState.isLegal((PentagoMove) moveFour) && boardState.getPieceAt(1, 3).toString().equals("w")) {
		    		return moveFour;
		    	}
		    	if(boardState.isLegal((PentagoMove) moveFive) && boardState.getPieceAt(4, 0).toString().equals("w")) {
		        	return moveFive;
		        }
		    	if(boardState.isLegal((PentagoMove) moveSix) && boardState.getPieceAt(4, 3).toString().equals("w")) {
		    		return moveSix;
		    	}
		    	if(boardState.getPieceAt(0, 1).toString().equals("b") && boardState.getPieceAt(1, 0).toString().equals("w")	
		    			&& boardState.isLegal((PentagoMove) moveSeven)) {
		        	return moveSeven;
		        }
		    	if(boardState.getPieceAt(0, 4).toString().equals("b") && boardState.getPieceAt(1, 3).toString().equals("w")
		    			&& boardState.isLegal((PentagoMove) moveEight)) {
		    		return moveEight;
		    	}
		    	if(boardState.getPieceAt(3, 1).toString().equals("b") && boardState.getPieceAt(4, 0).toString().equals("w")
		    			&& boardState.isLegal((PentagoMove) moveNine)) {
		        	return moveNine;
		        }
		    	if(boardState.getPieceAt(3, 4).toString().equals("b") && boardState.getPieceAt(4, 3).toString().equals("w")
		    			&& boardState.isLegal((PentagoMove) moveTen)) {
		    		return moveTen;
		    	}
		        else {
		        	System.out.println("random move at turn "+boardState.getTurnNumber() +".\n"); //TODO
		        	return boardState.getRandomMove();
		        }
	    	} else {
	    		if(boardState.isLegal((PentagoMove) moveThree) && boardState.getPieceAt(1, 0).toString().equals("b")) {
		        	return moveThree;
		        }
	    		if(boardState.isLegal((PentagoMove) moveFour) && boardState.getPieceAt(1, 3).toString().equals("b")) {
		        	return moveFour;
		        }
	    		if(boardState.isLegal((PentagoMove) moveFive) && boardState.getPieceAt(4, 0).toString().equals("b")) {
		        	return moveFive;
		        }
	    		if(boardState.isLegal((PentagoMove) moveSix) && boardState.getPieceAt(4, 3).toString().equals("b")) {
		        	return moveSix;
		        }
	    		if(boardState.getPieceAt(0, 1).toString().equals("w") && boardState.getPieceAt(1, 0).toString().equals("b")
	    				&& boardState.isLegal((PentagoMove) moveSeven)) {
		        	return moveSeven;
		        }
		    	if(boardState.getPieceAt(0, 4).toString().equals("w") && boardState.getPieceAt(1, 3).toString().equals("b")
		    			&& boardState.isLegal((PentagoMove) moveEight)) {
		    		return moveEight;
		    	}
		    	if(boardState.getPieceAt(3, 1).toString().equals("w") && boardState.getPieceAt(4, 0).toString().equals("b")
		    			&& boardState.isLegal((PentagoMove) moveNine)) {
		        	return moveNine;
		        }
		    	if(boardState.getPieceAt(3, 4).toString().equals("w") && boardState.getPieceAt(4, 3).toString().equals("b")
		    			&& boardState.isLegal((PentagoMove) moveTen)) {
		    		return moveTen;
		    	}
		        else {
		        	System.out.println("random move at turn "+boardState.getTurnNumber() +".\n"); //TODO
		        	return boardState.getRandomMove();
		        }
	    	}	   
	    }
//	    else if(boardState.getTurnNumber() == 4 || boardState.getTurnNumber() == 5) {
//	    	if(player==0) {
//		    	if(boardState.isLegal((PentagoMove) moveThree) && boardState.getPieceAt(1, 0).toString().equals("w")) {
//		        	return moveThree;
//		        }
//		    	if(boardState.isLegal((PentagoMove) moveFour) && boardState.getPieceAt(1, 3).toString().equals("w")) {
//		    		return moveFour;
//		    	}
//		    	if(boardState.isLegal((PentagoMove) moveFive) && boardState.getPieceAt(4, 0).toString().equals("w")) {
//		        	return moveFive;
//		        }
//		    	if(boardState.isLegal((PentagoMove) moveSix) && boardState.getPieceAt(4, 3).toString().equals("w")) {
//		    		return moveSix;
//		    	}
//		        else {
//		        	System.out.println("random move at turn "+boardState.getTurnNumber() +".\n"); //TODO
//		        	return boardState.getRandomMove();
//		        }
//	    	} else {
//	    		if(boardState.isLegal((PentagoMove) moveThree) && boardState.getPieceAt(1, 0).toString().equals("b")) {
//		        	return moveThree;
//		        }
//	    		if(boardState.isLegal((PentagoMove) moveFour) && boardState.getPieceAt(1, 3).toString().equals("b")) {
//		        	return moveFour;
//		        }
//	    		if(boardState.isLegal((PentagoMove) moveFive) && boardState.getPieceAt(4, 0).toString().equals("b")) {
//		        	return moveFive;
//		        }
//	    		if(boardState.isLegal((PentagoMove) moveSix) && boardState.getPieceAt(4, 3).toString().equals("b")) {
//		        	return moveSix;
//		        }
//		        else {
//		        	System.out.println("random move at turn "+boardState.getTurnNumber() +".\n"); //TODO
//		        	return boardState.getRandomMove();
//		        }
//	    	}	   
//	    }
//	    else if(boardState.getTurnNumber() == 4 || boardState.getTurnNumber() == 5 || boardState.getTurnNumber() == 6) {
//	    	return boardState.getRandomMove();
//	    }
	    else {   
//	    	Move myMove = MiniMax.miniMaxStrategy(boardState, boardState.getTurnPlayer());
	    	Move myMove = MiniMaxABPruning.miniMaxStrategy(boardState, boardState.getTurnPlayer());
//	    	Move myMove = MiniMaxABPruning.abPruningStrategy(boardState, boardState.getTurnPlayer());
			return myMove;
	    }
	}
}
