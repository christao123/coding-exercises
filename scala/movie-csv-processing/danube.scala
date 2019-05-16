
import io.Source
import scala.util._

def get_csv_url(url: String) : List[String] = {

    val csv = Try(Some(Source.fromURL(url).mkString)).getOrElse(None)
    if(csv != None){
        csv.mkString.split("\n").toList.drop(1)
    }
    else{
        List()
    }
}


val ratings_url = """https://nms.kcl.ac.uk/christian.urban/ratings.csv"""
val movies_url = """https://nms.kcl.ac.uk/christian.urban/movies.csv"""

// testcases
//-----------
//val ratings = get_csv_url(ratings_url)
//val movies = get_csv_url(movies_url)

//ratings.length  // 87313
//movies.length   // 9742


def process_ratings(lines: List[String]) : List[(String, String)] = {
    val filtered_list = lines.filter( _.split(",")(2).toInt >=4 );
    filtered_list.map(movie => (movie.split(",")(0), movie.split(",")(1)))
}

def process_movies(lines: List[String]) : List[(String, String)] = {
    lines.map(movie => (movie.split(",")(0), movie.split(",")(1)))
}

import scala.annotation.tailrec

def groupById(ratings: List[(String, String)], m: Map[String, List[String]]) :
            Map[String, List[String]] = { 
       @tailrec
       def groupAcc(ratings: List[(String, String)],  acc: Map[String, List[String]]) : 
            Map[String, List[String]] = {
            if(ratings.length <= 0){
                acc
            }
            else{
                groupAcc(
                        ratings.tail,
                        (acc + 
                            (ratings.head._1 //key
                                         -> 
                            (acc.get(ratings.head._1).getOrElse(List()):+ratings.head._2) //value
                            )
                        )
                    )
                }
            } 
       groupAcc(ratings.sortBy(_._1), m) 
    }




// testcases
//-----------
//val lst = List(
//("2", "x"), ("3", "a"),
//("1", "a"), ("1", "b"),
//("2", "y"), ("3", "c"))
//
//
//val a = Map("AL" -> "Alabama")
// val b = a + ("AK" -> "Alaska")
//
//groupById(lst, Map())
//
//val m2 = Map(("2", List("x")))
//m2 + (lst.head._1 -> lst.head._2)
//val m3 = m2 + ( "3" -> List("a"))
//
//m3 + ("3" -> (m3.get("5").getOrElse(List()) :+ "g"))
//
//m3.get("11").isDefined
//val ratings_map = groupById(good_ratings, Map())
//val movies_map = movie_names.toMap
//
//ratings_map.get("414").get.map(movies_map.get(_))
//    => most prolific recommender with 1227 positive ratings
//
//ratings_map.get("474").get.map(movies_map.get(_)).length
//    => second-most prolific recommender with 787 positive ratings
//
//ratings_map.get("214").get.map(movies_map.get(_)).length
//    => least prolific recommender with only 1 positive rating



def favourites(m: Map[String, List[String]], mov: String) : List[List[String]] = {
   m.toList.map(rating => {
        if(rating._2.contains(mov)){
            Some(rating._2.filterNot(_==mov))
        }
        else{
            None
        }
        }).flatten
}




def suggestions(recs: Map[String, List[String]], 
                mov_name: String) : List[String] = {
    val favs = favourites(recs, mov_name)
    favs.flatten.groupBy(identity).mapValues(v => v.size).toList.sortBy(_._2).reverse.map(pair => pair._1)
}

def recommendations(recs: Map[String, List[String]],
                    movs: Map[String, String],
                    mov_name: String) : List[String] = {
            
            val suggs = suggestions(recs, mov_name)
            if(suggs.size >= 2){
                List(movs(suggs(0)), movs(suggs(1)))
            }
            else if(suggs.size == 1){
                List(movs(suggs(0)))
            }
            else{
                Nil
            }                
}



// testcases
//-----------
// recommendations(ratings_map, movies_map, "912")
//   => List(Godfather, Star Wars: Episode IV - A NewHope (1977))

//recommendations(ratings_map, movies_map, "260")
//   => List(Star Wars: Episode V - The Empire Strikes Back (1980), 
//           Star Wars: Episode VI - Return of the Jedi (1983))

// recommendations(ratings_map, movies_map, "2")
//   => List(Lion King, Jurassic Park (1993))

// recommendations(ratings_map, movies_map, "0")
//   => Nil

// recommendations(ratings_map, movies_map, "1")
//   => List(Shawshank Redemption, Forrest Gump (1994))

// recommendations(ratings_map, movies_map, "4")
//   => Nil  (there are three ratings for this movie in ratings.csv but they are not positive)     

