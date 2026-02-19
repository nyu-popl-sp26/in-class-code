enum Bop:
  case Add, Mul

enum Expr:
  case Num(n: Int)
  case Var(x: String)
  case BinOp(bop: Bop, e1: Expr, e2: Expr)
  case ConstDecl(x: String, ed: Expr, eb: Expr)

import Bop._, Expr._

def ov(e: Expr): Set[String] =
  e match
    case Num(n) => Set()
    case Var(x) => Set(x)
    case BinOp(_, e1, e2) => ov(e1) ++ ov(e2)
    case ConstDecl(x, ed, eb) => Set(x) ++ ov(ed) ++ ov(eb)

def bv(e: Expr): Set[String] =
  e match
    case Num(n) => Set()
    case Var(x) => Set()
    case BinOp(_, e1, e2) => bv(e1) ++ bv(e2)
    case ConstDecl(x, ed, eb) => Set(x) ++ bv(ed) ++ bv(eb)

def fv(e: Expr): Set[String] =
  e match
    case Num(n) => Set()
    case Var(x) => Set(x)
    case BinOp(_, e1, e2) => fv(e1) ++ fv(e2)
    case ConstDecl(x, ed, eb) => fv(ed) ++ (fv(eb) - x)

type Env = Map[String, Int]

def dom(env: Env): Set[String] = env.keySet

def eval(env: Env, e: Expr): Int =
  require(fv(e) subsetOf dom(env))
  e match
    case Num(n) => n
    case Var(x) => env(x)
    case BinOp(Add, e1, e2) =>
      eval(env, e1) + eval(env, e2)
    case BinOp(Mul, e1, e2) =>
      eval(env, e1) * eval(env, e2)
    case ConstDecl(x, ed, eb) =>
      val v = eval(env, ed)
      eval(env + (x -> v), eb)

def subst(e: Expr, x: String, ex: Expr): Expr =
  e match
    case Num(n) =>
      Num(n)

    case Var(y) =>
      if x == y then ex else Var(y)

    case BinOp(bop, e1, e2) =>
      BinOp(bop, subst(e1, x, ex), subst(e2, x, ex))

    case ConstDecl(y, ed, eb) =>
      val ebNew =
        if x == y
        then
          eb
        else
          subst(eb, x, ex)
      ConstDecl(y, subst(ed, x, ex), ebNew)

val er: Expr = ConstDecl("z", BinOp(Mul, Num(2), Num(3)), BinOp(Add, Var("z"), Var("x")))
subst(er, "x", Var("y"))
subst(er, "x", Var("z")) // Does not behave correctly!

