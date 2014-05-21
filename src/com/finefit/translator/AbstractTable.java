package com.finefit.translator;

import java.util.ArrayList;
import java.util.List;

import fit.Parse;

public abstract class AbstractTable {

	private Parse parsedTable;
	
	public AbstractTable(Parse theTable) {
		this.parsedTable = theTable;
	}
	
	/**
	 * Get number of table rows excluding the Fixture Name row
	 * @return Integer
	 */
	public int getNumberOfRows() {
		int count = 0;
		Parse table = parsedTable;
		while (table != null) {
			Parse more = table.more;
			table = more;
			count++;
		}
		return count;
	}

	/**
	 * Get Row By index
	 * @param index
	 * @return Parse
	 */
	public Parse getRowByIndex(int index) {
		int numberOfRows = getNumberOfRows();
		Parse table = this.parsedTable;
		if (index < 0 || index > numberOfRows) {
			System.err.println("Failed to find row - Index not in range");
			return null;
		}

		for (int i = 0; i < numberOfRows; i++) {
			if (table != null) {
				if (i == index) {
					return table;
				} else {
					Parse more = table.more;
					table = more;
				}
			} else {
				System.err.println("Failed to find parsed table");
				return null;
			}
		}
		return null;
	}

	/**
	 * Get number of cells in row
	 * @param rowIndex
	 * @return Integer
	 */
	public int getNumberOfcellsInRow(int rowIndex) {
		int i = 0;
		// Parse table = this.parsedTable.parts;
		Parse exactRow = getRowByIndex(rowIndex).parts;
		for (; exactRow != null; i++) {
			exactRow = exactRow.more;
		}
		return i;
	}
	
	/**
	 * Get Cell text in a given row index and column index
	 * @param rowIndex
	 * @param cellIndex
	 * @return String
	 */
	public String getCellTextByIndex(int rowIndex, int colIndex) {
		Parse row = getRowByIndex(rowIndex).parts;

		int numOfCells = getNumberOfcellsInRow(rowIndex);
		for (int i = 0; i < numOfCells; i++) {
			if (row != null) {
				if (i == colIndex) {
					// System.out.println(row.parts.text());
					return row.text();
				} else {
					row = row.more;
				}
			}
		}
		return null;
	}
	
	/**
	 * Get cell object by index
	 * @param rowIndex
	 * @param colIndex
	 * @return Cell
	 */
	public Cell getCellByIndex(int rowIndex, int colIndex) {

		int numOfRows = getNumberOfRows();
		for (int i = 0; i < numOfRows; i++) {
			int numOfCells = getNumberOfcellsInRow(i);
			for (int j = 0; j < numOfCells; j++) {
				if(i == rowIndex && j == colIndex){
					return new Cell(i,j,getCellTextByIndex(i, j));
				}
			}
		}
		return null;
	}
	
	
	/**
	 * Get row (list of cells text)
	 * @return List
	 */
	public List<String> getRowCellsText(int rowIndex){
		List<String> listOfCells = new ArrayList<>();
		int listIndex = 0;
		int numOfCells = getNumberOfcellsInRow(rowIndex);
		for (int j = 0; j < numOfCells; j++) {
			listOfCells.add(listIndex, (getCellTextByIndex(rowIndex, j)));
			listIndex++;
		}
		return listOfCells;
	}
	
	/**
	 * Get row (list of cells Objects)
	 * @return List
	 */
	public List<Cell> getRowCellsObjects(int rowIndex){
		List<Cell> listOfCells = new ArrayList<>();
		int listIndex = 0;
		int numOfCells = getNumberOfcellsInRow(rowIndex);
		for (int j = 0; j < numOfCells; j++) {
			listOfCells.add(listIndex, new Cell(rowIndex,j,(getCellTextByIndex(rowIndex, j))));
			listIndex++;
		}
		return listOfCells;
	}
	
	/**
	 * Get column (list of cells Objects)
	 * @return List
	 */
	public List<Cell> getColCellsObjects(int colIndex){
		List<Cell> listOfCells = new ArrayList<>();
		int listIndex = 0;
		int numOfRows = getNumberOfRows();
		for (int j = 0; j < numOfRows; j++) {
			listOfCells.add(listIndex, new Cell(j,colIndex,(getCellTextByIndex(j, colIndex))));
			listIndex++;
		}
		return listOfCells;
	}
	
	/**
	 * Get Fixture name
	 * @return String
	 */
	public String getTableName(){
		String name = this.getCellByIndex(0, 0).getText(); // assuming operation name will be always at index (0,0)
		if(name != null){
			return name;
		}
		return null;
	}
}
