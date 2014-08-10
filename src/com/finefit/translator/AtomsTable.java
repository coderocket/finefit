package com.finefit.translator;

import com.finefit.base.FileUtils;
import fit.Parse;

public class AtomsTable extends AbstractTable {

	private String tableContent = "";
	
	public AtomsTable(Parse theTable) {
		super(theTable);
	}

	public void printStateAsAlloyX(String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "", true);
		printHeaderX(fileNameToPrint);
	}

	public void printStateAsAlloy(String additionalGroup, String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "", true);
		printHeader(additionalGroup,fileNameToPrint);
	}
	
	private void printHeaderX(String fileNameToPrint){
		for (int i = 1; i < getColCellsObjects(0).size(); i++) {
			FileUtils.writeToFile(fileNameToPrint, "sig " + getRowCellsText(i).get(0) + "{}\n", true);
		}
	}

	private void printHeader(String additionalGroup, String fileNameToPrint){
		String groups[] = additionalGroup.split(",");// if there is more than one group in the additionalgroup
		for(int i = 0; i < groups.length; i++){
			if(groups[i].contains(":")){
				int startIndex = groups[i].indexOf(":");
				FileUtils.writeToFile(fileNameToPrint, "sig " + groups[i].substring(startIndex+1) + "{}", true);
				tableContent = tableContent + "sig " + groups[i].substring(startIndex+1) + "{}";
			}
		}
		
	}
	
	public String getTableContent(){
		return this.tableContent;
	}
}
