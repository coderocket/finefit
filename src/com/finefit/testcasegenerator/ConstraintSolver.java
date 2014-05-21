package com.finefit.testcasegenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kodkod.ast.Formula;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.Bounds;
import kodkod.instance.Universe;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprQt;
import edu.mit.csail.sdg.alloy4compiler.ast.Func;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.BoundsExtractor;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

public class ConstraintSolver {

	private A4Reporter a4reporter = new A4Reporter();
	private A4Options a4options = new A4Options();
	private A4Solution a4solution;
	private Solver solver = new Solver();
	private Command command; 
	private Bounds bounds;
	private Formula formula;
	private Solution solution;
	private CompModule world;
	private Universe universe;
	
	public ConstraintSolver(CompModule world){
		this.world = world;
		this.solver.options().setSolver(SATFactory.DefaultSAT4J);
		setCommand(world);
		setA4Solution(this.a4reporter, this.world.getAllReachableSigs(), this.command, this.a4options);
		setBounds(this.a4solution);
		setFormula(this.a4solution, getRandomOperation());
		setSolution(this.solver, this.formula, this.bounds);
		setUnivers(this.solution);
	}
	
	public void setCommand(CompModule world){
		this.command = world.getAllCommands().get(0);
	}
	
	public Command getCommand(){
		return this.command;
	}
	
	public void setA4Solution(A4Reporter a4Reporter, ConstList<Sig> sigs, Command command, A4Options a4Options){
		try {
			this.a4solution = TranslateAlloyToKodkod.execute_command(a4Reporter, sigs, command, a4Options);
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public A4Solution getA4Solution(){
		return this.a4solution;
	}
	
	public void setBounds(A4Solution a4Solution){
		this.bounds = new BoundsExtractor(a4Solution).getBounds();
	}
	
	public Bounds getBounds(){
		return this.bounds;
	}
		
	public void setFormula(A4Solution a4Solution, Operation func){
		try {
			this.formula = (Formula) TranslateAlloyToKodkod.alloy2kodkod(a4Solution, ExprQt.Op.SOME.make(null,null, func.getOperation().decls, func.getOperation().getBody()));
		} catch (Err e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Formula getFormula(){
		return this.formula;
	}
	
	public void setSolution(Solver solver, Formula formula, Bounds bounds){
		this.solution = solver.solve(formula, bounds);
	}
	
	public Solution getSolution(){
		return this.solution;
	}
	
	public void setUnivers(Solution solution){
		this.universe = solution.instance().universe();
	}
	
	public Universe getUniverse(){
		return this.universe;
	}
	
	private List<Operation> getListOfFunc(){
		SafeList<Func> funcs =  this.world.getAllFunc();
		List<Operation> ops = new ArrayList<>();
		for(Func func: funcs){
			ops.add(new Operation(func));
		}
		return ops;
		
	}
	
	public List<Operation> getListOfOperations(){
		List<Operation> listOfOps = new ArrayList<>();
		for(Operation func : getListOfFunc()){
			if(func.getOperation().isPred && func.getOperation().count() >= 2){// Operation is pred with only more than 2 parameters 
				listOfOps.add(func);
			}
		}
		return listOfOps;
	}
	
	public Operation getRandomOperation(){
		List<Operation> funcs = getListOfFunc();
		Random random = new Random(); 
		SafeList<Operation> operations = new SafeList<>();
		
		for (Operation func : funcs) {
			if (func.getOperation().isPred && func.getOperation().count() >= 2) {// Operation is pred with only more than 2 parameters 
				operations.add(new Operation(func.getOperation()));
			}
		}
		
		int randomNumber = random.nextInt(operations.size());
		return operations.get(randomNumber);
	}
}
