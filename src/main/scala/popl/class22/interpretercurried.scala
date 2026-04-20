package popl.class22

object interpretercurried:
  import ast._

  /* Non-capture avoiding substitution */
  def subst(e: Expr, x: String, er: Expr): Expr =
    def substX(e: Expr): Expr = subst(e, x, er)
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
  def eval(e: Expr): Mem => (Mem, Val) =
    def eToNum(e: Expr): Mem => (Mem, Double) = m =>
      val (mp, v) = eval(e)(m)
      v match
        case Num(n) => (mp, n)
        case _ => throw StuckError(e)

    e match
      /** rule EvalVal */
      case v: Val => m => (m, v)

      /** rule EvalUMinus */
      case UnOp(UMinus, e1) => m =>
        val (mp, n1) = eToNum(e1)(m)
        (mp, Num(-n1))

      /** rule EvalDeref */
      case UnOp(Deref, a: Addr) => m =>
        m.get(a) match
          case Some(v) => (m, v)
          case None => throw StuckError(e)

      /** rule EvalPlus */
      case BinOp(Plus, e1, e2) => m =>
        val (mp, n1) = eToNum(e1)(m)
        val (mpp, n2) = eToNum(e2)(mp)
        (mpp, Num(n1 + n2))

      /** rule EvalAssignVar */
      case BinOp(Assign, UnOp(Deref, a: Addr), e2) => m =>
        val (mp, v2) = eval(e2)(m)
        (mp + (a -> v2), v2)

      /** rule EvalConstDecl */
      case Decl(MConst, x, ed, eb) => m =>
        val (mp, vd) = eval(ed)(m)
        eval(subst(eb, x, vd))(mp)

      /** rule EvalVarDecl */
      case Decl(MLet, x, ed, eb) => m =>
        val (md, vd) = eval(ed)(m)
        val (mp, a) = md.alloc(md, vd)
        eval(subst(eb, x, UnOp(Deref, a)))(mp)

  def main(args: Array[String]): Unit =
    // let x = 3; x + 1 
    val m_empty = Mem.empty
    val e1 = Num(3)
    val e2 = BinOp(Plus, Var("x"), Num(1))
    val e3 = eval(Decl(MLet, "x", e1, e2))(m_empty)
    println(e3.toString)
