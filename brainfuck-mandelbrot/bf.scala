// Part 1 about an Interpreter for the Brainf*** language
//========================================================


// representation of Bf memory 

type Mem = Map[Int, Int]


// (1) Write a function that takes a file name as argument and
// and requests the corresponding file from disk. It Returns the
// content of the file as a String. If the file does not exists,
// the function should Return the empty string.

import io.Source
import scala.util._

def load_bff(name: String) : String ={
      Try(Source.fromFile(name).mkString).getOrElse("")
}

// (2) Complete the functions for safely reading  
// and writing brainf*** memory. Safely read should
// Return the value stored in the Map for a given memory
// pointer, provided it exists; otherwise it Returns 0. The
// writing function generates a new Map with the
// same data, except at the given memory pointer the
// value v is stored.


def sread(mem: Mem, mp: Int) : Int =  mem.getOrElse(mp, 0)

def write(mem: Mem, mp: Int, v: Int) : Mem = mem + (mp -> v)
//
//write(Map(), 1, 2) == Map(1 -> 2)
//write(Map(1 -> 0), 1, 2) == Map(1 -> 2)
//

// (3) Implement the two jumping instructions in the 
// brainf*** language. In jumpRight, given a program and 
// a program counter move the program counter to the right 
// until after the *matching* ]-command. Similarly, 
// jumpLeft implements the move to the left to just after
// the *matching* [-command.

def jumpRight(prog: String, pc: Int, level: Int) : Int = {
if(pc+1 < prog.length){
      val newpc = pc+1
      prog(newpc) match{
            case ']' => if(level == 0) newpc+1 else jumpRight(prog, newpc, level-1)
            case '[' => jumpRight(prog,newpc, level + 1)
            case _ => jumpRight(prog, newpc, level)
      }
}
      else prog.length
}


def jumpLeft(prog: String, pc: Int, level: Int) : Int = {
      if(pc >= 0){
            val newpc = pc-1
            prog(pc) match{
                  case '[' => if(level == 0) pc+1 else jumpLeft(prog, newpc, level-1)
                  case ']' =>jumpLeft(prog, newpc, level + 1) 
                  case _ => jumpLeft(prog, newpc, level)
            }
      }
      else pc
}

// (4) Complete the compute function that interprets (runs) a brainf***
// program: the arguments are a program (represented as a string), a program 
// counter, a memory counter and a brainf*** memory. It Returns the
// memory at the stage when the execution of the brainf*** program
// finishes. The interpretation finishes once the program counter
// pc is pointing to something outside the program string.
// If the pc points to a character inside the program, the pc, 
// memory pointer and memory need to be updated according to 
// rules of the brainf*** language. Then, recursively, the compute 
// function continues with the command at the new program
// counter. 
//
// Implement the run function that calls compute with the program
// counter and memory counter set to 0.


def compute(prog: String, pc: Int, mp: Int, mem: Mem) : Mem = {
if(pc == prog.length || pc == -1)
      mem
else{
prog(pc) match{
      case '>' => compute(prog, pc+1, mp+1,mem)
      case '<' => compute(prog, pc+1, mp-1, mem)
      case '+' => compute(prog, pc+1, mp, write(mem, mp, sread(mem, mp)+1))
      case '-' => compute(prog, pc+1, mp, write(mem, mp, sread(mem, mp)-1))
      case '.' => {print(sread(mem,mp).toChar);compute(prog, pc+1,mp,mem)}
      case ',' => compute(prog, pc+1, mp, write(mem, mp, Console.in.read().toByte))
      case '[' => if(sread(mem, mp)==0) 
                        compute(prog, jumpRight(prog, pc+1, 0), mp,mem) 
                  else 
                        compute(prog, pc+1, mp,mem)

      case ']' => if(sread(mem, mp)!=0)
                         compute(prog, jumpLeft(prog, pc-1,0), mp, mem) 
                         
                  else 
                        compute(prog, pc+1,mp, mem)
      case _ => compute(prog, pc+1, mp, mem)
            }
      }
}

def run(prog: String, m: Mem = Map()) = compute(prog, 0, 0, m)