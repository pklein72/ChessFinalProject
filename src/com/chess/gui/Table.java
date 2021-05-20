package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MoveFactory;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;

public class Table {

	private final JFrame gameFrame;
	private final GameHistoryPanel gameHistoryPanel;
	private final TakenPiecesPanel takenPiecesPanel;
	private final BoardPanel boardPanel;
	private Board chessBoard;
	private final MoveLog moveLog;

	private Tile sourceTile;
	private Tile destTile;
	private Piece humanMovedPiece;
	private BoardDirection boardDirection;
	
	private boolean highlightLegalMoves;

	private static String defaultPieceImagesPath="art/pieces/plain/";
	private final static Color lightTileColor=Color.decode("#edcc85");
	private final static Color darkTileColor=Color.decode("#854e1c");

	private final static Dimension OUTER_FRAME_DIMENSION=new Dimension(600,600);
	private final static Dimension BOARD_PANEL_DIMENSION=new Dimension(400,350);
	private final static Dimension TILE_PANEL_DIMENSION=new Dimension(10, 10);

	public Table() {
		this.gameFrame=new JFrame("JChess");
		this.gameFrame.setLayout(new BorderLayout());
		final JMenuBar tableMenuBar=creatTableMenuBar();
		this.gameFrame.setJMenuBar(tableMenuBar);
		this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
		this.gameHistoryPanel=new GameHistoryPanel();
		this.takenPiecesPanel=new TakenPiecesPanel();
		this.chessBoard=Board.createStandardBoard();
		this.boardPanel=new BoardPanel();
		this.moveLog=new MoveLog();
		this.boardDirection=BoardDirection.NORMAL;
		this.highlightLegalMoves=true;
		this.gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
		this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
		this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
		this.gameFrame.setVisible(true);
	}

	private JMenuBar creatTableMenuBar() {
		final JMenuBar tableMenuBar=new JMenuBar();
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
		return tableMenuBar;
	}
	private JMenu createFileMenu() {
		final JMenu fileMenu=new JMenu("File");
		/*final JMenuItem openPGN=new JMenuItem("Load PGN File");
			openPGN.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("open that pgn file!");
			}
		});
		fileMenu.add(openPGN);*/

		final JMenuItem exitMenuItem=new JMenuItem("Exit");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		return fileMenu;
	}	
	
