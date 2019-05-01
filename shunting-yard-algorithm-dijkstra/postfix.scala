// Shunting Yard Algorithm
// by Edsger Dijkstra
// ========================


// type of tokens
type Toks = List[String]

// the operations in the basic version of the algorithm
val ops = List("+", "-", "*", "/")

// the precedences of the operators
val precs = Map("+" -> 1,
		"-" -> 1,
		"*" -> 2,
		"/" -> 2)

// helper function for splitting strings into tokens
def split(s: String) : Toks = s.split(" ").toList


// (6) Implement below the shunting yard algorithm. The most
// convenient way to this in Scala is to implement a recursive 
// function and to heavily use pattern matching. The function syard 
// takes some input tokens as first argument. The second and third 
// arguments represent the stack and the output of the shunting yard 
// algorithm.
//
// In the marking, you can assume the function is called only with 
// an empty stack and an empty output list. You can also assume the
// input os  only properly formatted (infix) arithmetic expressions
// (all parentheses will be well-nested, the input only contains 
// operators and numbers).

// You can implement any additional helper function you need. I found 
// it helpful to implement two auxiliary functions for the pattern matching:  
// 
 def is_op(op: String) : Boolean = List("+", "-", "*", "/").contains(op)
 def prec(op1: String, op2: String) : Boolean = {
	val precs = Map("+" -> 1,
		"-" -> 1,
		"*" -> 2,
		"/" -> 2)
	precs(op1) >= precs(op2)
 }

 def is_left_par(par : String) :Boolean = {
	val left_pars = List( "(", "[", "{" )
	left_pars.contains(par)
 }

 def is_right_par(par : String) :Boolean = {
	val right_pars = List( ")", "]", "}" )
	right_pars.contains(par)
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
					if(is_op(op2)&&prec(op2,toks_head)){
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
//syard(split("3 + 4 * ( 2 - 1 )"))  // 3 4 2 1 - * +
//syard(split("10 + 12 * 33"))       // 10 12 33 * +
//syard(split("( 5 + 7 ) * 2"))      // 5 7 + 2 *
//syard(split("5 + 7 / 2"))          // 5 7 2 / +
//syard(split("5 * 7 / 2"))          // 5 7 * 2 /
//syard(split("9 + 24 / ( 7 - 3 )")) // 9 24 7 3 - / +

//syard(split("3 + 4 + 5"))           // 3 4 + 5 +
//syard(split("( ( 3 + 4 ) + 5 )"))    // 3 4 + 5 +
//syard(split("( 3 + ( 4 + 5 ) )"))    // 3 4 5 + +
//syard(split("( ( ( 3 ) ) + ( ( 4 + ( 5 ) ) ) )")) // 3 4 5 + +

 
// (7) Implement a compute function that evaluates an input list
// in postfix notation. This function takes a list of tokens
// and a stack as argumenta. The function should produce the 
// result as an integer using the stack. You can assume 
// this function will be only called with proper postfix 
// expressions.    


def calculate(n1 : Int, n2 : Int, op : String) : Int = op match {
	case "*" => n1 * n2
	case "/" => n1 / n2
	case "+" => n1 + n2
	case "-" => n1 - n2
}

 def compute(toks: Toks, st: List[Int] = Nil) : Int = {
	if(st.length == 1 && toks.length == 0){
		st.head
	}
	else{
		val toks_head = toks.head
		val toks_tail = toks.tail
		if(!is_op(toks_head)){
			compute(toks_tail, toks_head.toInt::st)
		}
		else{
			val n2 = st.head.toInt
			val n1 = st.tail.head.toInt
			val n = calculate(n1, n2, toks_head)
			val new_st = n::(st.drop(2))
			compute(toks_tail, new_st)
		}
	}
}


// test cases
// compute(syard(split("3 + 4 * ( 2 - 1 )")))  // 7
// compute(syard(split("10 + 12 * 33")))       // 406
// compute(syard(split("( 5 + 7 ) * 2")))      // 24
// compute(syard(split("5 + 7 / 2")))          // 8
// compute(syard(split("5 * 7 / 2")))          // 17
// compute(syard(split("9 + 24 / ( 7 - 3 )"))) // 15




