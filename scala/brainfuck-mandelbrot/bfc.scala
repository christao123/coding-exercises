// Part 2 about a "Compiler" for the Brainf*** language
//======================================================
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


// testcases
//jumpRight("""--[..+>--],>,++""", 3, 0)         // => 10
//jumpLeft("""--[..+>--],>,++""", 8, 0)          // => 3
//jumpRight("""--[..[+>]--],>,++""", 3, 0)       // => 12
//jumpRight("""--[..[[-]+>[.]]--],>,++""", 3, 0) // => 18
//jumpRight("""--[..[[-]+>[.]]--,>,++""", 3, 0)  // => 22 (outside)
//jumpLeft("""[******]***""", 7, 0)              // => -1 (outside)



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

// If you need any auxiliary function, feel free to 
// implement it, but do not make any changes to the
// templates below.

def time_needed[T](n: Int, code: => T) = {
  val start = System.nanoTime()
  for (i <- 0 until n) code
  val end = System.nanoTime()
  (end - start)/(n * 1.0e9)
}

// DEBUGGING INFORMATION!!!
//
// Compiler, even real ones, are fiendishly difficult to get
// to produce correct code. The point is that for example for
// the Sierpinski program, they need to still generate code
// that displays such a triangle. If yes, then one usually
// can take comfort that all is well. If not, then something
// went wrong during the optimisations.


// ADVANCED TASKS
//================

// (5) Write a function jtable that precomputes the "jump
//     table" for a bf-program. This function takes a bf-program 
//     as an argument and Returns a Map[Int, Int]. The 
//     purpose of this map is to record the information
//     that given on the position pc is a '[' or a ']',
//     then to which pc-position do we need to jump next?
// 
//     For example for the program
//    
//       "+++++[->++++++++++<]>--<+++[->>++++++++++<<]>>++<<----------[+>.>.<+<]"
//
//     we obtain the map
//
//       Map(69 -> 61, 5 -> 20, 60 -> 70, 27 -> 44, 43 -> 28, 19 -> 6)
//  
//     This states that for the '[' on position 5, we need to
//     jump to position 20, which is just after the corresponding ']'.
//     Similarly, for the ']' on position 19, we need to jump to
//     position 6, which is just after the '[' on position 5, and so
//     on. The idea is to not calculate this information each time
//     we hit a bracket, but just look up this information in the 
//     jtable. You can use the jumpLeft and jumpRight functions
//     from Part 1 for calculating the jtable.
//
//     Then adapt the compute and run functions from Part 1 in order 
//     to take advantage of the information stored in the jtable. 
//     This means whenever jumpLeft and jumpRight was called previously,
//     you should look up the jump address in the jtable.
 
def jtable(pg: String) : Map[Int, Int] = 
      (0 until pg.length).collect { index => pg(index) match {
            case '[' => (index -> jumpRight(pg, index + 1, 0))
            case ']' => (index -> jumpLeft(pg, index - 1, 0))
      }}.toMap


def compute2(prog: String,table: Map[Int, Int], pc: Int, mp: Int, mem: Mem) : Mem = {
if(pc == prog.length || pc == -1)
      mem
else{
prog(pc) match{
      case '>' => compute2(prog,table, pc+1, mp+1,mem)
      case '<' => compute2(prog,table, pc+1, mp-1, mem)
      case '+' => compute2(prog,table, pc+1, mp, write(mem, mp, sread(mem, mp)+1))
      case '-' => compute2(prog,table, pc+1, mp, write(mem, mp, sread(mem, mp)-1))
      case '.' => {print(sread(mem,mp).toChar);compute2(prog,table, pc+1,mp,mem)}
      case ',' => compute2(prog,table, pc+1, mp, write(mem, mp, Console.in.read().toByte))
      case '[' => if(sread(mem, mp)==0) 
                        compute2(prog,table, table(pc), mp,mem) 
                  else 
                        compute2(prog,table, pc+1, mp,mem)

      case ']' => if(sread(mem, mp)!=0)
                         compute2(prog,table,table(pc), mp, mem) 
                  else 
                        compute2(prog,table, pc+1,mp, mem)
      case _ => compute2(prog,table, pc+1, mp, mem)
            }
      }
}

def run2(prog: String, m: Mem = Map()) = compute2(prog, jtable(prog), 0, 0, m)

// (6) Write a function optimise which deletes "dead code" (everything
// that is not a bf-command) and also replaces substrings of the form
// [-] by a new command 0. The idea is that the loop [-] just resets the
// memory at the current location to 0. In the compute3 and run3 functions
// below you implement this command by writing the number 0 to mem(mp), 
// that is write(mem, mp, 0). 
//
// The easiest way to modify a string in this way is to use the regular
// expression """[^<>+-.,\[\]]""", which recognises everything that is 
// not a bf-command and replace it by the empty string. Similarly the
// regular expression """\[-\]""" finds all occurrences of [-] and 
// by using the Scala method .replaceAll you can replace it with the 
// string "0" standing for the new bf-command.

def optimise(s: String) : String = s.replaceAll("""[^<>+-.,\[\]]""", "").replaceAll("""\[-\]""", "0")

