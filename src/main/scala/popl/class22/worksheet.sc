// Case study 1: lists
// A refresher of higher-order list functions from class15 
val my_list = List(1,2,3,4)
val my_string_list = List("a", "b", "c")

def map[A,B](ls: List[A], f: A => B): List[B] =
  ls match
    case Nil => Nil
    case hd :: tl => f(hd) :: map(tl, f)

map(my_list, _ * 2)
map(my_list, _ + 1)
map(my_string_list, _ + "_2026")
map(my_list, x => x.toString)

// Using for...yield
for (x <- my_list) yield x * 2
for (x <- my_list) yield x + 1
for (x <- my_string_list) yield x + "_2026"
for (x <- my_list) yield x.toString

def filter[A](ls: List[A], f: A => Boolean): List[A] =
  ls match
    case Nil => Nil
    case hd :: tl => if f(hd) then hd :: filter(tl, f) else filter(tl, f)

filter(my_list, x => x % 2 == 1)
filter(my_list, x => x % 2 == 0)

// Using for...yield
for
  x <- my_list
  if x % 2 == 0
yield x

for {
  x <- my_list
  if x % 2 == 1
} yield x

// Nesting for...yield expressions
for
  x <- my_list
  y <- my_string_list
yield (x, y)

map(my_list, x => (x, "a"))
map(my_list, x => map(my_string_list, y => (x,y)))

def flatten[A](ls: List[List[A]]): List[A] =
  ls match
    case Nil => Nil
    case hd :: tl => hd ++ flatten(tl)

// Expressing Cartesian product in terms of map
val res1 = map(my_list, x => map(my_string_list, y => (x,y)))

// More lists?
for
  x <- my_list
  y <- my_string_list
  z <- my_string_list
yield (x, y, z)

map(my_list, x => map(my_string_list, y => map(my_string_list, z => (x,y,z))))

// Need to flatten twice!
flatten(map(my_list, x => flatten(map(my_string_list, y => map(my_string_list, z => (x,y,z))))))

// Combining flatten and map
def flatMap[A,B](ls: List[A], f: A => List[B]): List[B] =
  ls match
    case Nil => Nil
    case hd :: tl => f(hd) ++ flatMap(tl, f)

flatMap(my_list, x => map(my_string_list, y => (x,y)))
flatMap(my_list, x => flatMap(my_string_list, y => map(my_string_list, z => (x,y,z))))

// Using Scala library functions
my_list.flatMap(x => my_string_list.flatMap(y => my_string_list.map(z => (x,y,z))))
// def map[A, B](op: A => B): List[B]

// for x1 <- e1; x2 <- e2; ...; xn <- en yield e
// is equivalent to
// e1.flatMap(x1 => e2.flatMap(x2 => ... .map(xn => en)...))

// Case study 2: option types
def divide(n: Int, d: Int): Option[Int] =
  if d == 0 then None else Some(n/d)

// Chaining operations: (3 / 10 / 0) + 3
//divide(divide(3,10),0) + 3

// Using for...yield
for {
  x <- divide(3,10)
  y <- divide(x,0)
} yield y + 3

// Reverse engineering the Option monad
enum MyOption[+A]:
  case None
  case Some(a: A)

import MyOption._

def map[A,B](o: MyOption[A], f: A => B): MyOption[B] =
  o match
    case Some(a) => Some(f(a))
    case None => None

def flatMap[A,B](o: MyOption[A], f: A => MyOption[B]): MyOption[B] =
  o match
    case Some(a) => f(a)
    case None => None

def dividecurried(a: Int): Int => MyOption[Int] =
  b => if a == 0 then None else Some(b/a)

def addcurried(a: Int): Int => Int =
  b => a + b

val x = flatMap(Some(3), dividecurried(10))
val y = flatMap(x, dividecurried(0))
val z = map(y, addcurried(3))
map(flatMap(flatMap(Some(3), dividecurried(10)), dividecurried(0)), addcurried(3))