enum Bop:
  case Add // "+"
  case Sub // "-"
  case Mul // "*"
  case Div // "/"

enum Expr:
  case Num(n: Int)
  case BinOp(bop: Bop, left: Expr, right: Expr)

import Bop._
import Expr._

val my_num = Num(2) // "2"
val my_num_other = Num(3)
val my_expr = BinOp(Add, Num(2), Num(3)) // "2 + 3"
val my_five = Num(5)

def printBop(b: Bop): String =
  if b == Add then "+" else (if b == Sub then "-" else (if b == Mul then "*" else "/"))

Add == Add
Add == Mul
Add.equals(Mul)
3.equals(1+2)
my_five.equals(my_expr) // 5 = 2 + 3


printBop(Add)
printBop(Mul)

def prettyExpr(e: Expr): String =
  e match
    case Num(n) => n.toString
    case BinOp(op,l,r) => "(" + prettyExpr(l) + printBop(op) + prettyExpr(r) + ")"

def calculator(e: Expr): Int =
  e match
    case Num(n) => n
    case BinOp(Add,l,r) => calculator(l) + calculator(r)
    case BinOp(Mul,l,r) => calculator(l) * calculator(r)
    case BinOp(Sub,l,r) => calculator(l) - calculator(r)
    case BinOp(Div,l,r) => calculator(l) / calculator(r)

prettyExpr(my_expr)
// (2 * 3) + 5
// 2 * (3 + 5)

val two_times_three = BinOp(Mul, Num(2), Num(3))
calculator(two_times_three)
val larger_expression = BinOp(Add, two_times_three, Num(5))
calculator(larger_expression)
val larger_expression2 = BinOp(Add, Num(3), Num(5))
calculator(larger_expression2)
val nested_expression = BinOp(Mul, Num(2), larger_expression2)
calculator(nested_expression)

prettyExpr(larger_expression)
prettyExpr(nested_expression)

def simplifyExpr(e: Expr): Expr =
  e match
    case BinOp(Add, Num(0), r) => r // 0 + e = e
    case BinOp(Add, r, Num(0)) => r // 0 + e = e
    // case BinOp(Mul, _, r) if r == Num(0) => r // e * 0 = 0
    case BinOp(Mul, _, r @ Num(0)) => r // e * 0 = 0
    case BinOp(Add, expr1, expr2) if expr1 == expr2 => BinOp(Mul, Num(2), expr2)  // e + e = 2*e
    // case BinOp(_,_,_) => e
    // case Num(_) => e
    case _ => e

calculator(nested_expression) == calculator(simplifyExpr(nested_expression))

val add_ex1 = BinOp(Add, Num(3), Num(3))
prettyExpr(simplifyExpr(add_ex1))

// Option integer types
enum Option:
  case None
  case Some(i: Int)

import Option._

def getListHead(ls: List[Int]): Option =
  ls match
    case List() => None
    case hd :: tl => Some(hd)

getListHead(List())
getListHead(List(1,2,3))

def optionPlus(o1: Option, o2: Option): Option =
  o1 match
    case None => (o2 match
      case None => None
      case Some(i) => None)
    case Some(i) => (o2 match
      case None => None
      case Some(j) => Some(i+j))

optionPlus(getListHead(List(1,2,3)), Some(2))

















