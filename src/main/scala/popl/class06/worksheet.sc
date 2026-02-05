enum List:
  case Nil
  case Cons(hd: Int, tl: List)

import List._

val my_list = Cons(3, Cons(2, Cons(1, Nil)))

def length(ls: List): Int =
  ls match
    case Nil => 0
    case Cons(hd, tl) => 1 + length(tl)

def append(ls1: List, ls2: List): List =
  ls1 match
    case Nil => ls2
    case Cons(hd, tl) => Cons(hd, append(tl, ls2))

def reverse(ls: List): List =
  ls match
    case Nil => Nil
    case Cons(hd, tl) => append(reverse(tl), Cons(hd, Nil))

def prettyList(ls: List): String = {
    def prettyListInner(ls: List): String =
      (ls match
      case Nil => ""
      case Cons(hd, Nil) => hd.toString
      case Cons(hd, tl) => hd.toString + ", " + prettyListInner(tl))
    "[" + prettyListInner(ls) + "]"
}

val my_list2 = Cons(5, Cons(4, Cons(3, Nil)))
prettyList(my_list)
prettyList(append(my_list, my_list2))
prettyList(reverse(append(my_list, my_list2)))