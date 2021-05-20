package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;

public class AttackMove extends Move {

	final Piece attackedPiece;

	public AttackMove(final Board board, final Piece movedPiece, final int destCoor, final Piece attackedPiece) {
		super(board, movedPiece, destCoor);
		this.attackedPiece=attackedPiece;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		return this.attackedPiece.hashCode()+super.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if(this==other) {
			return true;
		}
		if(!(other instanceof AttackMove)) {
			return false;
		}
		final AttackMove otherAttackMove=(AttackMove) other;
		return super.equals(otherAttackMove)&& getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
	}

	@Override
	public boolean isAttack() {
		return true;
	}

	@Override
	public Piece getAttackedPiece() {
		return this.attackedPiece;
	}

	public static class PawnAttackMove extends AttackMove{
		public PawnAttackMove(final Board board, final Piece movedPiece, final int destCoor, final Piece attackedPiece) {
			super(board, movedPiece, destCoor,attackedPiece);
		}

		@Override
		public boolean equals(final Object other)	 {
			return this==other || other instanceof PawnAttackMove&&super.equals(other);
		}

		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoor(this.movedPiece.getPiecePosition()).substring(0,1)+"x"+
					BoardUtils.getPositionAtCoor(this.destCoor);
		}
	}

	public static final class PawnEnPassantAttackMove extends PawnAttackMove{
		public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destCoor, final Piece attackedPiece) {
			super(board, movedPiece, destCoor,attackedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this==other||other instanceof PawnEnPassantAttackMove&&super.equals(other);
		}
		
		@Override
		public Board execute() {
			final Builder builder=new Builder();
			for(final Piece piece:this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece:this.board.currentPlayer().getOpponent().getActivePieces()) {
				if(!piece.equals(this.getAttackedPiece())){
					builder.setPiece(piece);
				}
			}
			builder.setPiece(this.movedPiece.movePiece(this));
			builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
		
	}
	
	public static class MajorAttackMove extends AttackMove {

		public MajorAttackMove(final Board board, final Piece movedPiece, final int destCoor, final Piece attackedPiece) {
			super(board, movedPiece, destCoor, attackedPiece);
		}
		
		@Override
		public boolean equals(final Object other) {
			return this==other||other instanceof MajorAttackMove&&super.equals(other);
		}
		
		@Override
		public String toString() {
			return movedPiece.getPieceType()+BoardUtils.getPositionAtCoor(this.destCoor);
		}
		
	}


}
