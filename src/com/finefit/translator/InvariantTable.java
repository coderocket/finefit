package com.finefit.translator;

import java.util.ArrayList;
import java.util.List;

import com.finefit.base.FileUtils;

import fit.Parse;

public class InvariantTable extends AbstractTable {
	
	private Combination combination;
	private String tableContent = "";
	
	public InvariantTable(Parse theTable) {
		super(theTable);
	}

	public void printInvariantAsAlloy(String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "", true);
		printHeader(fileNameToPrint);
		for(int j = 0; j < this.getRowCellsObjects(0).size(); j++){ // move on cols
			printColumnCombinationByIndex(fileNameToPrint, j);//move on rows
		}
		FileUtils.writeToFile(fileNameToPrint, "}", true);
		tableContent = tableContent + "}";
		//System.out.println("}");
	}
	
	
	private void printHeader(String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "pred " + getTableName() + "[s:State] {", true);
		tableContent = tableContent + "pred " + getTableName() + "[s:State] {";
	}
	
	private void printColumnCombinationByIndex(String fileNameToPrint, int colIndex){
		List<String> toFile = new ArrayList<>();
		
		for(int i = 1; i < this.getColCellsObjects(colIndex).size(); i++ ){ // move on all rows
			combination = new Combination(this, new Cell(i,colIndex, this.getCellByIndex(i, colIndex).getText()));
			toFile.add(this.getModifiedInv());
			tableContent = tableContent + this.getModifiedInv();
			
		}
		FileUtils.writeToFile(fileNameToPrint, toFile, true);
	}
	

	public String getModifiedInv(){
		String[] splitText = combination.getCombinationText().split(" ");
		List <Integer> indexes = new ArrayList<>();
		
		for(int i = 0; i < splitText.length; i++){
			if(splitText[i].equals("+") || splitText[i].equals("-") || splitText[i].equals("*") || splitText[i].equals("/")
					|| splitText[i].equals("and") || splitText[i].equals("or") || splitText[i].equals("no")
					|| splitText[i].equals("in")){
				indexes.add(i);
			}
		}
		for(int i = 0; i < indexes.size(); i++){
			if(i+1 == indexes.size()){
				if(indexes.get(i) == 0){
					splitText[indexes.get(i)+1] = "s." + splitText[indexes.get(i)+1]; 
				}else{
					splitText[indexes.get(i)-1] = "s." + splitText[indexes.get(i)-1]; 
					splitText[indexes.get(i)+1] = "s." + splitText[indexes.get(i)+1]; 
				}
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
	
	public String getTableContent(){
		return this.tableContent;
	}
}
