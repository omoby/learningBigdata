package quicklearnscala

object Transfer extends App{
  import java.util.{HashMap => JavaHashMap}
  import collection.mutable.{HashMap => ScalaHashMap}
  val javaMap = new JavaHashMap[Int, String]
  javaMap.put(1, "One");
  javaMap.put(2, "Two");
  javaMap.put(3, "Three");
  javaMap.put(4, "Four");
  val scalaMap = new ScalaHashMap[Int, String]
  for(key <- javaMap.keySet().toArray){
    scalaMap+= (key.asInstanceOf[Int] -> javaMap.get(key))
  }
  println(scalaMap.mkString(" "))
}