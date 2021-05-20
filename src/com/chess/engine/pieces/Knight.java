package com.chess.engine.pieces;

import com.chess.engine.Alliance;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import com.chess.engine.board.*;
//import com.chess.engine.pieces.Piece.PieceType;
import com.chess.engine.board.AttackMove.MajorAttackMove;



public class Knight extends Piece{

	private final static int[] CANDIDATE_MOVE_COORDINATES = {-17,-15,-10,-6,6,10,15,17};
	
	public Knight(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.KNIGHT,piecePosition, pieceAlliance, true);
		// TODO Auto-generated constructor stub
	}

	public Knight(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculatedLegalMoves(final Board board) {
		// TODO Auto-generated method stub
		final List<Move> legalMoves=new ArrayList<>();
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
			final int candidateDestCoor=this.piecePosition+currentCandidateOffset;
			if(BoardUtils.isValidTileCoordinate(candidateDestCoor)) {
				if(isFirstColumnExclusion(this.piecePosition, currentCandidateOffset)||
						isSecondColumnExclusion(this.piecePosition, currentCandidateOffset)||
						isSeventhColumnExclusion(this.piecePosition, currentCandidateOffset)||
						isEighthColumnExclusion(this.piecePosition, currentCandidateOffset)) {
					continue;
				}
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
		return PieceType.KNIGHT.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPos]&&(candidateOffset==-17||
				candidateOffset==-10||candidateOffset==6||candidateOffset==1);
	}
	private static boolean isSecondColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPos]&&(candidateOffset==-10||candidateOffset==6);
	}
	private static boolean isSeventhColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPos]&&(candidateOffset==-6||candidateOffset==10);
	}
	private static boolean isEighthColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPos]&&(candidateOffset==-15||candidateOffset==-6||
				candidateOffset==10||candidateOffset==17);
	}
	
	@Override
	public Knight movePiece(Move move) {
		// TODO Auto-generated method stub
		return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestCoor());
	}
	
}
