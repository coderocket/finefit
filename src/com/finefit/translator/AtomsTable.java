package com.finefit.translator;

import com.finefit.base.FileUtils;
import fit.Parse;

public class AtomsTable extends AbstractTable {

	private String tableContent = "";
	
	public AtomsTable(Parse theTable) {
		super(theTable);
	}

	public void printStateAsAlloy(String additionalGroup, String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "", true);
		printHeader(additionalGroup,fileNameToPrint);
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
