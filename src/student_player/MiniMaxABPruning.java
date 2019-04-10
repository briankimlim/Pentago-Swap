package student_player;

import java.util.ArrayList;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import boardgame.Board;

public class MiniMaxABPruning {
	private int maxDepth = 2;
	private int alpha = -100;
	private int beta = 100;
	
	/* Custom object used for storing a PentagoMove & its associated utility value.
	 */
	private class MoveObj {	
		//Object fields
	    private int value;
	    private PentagoMove move;
	    
	    //Public constructor
	    public MoveObj(int value) {
	        this.value = value;
	    }
	    
	    //Getters & Setters
	    public int getValue() {
	    	return this.value;
	    }
	    
	    public PentagoMove getMove() {
	    	return this.move;
	    }
	    
	    public void setValue(int value) {
	    	this.value = value;
	    }
	    
	    public void setMove(PentagoMove move) {
	    	this.move = move;
	    }
	}
	
	/** Returns the best move found using MiniMax alpha-beta pruning.
	 * @param boardState
	 * @param currPlayer
	 * @return PentagoMove to be played for the current turn
	 */
	public PentagoMove abPruningStrategy (PentagoBoardState boardState, int currPlayer){
		
		//start using maxDepth = 3 once enough turns have passed to safely be within 2000ms turn time limit
		if (boardState.getTurnNumber() > 7) {
			maxDepth = 3;
		}
		
		//Finds the best MoveObj using a-b pruning algorithm in evaluateBoardABPruning
		MoveObj bestMoveObj = evaluateBoardABPruning(boardState, currPlayer, 0, true, alpha, beta);
		
		//If the best move wasn't properly found, return the first legal move
		if (bestMoveObj.getMove() == null) {
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			return legalMoves.get(0);
		}
		return bestMoveObj.getMove();
	}
	
