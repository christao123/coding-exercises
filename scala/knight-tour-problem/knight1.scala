
type Pos = (Int, Int)    // a position on a chessboard
type Path = List[Pos]    // a path...a list of positions


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
