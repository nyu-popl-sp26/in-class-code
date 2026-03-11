// Scala lists: a refresher
val my_list = List(1,2,3,4)
my_list.head
my_list.tail
val my_string_list = List("a", "b", "c")
0::my_list

// Pattern 1: double, incr
def double(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => hd * 2 :: double(tl)

double(my_list)

def incr(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => hd+1 :: incr(tl)

incr(my_list)

def appendYear(ls: List[String]): List[String] =
  ls match
    case Nil => Nil
    case hd :: tl => hd + "_2026" :: appendYear(tl)

appendYear(my_string_list)

def convert(ls: List[Int]): List[String] =
  ls match
    case Nil => Nil
    case hd :: tl => hd.toString :: convert(tl)

convert(my_list)

def map[A,B](ls: List[A], f: A => B): List[B] =
  ls match
    case Nil => Nil
    case hd :: tl => f(hd) :: map(tl, f)

def doubleInt(i: Int): Int = i * 2
map(my_list, doubleInt)
map(my_list, (x: Int) => x + x)
map(my_list, _ * 2)
map(my_list, _ + 1)
map(my_string_list, _ + "_2026")
map(my_list, x => x.toString)

// Pattern 2: getOdd, getEven
def getOdd(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => if hd % 2 == 1 then hd :: getOdd(tl) else getOdd(tl)

getOdd(my_list)

def getEven(ls: List[Int]): List[Int] =
  ls match
    case Nil => Nil
    case hd :: tl => if hd % 2 == 0 then hd :: getEven(tl) else getEven(tl)

getEven(my_list)

def filter[A](ls: List[A], f: A => Boolean): List[A] =
  ls match
    case Nil => Nil
    case hd :: tl => if f(hd) then hd :: filter(tl, f) else filter(tl, f)

filter(my_list, x => x % 2 == 1)
filter(my_list, x => x % 2 == 0)

// Pattern 3: sum, concat, reverse
def sum(ls: List[Int]): Int =
  ls match
    case Nil => 0
    case hd :: tl => hd + sum(tl)

sum(my_list)

def concat(ls: List[Int], default: String): String =
  ls match
    case Nil => default
    case hd :: tl => hd.toString + concat(tl, default)

concat(my_list, "_2026")

def fold_right[A,B](ls: List[A], f: A => B => B, default: B): B =
  ls match
    case Nil => default
    case hd :: tl => f(hd)(fold_right(tl, f, default))

import annotation.tailrec
@tailrec
def fold_left[A,B](ls: List[A], f: A => B => B, acc: B): B =
  ls match
    case Nil => acc
    case hd :: tl => fold_left(tl, f, f(hd)(acc))

fold_right(my_list, x => (y: Int) => x-y, 0)
// fold(1::tail, f, 0)
// 1 - fold(2::tail', f, 0)
// 1 - (2 - fold(3::tail'', f, 0))
// 1 - (2 - (3 - (4 - 0)))
// -2
fold_left(my_list, x => (y: Int) => x-y, 0)
// fold_tr(1::tail, f, 0)
// fold_tr(2::tail', f, 1-0)
// fold_tr(tail', f, 2-(1-0))
// 4 - (3 - (2 - (1-0)))
// 2

// Surprising applications: compress (p08), run length (p10), pack (p09), decode (p12), duplicate (p14)
// https://aperiodic.net/pip/scala/s-99/

val my_symbol_list = List("a", "a", "a", "a", "b", "c", "c", "a", "a")
// compress(my_symbol_list) = List("a", "b", "c", "a")

def compare(hd: String)(acc: List[String]): List[String] =
  if acc == Nil then hd :: acc else if acc.head == hd then acc else hd :: acc

def append[A](ls1: List[A], ls2: List[A]): List[A] =
  fold_right(ls1, x => y => x :: y, ls2)

append(my_list, my_list)

def reverse[A](ls: List[A]): List[A] =
  fold_left(ls, x => y => x :: y, Nil)

reverse(my_list)

def compare_other(hd: String)(acc: List[String]): List[String] =
  if acc == Nil then append(acc, List(hd)) else if (reverse(acc)).head == hd then acc else append(acc, List(hd))

// compress (p08)
fold_right(my_symbol_list, compare, Nil)
fold_left(my_symbol_list, compare_other, Nil)

