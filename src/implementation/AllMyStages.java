/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.AllMyLatches.*;
import tools.InstructionSequence;
import utilitytypes.EnumOpcode;
import baseclasses.InstructionBase;
import baseclasses.PipelineRegister;
import baseclasses.PipelineStageBase;
import voidtypes.VoidLatch;
import baseclasses.CpuCore;

/**
 * The AllMyStages class merely collects together all of the pipeline stage 
 * classes into one place.  You are free to split them out into top-level
 * classes.
 * 
 * Each inner class here implements the logic for a pipeline stage.
 * 
 * It is recommended that the compute methods be idempotent.  This means
 * that if compute is called multiple times in a clock cycle, it should
 * compute the same output for the same input.
 * 
 * How might we make updating the program counter idempotent?
 * 
 * @author
 */
public class AllMyStages {
    /*** Fetch Stage ***/
    static class Fetch extends PipelineStageBase<VoidLatch,FetchToDecode> {
        public Fetch(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        @Override
        public String getStatus() {
            // Generate a string that helps you debug.
            return null;
        }

        @Override
        public void compute(VoidLatch input, FetchToDecode output) {
            GlobalData globals = (GlobalData)core.getGlobalResources();
            int pc = globals.program_counter;
            // Fetch the instruction
            InstructionBase ins = globals.program.getInstructionAt(pc);
           
            
            
            
            
            
           
            
            
            
           globals.program_counter++;
            
            
            
            
            if (ins.isNull()) return;

            System.out.println("in Fetch instruction is " +ins);
            // Do something idempotent to compute the next program counter.
            
            // Don't forget branches, which MUST be resolved in the Decode
            // stage.  You will make use of global resources to commmunicate
            // between stages.
            
            // Your code goes here...
            
            output.setInstruction(ins);
        }
        
        @Override
        public boolean stageWaitingOnResource() {
            // Hint:  You will need to implement this for when branches
            // are being resolved.
            return false;
        }
        
        
        /**
         * This function is to advance state to the next clock cycle and
         * can be applied to any data that must be updated but which is
         * not stored in a pipeline register.
         */
        @Override
        public void advanceClock() {
            // Hint:  You will need to implement this help with waiting
            // for branch resolution and updating the program counter.
            // Don't forget to check for stall conditions, such as when
            // nextStageCanAcceptWork() returns false.
        	System.out.println("in advanceClock");
        	
        	
        }
        
       
        
    }

    
    /*** Decode Stage ***/
    static class Decode extends PipelineStageBase<FetchToDecode,DecodeToExecute> {
        public Decode(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        @Override
        public boolean stageWaitingOnResource() {
            // Hint:  You will need to implement this to deal with 
            // dependencies.
            return false;
        }
        

        @Override
        public void compute(FetchToDecode input, DecodeToExecute output) {
            InstructionBase ins = input.getInstruction();
            
            
            System.out.println("in Decode ins is"+ins);
            // These null instruction checks are mostly just to speed up
            // the simulation.  The Void types were created so that null
            // checks can be almost completely avoided.
            if (ins.isNull()) return;
            
            GlobalData globals = (GlobalData)core.getGlobalResources();
            int[] regfile = globals.register_file;
            
        	
       	 int ipos =ins.getInstructionString().indexOf(";");
            
            //Delete the comments in the instruction....
       	 String s= ins.getInstructionString().substring(0, ipos);
            
            String[] instarray = s.split("\\s+");
            
            for (int i = 0; i < instarray.length; i++) {
               
            	instarray[i] = instarray[i].replaceAll("[^\\w]", "");
               // System.out.println("ELEMENT at :"+i+ "is"+instarray[i] );
            }
       	
       	
            String st= ins.getOpcode().toString().toLowerCase();
       	
       	switch(st){
       		
       		
       	
       		case  "movc":
       			System.out.println("type is movc");
       			
       			globals.hm.put(instarray[2], 0);
       			ins.getSrc1().setValue(100);
       			ins.getSrc2().setValue(Integer.parseInt(instarray[3]));
       			
       			break;
       			
       		case "add" :
       			
       			System.out.println("type is add");
       			
       			break;
       			
       			
       		case "cmp" :
       			
       			System.out.println("type is cmp");
       			
       			break;
       			
       		case "store":
       			
       			System.out.println("type is store");
       			
       			break;
       			
       		case "HALT":
       			
       			System.out.println("type is halt");
       			
       			break;
       			
       		case "load":
       			
       			
       			break;
       		
       		case "bra":
       			
       			
       			break;
       			
       			
       			
       		default :
       			throw new IllegalArgumentException("Invalid Exception Type");
       			
       			
       		
       	}
       	
       
            
           
           
            
            // Do what the decode stage does:
            // - Look up source operands
            // - Decode instruction
            // - Resolve branches            

            output.setInstruction(ins);
            // Set other data that's passed to the next stage.
        }
        
        public void typecheck(InstructionBase ib){}
        
        
        
        
        
        
        
        
    }
    

    /*** Execute Stage ***/
    static class Execute extends PipelineStageBase<DecodeToExecute,ExecuteToMemory> {
        public Execute(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(DecodeToExecute input, ExecuteToMemory output) {
            InstructionBase ins = input.getInstruction();
            
            System.out.println("in Execute ins is :"+ins);
            
            if (ins.isNull()) return;

            int source1 = ins.getSrc1().getValue();
            int source2 = ins.getSrc2().getValue();
            int oper0 =   ins.getOper0().getValue();
            
            System.out.println("Source1 :"+source1+"Source2 :"+source2+"oper0 :"+oper0 );

            int result = MyALU.execute(ins.getOpcode(), source1, source2, oper0);
            
            System.out.println("Result is :"+result);
                        
            // Fill output with what passes to Memory stage...
            output.setInstruction(ins);
            // Set other data that's passed to the next stage.
        }
    }
    

    /*** Memory Stage ***/
    static class Memory extends PipelineStageBase<ExecuteToMemory,MemoryToWriteback> {
        public Memory(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(ExecuteToMemory input, MemoryToWriteback output) {
            InstructionBase ins = input.getInstruction();
            
            System.out.println("in Memory ins is "+ins);
            if (ins.isNull()) return;

            GlobalData globals = (GlobalData)core.getGlobalResources();
            
            // Access memory...

            System.out.println("Access memory :"+ins.getSrc1().getValue() );
           
           
            
            
            output.setInstruction(ins);
            // Set other data that's passed to the next stage.
        }
    }
    

    /*** Writeback Stage ***/
    static class Writeback extends PipelineStageBase<MemoryToWriteback,VoidLatch> {
        public Writeback(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(MemoryToWriteback input, VoidLatch output) {
            InstructionBase ins = input.getInstruction();
            
            System.out.println("in WB ins is"+ins);
            if (ins.isNull()) return;

            // Write back result to register file
            GlobalData globals = (GlobalData)core.getGlobalResources();
            
            int[] regfile = globals.register_file;
            
            
            
            
            for(int i=0;i<regfile.length;) {
            	
            	if(regfile[i]!=0) {
            		
            		i++;
            		
            	}else {
            		regfile[i]=ins.getSrc1().getValue();
            		break;
            	}
            	
            }
            
            for(int i=0;i<regfile.length;i++) {
            	
            	System.out.println(regfile[i]);
            	
            }
            
            
            if (input.getInstruction().getOpcode() == EnumOpcode.HALT) {
                // Stop the simulation
            }
        }
    }
}
