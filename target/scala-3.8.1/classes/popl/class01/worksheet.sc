3 + 4

val y = 6
val z = 10

var x: Int = 3 + 4
val max = if x > 0 then y else z

val q: Unit = if x > 0 then
  println("x larger than 0")
else println("x smaller or equal to 0")

def max(x: Int, y: Int): Int =
  if x <= y then y else x
end max

max(2, 3)
max(3, 2)

def pow(x: Int, n: Int): Int =
  if n == 0 then 1 else x * pow(x, n-1)

pow(2,3)
pow(3,10)
pow(3,100)
pow(3,100000)

val my_pair = (2,3)
val my_pair2 = (1, "banana")
my_pair._1
my_pair2._2
