enum Nat:
  case Zero
  case Succ(n: Nat)

import Nat._

Zero
Succ(Succ(Succ(Zero)))

def D(n: Nat, acc: Int): Int = {
  n match
    case Zero => acc
    case Succ(m) => D(m, acc+1)
}

def my_three = Succ(Succ(Succ(Zero)))
D(my_three, 0)

def Collatz(n: Int): Int =
  if n == 1 then 1 else (if n % 2 == 0 then Collatz(n/2) else Collatz(3*n+1))

def Collatz_match(n: Int): Int =
  n match
    case 1 => 1
    case _ => (if n % 2 == 0 then Collatz_match(n/2) else Collatz_match(3*n+1))

def MyAdd(n: Nat, m: Nat): Nat =
  n match
    case Zero => m
    case Succ(k) => Succ(MyAdd(k,m))

D(MyAdd(my_three, my_three),0)


























/*
def f(n: Int): Int =
  if n == 1 then 1 else if n % 2 == 0 then f(n/2) else f(3*n+1)

def g(n: Int, m: Int): Int =
  if n == 1 then m+1 else (if m == 0 then g(n-1,1) else g(n-1,g(n,m-1)))

def g_case(n: Int, m: Int): Int =
  (n,m) match
    case (0,m) => m+1
    case (n,0) => println(s"g_case(${n-1},1)"); g_case(n-1,1)
    case _ => println(s"g_case(${n-1},g_case($n,${m-1}))"); g_case(n-1, g_case(n,m-1))

g_case(1,2)
*/






/*
enum Nat:
  case Zero
  case Succ(n: Nat)

import Nat._

def Add(n: Nat, m: Nat): Nat =
  n match
    case Zero => m
    case Succ(k) => Add(k, Succ(m))

Add(Succ(Zero), Succ(Succ(Succ(Zero))))


def NatToInt(n: Nat): Int =
  n match
    case Zero => 0
    case Succ(k) => 1 + NatToInt(k)

NatToInt(Succ(Succ(Succ(Zero))))

*/