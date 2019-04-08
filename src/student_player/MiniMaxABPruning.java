package student_player;

import java.util.ArrayList;
import java.util.Date;

import pentago_swap.PentagoBoardState;
import pentago_swap.PentagoMove;
import boardgame.Board;;

public class MiniMaxABPruning {
private static int maxDepth = 2;
private static int alpha = -100;
private static int beta = 100;

private static int computeCounter = 0;
private static int maxLoopIndex = 0;
private static int minLoopIndex = 0;

private static long elapsedTime = 0L;
private static long startTime = 0L;
private static int TIME_LIMIT = 1800;

private static PentagoMove bestMove = null;
private static PentagoMove firstMove = null;

private static int bestUtilitySoFar = 0;

	/**
	 * @param boardState
	 * @param currPlayer
	 * @return bestMove
	 */
	public static PentagoMove miniMaxStrategy (PentagoBoardState boardState, int currPlayer){
		//PentagoMove bestMove = null;
		int bestMoveUtility = 0;
		int someMoveUtility = 0;
//		int ctr = 0;
		elapsedTime = 0L;
		startTime = System.currentTimeMillis();
	
		while(elapsedTime < TIME_LIMIT) {
			
//			if(elapsedTime >= TIME_LIMIT) {
//				break;
//			}
			
			if (boardState.getTurnNumber() > 8) {
				maxDepth = 3;
			}
			
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			
			firstMove = legalMoves.get(0);
			
	//		ArrayList<PentagoMove> culledList = new ArrayList<PentagoMove>(legalMoves.subList(0, legalMoves.size()/(1)));
	
	//		System.out.println("Branching factor: " + culledList.size());
	//		for(PentagoMove someMove : culledList) {
			System.out.println("Branching factor: " + legalMoves.size()); //TODO		
			for(PentagoMove someMove : legalMoves) {
				computeCounter = 0;
				elapsedTime = (new Date()).getTime() - startTime;
//				if (ctr == 0) {
//					firstMove = someMove;
//				}
	//			PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
	//			clonedBoardState.processMove(someMove);
	//			someMoveUtility = evaluateBoard(clonedBoardState, currPlayer, 0, true, alpha, beta);
				someMoveUtility = evaluateBoard(boardState, currPlayer, 0, true, alpha, beta, elapsedTime);
				elapsedTime = (new Date()).getTime() - startTime;
				
				if (someMoveUtility > 1337000) {
					if (bestMove == null) {
						System.out.println("1337code: firstmove");
						elapsedTime = 0L;
						return firstMove;
					} else {
						System.out.println("1337code: best-move");
						elapsedTime = 0L;
						return bestMove;
					}
				}
				
				if (someMoveUtility > bestMoveUtility) {
					bestMoveUtility = someMoveUtility;
					bestMove = someMove;
				}
				
//				ctr++;
	//			System.out.println("Node: "+ctr);
	//			System.out.println("Turn: "+boardState.getTurnNumber()+" utility: "+someMoveUtility); //TODO
	//			System.out.println("maxDepth = "+maxDepth+"\n"
	//							+"branching: "+ legalMoves.size() +"\n"
	//							+ " prune counter: "+computeCounter+"\n");
				
	//			if (bestMoveUtility > 9000) {
	//				break;
	//			}
			}
	//		System.out.println("Chosen move: "+bestMove.toPrettyString()+" utility: "+bestMoveUtility); //TODO

			
			if (bestMove == null) {
				System.out.println("Elapsed time: "+elapsedTime+"\nwhile break: firstmove");
				return firstMove;
			}
			System.out.println("Elapsed time: "+elapsedTime+"\nwhile break: BEST move");
			return bestMove;
		}
		System.err.println("Timeout: using first move");
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		return legalMoves.get(0);
	}
	
