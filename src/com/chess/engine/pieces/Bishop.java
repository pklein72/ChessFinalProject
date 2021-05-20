package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.MajorMove;
import com.chess.engine.board.AttackMove.MajorAttackMove;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;

public class Bishop extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES= {-9, -7, 7, 9};

	public Bishop(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.BISHOP,piecePosition, pieceAlliance, true);
		// TODO Auto-generated constructor stub
	}
	
	public Bishop(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.BISHOP, piecePosition, pieceAlliance, isFirstMove);
	}

	@Override
	public Collection<Move> calculatedLegalMoves(final Board board) {
		// TODO Auto-generated method stub
		final List<Move> legalMoves=new ArrayList<>();
		for(final int candidateCoorOffset : CANDIDATE_MOVE_VECTOR_COORDINATES) {
			int candidateDestCoor=this.piecePosition;
			while(BoardUtils.isValidTileCoordinate(candidateDestCoor)) {
				if(isFirstColumnExclusion(candidateDestCoor, candidateCoorOffset)||
						isEighthColumnExclusion(candidateDestCoor, candidateCoorOffset)) {
					break;
				}
				candidateDestCoor+=candidateCoorOffset;
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
						break;
					}
				}
			}

		}
		return Collections.unmodifiableList(legalMoves);
	}
	
	@Override
	public String toString() {
		return PieceType.BISHOP.toString();
	}
	
	private static boolean isFirstColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPos]&&(candidateOffset==-9||candidateOffset==7);
	}
	
	private static boolean isEighthColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPos]&&(candidateOffset==-7||candidateOffset==9);
	}

	@Override
	public Bishop movePiece(Move move) {
		// TODO Auto-generated method stub
		return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestCoor());
	}
}
