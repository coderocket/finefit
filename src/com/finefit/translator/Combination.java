package com.finefit.translator;

import java.util.ArrayList;
import java.util.List;

public class Combination {

	private AbstractTable parsedTable;
	private Cell combination;
	
	public Combination (AbstractTable table, Cell up, Cell left){
		parsedTable = table;
		this.combination = getMatch(up, left);
	}
	
	public Combination (AbstractTable table, Cell cell){
		parsedTable = table;
		this.combination = cell;
	}
	
	/**
	 * Get the matched (the combination) cell
	 * @param up
	 * @param left
	 * @return Cell
	 */
	public Cell getMatch(Cell up, Cell left){
		Cell cell = new Cell(left.getRowIndex(),up.getColIndex(),getCellText(up,left));
		return cell;
	}
	
	/**
	 * Get cell text
	 * @param up
	 * @param left
	 * @return String
	 */
	private String getCellText(Cell up, Cell left){
		int row = left.getRowIndex();
		int col = up.getColIndex();
		
		String text = parsedTable.getCellTextByIndex(row, col);
		if(text == null){
			return null;
		}
		return text;
	}
	
	public String getCombinationText(){
		return this.combination.getText();
	}
	
	/**
	 * Add to combination cell text the new indication state
	 * @return
	 */
	public String getModifiedState(){
		String[] splitText = combination.getText().split(" ");
		List <Integer> indexes = new ArrayList<>();
		
		for(int i = 0; i < splitText.length; i++){
			if(splitText[i].equals("+") || splitText[i].equals("-") || splitText[i].equals("*") || splitText[i].equals("/")
					|| splitText[i].equals("and") || splitText[i].equals("or") || splitText[i].equals("no")
					|| splitText[i].equals("in")){
				indexes.add(i);
			}
		}
		
		if(indexes.isEmpty()){
			splitText[splitText.length-1] = "s." + splitText[splitText.length-1];
		}
		
		for(int i = 0; i < indexes.size(); i++){
			splitText[indexes.get(i)-1] = "s." + splitText[indexes.get(i)-1]; 
		}
		
		String newState = "";
		for(int i = 0; i < splitText.length; i++ ){
			newState = newState + splitText[i];
			newState = newState + " ";
		}
		return newState;
	}
}
