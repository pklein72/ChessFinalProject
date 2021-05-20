package com.chess.engine.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.PawnJump;
import com.chess.engine.board.Move.PawnMove;
import com.chess.engine.board.Move.PawnPromotion;
import com.chess.engine.board.AttackMove.PawnAttackMove;
import com.chess.engine.board.AttackMove.PawnEnPassantAttackMove;

public class Pawn extends Piece {

	private final static int[] CANDIDATE_MOVE_COORDINATE= {8, 16, 7, 9};

	public Pawn(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.PAWN,piecePosition, pieceAlliance, true);
	}

	public Pawn(final Alliance pieceAlliance, final int piecePosition, final boolean isFirstMove) {
		super(PieceType.PAWN, piecePosition, pieceAlliance, isFirstMove);
	}
	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	@Override
	public Collection<Move> calculatedLegalMoves(final Board board) {

		final List<Move> legalMoves=new ArrayList<>();
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			final int candidateDestCoor=this.piecePosition+(this.getPieceAlliance().getDirection()*currentCandidateOffset);
			if(!BoardUtils.isValidTileCoordinate(candidateDestCoor)) {
				continue;
			}
			if(currentCandidateOffset==8 && !board.getTile(candidateDestCoor).isTileOccupied()) {
				if(this.pieceAlliance.isPawnPromotionSquare(candidateDestCoor)) {
					legalMoves.add(new PawnPromotion(new PawnMove(board,this, candidateDestCoor)));
				} else {
					legalMoves.add(new PawnMove(board, this, candidateDestCoor));
				}
			}else if(currentCandidateOffset==16 && this.isFirstMove() && 
					((BoardUtils.SEVENTH_RANK[this.piecePosition] &&this.pieceAlliance.isBlack()) || 
							(BoardUtils.SECOND_RANK[this.piecePosition] && this.pieceAlliance.isWhite()))) {
				final int behindCandDestCoor=this.piecePosition+(this.pieceAlliance.getDirection()*8);
				if (!board.getTile(behindCandDestCoor).isTileOccupied()&&
						!board.getTile(candidateDestCoor).isTileOccupied()) {
					legalMoves.add(new PawnJump(board, this, candidateDestCoor));
				}
			} else if(currentCandidateOffset==7 &&
					!((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()||
							(BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
				if(board.getTile(candidateDestCoor).isTileOccupied()) {
					final Piece pieceOnCandidate=board.getTile(candidateDestCoor).getPiece();
					if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()) {
						if(this.pieceAlliance.isPawnPromotionSquare(candidateDestCoor)) {
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestCoor, pieceOnCandidate)));
						} else {
							legalMoves.add(new PawnAttackMove(board, this, candidateDestCoor, pieceOnCandidate));
						}
					}
				} else if(board.getEnPassantPawn() !=null) {
					if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition+(this.pieceAlliance.getOppositeDirection()))) {
						final Piece pieceOnCandid=board.getEnPassantPawn();
						if(this.pieceAlliance!=pieceOnCandid.getPieceAlliance())	{
							legalMoves.add(new PawnEnPassantAttackMove(board,this,candidateDestCoor, pieceOnCandid));
						}
					}
				}
			}else if (currentCandidateOffset==9 && 
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()||
							(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())))){
				if(board.getTile(candidateDestCoor).isTileOccupied()) {
					final Piece pieceOnCandidate=board.getTile(candidateDestCoor).getPiece();
					if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()) {
						if(this.pieceAlliance.isPawnPromotionSquare(candidateDestCoor)) {
							legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, candidateDestCoor, pieceOnCandidate)));
						} else {	
							legalMoves.add(new PawnAttackMove(board, this, candidateDestCoor, pieceOnCandidate));
						}
					}
				}
			} else if(board.getEnPassantPawn() !=null) {
				if(board.getEnPassantPawn().getPiecePosition()==(this.piecePosition-(this.pieceAlliance.getOppositeDirection()))) {
					final Piece pieceOnCandidate=board.getEnPassantPawn();
					if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()) {
						legalMoves.add(new PawnEnPassantAttackMove(board,this,candidateDestCoor, pieceOnCandidate));
					}

				}

			}
		}
		return Collections.unmodifiableList(legalMoves);
	}

	@Override
	public Pawn movePiece(Move move) {
		// TODO Auto-generated method stub
		return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestCoor());
	}
	
	public Piece getPromotionPiece() {
		return new Queen(this.getPieceAlliance(), this.getPiecePosition(), false);
	}

}