	private JMenu createPreferencesMenu() {
		final JMenu preferencesMenu=new JMenu("Preferences");
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent event) {
				boardDirection=boardDirection.oppsite();
				boardPanel.drawBoard(chessBoard);
			}
		});
		preferencesMenu.add(flipBoardMenuItem);
		preferencesMenu.addSeparator();
		final JCheckBoxMenuItem legalMoveHighlightCheckBox=new JCheckBoxMenuItem("Highlight Legal Moves", false);
		legalMoveHighlightCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				highlightLegalMoves=legalMoveHighlightCheckBox.isSelected();
			}
		});
		preferencesMenu.add(legalMoveHighlightCheckBox);
		
		return preferencesMenu;
	}
	
	public static class MoveLog {
		
		private final List<Move> moves;
		
		MoveLog() {
			this.moves=new ArrayList<>();
		}
		
		public List<Move> getMoves() {
			return moves;
		}
		
		public void addMove(final Move move) {
			this.moves.add(move);
		}
		
		public int size() {
			return this.moves.size();
		}
		
		public void clear() {
			this.moves.clear();
		}
		
		public Move removeMove(int index) {
			return this.moves.remove(index);
		}
		
		public boolean removeMove(final Move move) {
			return this.moves.remove(move);
		}
		
	}

	private class TilePanel extends JPanel {
		
		private final int tileID;

		TilePanel(final BoardPanel boardPanel, final int tileID) {
			super(new GridBagLayout());
			this.tileID=tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assignTileColor();
			assignTilePieceIcon(chessBoard);
			addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(final MouseEvent event) {
					// TODO Auto-generated method stub
					if (SwingUtilities.isRightMouseButton(event)) {
						sourceTile=null;
						destTile=null;
						humanMovedPiece=null;
					}else if (SwingUtilities.isLeftMouseButton(event)){
						if(sourceTile==null) {
							sourceTile=chessBoard.getTile(tileID);
							//System.out.println("SOURCE PIECE: "+sourceTile.getPiece());
							humanMovedPiece=sourceTile.getPiece();
							//System.out.println("MOVED PIECE: "+humanMovedPiece)	;
							if(humanMovedPiece==null) {
								sourceTile=null;
							}
						}else {
							//System.out.println("PIECE: "+chessBoard.getTile(tileID).getPiece());
							destTile=chessBoard.getTile(tileID);
							//System.out.println("DEST TILE: "+destTile);
							final Move move=MoveFactory.createMove(chessBoard, sourceTile.getTileCoor(), destTile.getTileCoor());
							final MoveTransition transition=chessBoard.currentPlayer().makeMove(move);
							if(transition.getMoveStatus().isDone())	 {
								chessBoard=transition.getTransitionBoard();
								moveLog.addMove(move);
							}
							sourceTile=null;
							destTile=null;
							humanMovedPiece=null;
						}
					}
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							gameHistoryPanel.redo(chessBoard, moveLog);
							takenPiecesPanel.redo(moveLog);
							boardPanel.drawBoard(chessBoard);
						}
					});
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub

				}

			});

			validate();
		}

		public void drawTile(final Board board) {
			assignTileColor();
			assignTilePieceIcon(board);
			highlightLegals(board);
			validate();
			repaint();
		}

		private void assignTilePieceIcon(final Board board) {
			this.removeAll();
			if(board.getTile(this.tileID).isTileOccupied()) {
				//System.out.println(defaultPieceImagesPath+board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0,1)+board.getTile(this.tileID).getPiece().toString()+".gif");
				try {
					final BufferedImage image=
							ImageIO.read(new File(defaultPieceImagesPath+board.getTile(this.tileID).getPiece().getPieceAlliance().toString().substring(0, 1)+
									board.getTile(this.tileID).getPiece().toString()+".gif"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void highlightLegals(final Board board) {
			if(highlightLegalMoves) {
				for(final Move move: pieceLegalMoves(board)) {
					if(move.getDestCoor()==this.tileID) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("art/misc/green_dot.png")))));
						} catch (Exception exception) {
							exception.printStackTrace();
						}
					}
				}
			}
		}
		
		private Collection<Move> pieceLegalMoves(final Board board) {
			if(humanMovedPiece!=null && humanMovedPiece.getPieceAlliance()==board.currentPlayer().getAlliance()) {
				return humanMovedPiece.calculatedLegalMoves(board);
			}
			return Collections.emptyList();
		}
		
		private void assignTileColor() {

			if(BoardUtils.EIGHTH_RANK[this.tileID]||
					BoardUtils.SIXTH_RANK[this.tileID]||
					BoardUtils.FOURTH_RANK[this.tileID]||
					BoardUtils.SECOND_RANK[this.tileID]) {
				setBackground(this.tileID%2==0?Table.lightTileColor:Table.darkTileColor);
			}else if(BoardUtils.SEVENTH_RANK[this.tileID]||
					BoardUtils.FIFTH_RANK[this.tileID]||
					BoardUtils.THIRD_RANK[this.tileID]||
					BoardUtils.FIRST_RANK[this.tileID]) {
				setBackground(this.tileID%2!=0?Table.lightTileColor:Table.darkTileColor);
			}
		}
	}

	private class BoardPanel extends JPanel {

		final List<TilePanel> boardTiles;

		BoardPanel() {
			super(new GridLayout(8, 8));
			this.boardTiles=new ArrayList<>();
			for(int i=0;i<BoardUtils.NUM_TILES;i++) {
				final TilePanel tilePanel=new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(Table.BOARD_PANEL_DIMENSION);
			validate();
		}

		public void drawBoard(Board board) {
			// TODO Auto-generated method stub
			removeAll();
			for(final TilePanel tilePanel: boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			validate();
			repaint();
		}

	}
	
	
	public enum BoardDirection	 {
		
		NORMAL {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// TODO Auto-generated method stub
				return boardTiles;
			}

			@Override
			BoardDirection oppsite() {
				// TODO Auto-generated method stub
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(List<TilePanel> boardTiles) {
				// TODO Auto-generated method stub
				return Lists.reverse(boardTiles);
			}

			@Override
			BoardDirection oppsite() {
				// TODO Auto-generated method stub
				return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection oppsite();
	}


}
