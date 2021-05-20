package com.chess.engine.board;

import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;

public abstract class Move {

	protected final Board board;
	protected final Piece movedPiece;
	protected final int destCoor;

	protected final boolean isFirstMove;

	protected Move(final Board board, final Piece movedPiece, final int destCoor) {
		this.board=board;
		this.movedPiece=movedPiece;
		this.destCoor=destCoor;
		this.isFirstMove=movedPiece.isFirstMove();
	}	

	private Move(final Board board, final int destCoor) {
		this.board=board;
		this.destCoor=destCoor;
		this.movedPiece=null;
		this.isFirstMove=false;
	}

	@Override
	public int hashCode() {
		final int prime=31;
		int result=1;
		result=prime*result+this.destCoor;
		result=prime*result+this.movedPiece.hashCode();
		result=prime*result+this.movedPiece.getPiecePosition();
		return result;
	}

	@Override
	public boolean equals(final Object other) {
		if(this==other) {
			return true;
		}
		if(!(other instanceof Move)) {
			return false;
		}
		final Move otherMove=(Move)	other;
		return  getCurrentCoor()==otherMove.getCurrentCoor()	&&
				getDestCoor()==otherMove.getDestCoor()&&
				getMovedPiece().equals(otherMove.getMovedPiece());
	}

	public Board getBoard() {
		return this.board;
	}

	public int getCurrentCoor() {
		return this.getMovedPiece().getPiecePosition();
	}

	public int getDestCoor() {
		return this.destCoor;
	}

	public Piece getMovedPiece() {
		return movedPiece;
	}

	public boolean isAttack() {
		return false;
	}

	public boolean isCastlingMove() {
		return false;
	}

	public Piece getAttackedPiece() {
		return null;
	}

	public Board execute() {
		final Board.Builder builder = new Board.Builder();
		for(final Piece piece: this.board.currentPlayer().getActivePieces() ) {
			if(!this.movedPiece.equals(piece)) {
				builder.setPiece(piece);
			}
		}
		for(final Piece piece: this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
		}
		//move the moved piece
		builder.setPiece(this.movedPiece.movePiece(this));
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());

