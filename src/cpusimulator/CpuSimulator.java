/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpusimulator;

import implementation.GlobalData;
import implementation.MyCpuCore;
import java.io.IOException;
import tools.InstructionSequence;

/**
 *
 * @author millerti
 */
public class CpuSimulator {
    
    public static boolean printStagesEveryCycle = false;
    public static int cyc=0;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {
        InstructionSequence seq = new InstructionSequence();
        seq.loadFile("samples/sieve.asm");
        seq.printProgram();
        
       cyc=seq.cycle_counter;
       System.out.println("CYCLES is "+cyc);
       
        
        
        MyCpuCore core = new MyCpuCore();
        core.loadProgram(seq);
        core.runProgram();
    }    
}
