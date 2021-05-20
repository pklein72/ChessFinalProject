package com.chess.engine.board;

//import java.util.Arrays;
//import java.util.Collections;
import java.util.HashMap;
//import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class BoardUtils {
	public static final boolean[] FIRST_COLUMN = initColumn(0);
	public static final boolean[] SECOND_COLUMN = initColumn(1)	;
	public static final boolean[] SEVENTH_COLUMN = initColumn(6);
	public static final boolean[] EIGHTH_COLUMN = initColumn(7);

	public static final boolean[] EIGHTH_RANK=initRow(0);
	public static final boolean[] SEVENTH_RANK=initRow(8);
	public static final boolean[] SIXTH_RANK=initRow(16);
	public static final boolean[] FIFTH_RANK=initRow(24);
	public static final boolean[] FOURTH_RANK=initRow(32);
	public static final boolean[] THIRD_RANK=initRow(40);
	public static final boolean[] SECOND_RANK=initRow(48);
	public static final boolean[] FIRST_RANK=initRow(56);

	public static final int START_TILE_INDEX=0;
	public static final String[] ALGEBRAIC_NOTATION=initializeAlgebraicNotation();
	public static final Map<String, Integer> POSITION_TO_COOR=initializePositionToCoorMap();

	public static final int NUM_TILES=64;
	public static final int NUM_TILES_PER_ROW=8;
	private BoardUtils() {
		throw new RuntimeException("You cannot instantiate this class!");
	}
	private static String[] initializeAlgebraicNotation() {
		return new String[] {
				"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
				"a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
				"a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
				"a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
				"a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
				"a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
				"a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
				"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
		};
	}
	private static Map<String, Integer> initializePositionToCoorMap() {
		final Map<String, Integer> positionToCoor=new HashMap<>();
		for(int i=START_TILE_INDEX; i<NUM_TILES; i++) {
			positionToCoor.put(ALGEBRAIC_NOTATION[i], i);
		}
		return ImmutableMap.copyOf(positionToCoor);
	}
	private static boolean[] initRow(int rowNumber) {
		// TODO Auto-generated method stub

		final boolean[] row=new boolean[NUM_TILES];
		do {
			row[rowNumber]=true;
			rowNumber++;
		}while(rowNumber%NUM_TILES_PER_ROW !=0);
		return row;
	}
	private static boolean[] initColumn(int columnNumber) {
		// TODO Auto-generated method stub
		final boolean[] column=new boolean[64];
		do{
			column[columnNumber]=true;
			columnNumber+=NUM_TILES_PER_ROW;
		} while(columnNumber<NUM_TILES);
		return column;
	}
	public static boolean isValidTileCoordinate(final int coordinate) {
		// TODO Auto-generated method stub
		return coordinate>=0&&coordinate<64;
	}
	public static int getCoorAtPosition(final String position) {
		// TODO Auto-generated method stub
		return POSITION_TO_COOR.get(position);
	}

	public static String getPositionAtCoor(final int coor) {
		return ALGEBRAIC_NOTATION[coor];
	}

}
