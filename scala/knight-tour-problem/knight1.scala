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
    if(x._1 >= 0 && x._1 < dim && x._2 >= 0 && x._2 < dim){
        if(!path.contains(x))
          true
        else
          false
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
      (1,-2),
      (-1, -2),
      (-2 , -1),
      (-2 , 1),
      (-1 , 2)
  )
}

def legal_moves(dim: Int, path: Path, x: Pos) : List[Pos] = {
    val possible_moves = get_possible_translations

  val valid_moves = possible_moves.map( traslation =>(

     if(is_legal(dim, path, (traslation._1+x._1 , traslation._2+x._2))){
        Some(traslation._1+x._1 , traslation._2+x._2)
     }
      else{
        None
      }

      )
    )

  valid_moves.flatten

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



// for measuring time
//def time_needed[T](code: => T) : T = {
 // val start = System.nanoTime()
//  val result = code
//  val end = System.nanoTime()
//  println(f"Time needed: ${(end - start) / 1.0e9}%3.3f secs.")
 // result
//}
// can be called for example with
//     time_needed(count_tours(dim, List((0, 0))))
// in order to print out the time that is needed for 
// running count_tours

// for printing a board
//def print_board(dim: Int, path: Path): Unit = {
//  println
//  for (i <- 0 until dim) {
//    for (j <- 0 until dim) {
//      print(f"${path.reverse.indexOf((j, dim - i - 1))}%3.0f ")
//    }
//    println
//  } 
//}
