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


public class BlackPlayer extends Player {

	public BlackPlayer(Board board, 
			Collection<Move> whiteStandardLegalMoves,
			Collection<Move> blackStandardLegalMoves) {
		super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
	}

	@Override
	public Collection<Piece> getActivePieces() {
		// TODO Auto-generated method stub
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		// TODO Auto-generated method stub
		return Alliance.BLACK;
	}

	@Override
	public Player getOpponent() {
		// TODO Auto-generated method stub
		return this.board.whitePlayer();
	}

	@Override
	protected Collection<Move> calculateKingCastles(final Collection<Move> playerLegals, final Collection<Move> opponentsLegals) {
		final List<Move> kingCastles=new ArrayList<>();
		if(this.playerKing.isFirstMove()&&!this.isInCheck()) {
			//blacks king side castle
			if(!this.board.getTile(5).isTileOccupied()&&!this.board.getTile(6).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(7);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove()) {
					if(Player.calculateAttacksOnTile(5,  opponentsLegals).isEmpty()&&
							Player.calculateAttacksOnTile(6, opponentsLegals).isEmpty()&&
							rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new KingSideCastleMove(this.board,
																this.playerKing,
																6,
																(Rook) rookTile.getPiece(),
																rookTile.getTileCoor(),
																5));
					}
				}
			}
			if(!this.board.getTile(1).isTileOccupied()&&
					!this.board.getTile(2).isTileOccupied()&&
					!this.board.getTile(3).isTileOccupied()) {
				final Tile rookTile=this.board.getTile(0);
				if(rookTile.isTileOccupied()&&rookTile.getPiece().isFirstMove()&&
						Player.calculateAttacksOnTile(2,  opponentsLegals).isEmpty() &&
						Player.calculateAttacksOnTile(3	, opponentsLegals).isEmpty()&&
						rookTile.getPiece().getPieceType().isRook()) {
					kingCastles.add(new QueenSideCastleMove(this.board,
															this.playerKing,
															2,
															(Rook) rookTile.getPiece(),
															rookTile.getTileCoor(),
															3));
				}
			}
		}
		return Collections.unmodifiableList(kingCastles);
	}
}
