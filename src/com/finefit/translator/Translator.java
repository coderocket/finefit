package com.finefit.translator;

import java.text.ParseException;

import com.finefit.base.FileUtils;

import fit.Parse;

public class Translator {

final static String SYSTEM_SPECIFICATION = "SystemSpecification.als";
	 
	private String additionalGroup = "";
	private OperationTable parsedTable;
	private OperationTable fixtureTable = null;
	private InvariantTable invariantTable = null;
	private AtomsTable atomsTable = null;
	private StateTable stateTable = null;
	
	public Translator(){
		
	}
	
	private Parse readHtmlSpecificationFile(String htmlSpecFile) throws ParseException{
		Parse tabularModeledFile = new Parse(FileUtils.readFile(htmlSpecFile));
		return tabularModeledFile;
	}
	
	public String translateSpecificationFile(String htmlSpecFile) throws ParseException{
		Parse tabularModeledFile = readHtmlSpecificationFile(htmlSpecFile);
		FileUtils.writeToFile(SYSTEM_SPECIFICATION, "", false); //clean the file
		FileUtils.writeToFile(SYSTEM_SPECIFICATION, "open util/ordering[State] as StateOrder ", true);
		while (tabularModeledFile != null) {
			tabularModelTranslator(tabularModeledFile.parts);
	        Parse more = tabularModeledFile.more;
	        tabularModeledFile = more;
        }
		FileUtils.writeToFile(SYSTEM_SPECIFICATION, "run{} for 3 but 2 State", true);
		return this.SYSTEM_SPECIFICATION;
	}

	private void tabularModelTranslator(Parse table) {
		parsedTable = new OperationTable(table);
		
		switch (parsedTable.getTableName()){
		case "sample.OperationFixture":
			fixtureTable = new OperationTable(table);
			//additionalGroup = fixtureTable.getAdditionalGroup();
			fixtureTable.printOperationAsAlloy(SYSTEM_SPECIFICATION);
			break;
		case "Invariant":
			invariantTable = new InvariantTable(table);
			invariantTable.printInvariantAsAlloy(SYSTEM_SPECIFICATION);
			break;
		case "Atom":
			atomsTable = new AtomsTable(table);
			atomsTable.printStateAsAlloyX(SYSTEM_SPECIFICATION);
			break;
		case "Relation":
			stateTable = new StateTable(table);
			stateTable.printStateAsAlloy(SYSTEM_SPECIFICATION);
			break;
		}
	}
}
