package com.finefit.translator;

import java.util.ArrayList;
import java.util.List;

import com.finefit.base.FileUtils;

import fit.Parse;

public class OperationTable extends AbstractTable {

	private Combination combination;
	private String tableContent = "";
	
	public OperationTable(Parse theTable) {
		super(theTable);
	}
	
	/**
	 * Print each column combination as Alloy Code
	 * @param fileNameToPrint
	 */
	public void printOperationAsAlloy(String fileNameToPrint){
		//FPUtils.writeToFile(fileNameToPrint, "", false); // i removed this line since it might be more than one operation table so it won't override the tables
		printOperationHeader(fileNameToPrint);
		for(int j = 1; j < this.getRowCellsObjects(2).size(); j++){ // move on cols
			printColumnCombinationByIndex(fileNameToPrint, j);
		}
		FileUtils.writeToFile(fileNameToPrint, "}", true);
		tableContent = tableContent + "}";
		//System.out.println("}");
	}
	
	
	/**
	 * Print column combination as Alloy Code by column index 
	 * @param fileNameToPrint
	 * @param colIndex
	 */
	public void printColumnCombinationByIndex(String fileNameToPrint, int colIndex){
		List<String> toFile = new ArrayList<>();
		boolean startPrinting = true;
		
		
		for(int i = 3; i < this.getColCellsObjects(0).size(); i++ ){ // move on all rows
			combination = new Combination(this, new Cell(2,colIndex), new Cell(i,0));
			if(startPrinting){
				
				toFile.add("(" + this.getCellByIndex(2, colIndex).getModifiedText() + "=> {");
				tableContent = tableContent + "(" +  this.getCellByIndex(2, colIndex).getModifiedText() + "=> {";
				
				startPrinting = false;
			}

			toFile.add("s'." + this.getCellByIndex(i, 0).getText() + " = " + combination.getModifiedState());
			tableContent = tableContent + "s'." + this.getCellByIndex(i, 0).getText() + " = " + combination.getModifiedState();
			if(i+1 == this.getColCellsObjects(0).size()){
				toFile.add( "})");
				tableContent = tableContent + "})";
			}
			
			
		}
		FileUtils.writeToFile(fileNameToPrint, toFile, true);
	}
	
	private void printOperationHeader(String fileNameToPrint){
		FileUtils.writeToFile(fileNameToPrint, "pred " + getOperationName() + "[s,s':State" +  getAdditionalGroup()  + "]" + " {", true);
		tableContent = tableContent + "pred " + getOperationName() + "[s,s':State" +  getAdditionalGroup()  + "]" + " {";
		FileUtils.writeToFile(fileNameToPrint, "s' = s.next", true);
		tableContent = tableContent + "s' = s.next";
		FileUtils.writeToFile(fileNameToPrint, "s != s'", true);
		tableContent = tableContent + "s != s'";
		FileUtils.writeToFile(fileNameToPrint, getConstraint(), true);
		tableContent = tableContent + getConstraint();
	}

	// return in each operation the constraint
	private String getConstraint(){
		String constraint = "(";
		for(int i = 1; i < this.getRowCellsObjects(2).size(); i++){
			constraint = constraint + this.getCellByIndex(2, i).getModifiedText();
			if(i + 1 < this.getRowCellsObjects(2).size()){
				constraint = constraint + " or ";
			}
		}
		constraint = constraint + ")";
		return constraint;
	}

	/**
	 * Get Operation name
	 * @return String
	 */
	public String getOperationName(){
		String name = this.getCellByIndex(1, 0).getText(); // assuming operation name will be always at index (0,0)
		if(name != null){
			return name;
		}
		return null;
	}
	
	/**
	 * Get additional group e.g 'u:User' , Assuming that new group will be added to table at index(1,0) always
	 * @return String
	 */
	public String getAdditionalGroup(){
		String newGroup = "";
		if(!this.getCellTextByIndex(2, 0).equals("")){ 
			return newGroup =  ", " + this.getCellTextByIndex(2, 0);
		}
		return newGroup;
	}
	
	public String getTableContent(){
		return this.tableContent;
	}
}
