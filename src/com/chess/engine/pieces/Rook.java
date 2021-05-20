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

public class Rook extends Piece {

	private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES= {-8, -1, 1, 8};

	public Rook(Alliance pieceAlliance, int piecePosition) {
		super(PieceType.ROOK, piecePosition, pieceAlliance, true);
	}

	public Rook(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.ROOK, piecePosition, pieceAlliance, isFirstMove);
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
		return PieceType.ROOK.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPos]&&(candidateOffset==-1);
	}

	private static boolean isEighthColumnExclusion(final int currentPos, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPos]&&(candidateOffset==1);
	}
	
	@Override
	public Rook movePiece(Move move) {
		// TODO Auto-generated method stub
		return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestCoor());
	}
}
