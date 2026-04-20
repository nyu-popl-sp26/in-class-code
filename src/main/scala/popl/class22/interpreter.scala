package popl.class22

object interpreter:
  import ast._

  /* Non-capture-avoiding substitution */
  def subst(e: Expr, x: String, er: Expr): Expr =
    def substX(e: Expr): Expr = subst(e, x, er)
    /* Body */
    e match
      case Num(_) | Addr(_) => e
      case Var(y) => if x == y then er else e
      case UnOp(uop, e1) => UnOp(uop, substX(e1))
      case BinOp(bop, e1, e2) => BinOp(bop, substX(e1), substX(e2))
      case Decl(mut, y, ed, eb) =>
        Decl(mut, y, substX(ed), if x == y then eb else substX(eb))

  /* Memory */
  class Mem private(map: Map[Addr, Val], nextAddr: Int):
    def apply(key: Addr): Val = map(key)

    def get(key: Addr): Option[Val] = map.get(key)

    def +(kv: (Addr, Val)): Mem = new Mem(map + kv, nextAddr)

    def contains(key: Addr): Boolean = map.contains(key)

    def alloc(m: Mem, v: Val): (Mem, Addr) =
      val freshAddr = Addr(nextAddr)
      (new Mem(map + (freshAddr -> v), nextAddr + 1), freshAddr)

    override def toString: String = map.toString

  object Mem:
    def empty = new Mem(Map.empty, 1)

  /* Interpreter */
  def eval(m: Mem, e: Expr): (Mem, Val) =
    def eToNum(m: Mem, e: Expr): (Mem, Double) =
      val (mp, v) = eval(m, e)
      v match
        case Num(n) => (mp, n)
        case _ => throw StuckError(e)

    e match
      /** rule EvalVal */
      case v: Val => (m, v) // v 

      /** rule EvalUMinus */
      case UnOp(UMinus, e1) =>
        val (mp, n1) = eToNum(m, e1)
        (mp, Num(-n1))

      /** rule EvalPlus */
      case BinOp(Plus, e1, e2) => // eToNum(eval(e1)) + eToNum(eval(e2))
        val (mp, n1) = eToNum(m, e1)
        val (mpp, n2) = eToNum(mp, e2)
        (mpp, Num(n1 + n2))

      /** rule EvalConstDecl */
      case Decl(MConst, x, ed, eb) =>
        val (mp, vd) = eval(m, ed)
        eval(mp, subst(eb, x, vd))

      /** rule EvalDeref */ // * a 
      case UnOp(Deref, a: Addr) =>
        m.get(a) match
          case Some(v) => (m, v)
          case None => throw StuckError(e)

      /** rule EvalAssignVar */
      case BinOp(Assign, UnOp(Deref, a: Addr), e2) =>
        val (mp, v2) = eval(m, e2)
        (mp + (a -> v2), v2)

      /** rule EvalVarDecl */
      case Decl(MLet, x, ed, eb) =>
        val (md, vd) = eval(m, ed)
        val (mp, a) = md.alloc(md, vd)
        eval(mp, subst(eb, x, UnOp(Deref, a)))

  def main(args: Array[String]): Unit =
    // let x = 3; x + 1 
    val m_empty = Mem.empty
    val e1 = Num(3)
    val e2 = BinOp(Plus, Var("x"), Num(1))
    val e3 = eval(m_empty, Decl(MLet, "x", e1, e2))
    println(e3.toString)
