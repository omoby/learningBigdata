package SparkSQL

import java.io.{File, FileWriter, PrintWriter}
import java.text.SimpleDateFormat
import java.util.{Calendar, Random}

import scala.reflect.macros.ParseException

/**
  * FileName: SparkSQLUserLogsManually
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-11-24 下午9:48
  * Description:
  *
  */
object SparkSQLUserLogsManually {
  def main (args: Array[String]): Unit = {
    //数据的容量为10000条
    val numberItems = 10000
    //数据存储位置
    val pathPath = "/home/hadoop/IdeaScala/"
    ganerateUserLogs(numberItems,pathPath)
  }

  /**
    * 将数据写入到指定的文件中
    * @param pathPath 数据存储的目录
    * @param fileName 数据存储的文件
    * @param strUserLog
    */
  def writeLog (pathPath: String, fileName: String, strUserLog: String) = {
    var fw:FileWriter = null
    var out:PrintWriter = null
    try{
      val writeFile = new File(pathPath+fileName)
      if(!writeFile.exists()){
        writeFile.createNewFile()
      }else{
        writeFile.delete()
      }
       fw = new FileWriter(writeFile,true)
       out = new PrintWriter(fw)
       out.print(strUserLog)
    }catch{
      case e:Exception=>e.printStackTrace()
    }finally {
      try{
        if (out!=null)
          out.close
        if (fw != null)
          fw.close
      }catch{
        case e:Exception=>e.printStackTrace()
      }
    }

  }

  /**
    *a
    * @param value
    * @param formate
    * @param step
    * @return
    */
  def getCountDate (value: Null, formate: String, step: Int) = {
    val sdf = new SimpleDateFormat(formate)
    val cal = Calendar.getInstance()
    if(value != null){
      try{
        cal.setTime(sdf.parse(value))
      }catch{
        case e:ParseException=>e.printStackTrace()
      }
    }
    cal.add(Calendar.DAY_OF_MONTH,step)
    sdf.format(cal.getTime)
  }


  def ganerateUserID () = {
    val random = new Random
    val userID = Array[String](
      "98415b9c-f3d4-45c3-bc7f-dce3126c6c0b", "7371b4bd-8535-461f-a5e2-c4814b2151e1",
      "49852bfa-a662-4060-bf68-0dddde5feea1", "8768f089-f736-4346-a83d-e23fe05b0ecd",
      "a76ff021-049c-4a1a-8372-02f9c51261d5", "8d5dc011-cbe2-4332-99cd-a1848ddfd65d",
      "a2bccbdf-f0e9-489c-8513-011644cb5cf7", "89c79413-a7d1-462c-ab07-01f0835696f7",
      "8d525daa-3697-455e-8f02-ab086cda7851", "c6f57c89-9871-4a92-9cbe-a2d76cd79cd0",
      "19951134-97e1-4f62-8d5c-134077d1f955", "3202a063-4ebf-4f3f-a4b7-5e542307d726",
      "40a0d872-45cc-46bc-b257-64ad898df281", "b891a528-4b5e-4ba7-949c-2a32cb5a75ec",
      "0d46d52b-75a2-4df2-b363-43874c9503a2", "c1e4b8cf-0116-46bf-8dc9-55eb074ad315",
      "6fd24ac6-1bb0-4ea6-a084-52cc22e9be42", "5f8780af-93e8-4907-9794-f8c960e87d34",
      "692b1947-8b2e-45e4-8051-0319b7f0e438", "dde46f46-ff48-4763-9c50-377834ce7137")

    userID(random.nextInt(20))
  }

  def ganerateItemID()={
    val random = new Random
    val itemID = Array("小米","休闲鞋","洗衣机","显示器","显卡","洗衣液","行车记录仪")
    itemID(random.nextInt(7))
  }

  def ganerateCityID()={
    val random = new Random
    val CityNames = Array("上海", "北京", "深圳", "广州", "纽约", "伦敦", "东京", "首尔", "莫斯科", "巴黎")
    CityNames(random.nextInt(10))
  }

  def ganerateDevice()={
    val random = new Random
    val Devices = Array("android","iphone","ipad","PC")
    Devices(random.nextInt(4))
  }

  def ganerateUserLogs(numberItems: Int, pathPath: String): Unit = {
    val userLogBuffer = new StringBuffer()
    val fileName = "SparkSQLUserlogsHottest.log"
    val formate = "yyyy-MM-dd"
    for (i <- 0 until  numberItems){
      val date = getCountDate(null,formate,-1)
      val userID = ganerateUserID()
      val itemID = ganerateItemID()
      val cityID = ganerateCityID()
      val device = ganerateDevice()
      userLogBuffer.append(date+"\t"
        +userID +"\t"+itemID+"\t"+cityID+"\t"+device+"\n")
      println(userLogBuffer.toString)
      writeLog(pathPath,fileName,userLogBuffer+"")
    }

  }


}
