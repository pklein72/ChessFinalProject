package com.chess.engine.board;


import java.util.*;

import com.chess.engine.pieces.Piece;
public abstract class Tile {
	protected final int tileCoordinate;
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		// TODO Auto-generated method stub
		final Map<Integer, EmptyTile> emptyTileMap=new HashMap<>();
		for(int i=0; i<BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		return Collections.unmodifiableMap(emptyTileMap);
	}
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	protected Tile(final int tileCoordinate) {
		this.tileCoordinate=tileCoordinate;
	}
	
	public int getTileCoor() {
		return this.tileCoordinate;
	}
	
	public abstract boolean isTileOccupied();
	
	public abstract Piece getPiece();
	
}
