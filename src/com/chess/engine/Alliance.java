package com.chess.engine;

import com.chess.engine.board.BoardUtils;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;

public enum Alliance {
	WHITE
	{
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return -1;
		}
		@Override
		public int getOppositeDirection() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return true;
		}
		
		@Override
		public boolean isPawnPromotionSquare(int pos) {
			// TODO Auto-generated method stub
			return BoardUtils.EIGHTH_RANK[pos];
		}
		
		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return whitePlayer;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "White";
		}
	
	},
	BLACK
	{
		@Override
		public int getDirection() {
			// TODO Auto-generated method stub
			return 1;
		}
		@Override
		public int getOppositeDirection() {
			// TODO Auto-generated method stub
			return -1;
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isWhite() {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean isPawnPromotionSquare(int pos) {
			// TODO Auto-generated method stub
			return BoardUtils.FIRST_RANK[pos];
		}

		@Override
		public Player choosePlayer(final WhitePlayer whitePlayer, final BlackPlayer blackPlayer) {
			// TODO Auto-generated method stub
			return blackPlayer;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "Black";
		}
		
	};	
	
	public abstract int getDirection();
	public abstract int getOppositeDirection();
	public abstract boolean isBlack();
	public abstract boolean isWhite();
	public abstract boolean isPawnPromotionSquare(int pos);
	public abstract String toString();
	
	public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
