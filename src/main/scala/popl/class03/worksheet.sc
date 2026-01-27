class MyPair(var first: Int, val second: Int):
  def updateFirst(fst: Int) = new MyPair(fst, second)
  def updateSecond(snd: Int) = new MyPair(first, snd)
  override def toString(): String = s"Pair($first, $second)"
  private val my_special_number = 17

val my_pair = new MyPair(1,2)
my_pair.first
my_pair.second
println(my_pair.toString)

object MyPair:
  def apply(fst: Int, snd: Int) = new MyPair(fst, snd)

class MyTriple(first: Int, second: Int, val third: Int) extends MyPair(first, second):
  override def toString(): String = s"Triple($first, $second, $third)"
  def updateThird(thd: Int) = new MyTriple(first, second, thd)

val my_other_pair = MyPair(2,3)
my_other_pair // Triple(2,3,4)
val my_triple = new MyTriple(2,3,4)
my_triple.toString
my_triple.updateFirst(1)
my_triple.updateSecond(2)
my_triple.updateThird(17)