package DT

import shapeless.ops.zipper.First

/**
  * FileName: SecondarySortKey
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-9-7 下午2:46
  * Description:
  *
  */
class SecondarySortKey(val first:Int,val second:Int) extends Ordered[SecondarySortKey] with Serializable {
   def compare(that: SecondarySortKey): Int = {
     if (this.first - that.first != 0){
       this.first - that.first
     }else{
       this.second - that.second
     }
   }
}

object SecondarySortKey extends scala.AnyRef with Serializable{
  def apply(first: Int,second:Int):SecondarySortKey={
    new SecondarySortKey(first,second)
  }
}
