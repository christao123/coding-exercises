
def clean(s: String) : List[String] = {
    val rgx = """(\w+)""".r;
    rgx.findAllIn(s).toList;
}

def occurrences(xs: List[String]): Map[String, Int] = {
    val cleaned = xs.distinct
    cleaned.map(line =>(line, xs.count(_==line))).toMap
}

def fetchValue(pr: (String, Int), list: List[(String,Int)]): Int = {
    val word = pr._1;

    if(list.find(_._1 == word)!=None){
        list.find(_._1==word).toList(0)._2
        }
    else 
        0
}

def prod(lst1: List[String], lst2: List[String]) : Int = {
    val ocmap1 = occurrences(lst1).toList
    val ocmap2 = occurrences(lst2).toList
    
    ocmap1.map(wrd => fetchValue(wrd,ocmap1) * fetchValue(wrd,ocmap2)).sum

}

def overlap(lst1: List[String], lst2: List[String]) : Double = {

    prod(lst1, lst2) / (List( prod(lst1,lst1), prod(lst2,lst2)).max.toDouble)

}

def similarity(s1: String, s2: String) : Double = {
    val cleaned_s1 = clean(s1)
    val cleaned_s2 = clean(s2)

    overlap(cleaned_s1,cleaned_s2)

}