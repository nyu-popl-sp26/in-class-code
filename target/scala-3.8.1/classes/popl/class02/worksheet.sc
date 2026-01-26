import scala.annotation.tailrec

def multLoop(m: Int, n: Int): Int =
  var acc = 0
  var i = n
  while i > 0 do
    acc = acc + m
    i = i - 1
  acc

multLoop(3,100000)

def mult(m: Int, n: Int): Int =
  if n == 0 then 0 else m + mult(m, n-1)

mult(3, 100000)

@tailrec
def multTailRec(m: Int, n: Int, acc: Int): Int =
  if n == 0 then acc else multTailRec(m, n-1, acc+m)

def multRec(m: Int, n: Int): Int =
  multTailRec(m, n, 0)

multRec(3, 100000)
