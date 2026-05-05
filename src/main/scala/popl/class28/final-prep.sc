import annotation.tailrec

@tailrec
def dropWhile[A](p: A => Boolean)(l: List[A]): List[A] =
  l match
    case Nil => Nil
    case hd :: tl => if p(hd) then dropWhile(p)(tl) else hd :: tl

dropWhile((x: Int) => x < 0)(List(-1, -2, 3, -5, 6))
dropWhile((x: Int) => x < 0)(List(1, 2, 3, -5, 6))

def dropWhile1[A](p: A => Boolean)(l: List[A]): List[A] =
  // Using foldLeft
  val init = (true, Nil)
  def op(acc: (Boolean, List[A]), a: A): (Boolean, List[A]) = {
    val b = acc._1
    val ls = acc._2
    if b then (if p(a) then (true, ls) else (false, a :: ls))
    else (b, a :: ls)
  }
  val lp = l.foldLeft(init)(op)._2
  lp.foldLeft(Nil)((acc,a) => a::acc)

dropWhile1((x: Int) => x < 0)(List(-1, -2, 3, -5, 6))
dropWhile1((x: Int) => x < 0)(List(1, 2, 3, -5, 6))

def countSat[A](p: A => Boolean)(l: List[A]): Int =
  l match
    case Nil => 0
    case hd :: tl => if p(hd) then 1 + countSat(p)(tl) else countSat(p)(tl)

countSat((x: Int) => x < 0)(List(-1, -2, 3, -5, 6))
countSat((x: Int) => x >= 0)(List(-1, -2, 3, -5, 6))

def countSat1[A](p: A => Boolean)(l: List[A]): Int =
  @tailrec
  def countSat(ls: List[A], acc: Int): Int =
    ls match
      case Nil => acc
      case hd :: tl => if p(hd) then countSat(tl,acc+1) else countSat(tl,acc)
  countSat(l,0)

countSat1((x: Int) => x < 0)(List(-1, -2, 3, -5, 6))
countSat1((x: Int) => x >= 0)(List(-1, -2, 3, -5, 6))

def countSat2[A](p: A => Boolean)(l: List[A]): Int =
  val init = 0
  def op(a: A, acc: Int): Int =
    if p(a) then acc+1 else acc
  l.foldRight(init)(op)

countSat2((x: Int) => x < 0)(List(-1, -2, 3, -5, 6))
countSat2((x: Int) => x >= 0)(List(-1, -2, 3, -5, 6))