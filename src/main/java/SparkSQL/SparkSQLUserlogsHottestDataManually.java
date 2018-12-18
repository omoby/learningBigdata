package SparkSQL;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

/**
 * FileName: testl
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-24 下午9:07
 * Description:
 */

public class SparkSQLUserlogsHottestDataManually {

        public static void main(String[] args) {
            long numberItems = 10000;
            String logPath = "/home/hadoop/IdeaScala/";
            ganerateUserLogs(numberItems,logPath);
        }

        /**
          * 生成userLog
          * @param numberItems
          * @param path
          */
        private static void ganerateUserLogs(long numberItems, String path) {
            StringBuffer userLogBuffer = new StringBuffer();
            // String filename = getCountDate(null, "yyyyMMddHHmmss", -1) + ".log";
            String filename = "SparkSQLUserlogsHottest.log";
            // 元数据：Date、UserID、Item、City、Device；
            for (int i = 0; i < numberItems; i++) {
                String date = getCountDate(null, "yyyy-MM-dd", -1);
                // String timestamp = getCountDate(null, "yyyy-MM-dd HH:mm:ss", -1);
                String userID = ganerateUserID();
                String ItemID = ganerateItemID();
                String CityID = ganerateCityIDs();
                String Device = ganerateDevice();
                /* userLogBuffer.append(date + "\t" + timestamp + "\t" + userID
                + "\t" + pageID + "\t" + channelID + "\t" + action + "\n");*/

                userLogBuffer.append(date + "\t" + userID
                        + "\t" + ItemID + "\t" + CityID + "\t" + Device + "\n");
                System.out.println(userLogBuffer);
                WriteLog(path, filename, userLogBuffer + "");
            }
        }


        public static void WriteLog(String path, String filename, String strUserLog)
        {
            FileWriter fw = null;
            PrintWriter out = null;
            try {
                File writeFile = new File(path + filename);
                if (!writeFile.exists())
                    writeFile.createNewFile();
                else {
                    writeFile.delete();
                }
                fw = new FileWriter(writeFile, true);
                out = new PrintWriter(fw);
                out.print(strUserLog);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null)
                        out.close();
                    if (fw != null)
                        fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
          * 获得日期
          * @param date
          * @param patton
          * @param step
          * @return
          */

        public static String getCountDate(String date, String patton, int step) {
            SimpleDateFormat sdf = new SimpleDateFormat(patton);
            Calendar cal = Calendar.getInstance();
            if (date != null) {
                try {
                    cal.setTime(sdf.parse(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            cal.add(Calendar.DAY_OF_MONTH, step);
            return sdf.format(cal.getTime());
        }

        /**
          * 随机生成用户ID
          * @return
          */

        private static String ganerateUserID() {
            Random random = new Random();
            String[] userID = { "98415b9c-f3d4-45c3-bc7f-dce3126c6c0b", "7371b4bd-8535-461f-a5e2-c4814b2151e1",
                    "49852bfa-a662-4060-bf68-0dddde5feea1", "8768f089-f736-4346-a83d-e23fe05b0ecd",
                    "a76ff021-049c-4a1a-8372-02f9c51261d5", "8d5dc011-cbe2-4332-99cd-a1848ddfd65d",
                    "a2bccbdf-f0e9-489c-8513-011644cb5cf7", "89c79413-a7d1-462c-ab07-01f0835696f7",
                    "8d525daa-3697-455e-8f02-ab086cda7851", "c6f57c89-9871-4a92-9cbe-a2d76cd79cd0",
                    "19951134-97e1-4f62-8d5c-134077d1f955", "3202a063-4ebf-4f3f-a4b7-5e542307d726",
                    "40a0d872-45cc-46bc-b257-64ad898df281", "b891a528-4b5e-4ba7-949c-2a32cb5a75ec",
                    "0d46d52b-75a2-4df2-b363-43874c9503a2", "c1e4b8cf-0116-46bf-8dc9-55eb074ad315",
                    "6fd24ac6-1bb0-4ea6-a084-52cc22e9be42", "5f8780af-93e8-4907-9794-f8c960e87d34",
                    "692b1947-8b2e-45e4-8051-0319b7f0e438", "dde46f46-ff48-4763-9c50-377834ce7137" };
            return userID[random.nextInt(20)];
        }

        /**
          * 随机生成pageID
          * @return
          */

        private static String ganerateItemID() {
            Random random = new Random();
            //String[] ItemIDs = { "xiyiji", "binxiang", "kaiguan", "reshuiqi", "ranqizao", "dianshiji", "kongtiao" };
            String[] ItemIDs = { "小米", "休闲鞋", "洗衣机", "显示器", "显卡", "洗衣液", "行车记录仪" };
            return ItemIDs[random.nextInt(7)];
        }

        /**
          * 随机生成channelID
          * @return
          */

        private static String ganerateCityIDs() {
            Random random = new Random();
            /*String[] CityNames = { "shanghai", "beijing", "ShenZhen", "HangZhou", "Tianjin", "Guangzhou", "Nanjing", "Changsha", "WuHan","jinan" }*/;
            String[] CityNames = { "上海", "北京", "深圳", "广州", "纽约", "伦敦", "东京", "首尔", "莫斯科", "巴黎" };
            return CityNames[random.nextInt(10)];
        }

        /**
          * 随机生成action
          * @return
          */
        private static String ganerateDevice() {
            Random random = new Random();
            String[] Devices = { "android", "iphone", "ipad" };
            return Devices[random.nextInt(3)];
        }

        /**
          * 生成用户Userid
          * @param num
          * @return
          */

        private static String ganerateUserID(int num) {
            StringBuffer userid = new StringBuffer();
            for (int i = 0; i < num; i++) {
                UUID uuid = UUID.randomUUID();
                userid.append("\"" + uuid + "\",");
            }
            System.out.println(userid);
            return userid + "";
        }

}
