package DT;




import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * FileName: HelloHDFS
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-4 上午11:51
 * Description:
 */
public class HelloHDFS {
    public static void main(String[] args) throws IOException {
        String uri = "hdfs://Master:9000";
        Configuration config = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(uri),config);
        //列出hdfs上/usr/spark目录下的所有文件和目录
        FileStatus[] statuses = fs.listStatus(new Path("/"));
        for ( FileStatus status : statuses){
            System.out.println(status);
        }
        //在hdfs的/usr/spark目录中写入一个文件，并写入一个文本行
        FSDataOutputStream os = fs.create(new Path("/user/hadoop/test.log"));
        os.write("Hello World!".getBytes());
        os.flush();
        os.close();
        //显示在hdfs的//home/hadoop/test/test.log下的内容
        InputStream is = fs.open(new Path("/user/hadoop/test.log"));
        IOUtils.copyBytes(is,System.out,1024,true);
    }
}
