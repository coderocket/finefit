package com.finefit.translator;

import com.finefit.base.FileUtils;

import fit.Parse;

public class StateTable extends AbstractTable {

	private String tableContent = "";
	
	public StateTable(Parse theTable) {
		super(theTable);
	}
	
	public void printStateAsAlloy(String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "", true);
		printHeader(fileNameToPrint);
		
		for(int i = 1; i < this.getColCellsObjects(0).size(); i++ ){ // move on all rows
			if(i+1 < this.getColCellsObjects(0).size() ){
				FileUtils.writeToFile(fileNameToPrint, getRowCellsText(i).get(0)  + " : " + getRowCellsText(i).get(1) + ",", true);	
				tableContent = tableContent + getRowCellsText(i).get(0)  + " : " + getRowCellsText(i).get(1) + ",";
			}else {
				FileUtils.writeToFile(fileNameToPrint, getRowCellsText(i).get(0)  + " : " + getRowCellsText(i).get(1), true);	
				tableContent = tableContent + getRowCellsText(i).get(0)  + " : " + getRowCellsText(i).get(1);
			}
			
		}
		
		FileUtils.writeToFile(fileNameToPrint, "}", true);
		tableContent = tableContent + "}";
	}
	
	private void printHeader(String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "sig " + "State {", true);
		tableContent = tableContent + "sig " + "State {";
	}
	
	
	public String getTableContent(){
		return this.tableContent;
	}
}
