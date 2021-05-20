package com.chess.gui;

public class Row {

	private String whiteMove;
	private String blackMove;
	
	Row() {
		
	}
	
	public String getWhiteMove() {
		return this.whiteMove;
	}
	
	public String getBlackMove() {
		return this.blackMove;
	}
	
	public void setWhiteMove(final String move) {
		this.whiteMove=move;
	}
	
	public void setBlackMove(final String move) {
		this.blackMove=move;
	}
	
}
