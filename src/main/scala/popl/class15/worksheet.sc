// Scala lists: a refresher
def my_list = List(1,2,3,4)
def my_boolean_list = List(true, false, true)
1::my_list
my_list.head
my_list.tail

// Map
def incr(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => hd+1 :: incr(tl)

def double(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => hd*2 :: incr(tl)

def map[A, B](ls: List[A], f: A => B): List[B] =
  ls match
    case Nil => Nil
    case hd :: tl => f(hd) :: map(tl, f)

// Filter
def get_even(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => if hd % 2 == 0 then hd :: get_even(tl) else get_even(tl)

def get_odd(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => if hd % 2 == 1 then hd :: get_odd(tl) else get_odd(tl)

def filter[A](ls: List[A], f: A => Boolean): List[A] =
  ls match
    case Nil => Nil
    case hd :: tl => if f(hd) then hd :: filter(tl,f) else filter(tl,f)

// Folding left and right
def sum(ls: List[Int]): Int =
  ls match
    case Nil => 0
    case hd :: tl => hd + sum(tl)

def length(ls: List[Int]): Int =
  ls match
    case Nil => 0
    case hd :: tl => 1 + length(tl)

def concat(ls: List[Int]): String =
  ls match
    case Nil => ""
    case hd :: tl => hd.toString + concat(tl)

def fold_right[A,B](ls: List[A], f: A => B => B, default: B): B =
  ls match
    case Nil => default
    case hd :: tl => f(hd)(fold_right(tl, f, default))

fold_right[Int, Int](my_list, (x: Int) => (y: Int) => x+y, 0)
fold_right[Int, String](my_list, (x: Int) => (y: String) => x.toString+y, "")

// Tail recursive folding
import annotation.tailrec
@tailrec
def sum_tr(ls: List[Int], acc: Int): Int =
  ls match
    case Nil => acc
    case hd :: tl => sum_tr(tl, hd+acc)

@tailrec
def concat_tr(ls: List[Int], acc: String): String =
  ls match
    case Nil => acc
    case hd :: tl => concat_tr(tl, acc+hd.toString)

sum_tr(my_list, 0)
concat_tr(my_list, "")

@tailrec
def fold_left[A,B](ls: List[A], f: A => B => B, acc: B): B =
  ls match
    case Nil => acc
    case hd :: tl => fold_left(tl, f, f(hd)(acc))

fold_left[Int,Int](my_list, (x => y => x+y), 0)
fold_left[Int,String](my_list, (x => y => y + x.toString), "")
fold_left[Int, Int](my_list, ((x: Int) => (y: Int) => 1 + y), 0)

fold_right[Int,Int](my_list, (x => y => x-y), 0) // fold-right
// 1-(2-(3-(4-0))) = -2
fold_left[Int,Int](my_list, (x => y => x-y), 0)
// generalize_tailrec(...,f,4-generalize_tailrec(...,f,3-generalize_tailrec(...,f,2-(generalize_tailrec(...,f,1-0))))
// 4-(3-(2-(1-0))) = 2
fold_left[Int,List[Int]](List(1,2), (x => y => x :: y), List())

// Surprising applications: compress (p08), run length (p10), pack (p09), decode (p12), duplicate (p14)
def pack_op(x: String)(ls: List[String]): List[String] =
  ls match
    case Nil => x :: ls
    case hd::tl => if x == hd then ls else x :: ls

def pack(ls: List[String]) = fold_right[String, List[String]](ls, pack_op, Nil)

val my_symbol_list = List("a", "a", "a", "b", "c", "c", "a", "a")
pack(my_symbol_list)

def count_op(x: String)(ls: List[(String, Int)]): List[(String, Int)] =
  ls match
    case Nil => List((x,1))
    case hd::tl => if x == hd._1 then (x,hd._2+1)::tl else (x,1)::ls

def count(ls: List[String]) = fold_right[String, List[(String, Int)]](ls, count_op, Nil)

count(my_symbol_list)

def repeat(sym: String, n: Int): List[String] =
  if n == 0 then Nil else sym :: repeat(sym,n-1)

map(count(my_symbol_list), ((sym, count) => repeat(sym, count)))

def my_string_append(ls1: List[String], ls2: List[String]): List[String] =
  fold_right(ls1, (x => y => x :: y), ls2)

def flatten(ls: List[List[String]]): List[String] =
  fold_left(ls, (x => y => my_string_append(x,y)), Nil)

def reverse[A](ls: List[A]) = fold_left[A, List[A]](ls, (x => y => x :: y), Nil)
reverse(flatten(map(count(my_symbol_list), ((sym, count) => repeat(sym, count)))))
my_symbol_list
reverse(my_list)






















