class repeat(body: => Unit):
  infix def until(cond: => Boolean): Unit =
    body
    if (!cond) then until(cond)

//object repeat:
//  def apply(body: => Unit) = new repeat(body)

var x = 0
x.toString
repeat({println(x); x = x + 1}).until(x == 12)

repeat {
  println(x)
  x = x + 1
} until (x == 22)