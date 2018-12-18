package DT;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * FileName: JavaIO
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-2 下午4:22
 * Description:Java IO操作
 */
public class JavaIO {
    public static void main(String[] args) throws FileNotFoundException {
     //creatFile(); //
     //deleteFile();
     //creatDir();
       //listDir();
        //isDir();
        //listAllFiles("/home/hadoop/learnJava");
        //randomAccessFileOps();
        //outputStreamOps();
//        InputStreamOps();
//        outputStreamReaderWriter();
//        byteArrayStream();
//        systemOut();
//        zipStream(); //压缩流
        serialiazable();
    }

    private static void serialiazable() throws FileNotFoundException {
        File file = new File("/home/hadoop/learnJava/Spark/JavaFile/Java.txt");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(new Worker("hadoop",10));
            outputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            Object object = objectInputStream.readObject();
            System.out.println(object);
            objectInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    static class Worker implements Serializable {
        private transient String name;
        private int age;

        public Worker(String name,int age){
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }


    }

    private static void zipStream() throws FileNotFoundException {
        File zipFile = new File("/home/hadoop/learnJava/Spark/JavaFile/Java.zip");
        InputStream fileInputStrem = new FileInputStream(new File("/home/hadoop/learnJava/Spark/JavaFile/Java.txt"));
        ZipOutputStream zipOutputStream = null;
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
            zipOutputStream.putNextEntry(new ZipEntry("/home/hadoop/learnJava/Spark/JavaFile/Java.txt"));
            zipOutputStream.setComment("DT;");
            int tmp = 0;
            while ((tmp = fileInputStrem.read()) != -1) {
                zipOutputStream.write(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fileInputStrem.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            zipOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void systemOut() {
       /* OutputStream out  = System.out;
        try {
            out.write("Hello Spark".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       InputStream in = System.in;
       byte[] b = new byte[1024];
       System.out.println("Please type your content here: ");
        try {
            int length = in.read(b);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The content your typed is : "+ new String(b));
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void byteArrayStream() {
        String content = "Spark is the most popular,Computation Framework for big data!!!";
       ByteArrayInputStream byteArrayInputStream =  new ByteArrayInputStream(content.getBytes());
       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
       int tmp = 0 ;
       while((tmp = byteArrayInputStream.read())!= -1){
           char c = (char)tmp;
           byteArrayOutputStream.write(Character.toUpperCase(c));
       }
           System.out.println(byteArrayOutputStream.toString());
       try{
           byteArrayInputStream.close();
           byteArrayOutputStream.close();
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    private static void outputStreamReaderWriter() {
        File file = new File("/home/hadoop/learnJava/Spark/JavaFile/Java.txt");
        try{
            OutputStream outputStream = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(outputStream);
            writer.write("Just do it!\n");
            writer.flush();
            writer.close();
        }catch ( Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 对文件输入流的处理
     */
    private static void InputStreamOps() {
           File file = new File("/home/hadoop/learnJava/Spark/JavaFile/Spark.txt");
           try{
                InputStream inputStream = new FileInputStream(file);//创建一个向指定 File 对象表示的文件中写入数据的文件输出流。如果第二个参数为 true，则将字节写入文件末尾处，而不是写入文件开始处。创建一个新 FileDescriptor 对象来表示此文件连接。
                byte[] content = new byte[1024];
                inputStream.read(content);
                System.out.println(new String(content));
                inputStream.close();
           }catch (Exception e){
               e.printStackTrace();
           }


    }

    /**
     * 对文件输出流的处理
     */
    private static void outputStreamOps() {
        File file = new File("/home/hadoop/learnJava/Spark/JavaFile/Spark.txt");
        try{
             OutputStream out  = new FileOutputStream(file,true);//创建一个向指定 File 对象表示的文件中写入数据的文件输出流。如果第二个参数为 true，则将字节写入文件末尾处，而不是写入文件开始处。创建一个新 FileDescriptor 对象来表示此文件连接。
             byte[] data = "Hello BigData".getBytes();
             out.write(data);
             out.close();
        }catch (IOException e){
            e.printStackTrace();
     }

    }

    /**
     * 随机的读取文件
     */
    private static void randomAccessFileOps() {
        File file = new File("/home/hadoop/learnJava/Spark/JavaFile/Spark.txt");
        try {
            RandomAccessFile rdf  = new RandomAccessFile(file,"rw");
            rdf.writeBytes("Scala\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环遍历目录下的所有文件
     * @param path
     */
    private static void listAllFiles(String path) {
        File file = new File(path);
        //如果下一级是目录，接着循环遍历
        if (file.isDirectory()){
            File[] f = file.listFiles();
            if (f != null){
                for (File item :f){
                    listAllFiles(item.getAbsolutePath());
                }
            }
        }else{ //如果是文件，则输出文件
            System.out.println(file);
        }
    }

    /**
     * 判断是否是路径
     */
    private static void isDir() {
        File file = new File("/home/hadoop/learnJava");
        if (file.isDirectory()){
            System.out.println(file.getPath() + " is a directory!");
        }
    }

    /**
     * 列出指定文件夹下的文件
     */
    private static void listDir() {
        //指定要列出的文件路径
        File file = new File("/home/hadoop/learnJava");
        // 列出当前路径下的所有文件
       /* String[] files = file.list();
        for (String item :files){
            System.out.println(item);
        }*/
        //列出完整路径的文件
        File[] files = file.listFiles();

        for(File fileName:files){
            System.out.println(fileName);
        }
    }

    /**
     * 创建制定为文件目录
     */
    private static void creatDir() {
        //指定要创建的文件路径
        File file = new File("/home/hadoop/learnJava/Spark/JavaFile");
        if(!file.exists()){
            file.mkdir();//创建文件
            //file.mkdirs();//创建多级目录
        }

    }

    /**
     * 删除指定的文件
     */
    private static void deleteFile() {
        //指定要删除的文件
        File file  = new File("/home/hadoop/learnJava/Java");
        //删除指定文件
        if (file.exists()){
           file.delete();
        }
    }

    /**
     * 对文件操作,创建不存在的文件
     */

    private static void creatFile() {
        //指定创建的文件对象
        File file = new File("/home/hadoop/learnJava/Java.txt");
        //判断要创建的文件是否存在，如果存在删除后在创建
        if (file.exists()) {
            file.delete();
        }
        //创建文件
        try {
            file.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
