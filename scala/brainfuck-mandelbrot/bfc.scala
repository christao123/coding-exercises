


type Mem = Map[Int, Int]


import io.Source
import scala.util._

def load_bff(name: String) : String ={
      Try(Source.fromFile(name).mkString).getOrElse("")
}


def sread(mem: Mem, mp: Int) : Int =  mem.getOrElse(mp, 0)

def write(mem: Mem, mp: Int, v: Int) : Mem = mem + (mp -> v)


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


def time_needed[T](n: Int, code: => T) = {
  val start = System.nanoTime()
  for (i <- 0 until n) code
  val end = System.nanoTime()
  (end - start)/(n * 1.0e9)
}

 
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

