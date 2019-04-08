package student_player;

import java.util.ArrayList;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;

public class AMiniMaxABPruningWIP {
private static int maxDepth = 2;
private static int alpha = -100;
private static int beta = 100;

private static int computeCounter = 0;
private static int maxLoopIndex = 0;
private static int minLoopIndex = 0;
private static int recursionCounter = 0;

private static PentagoMove firstMove = null;
private static PentagoMove bestMove = null;
private static int bestMoveUtility = 0;

	public static PentagoMove getBestMove() {
		return bestMove;
	}

	/**
	 * @param boardState
	 * @param currPlayer
	 * @return bestMove
	 */
	public static PentagoMove miniMaxStrategy (PentagoBoardState boardState, int currPlayer){
		//PentagoMove bestMove = null;
		int bestMoveUtility = 0;
		int someMoveUtility = 0;
		
		if (boardState.getTurnNumber() > 6) {
			maxDepth = 3;
		}
		
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		
//		ArrayList<PentagoMove> culledList = new ArrayList<PentagoMove>(legalMoves.subList(0, legalMoves.size()/(1)));

//		System.out.println("Branching factor: " + culledList.size());
//		for(PentagoMove someMove : culledList) {
		System.out.println("Branching factor: " + legalMoves.size()); //TODO		
		for(PentagoMove someMove : legalMoves) {
			computeCounter = 0;
			PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
			clonedBoardState.processMove(someMove);
			someMoveUtility = evaluateBoard(clonedBoardState, currPlayer, 0, true, alpha, beta);
			//System.out.println("\n Turn: "+boardState.getTurnNumber()+" utility: "+someMoveUtility); //TODO
			if (someMoveUtility > bestMoveUtility) {
				bestMoveUtility = someMoveUtility;
				bestMove = someMove;
			}

			System.out.println("maxDepth = "+maxDepth+"\n"
							+"branching: "+ legalMoves.size() +"\n"
							+ " prune counter: "+computeCounter+"\n");
		}
		System.out.println("Chosen move: "+bestMove.toPrettyString()+" utility: "+bestMoveUtility); //TODO
		
		if (bestMove == null) {
			bestMove = (PentagoMove) boardState.getRandomMove();
			return bestMove;
		}
		return bestMove;
		
	}
	
	public static PentagoMove abPruningStrategy (PentagoBoardState boardState, int currPlayer){
		recursionCounter = 0;
		evaluateBoard(boardState, currPlayer, 0, true, alpha, beta);
		if (bestMove == null && boardState.isLegal(firstMove)) {
			return firstMove;
		} 
		return bestMove;
	}
	
	public static int evaluateBoard(PentagoBoardState boardState, int currPlayer, int depth, boolean isMaximizer, int alpha, int beta) {
		
		if (boardState.getTurnNumber() > 6) {
			maxDepth = 3;
		}
		 
		if (depth == maxDepth) {
			return getUtility(boardState, currPlayer);
		}
		
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		if(recursionCounter == 0) {
			firstMove = legalMoves.get(0);
		}
		recursionCounter++;
		
		if(isMaximizer) {
			maxLoopIndex = 0;
			int bestMaxValue = -100;
			
			for(PentagoMove someMove: legalMoves) {				
				maxLoopIndex++;
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				int someMoveValue = evaluateBoard(clonedBoardState, boardState.getOpponent(), depth+1, false, alpha, beta);
				//bestMaxValue = Math.max(bestMaxValue, someMoveValue);
				if (bestMaxValue < someMoveValue) {
					bestMaxValue = someMoveValue;
					bestMove = someMove;
				}
				
				alpha = Math.max(alpha, bestMaxValue);
				
//				if (bestMaxValue > bestMoveUtility) {
//					bestMoveUtility = bestMaxValue;
//					bestMove = someMove;
//				}
				if(alpha >= beta) {
					computeCounter++;
					break;			
				}
			}
//			System.out.println("Total nodes: "+legalMoves.size()+"\n Min Nodes processed: "+maxLoopIndex);
			
			return bestMaxValue;
		}
		else {
			minLoopIndex = 0;
			int bestMinValue = 100;
			//ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				minLoopIndex++;
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				int someMoveValue = evaluateBoard(clonedBoardState, boardState.getOpponent(), depth+1, true, alpha, beta);
				bestMinValue = Math.min(bestMinValue, someMoveValue);
				beta = Math.min(beta, bestMinValue);
				
				if(alpha >= beta) {
					computeCounter++;
					break;
				}
//				if (someMoveValue < bestMinValue) {
//					bestMinValue = someMoveValue;
//				}
			}
//			System.out.println("Total nodes: "+legalMoves.size()+"\n Min Nodes processed: "+minLoopIndex);
			return bestMinValue;
		}
	}
	
	public static int calculateUtility(int utility, int adjacencyBonus) {		
		if(adjacencyBonus == 0) {
			return utility++;
		} 
		else {
			return (utility + adjacencyBonus + 1);
		}
	}
	
	public static int getUtility(PentagoBoardState boardState, int currPlayer) {
		int utility = 0;
		int adjacencyBonus = 0;
		
		if (boardState.gameOver()) {
			if (boardState.getWinner() == currPlayer) {
				utility = 9001;
				return utility;
			}
			else {
				utility = -1000;
				return utility;
				}
		}
		
		// row adjacency check
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 5; j++) {
				if (currPlayer == 0) {
					if (boardState.getPieceAt(i, j).toString().equals("w")
							&& boardState.getPieceAt(i, j+1).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} else {
						adjacencyBonus = 0;
					} 
				} 
				else if (currPlayer == 1){
					if (boardState.getPieceAt(i, j).toString().equals("b")
							&& boardState.getPieceAt(i, j+1).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} else {
						adjacencyBonus = 0;
					} 
				}
			}
		}
		
		// column adjacency check
		for(int j = 0; j < 6; j++) {
			for (int i = 0; i < 5; i++) {
				if (currPlayer == 0) {
					if (boardState.getPieceAt(i, j).toString().equals("w")
							&& boardState.getPieceAt(i+1, j).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} else {
						adjacencyBonus = 0;
					} 
				}
				else if(currPlayer == 1){
					if (boardState.getPieceAt(i, j).toString().equals("b")
							&& boardState.getPieceAt(i+1, j).toString().equals("b")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} else {
						adjacencyBonus = 0;
					}
				}				
			}
		}
		
		// Top-left to bottom-right diagonal adjacency check
		for(int i = 0; i < 5; i++) {
			if (currPlayer == 0) {
				if (boardState.getPieceAt(i, i).toString().equals("w")
							&& boardState.getPieceAt(i+1, i+1).toString().equals("w")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
				} 
				if(i < 4) {
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
				}
				else {
					adjacencyBonus = 0;
				}
			}
			else if (currPlayer == 1) {
				if (boardState.getPieceAt(i, i).toString().equals("b")
						&& boardState.getPieceAt(i+1, i+1).toString().equals("b")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
				} else {
					adjacencyBonus = 0;
				}
			}
		}
		
		// Top-right to bottom-left diagonal adjacency check
		for(int i = 0; i < 5; i++) {
			if (currPlayer == 0) {
				if (boardState.getPieceAt(i, 5-i).toString().equals("w")
							&& boardState.getPieceAt(i+1, 4-i).toString().equals("w")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
				} 
				if(i < 4) {				
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
				if (boardState.getPieceAt(i, 5-i).toString().equals("b")
						&& boardState.getPieceAt(i+1, 4-i).toString().equals("b")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
				} 
				if(i < 4) {				
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
		
		return utility;
	}
}
