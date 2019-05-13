import scala.annotation.tailrec;
// Part 2 about finding a single tour for a board using the Warnsdorf Rule
//=========================================================================

// Part 1 about finding Knight's tours
//=====================================

// If you need any auxiliary function, feel free to 
// implement it, but do not make any changes to the
// templates below. Also have a look whether the functions
// at the end are of any help.



type Pos = (Int, Int)    // a position on a chessboard
type Path = List[Pos]    // a path...a list of positions

//(1) Complete the function that tests whether the position x
//    is inside the board and not yet element in the path.

def is_legal(dim: Int, path: Path, x: Pos) : Boolean = {
    if(x._1 >= 0 && x._1 < dim && x._2 >= 0 && x._2 < dim
        && !path.contains(x)
    ){
        true
    }
    else{
      false
    }
}

//(2) Complete the function that calculates for a position x
//    all legal onward moves that are not already in the path.
//    The moves should be ordered in a "clockwise" manner.

def get_possible_translations() : List[Pos] = {
  List(
      (1 , 2),
      (2 , 1),
      (2 , -1),
      (1, -2),
      (-1, -2),
      (-2 , -1),
      (-2 , 1),
      (-1 , 2),
  )
}

def legal_moves(dim: Int, path: Path, x: Pos) : List[Pos] = {
get_possible_translations.map( traslation =>(
    
     if(is_legal(dim, path, (traslation._1+x._1 , traslation._2+x._2))){
        Some(traslation._1+x._1 , traslation._2+x._2)
     }
      else{
        None
      }
      )
    ).flatten

}


//(3) Complete the two recursive functions below.
//    They exhaustively search for knight's tours starting from the
//    given path. The first function counts all possible tours,
//    and the second collects all tours in a list of paths.
  def count_tours(dim: Int, path: Path) : Int = {
  
    if(path == Nil)
    0
    else{
      val head = path(0)

      val possible_moves = legal_moves(dim, path, head)

      if(possible_moves.length == 0){
          if(path.length == (dim*dim)){
            1
        }
        else{
          0
        }
      }
      else{
        possible_moves.map(move => count_tours(dim , move::path)).sum
      }
      }
  }

def enum_tours(dim: Int, path: Path) : List[Path] = {
if(path == Nil)
    List()
    else{
      val head = path(0)

      val possible_moves = legal_moves(dim, path, head)

      if(possible_moves.length == 0){
      
        if(path.length == (dim*dim)){
            List(path)
        }
        else{
          List()
        }
      }
      else{
        possible_moves.map(move => enum_tours(dim , move::path)).flatten
      }
      }
}


//(4) Implement a first-function that finds the first 
//    element, say x, in the list xs where f is not None. 
//    In that case Return f(x), otherwise None. If possible,
//    calculate f(x) only once.
@tailrec
def first(xs: List[Pos], f: Pos => Option[Path]) : Option[Path] = {
    if(xs == Nil){
      None
    }
    else{
        val head = f(xs.head)
        if( head.isDefined ){
          head
        }
        else{
          first(xs.tail, f)
        }
     }
}


// test cases
//def foo(x: (Int, Int)) = if (x._1 > 3) Some(List(x)) else None
//
//first(List((1, 0),(2, 0),(3, 0),(4, 0)), foo)   // Some(List((4,0)))
//first(List((1, 0),(2, 0),(3, 0)), foo)          // None




//(5) Implement a function that uses the first-function from (4) for
//    trying out onward moves, and searches recursively for a
//    knight tour on a dim * dim-board.


def first_tour(dim: Int, path: Path) : Option[Path] = {
      val possible_moves = legal_moves(dim, path, path.head)
      
      first(possible_moves, (p:Pos) =>{
          if(legal_moves(dim, p::path, p).length == 0 && (p::path).length == dim*dim){
            Some(p::path)
          }
          else{
            first_tour(dim, p::path)
          }
      })
}


//(6) Complete the function that calculates a list of onward
//    moves like in (2) but orders them according to Warnsdorf’s 
//    rule. That means moves with the fewest legal onward moves 
//    should come first.


def ordered_moves(dim: Int, path: Path, x: Pos) : List[Pos] = {
    legal_moves(dim, path,x).sortBy(
        legal_moves(dim, x::path , _ ).length
    )
}


//(7) Complete the function that searches for a single *closed* 
//    tour using the ordered_moves function from (6). This
//    function will be tested on a 6 x 6 board. 

def is_in_range_of_origin(x: Pos, o: Pos, dim: Int) : Boolean = {
  legal_moves(dim, Nil, o).contains(x)
}


def first_closed_tour_heuristic(dim: Int, path: Path) : Option[Path] = {

  val possible_moves = ordered_moves(dim, path, path.head)
    
      first(possible_moves, (p:Pos) =>{
        val updated_path = p::path
          if(ordered_moves(dim, updated_path, p).length == 0 && (updated_path).length == dim*dim){
            if(is_in_range_of_origin(p,updated_path.head,dim)){
                Some(updated_path)
                }
            else{
                None
            }
          }
          else{
            first_tour(dim, updated_path)
          }
      })
}


//(8) Same as (7) but searches for *non-closed* tours. This 
//    version of the function will be called with dimensions of 
//    up to 30 * 30.



def first_tour_heuristic(dim: Int, path: Path) : Option[Path] = { 
    if(path.length == dim*dim){
        Some(path)
    }
    else first(ordered_moves(dim, path, path.head), (p:Pos) =>{
        first_tour_heuristic(dim, p::path)
      })
}


/*
def time_needed[T](code: => T) : T = {
 val start = System.nanoTime()
  val result = code
  val end = System.nanoTime()
  println(f"Time needed: ${(end - start) / 1.0e9}%3.3f secs.")
 result
 }

 def print_board(dim: Int, path: Path): Unit = {
  println
  for (i <- 0 until dim) {
    for (j <- 0 until dim) {
      print(f"${path.reverse.indexOf((j, dim - i - 1))}%3.0f ")
    }
    println
  } 
}
*/