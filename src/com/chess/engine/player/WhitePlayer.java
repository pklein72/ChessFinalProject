package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.pieces.Rook;
import com.chess.engine.board.Move.KingSideCastleMove;
import com.chess.engine.board.Move.QueenSideCastleMove;


public class WhitePlayer extends Player {

	public WhitePlayer(Board board, 
			Collection<Move> whiteStandardLegalMoves,
			Collection<Move> blackStandardLegalMoves) {
		super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		// TODO Auto-generated method stub
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		// TODO Auto-generated method stub
		return Alliance.WHITE;
	}
	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.blackPlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
		// TODO Auto-generated method stub
		
		final List<Move> kingCastles=new ArrayList<>();
		
		if(this.playerKing.isFirstMove()&&!this.isInCheck()) {
			//whites king side castle
			if(!this.board.getTile(61).isTileOccupied()&&!this.board.getTile(62).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(63);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(61,  opponentsLegals).isEmpty()&&
							Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty()&&
							rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new KingSideCastleMove(board,
																this.playerKing,
																62, 
																(Rook)rookTile.getPiece(), 
																rookTile.getTileCoor(),
																59));
					}
				}
			}
			if(!this.board.getTile(59).isTileOccupied()&&
					!this.board.getTile(58).isTileOccupied()&&
					!this.board.getTile(57).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(56);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove()&&
						Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty()&&
						Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty()&&
						rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new QueenSideCastleMove(this.board,
															this.playerKing,
															58,
															(Rook) rookTile.getPiece(),
															rookTile.getTileCoor(),
															59));
				}
			}
		}
		return Collections.unmodifiableList(kingCastles);
	}
}
