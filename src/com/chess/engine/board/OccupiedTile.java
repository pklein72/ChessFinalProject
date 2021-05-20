package com.chess.engine.board;


import com.chess.engine.pieces.Piece;

public final class OccupiedTile extends Tile {
	private final Piece pieceOnTile;
	
	protected OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
		super(tileCoordinate);
		this.pieceOnTile=pieceOnTile;
	}
	
	@Override
	public String toString() {
		return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
			getPiece().toString();
	}
	
	@Override
	public boolean isTileOccupied() {
		return true;
	}
	@Override
	public Piece getPiece() {
		// TODO Auto-generated method stub
		return this.pieceOnTile;
	}
	
	

}
