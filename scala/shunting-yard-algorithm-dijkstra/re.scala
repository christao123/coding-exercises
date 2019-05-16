
// Regular Expressions
abstract class Rexp
case object ZERO extends Rexp
case object ONE extends Rexp
case class CHAR(c: Char) extends Rexp
case class ALT(r1: Rexp, r2: Rexp) extends Rexp   // alternative 
case class SEQ(r1: Rexp, r2: Rexp) extends Rexp   // sequence
case class STAR(r: Rexp) extends Rexp             // star


// some convenience for typing regular expressions

import scala.language.implicitConversions    
import scala.language.reflectiveCalls 

def charlist2rexp(s: List[Char]): Rexp = s match {
  case Nil => ONE
  case c::Nil => CHAR(c)
  case c::s => SEQ(CHAR(c), charlist2rexp(s))
}
implicit def string2rexp(s: String): Rexp = charlist2rexp(s.toList)

implicit def RexpOps (r: Rexp) = new {
  def | (s: Rexp) = ALT(r, s)
  def % = STAR(r)
  def ~ (s: Rexp) = SEQ(r, s)
}

implicit def stringOps (s: String) = new {
  def | (r: Rexp) = ALT(s, r)
  def | (r: String) = ALT(s, r)
  def % = STAR(s)
  def ~ (r: Rexp) = SEQ(s, r)
  def ~ (r: String) = SEQ(s, r)
}


def nullable (r: Rexp) : Boolean = r match {
  case ZERO => false
  case ONE => true
  case CHAR(c) => false
  case ALT(r1,r2) => (nullable(r1) || nullable(r2))
  case SEQ(r1,r2) => (nullable(r1) && nullable(r2))
  case STAR(rstar) => true
}



def der (c: Char, r: Rexp) : Rexp = r match{
  case ZERO => ZERO
  case ONE => ZERO
  case CHAR(d) => if(c==d) ONE else ZERO
  case ALT(r1,r2) => der(c,r1) | der(c,r2)
  case SEQ(r1,r2) => if(nullable(r1)) ((der(c,r1) ~ r2) | (der(c,r2))) else (der(c,r1) ~ r2)
  case STAR(rstar) => der(c,rstar) ~ STAR(rstar)
}


def simp(r: Rexp) : Rexp = r match{
case SEQ(r1, r2) => {
    val r1_s = simp(r1)
    val r2_s = simp(r2)
    (r1_s, r2_s) match{
      case (_ , ZERO) => ZERO
      case (ZERO, _) => ZERO
      case (rx, ONE) => simp(rx)
      case (ONE, rx) => simp(rx)
      case (a, b) => simp(a) ~ simp(b) 
    }
  }

case ALT(r1 , r2) => {
   val r1_s = simp(r1)
   val r2_s = simp(r2)
    (r1_s, r2_s) match{
      case (rx , ZERO) => simp(rx)
      case (ZERO, rx) => simp(rx)
      case (a, b) => if(a == b ) simp(a) else simp(a) | simp(b)
    }
}
case STAR(r1) => STAR(r1)
case (r1) => r1
}


def ders (s: List[Char], r: Rexp) : Rexp = s match{
  case Nil => r
  case c::cs => ders(cs, simp(der(c,r)))
}

def matcher(r: Rexp, s: String): Boolean = {
  nullable(ders(s.toList,r))  
}


def size(r: Rexp): Int = r match {
  case ZERO => 1
  case ONE => 1
  case CHAR(c) => 1
  case ALT(r1,r2) => 1 + size(r1) + size(r2)
  case SEQ(r1,r2) => 1+size(r1)+size(r2)
  case STAR(rstar) => 1+size(rstar)
}

