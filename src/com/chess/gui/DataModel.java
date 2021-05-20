package com.chess.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import com.chess.engine.board.Move;
import com.chess.gui.Row;

class DataModel extends DefaultTableModel {

	private final List<Row> values;
	private static final String[] NAMES= {"White", "Black"};
	
	DataModel() {
		this.values=new ArrayList<>();
	}
	
	public void clear() {
		this.values.clear();
		setRowCount(0);
	}
	
	@Override
	public int getRowCount() {
		if(this.values==null) {
			return 0;
		}
		return this.values.size();
	}
	
	@Override
	public int getColumnCount() {
		return NAMES.length;
	}
	
	@Override
	public Object getValueAt(final int row, final int column) {
		final Row currentRow=this.values.get(row);
		if(column==0) {
			return currentRow.getWhiteMove();
		} else if(column==1) {
			return currentRow.getBlackMove()	;
		}
		return null;
	}
	
	@Override
	public void setValueAt(final Object aValue, final int row, final int column) {
		final Row currentRow;
		if(this.values.size()<=row) {
			currentRow=new Row();
			this.values.add(currentRow);
		} else {
			currentRow=this.values.get(row);
		}
		if(column==0) {
			currentRow.setWhiteMove((String)aValue);
			fireTableRowsInserted(row, row);
		} else if(column==1) {
			currentRow.setBlackMove((String)aValue);
			fireTableCellUpdated(row, column);
		}
		
	}
	
	@Override
	public Class<?> getColumnClass(final int column) {
		return Move.class;
	}
	
	@Override
	public String getColumnName(final int column) {
		return NAMES[column];
	}

}
