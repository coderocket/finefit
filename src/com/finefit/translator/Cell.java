package com.finefit.translator;

import java.util.ArrayList;
import java.util.List;

public class Cell {

	private String text;
	private int x;
	private int y;
	
	public Cell(int rowIndex, int colIndex, String cellText){
		this.x = rowIndex;
		this.y = colIndex;
		this.text = cellText;
	}
	
	public Cell(int rowIndex, int colIndex){
		this.x = rowIndex;
		this.y = colIndex;
	}
	
	public int getRowIndex(){
		return this.x;
	}
	
	public int getColIndex(){
		return this.y;
	}
	
	public String getText(){
		return this.text;
	}
	
	public void setRowIndex(int rowIndex){
		this.x = rowIndex;
	}
	
	public void setColIndex(int colIndex){
		this.y = colIndex;
	}
	
	public void setText(String cellText){
		this.text = cellText;
	}
	
	public String getModifiedText(){
		String[] splitText = this.text.split(" ");
		List <Integer> indexes = new ArrayList<>();
		
		for(int i = 0; i < splitText.length; i++){
			if(splitText[i].equals("+") || splitText[i].equals("-") || splitText[i].equals("*") || splitText[i].equals("/")
					|| splitText[i].equals("and") || splitText[i].equals("or")){
				indexes.add(i);
			}
		}
		
		if(indexes.isEmpty()){
			splitText[splitText.length-1] = "s." + splitText[splitText.length-1];
		}
	
		for(int i = 0; i < indexes.size(); i++){
			if(i+1 == indexes.size()){
				splitText[indexes.get(i)-1] = "s." + splitText[indexes.get(i)-1]; 
				splitText[indexes.get(i)+1] = "s." + splitText[indexes.get(i)+1]; 
			}else {
				splitText[indexes.get(i)+1] = "s." + splitText[indexes.get(i)+1]; 
			}
		}
		
		String newState = "";
		for(int i = 0; i < splitText.length; i++ ){
			newState = newState + splitText[i];
			newState = newState + " ";
		}
		return newState;
	}
}