def compute3(prog: String,table: Map[Int, Int], pc: Int, mp: Int, mem: Mem) : Mem = {
if(pc == prog.length || pc == -1)
      mem
else{
prog(pc) match{
      case '0' => compute3(prog, table, pc+1, mp, write(mem, mp, 0))
      case '>' => compute3(prog,table, pc+1, mp+1,mem)
      case '<' => compute3(prog,table, pc+1, mp-1, mem)
      case '+' => compute3(prog,table, pc+1, mp, write(mem, mp, sread(mem, mp)+1))
      case '-' => compute3(prog,table, pc+1, mp, write(mem, mp, sread(mem, mp)-1))
      case '.' => {print(sread(mem,mp).toChar);compute3(prog,table, pc+1,mp,mem)}
      case ',' => compute3(prog,table, pc+1, mp, write(mem, mp, Console.in.read().toByte))
      case '[' => if(sread(mem, mp)==0) 
                        compute3(prog,table, table(pc), mp,mem) 
                  else 
                        compute3(prog,table, pc+1, mp,mem)

      case ']' => if(sread(mem, mp)!=0)
                         compute3(prog,table,table(pc), mp, mem) 
                  else 
                        compute3(prog,table, pc+1,mp, mem)
      case _ => compute3(prog,table, pc+1, mp, mem)
            }
      }

}
def run3(pg: String, m: Mem = Map()) = {
      val newpg = optimise(pg)
      compute3(newpg, jtable(newpg), 0, 0, m)
}

// (7)  Write a function combine which replaces sequences
// of repeated increment and decrement commands by appropriate
// two-character commands. For example for sequences of +
//
//              orig bf-cmds  | replacement
//            ------------------------------
//              +             | +A 
//              ++            | +B
//              +++           | +C
//                            |
//              ...           |
//                            | 
//              +++....+++    | +Z
//                (where length = 26)
//
//  Similar for the bf-command -, > and <. All other commands should
//  be unaffected by this change.
//
//  Adapt the compute4 and run4 functions such that they can deal
//  appropriately with such two-character commands.

def modify_sequence(prog : String, start_seq : Int, end_seq: Int, char_to_find : Char) : (String, Int) = {
     if(end_seq-start_seq == 25){
            val alphabet = List("A", "B", "C", "D", "E", "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z") 
            (prog.substring(0, start_seq) + char_to_find + alphabet(end_seq-start_seq)+ prog.substring(end_seq+1, prog.length), end_seq-(end_seq-start_seq)+1)
     }
      else{
            if(end_seq < prog.length - 1){
                        if(prog(end_seq + 1) == char_to_find){
                              modify_sequence(prog, start_seq, end_seq+1, char_to_find)   
                        }
                        else{
                              val alphabet = List("A", "B", "C", "D", "E", "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
                              (prog.substring(0, start_seq) + char_to_find + alphabet(end_seq-start_seq)+ prog.substring(end_seq+1, prog.length), end_seq-(end_seq-start_seq)+1)
                        }
                  }
                  else{
                        val alphabet = List("A", "B", "C", "D", "E", "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z")
                  ( prog.substring(0, start_seq) + char_to_find + alphabet(end_seq-start_seq)+ prog.substring(end_seq+1, prog.length), end_seq-(end_seq-start_seq)+1)
                  }
      }
}

def combine_string(s: String, i : Int) : String ={
 (i+1 < s.length) match {
      case true => {
            val c = s(i)
            if(List('<','>','+','-').contains(c)){
                  val n = modify_sequence(s, i, i, s(i))
                  combine_string(n._1, n._2+1)
            }
            else{
                  combine_string(s, i+1)
            }
      }
      case false => s
      }
}

def combine(s: String) : String = {
      combine_string(s,0)
}

def compute4(prog: String, table: Map[Int, Int], pc: Int, mp: Int, mem: Mem) : Mem = {
if(pc == prog.length || pc == -1)
      mem
else{
prog(pc) match{
      case '0' => compute4(prog, table, pc+1, mp, write(mem, mp, 0))
      case '>' => compute4(prog,table, pc+1, mp+(prog(pc + 1) - '@'),mem)
      case '<' => compute4(prog,table, pc+1, mp-(prog(pc + 1) - '@'), mem)
      case '+' => compute4(prog,table, pc+1, mp, write(mem, mp, sread(mem, mp)+(prog(pc + 1) - '@')))
      case '-' => compute4(prog,table, pc+1, mp, write(mem, mp, sread(mem, mp)-(prog(pc + 1) - '@')))
      case '.' => {print(sread(mem,mp).toChar);compute4(prog,table, pc+1,mp,mem)}
      case ',' => compute4(prog,table, pc+1, mp, write(mem, mp, Console.in.read().toByte))
      case '[' => if(sread(mem, mp)==0) 
                        compute4(prog,table, table(pc), mp,mem) 
                  else 
                        compute4(prog,table, pc+1, mp,mem)

      case ']' => if(sread(mem, mp)!=0)
                         compute4(prog,table,table(pc), mp, mem) 
                  else 
                        compute4(prog,table, pc+1,mp, mem)
      case _ => compute4(prog,table, pc+1, mp, mem)
            }
      }
}

def run4(pg: String, m: Mem = Map()) = { 
  val optimised = combine(optimise(pg))
  compute4(optimised, jtable(optimised), 0, 0, m)
}

