import scala.annotation.tailrec;


type Pos = (Int, Int)    // a position on a chessboard
type Path = List[Pos]    // a path...a list of positions


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




def ordered_moves(dim: Int, path: Path, x: Pos) : List[Pos] = {
    legal_moves(dim, path,x).sortBy(
        legal_moves(dim, x::path , _ ).length
    )
}



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

def first_tour_heuristic(dim: Int, path: Path) : Option[Path] = { 
    if(path.length == dim*dim){
        Some(path)
    }
    else first(ordered_moves(dim, path, path.head), (p:Pos) =>{
        first_tour_heuristic(dim, p::path)
      })
}



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