		return builder.build();
	}

	public static final class PawnMove extends Move {
		public PawnMove(final Board board, final Piece movedPiece, final int destCoor) {
			super(board, movedPiece, destCoor);
		}

		@Override
		public boolean equals(final Object other) {
			return this==other||other instanceof PawnMove&&super.equals(other);
		}

		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoor(this.destCoor);
		}

	}

	public static class PawnPromotion extends Move {

		final Move decoratedMove;
		final Pawn promotedPawn;

		public PawnPromotion(final Move decoratedMove) {
			super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestCoor());
			this.decoratedMove=decoratedMove;
			this.promotedPawn=(Pawn) decoratedMove.getMovedPiece();
		}

		@Override
		public int hashCode() {
			return decoratedMove.hashCode()+(31*promotedPawn.hashCode());
		}

		@Override
		public boolean equals(final Object other) {
			return this==other||other instanceof PawnPromotion&&(super.equals(other));
		}

		@Override
		public Board execute() {

			final Board pawnMovedBoard=this.decoratedMove.execute();
			final Board.Builder builder=new Builder();
			for(final Piece piece:pawnMovedBoard.currentPlayer().getActivePieces()) {
				if(!this.promotedPawn.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece:pawnMovedBoard.currentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
			builder.setMoveMaker(pawnMovedBoard.currentPlayer().getAlliance());
			return builder.build();
		}

		@Override
		public boolean isAttack() {
			return this.decoratedMove.isAttack();
		}

		@Override
		public Piece getAttackedPiece() {
			return this.decoratedMove.getAttackedPiece();
		}

		@Override
		public String toString() {
			return "";
		}

	}

	public static class PawnJump extends Move{
		public PawnJump(final Board board, final Piece movedPiece, final int destCoor) {
			super(board, movedPiece, destCoor);
		}
		@Override
		public Board execute() {
			final Builder b=new Builder();
			for(final Piece p:this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(p)) {
					b.setPiece(p);
				}
			}
			for(final Piece p: this.board.currentPlayer().getOpponent().getActivePieces()) {
				b.setPiece(p);
			}
			final Pawn movedPawn=(Pawn) this.movedPiece.movePiece(this);
			b.setPiece(movedPawn);
			b.setEnPassantPawn(movedPawn);
			b.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return b.build();
		}	

		@Override
		public String toString() {
			return BoardUtils.getPositionAtCoor(this.destCoor);
		}

	}
	public static abstract class CastleMove extends Move{

		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDest;

		public CastleMove(final Board board, final Piece movedPiece, final int destCoor, 
				final Rook cR, final int cRS, final int cRD) {
			super(board, movedPiece, destCoor);
			castleRook=cR;
			castleRookStart=cRS;
			castleRookDest=cRD;
		}
		public Rook getCastleRook() {
			return castleRook;
		}

		public boolean isCastlingMove() {
			return true;
		}

		@Override
		public Board execute() {
			final Builder b=new Builder();
			for(final Piece p:this.board.currentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(p)) {
					b.setPiece(p);
				}
			}
			for(final Piece p: this.board.currentPlayer().getOpponent().getActivePieces()) {
				b.setPiece(p);
			}
			b.setPiece(this.movedPiece.movePiece(this));
			b.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDest));
			b.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
			return b.build();
		}

		@Override
		public int hashCode() {
			final int prime=31;
			int result=super.hashCode();
			result=prime*result+this.castleRook.hashCode()	;
			result=prime*result+this.castleRookDest;
			return result;
		}

		@Override
		public boolean equals(final Object other) {
			if(this==other) {
				return true;
			}
			if(!(other instanceof CastleMove))	{
				return false;
			}
			final CastleMove otherCastleMove=(CastleMove)other;
			return super.equals(otherCastleMove)&&this.castleRook.equals(otherCastleMove.getCastleRook());

		}

	}
	public static class KingSideCastleMove extends CastleMove{


		public KingSideCastleMove(final Board board, final Piece movedPiece, final int destCoor, 
				final Rook cR, final int cRS, final int cRD) {
			super(board, movedPiece, destCoor, cR, cRS, cRD);
		}

		@Override
		public boolean equals(final Object other) {
			return this==other||other instanceof KingSideCastleMove&&super.equals(other);
		}

		@Override
		public String toString() {
			return "0-0";
		}

	}
	public static class QueenSideCastleMove extends CastleMove{
		public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destCoor,
				final Rook cR, final int cRS, final int cRD) {
			super(board, movedPiece, destCoor, cR, cRS, cRD);
		}

		@Override
		public boolean equals(final Object other) {
			return this==other||other instanceof QueenSideCastleMove&&super.equals(other);
		}

		@Override
		public String toString() {
			return "0-0-0";
		}

	}

	public static class NullMove extends Move{
		public NullMove() {
			super(null, 65);
		}

		@Override
		public Board execute() {
			throw new RuntimeException("You cannot execute the null move!");
		}

		@Override
		public int getCurrentCoor() {
			return -1;
		}

	}

	public static class MoveFactory {

		private static final Move NULL_MOVE=new NullMove();

		private MoveFactory() {
			throw new RuntimeException("Not instantiable!");
		}

		public static Move getNullMove() {
			return NULL_MOVE;
		}

		public static Move createMove(final Board board,
				final int currentCoordinate,
				final int destinationCoordinate) {
			for (final Move move : board.getAllLegalMoves()) {
				if (move.getCurrentCoor() == currentCoordinate &&
						move.getDestCoor() == destinationCoordinate) {
					return move;
				}
			}
			return NULL_MOVE;
		}

	}

}


