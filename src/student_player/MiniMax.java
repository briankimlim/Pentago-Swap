package student_player;

import java.util.ArrayList;

import boardgame.Board;
import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import pentago_swap.PentagoPlayer;

public class MiniMax {
	private static int maxDepth = 2;
	
	/**
	 * @param boardState
	 * @param currPlayer
	 * @return bestMove
	 */
	public static PentagoMove miniMaxStrategy (PentagoBoardState boardState, int currPlayer){
		PentagoMove bestMove = (PentagoMove) boardState.getRandomMove();
		int bestMoveUtility = 0;
		int someMoveUtility = 0;
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		
		ArrayList<PentagoMove> culledList = new ArrayList<PentagoMove>(legalMoves.subList(0, legalMoves.size()/(1)));
		
//		System.out.println("Branching factor: " + legalMoves.size()); //TODO
		System.out.println("Branching factor: " + culledList.size()); //TODO
//		for(PentagoMove someMove : legalMoves) {
		for(PentagoMove someMove : culledList) {
			PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
			clonedBoardState.processMove(someMove);
			someMoveUtility = evaluateBoard(boardState, currPlayer, 0, true);
//			System.out.println("Turn: "+boardState.getTurnNumber()+" utility: "+someMoveUtility); //TODO
			if (someMoveUtility > bestMoveUtility) {
				bestMoveUtility = someMoveUtility;
				bestMove = someMove;
//				System.out.println("Chosen move: "+bestMove.toPrettyString()+" utility: "+bestMoveUtility); //TODO
			}
			
			if(boardState.getTurnNumber() < 6) {
				return someMove;
			}
		}
		return bestMove;
	}
	
	public static int evaluateBoard(PentagoBoardState boardState, int currPlayer, int depth, boolean isMaximizer) {
		if (depth == maxDepth || boardState.gameOver()) {
//			return getUtility(boardState, currPlayer);
			return getUtility2(boardState, currPlayer);
		}
		
		if(isMaximizer) {
			int bestMaxValue = -100;
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				int someMoveValue = evaluateBoard(clonedBoardState, currPlayer, depth+1, false);
				if (someMoveValue > bestMaxValue) {
					bestMaxValue = someMoveValue;
				}
			}
			return bestMaxValue;
		}
		else {
			int bestMinValue = 100;
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				int someMoveValue = evaluateBoard(clonedBoardState, currPlayer, depth+1, true);
				if (someMoveValue < bestMinValue) {
					bestMinValue = someMoveValue;
				}
			}
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
	
	//TODO
	public static int getUtility2(PentagoBoardState boardState, int currPlayer) {
		if (boardState.getWinner() == currPlayer) {
			return 1;
		}

		else if (boardState.getWinner() == Board.DRAW) {
			return 0;
		}

		else return -1;
	}
	
	public static int getUtility(PentagoBoardState boardState, int currPlayer) {
		int utility = 0;
		int adjacencyBonus = 0;
		
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
