package com.chess.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.*;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.gui.DataModel;

public class GameHistoryPanel extends JPanel {

	private final DataModel model;
	private final JScrollPane scrollPane;
	private static final Dimension HISTORY_PANEL_DIMENSION= new Dimension(100, 400);
	
	GameHistoryPanel() {
		this.setLayout(new BorderLayout());
		this.model=new DataModel();
		final JTable table=new JTable(model);
		table.setRowHeight(15);
		this.scrollPane =new JScrollPane(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
		this.add(scrollPane, BorderLayout.CENTER);
		this.setVisible(true);
		
	}
	
	void redo(final Board board, final Table.MoveLog moveHistory) {
		
		int currentRow=0;
		this.model.clear();
		for(final Move move: moveHistory.getMoves()) {
			final String moveText=move.toString();
			if(move.getMovedPiece().getPieceAlliance().isWhite()) {
				this.model.setValueAt(moveText, currentRow, 0);
			} else if(move.getMovedPiece().getPieceAlliance().isBlack()) {
				this.model.setValueAt(moveText, currentRow, 1);
				currentRow++;
			}
		}
		
		if(moveHistory.getMoves().size()>0) {
			final Move lastMove=moveHistory.getMoves().get(moveHistory.size() -1);
			final String moveText=lastMove.toString();
			
			if(lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
				this.model.setValueAt(moveText+caluculateCheckAndCheckMateHash(board), currentRow, 0);
			}else if(lastMove.getMovedPiece().getPieceAlliance().isBlack()){
				this.model.setValueAt(moveText+ caluculateCheckAndCheckMateHash(board), currentRow-1, 1);
			}
		}
		
		final JScrollBar vertical=scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
		
	}

	private String caluculateCheckAndCheckMateHash(final Board board) {
		if(board.currentPlayer().isInCheckmate()) {
			return "#";
		} else if(board.currentPlayer().isInCheck()) {
			return "+";
		}
		return "";
	}
	
}
