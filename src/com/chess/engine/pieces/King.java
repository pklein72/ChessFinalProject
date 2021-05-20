package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
//import com.chess.engine.board.AttackMove;
import com.chess.engine.board.AttackMove.MajorAttackMove;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.MajorMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
//import com.chess.engine.pieces.Piece.PieceType;

public class King extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE= {-9, -8, -7, -1, 1, 7, 8, 9};
	
	public King(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.KING,piecePosition, pieceAlliance, true);
		// TODO Auto-generated constructor stub
	}

	public King(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.KING, piecePosition, pieceAlliance, isFirstMove);
	}
	
	@Override
	public Collection<Move> calculatedLegalMoves(Board board) {
		// TODO Auto-generated method stub
		
		final List<Move> legalMoves=new ArrayList<>();
		for(final int currentCandOff:CANDIDATE_MOVE_COORDINATE) {
			final int candidateDestCoor=this.piecePosition+currentCandOff;
			if (isFirstColumnExclusion(this.piecePosition, currentCandOff)||
				isEighthColumnExclusion(this.piecePosition, currentCandOff)) {
				continue;
			}
			if(BoardUtils.isValidTileCoordinate(candidateDestCoor)) {
				final Tile candidateDestTile=board.getTile(candidateDestCoor);
				if(!candidateDestTile.isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestCoor));
				} else {
					final Piece pieceAtDest=candidateDestTile.getPiece();
					final Alliance pieceAlliance=pieceAtDest.getPieceAlliance();
					if(this.pieceAlliance!=pieceAlliance)	{
						legalMoves.add(new MajorAttackMove(board, this, candidateDestCoor, pieceAtDest));
					}
				}				
			}
		}
		
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.KING.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPos]&&(candidateOffset==-9||
				candidateOffset==-1||candidateOffset==7);
	}
	private static boolean isEighthColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPos]&&(candidateOffset==-7||candidateOffset==1||
				candidateOffset==9);
	}
	@Override
	public King movePiece(Move move) {
		// TODO Auto-generated method stub
		return new King(move.getMovedPiece().getPieceAlliance(), move.getDestCoor());
	}

}
