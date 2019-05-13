// Shunting Yard Algorithm 
// including Associativity for Operators 
// =====================================

// type of tokens
type Toks = List[String]

// helper function for splitting strings into tokens
def split(s: String) : Toks = s.split(" ").toList

// let- and right-associativity
abstract class Assoc
case object RA extends Assoc
case object LA extends Assoc

// power is right-associative,
// everything else is left-associative
def assoc(s: String) : Assoc = s match {
  case "^" => RA
  case _ => LA
}

 def is_op(op: String) : Boolean = List("+", "-", "*", "/", "^").contains(op)
 
 def is_left_par(par : String) :Boolean = {
	val left_pars = List( "(", "[", "{" )
	left_pars.contains(par)
 }

 def is_right_par(par : String) :Boolean = {
	val right_pars = List( ")", "]", "}" )
	right_pars.contains(par)
 }

// the precedences of the operators
val precs = Map("+" -> 1,
  		"-" -> 1,
		"*" -> 2,
		"/" -> 2,
    "^" -> 4)

// the operations in the basic version of the algorithm
val ops = List("+", "-", "*", "/", "^")



// (8) Implement the extended version of the shunting yard algorithm.
// This version should properly account for the fact that the power 
// operation is right-associative. Apart from the extension to include
// the power operation, you can make the same assumptions as in 
// basic version.


 def precpow(op1: String, op2: String) : Boolean = {
	val precs = Map("+" -> 1,
		"-" -> 1,
		"*" -> 2,
		"/" -> 2,
    "^" -> 4
    )
    if(op1 == "^")
	    precs(op1) > precs(op2)
    else
      precs(op1) >= precs(op2)
 }

 def syard(toks: Toks, st: Toks = Nil, out: Toks = Nil, done : Boolean = false) : Toks =
 {
	if(toks == Nil)
		if(st.length == 0) out else out:::st
	else{
		val toks_head = toks.head 
		val toks_tail = toks.tail 
		 is_op(toks_head) match{
		 	case true => {
				if (st.length > 0){
					val op2 = st(0)
					if(is_op(op2)&&precpow(op2,toks_head)){
						syard(toks,st.drop(1),out:::List(op2))
					}else{
						syard(toks_tail,toks_head::st,out)
					}
			 	}
				else syard(toks_tail,toks_head::st,out)
			}
			case false => {
				if(is_left_par(toks_head)){
					syard(toks_tail,toks_head::st,out)
				}else if(is_right_par(toks_head)){
					val head = st(0)
					if(!is_left_par(head)){
						syard(toks,st.drop(1),out:::List(head))
					}
					else{
						syard(toks_tail,st.drop(1),out)
					}
				}else if(toks_tail == Nil){
					if(st.length == 0){
						out:::List(toks_head)
					}else{
						(out:::List(toks_head)):::st
					}
				}else {
					syard(toks_tail,st,out:::List(toks_head))
				}
			}
		 }

	}
}


// test cases
//syard(split("3 + 4 * 8 / ( 5 - 1 ) ^ 2 ^ 3"))  // 3 4 8 * 5 1 - 2 3 ^ ^ / +


// (9) Implement a compute function that produces a Long(!) for an
// input list of tokens in postfix notation.


def calculate(n1 : Long, n2 : Long, op : String) : Long = op match {
	case "*" => n1 * n2
	case "/" => n1 / n2
	case "+" => n1 + n2
	case "-" => n1 - n2
  case "^" => Math.pow(n1, n2).toLong
}

 def compute(toks: Toks, st: List[Long] = Nil) : Long = {
	if(st.length == 1 && toks.length == 0){
		st.head
	}
	else{
		val toks_head = toks.head
		val toks_tail = toks.tail
		if(!is_op(toks_head)){
			compute(toks_tail, toks_head.toLong::st)
		}
		else{
			val n2 = st.head.toLong
			val n1 = st.tail.head.toLong
			val n = calculate(n1, n2, toks_head)
			val new_st = n::(st.drop(2))
			compute(toks_tail, new_st)
		}
	}
}
// test cases
// compute(syard(split("3 + 4 * ( 2 - 1 )")))   // 7
// compute(syard(split("10 + 12 * 33")))       // 406
// compute(syard(split("( 5 + 7 ) * 2")))      // 24
// compute(syard(split("5 + 7 / 2")))          // 8
// compute(syard(split("5 * 7 / 2")))          // 17
// compute(syard(split("9 + 24 / ( 7 - 3 )"))) // 15
// compute(syard(split("4 ^ 3 ^ 2")))      // 262144
// compute(syard(split("4 ^ ( 3 ^ 2 )")))  // 262144
// compute(syard(split("( 4 ^ 3 ) ^ 2")))  // 4096
// compute(syard(split("( 3 + 1 ) ^ 2 ^ 3")))   // 6553