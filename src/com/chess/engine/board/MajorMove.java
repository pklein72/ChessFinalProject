package com.chess.engine.board;

import com.chess.engine.pieces.Piece;

public class MajorMove extends Move {

	public MajorMove(final Board board, final Piece movedPiece, final int destCoor) {
		super(board, movedPiece, destCoor);
	}
	@Override
	public boolean equals(final Object other) {
		return this==other||other instanceof MajorMove&&super.equals(other);
	}
	@Override
	public String toString() {
		return movedPiece.getPieceType().toString()+BoardUtils.getPositionAtCoor(this.destCoor);
	}

}