	/** Recursively finds the optimal move and its associated utility value using alpha-beta pruning.
	 * @param boardState
	 * @param currPlayer
	 * @param depth
	 * @param isMaximizer
	 * @param alpha
	 * @param beta
	 * @return bestMoveObj containing the optimal move and its associated utility value.
	 */
	private MoveObj evaluateBoardABPruning(PentagoBoardState boardState, int currPlayer, int depth, boolean isMaximizer, int alpha, int beta) {
		
		// terminating condition when we reach designated max depth or game over
		if (depth == maxDepth || boardState.gameOver()) {
			int value = getUtility(boardState, currPlayer);
			return new MoveObj(value);
		}

		//If the current call is the maximizer
		if(isMaximizer) {
			MoveObj bestMoveObj = new MoveObj(-100);
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				MoveObj someMoveObj = evaluateBoardABPruning(clonedBoardState, currPlayer, depth+1, false, alpha, beta);

				if (bestMoveObj.getValue() < someMoveObj.getValue()) {
					bestMoveObj = someMoveObj;
					bestMoveObj.setMove(someMove);
				}
				
				if (someMoveObj.getValue() > alpha) {
					alpha = someMoveObj.getValue();
				}
				
				//if true, pruning occurs
				if(beta <= alpha) {
					bestMoveObj.setValue(beta);
					bestMoveObj.setMove(null);
					return bestMoveObj;
				}
			}
			return bestMoveObj;
		}
		// minimizer
		else {
			MoveObj bestMoveObj = new MoveObj(100);
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				MoveObj someMoveObj = evaluateBoardABPruning(clonedBoardState, currPlayer, depth+1, true, alpha, beta);

				if (bestMoveObj.getValue() > someMoveObj.getValue()) {
					bestMoveObj = someMoveObj;
					bestMoveObj.setMove(someMove);
				}
				
				if (someMoveObj.getValue() < beta) {
					beta = someMoveObj.getValue();
				}
				
				//if true, pruning occurs
				if(beta <= alpha) {
					bestMoveObj.setValue(alpha);
					bestMoveObj.setMove(null);
					return bestMoveObj;
				}
			}
			return bestMoveObj;
		}
	}
	
	/** Used for tinkering with utility based on current utility & adjacencyBonus.
	 * @param utility
	 * @param adjacencyBonus
	 * @return utility value
	 */
	private int calculateUtility(int utility, int adjacencyBonus) {		
		if(adjacencyBonus == 0) {
			return (utility + 1);
		} 
		else {
			return (utility + adjacencyBonus + 1);
		}
	}
	
	/** Returns the utility of the board state configuration for the Player.
	 * @param boardState
	 * @param currPlayer
	 * @return utility value
	 */
	private int getUtility(PentagoBoardState boardState, int currPlayer) {
		int utility = 0;
		int adjacencyBonus = 0;
		
		/* If a board (win, loss, draw) is detected, award points accordingly.
		 */
		if (boardState.gameOver()) {
			if (boardState.getWinner() == currPlayer) {
				utility = 50;
				return utility;
			}
			else if (boardState.getWinner() == Board.DRAW) {
				utility = -5;
				return utility;
			}
			else {
				utility = -50;
				return utility;
				}
		}
		
		/* Utility points are awarded for having adjacent pieces of the Player's color
		 * in horizontal, vertical and diagonal orientations.
		 */
		for(int i = 0; i < 6; i++) {
			
			//Diagonals adjacency checks
			if(i < 5) {
				// White player
				if (currPlayer == 0) {
					
					// Top-left to bottom-right diagonal adjacency check
					if (boardState.getPieceAt(i, i).toString().equals("w")
							&& boardState.getPieceAt(i+1, i+1).toString().equals("w")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
					} 
					// Top-right to bottom-left diagonal adjacency check
					if (boardState.getPieceAt(i, 5-i).toString().equals("w")
							&& boardState.getPieceAt(i+1, 4-i).toString().equals("w")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
					}
					//Makes sure we stay within Board's bounds
					if(i < 4) {
						// Top-left to bottom-right diagonal adjacency check
						if (boardState.getPieceAt(i, i+1).toString().equals("w")
								&& boardState.getPieceAt(i+1, i+2).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						} 
						if (boardState.getPieceAt(i+1, i).toString().equals("w")
								&& boardState.getPieceAt(i+2, i+1).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						}
						
						// Top-right to bottom-left diagonal adjacency check
						if (boardState.getPieceAt(i, 4-i).toString().equals("w")
								&& boardState.getPieceAt(i+1, 3-i).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						} 
						if (boardState.getPieceAt(i+1, 5-i).toString().equals("w")
								&& boardState.getPieceAt(i+2, 4-i).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						}
					}				
					else {
						adjacencyBonus = 0;
					}
				} 
				else if (currPlayer == 1) {
					
					// Top-left to bottom-right diagonal adjacency check
					if (boardState.getPieceAt(i, i).toString().equals("b")
							&& boardState.getPieceAt(i+1, i+1).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} 
					
					// Top-right to bottom-left diagonal adjacency check
					if (boardState.getPieceAt(i, 5-i).toString().equals("b")
							&& boardState.getPieceAt(i+1, 4-i).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					}
					//Makes sure we stay within Board's bounds
					if(i < 4) {
						// Top-left to bottom-right diagonal adjacency check
						if (boardState.getPieceAt(i, i+1).toString().equals("b")
								&& boardState.getPieceAt(i+1, i+2).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						} 
						if (boardState.getPieceAt(i+1, i).toString().equals("b")
								&& boardState.getPieceAt(i+2, i+1).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						}
						
						// Top-right to bottom-left diagonal adjacency check
						if (boardState.getPieceAt(i, 4-i).toString().equals("b")
								&& boardState.getPieceAt(i+1, 3-i).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						} 
						if (boardState.getPieceAt(i+1, 5-i).toString().equals("b")
								&& boardState.getPieceAt(i+2, 4-i).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
						}	
					}
					else {
						adjacencyBonus = 0;
					}
				}
			}
			
			for(int j = 0; j < 5; j++) {
				// White player
				if (currPlayer == 0) {
					//row adjacency check
					if (boardState.getPieceAt(i, j).toString().equals("w")
							&& boardState.getPieceAt(i, j+1).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} 	
					
					//column adjacency check
					if (boardState.getPieceAt(j, i).toString().equals("w")
							&& boardState.getPieceAt(j+1, i).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} 
					else {
						adjacencyBonus = 0;
					} 
				}
				// Black player
				else if (currPlayer == 1){
					//row adjacency check
					if (boardState.getPieceAt(i, j).toString().equals("b")
							&& boardState.getPieceAt(i, j+1).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} 
					
					//column adjacency check
					if (boardState.getPieceAt(j, i).toString().equals("b")
							&& boardState.getPieceAt(j+1, i).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} 
					else {
						adjacencyBonus = 0;
					}
				}
			}
		}
		return utility;
	}

	/** Simple utility calculation function, not used by default.
	 * @param boardState
	 * @param currPlayer
	 * @return utility value
	 */
	private int getUtilitySimple(PentagoBoardState boardState, int currPlayer) {
		//player win
		if (boardState.getWinner() == currPlayer) {
			return 1;
		}
		//draw
		else if (boardState.getWinner() == Board.DRAW) {
			return 0;
		} 
		//player loss
		else {
			return -1;
		}
	}
}
