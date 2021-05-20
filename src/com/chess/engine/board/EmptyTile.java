package com.chess.engine.board;


import com.chess.engine.pieces.Piece;

public final class EmptyTile extends Tile {
	
	protected EmptyTile(final int coordinate) {
		super(coordinate);
	}
	
	@Override
	public String toString() {
		return "-";
	}

	@Override
	public boolean isTileOccupied() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Piece getPiece() {
		// TODO Auto-generated method stub
		return null;
	}

}
