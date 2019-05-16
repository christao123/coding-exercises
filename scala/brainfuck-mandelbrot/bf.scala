
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