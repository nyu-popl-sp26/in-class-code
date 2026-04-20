package popl.class22

object interpretermonad:
  import ast.*

  /* Substitutions e[er/x] */
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

  def eval(e: Expr): State[Mem, Val] =
    def eToNum(e: Expr): State[Mem, Double] = m =>
      val (mp, v) = eval(e)(m)
        v match
          case Num(n) => (mp, n)
          case _ => throw StuckError(e)

    e match
      /** rule EvalVal */
      case v: Val =>
        State { m => (m,v)}

      /** rule EvalUMinus */
      case UnOp(UMinus, e1) =>
        for
          n1 <- eToNum(e1)
        yield Num(-n1)

      /** rule EvalPlus */
      case BinOp(Plus, e1, e2) =>
        for
          n1 <- eToNum(e1)
          n2 <- eToNum(e2)
        yield Num(n1 + n2)

      /** rule EvalConstDecl */
      case Decl(MConst, x, ed, eb) =>
        for
          vd <- eval(ed)
          v <- eval(subst(eb, x, vd))
        yield v

      /** rule EvalDeref */
      case UnOp(Deref, a: Addr) =>
        State.read[Mem,Val](m => m(a))

      /** rule EvalAssignVar */
      case BinOp(Assign, UnOp(Deref, a: Addr), e2) =>
        for
          v2 <- eval(e2)
          _ <- State.write[Mem](m => m + (a -> v2))
        yield v2

      /** rule EvalVarDecl */
      case Decl(MLet, x, ed, eb) =>
        State {m =>
          val (md, vd) = eval(ed)(m)
          val (mp, a) = md.alloc(md, vd)
          eval(subst(eb, x, UnOp(Deref, a)))(mp)
        }

  def main(args: Array[String]): Unit =
    // let x = 3; let y = x = x + 1; y + 1
    val m_empty = Mem.empty
    val e1 = BinOp(Assign, Var("x"), BinOp(Plus, Var("x"), Num(1)))
    val e_decl2 = Decl(MLet, "y", e1, BinOp(Plus, Var("y"), Num(1)))
    val e_decl1 = Decl(MLet, "x", Num(3), e_decl2)
    val res = eval(e_decl1)(m_empty)
    println(res.toString)