	public static int evaluateBoard(PentagoBoardState boardState, int currPlayer, int depth, boolean isMaximizer, int alpha, int beta, long elapsedTime) {
		 //int computeCounter = 0;
		
		if(elapsedTime > TIME_LIMIT) {
			System.err.println("Bad bad timeout code....");
			return 13371337;
		}
		
		if (depth == maxDepth || boardState.gameOver()) {
			return getUtility(boardState, currPlayer);
//			return getUtility2(boardState, currPlayer);
//			return getUtility3(boardState, currPlayer);
		}
		elapsedTime = (new Date()).getTime() - startTime;
		if(isMaximizer) {
			maxLoopIndex = 0;
			int bestMaxValue = -100;
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				maxLoopIndex++;
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				int someMoveValue = evaluateBoard(clonedBoardState, currPlayer, depth+1, false, alpha, beta, elapsedTime);
				
				if(elapsedTime > TIME_LIMIT) {
					System.err.println("break timeout code!");
					break;
				}
				
				bestMaxValue = Math.max(bestMaxValue, someMoveValue);
				alpha = Math.max(alpha, bestMaxValue);
				
				if(beta <= alpha) {
					computeCounter++;
					break;	
//					return bestMaxValue;
				}
			}
//			System.out.println("Total nodes: "+legalMoves.size()+"\n Min Nodes processed: "+maxLoopIndex);
//			System.out.println("\nalpha: "+alpha+" beta: "+beta);
			return bestMaxValue;
		}
		else {
			minLoopIndex = 0;
			int bestMinValue = 100;
			ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
			for(PentagoMove someMove: legalMoves) {
				minLoopIndex++;
				PentagoBoardState clonedBoardState = (PentagoBoardState) boardState.clone();
				clonedBoardState.processMove(someMove);
				int someMoveValue = evaluateBoard(clonedBoardState, currPlayer, depth+1, true, alpha, beta, elapsedTime);
				bestMinValue = Math.min(bestMinValue, someMoveValue);
				beta = Math.min(beta, bestMinValue);
				
				if(beta <= alpha) {
					computeCounter++;
					break;
//					return bestMinValue;
				}
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
				utility = -5000;
				return utility;
				}
		}
		
		// row adjacency check
		for(int i = 0; i < 6; i++) {
			elapsedTime = (new Date()).getTime() - startTime;
			
			//Diagonals adjacency checks
			if(i < 5) {
				if (currPlayer == 0) {
					
					// Top-left to bottom-right diagonal adjacency check
					if (boardState.getPieceAt(i, i).toString().equals("w")
							&& boardState.getPieceAt(i+1, i+1).toString().equals("w")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
					} 
					// 
					if (boardState.getPieceAt(i, 5-i).toString().equals("w")
							&& boardState.getPieceAt(i+1, 4-i).toString().equals("w")) {
					utility = calculateUtility(utility, adjacencyBonus);
					adjacencyBonus++;
					} 
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
				elapsedTime = (new Date()).getTime() - startTime;
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
//		elapsedTime = (new Date()).getTime() - startTime;
//		// column adjacency check
//		for(int j = 0; j < 6; j++) {
//			elapsedTime = (new Date()).getTime() - startTime;
//			for (int i = 0; i < 5; i++) {
//				elapsedTime = (new Date()).getTime() - startTime;
//				if (currPlayer == 0) {
//					if (boardState.getPieceAt(i, j).toString().equals("w")
//							&& boardState.getPieceAt(i+1, j).toString().equals("w")) {
//						utility = calculateUtility(utility, adjacencyBonus);
//						adjacencyBonus++;
//					} else {
//						adjacencyBonus = 0;
//					} 
//				}
//				else if(currPlayer == 1){
//					if (boardState.getPieceAt(i, j).toString().equals("b")
//							&& boardState.getPieceAt(i+1, j).toString().equals("b")) {
//						utility = calculateUtility(utility, adjacencyBonus);
//						adjacencyBonus++;
//					} else {
//						adjacencyBonus = 0;
//					}
//				}			
//			}
//		}
		
//		elapsedTime = (new Date()).getTime() - startTime;
//		// Top-left to bottom-right diagonal adjacency check
//		for(int i = 0; i < 5; i++) {
//			elapsedTime = (new Date()).getTime() - startTime;
//			if (currPlayer == 0) {
//				if (boardState.getPieceAt(i, i).toString().equals("w")
//							&& boardState.getPieceAt(i+1, i+1).toString().equals("w")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//				} 
//				if(i < 4) {
//					if (boardState.getPieceAt(i, i+1).toString().equals("w")
//							&& boardState.getPieceAt(i+1, i+2).toString().equals("w")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					} 
//					if (boardState.getPieceAt(i+1, i).toString().equals("w")
//							&& boardState.getPieceAt(i+2, i+1).toString().equals("w")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					} 
//				}
//				else {
//					adjacencyBonus = 0;
//				}
//			}
//			else if (currPlayer == 1) {
//				if (boardState.getPieceAt(i, i).toString().equals("b")
//						&& boardState.getPieceAt(i+1, i+1).toString().equals("b")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//				} 
//				if(i < 4) {
//					if (boardState.getPieceAt(i, i+1).toString().equals("b")
//							&& boardState.getPieceAt(i+1, i+2).toString().equals("b")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					} 
//					if (boardState.getPieceAt(i+1, i).toString().equals("b")
//							&& boardState.getPieceAt(i+2, i+1).toString().equals("b")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					} 
//				}
//				else {
//					adjacencyBonus = 0;
//				}
//			}
//		}
		
//		elapsedTime = (new Date()).getTime() - startTime;
//		// Top-right to bottom-left diagonal adjacency check
//		for(int i = 0; i < 5; i++) {
//			elapsedTime = (new Date()).getTime() - startTime;
//			if (currPlayer == 0) {
//				if (boardState.getPieceAt(i, 5-i).toString().equals("w")
//							&& boardState.getPieceAt(i+1, 4-i).toString().equals("w")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//				} 
//				if(i < 4) {				
//					if (boardState.getPieceAt(i, 4-i).toString().equals("w")
//							&& boardState.getPieceAt(i+1, 3-i).toString().equals("w")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					} 
//					if (boardState.getPieceAt(i+1, 5-i).toString().equals("w")
//							&& boardState.getPieceAt(i+2, 4-i).toString().equals("w")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					}								 
//				}
//				else {
//					adjacencyBonus = 0;
//				}
//			}
//			else if (currPlayer == 1) {
//				if (boardState.getPieceAt(i, 5-i).toString().equals("b")
//						&& boardState.getPieceAt(i+1, 4-i).toString().equals("b")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//				} 
//				if(i < 4) {				
//					if (boardState.getPieceAt(i, 4-i).toString().equals("b")
//							&& boardState.getPieceAt(i+1, 3-i).toString().equals("b")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					} 
//					if (boardState.getPieceAt(i+1, 5-i).toString().equals("b")
//							&& boardState.getPieceAt(i+2, 4-i).toString().equals("b")) {
//					utility = calculateUtility(utility, adjacencyBonus);
//					adjacencyBonus++;
//					}								 
//				}				
//				else {
//					adjacencyBonus = 0;
//				}
//			}
//		}
		
		return utility;
	}

	public static int getUtility2(PentagoBoardState boardState, int currPlayer) {
		if (boardState.getWinner() == currPlayer) {
			return 1;
		}

		else if (boardState.getWinner() == Board.DRAW) {
			return 0;
		}

		else return -1;
	}
	
	public static int getUtility3(PentagoBoardState boardState, int currPlayer) {
		int utility = 0;
		int adjacencyBonus = 0;
		
		if (boardState.gameOver()) {
			if (boardState.getWinner() == currPlayer) {
				utility = 9001;
				return utility;
			}
			else {
				utility = -5000;
				return utility;
				}
		}
		elapsedTime = (new Date()).getTime() - startTime;
		// row adjacency check
		for(int i = 0; i < 6; i++) {
			elapsedTime = (new Date()).getTime() - startTime;
			for(int j = 0; j < 5; j++) {
				elapsedTime = (new Date()).getTime() - startTime;
				if (currPlayer == 0) {
					if (boardState.getPieceAt(i, j).toString().equals("w")
							&& boardState.getPieceAt(i, j+1).toString().equals("w")) {
						utility = calculateUtility(utility, adjacencyBonus);
						adjacencyBonus++;
					} 
					
					else {
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
		elapsedTime = (new Date()).getTime() - startTime;
		// column adjacency check
		for(int j = 0; j < 6; j++) {
			elapsedTime = (new Date()).getTime() - startTime;
			for (int i = 0; i < 5; i++) {
				elapsedTime = (new Date()).getTime() - startTime;
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
		elapsedTime = (new Date()).getTime() - startTime;
		// Top-left to bottom-right diagonal adjacency check
		for(int i = 0; i < 5; i++) {
			elapsedTime = (new Date()).getTime() - startTime;
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
		elapsedTime = (new Date()).getTime() - startTime;
		// Top-right to bottom-left diagonal adjacency check
		for(int i = 0; i < 5; i++) {
			elapsedTime = (new Date()).getTime() - startTime;
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
		elapsedTime = (new Date()).getTime() - startTime;
		return utility;
	}
}
