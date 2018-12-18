package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * FileName: SelfJion
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-9 下午4:44
 * Description:
 */
public class SelfJion {
    //使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
    public static class SelfJionMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] array;
          array = value.toString().trim().split(" ");
          if (array.length==2){
              context.write(new Text(array[1].trim()),new Text("1_"+array[0])); //左表
              context.write(new Text(array[0].trim()),new Text("0_"+array[1]));//右表
          }

        }
    }

    //使用Reducer将输入的key本身作为key直接输出
    public static class SelfJionReducer extends Reducer<Text, Text, Text,Text> {

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Iterator<Text> iterator = values.iterator();
            List<String> grandChildList = new ArrayList<String>();
            List<String> grandParentList = new ArrayList<String>();
            while (iterator.hasNext()){
                String item = iterator.next().toString();
                String[] splited = item.split("_");
                if (splited[0].equals("1")){
                    grandChildList.add(splited[1]);
                }else {
                    grandParentList.add(splited[1]);

                }
            }
            if (grandChildList.size() >0 && grandParentList.size()>0){
                for(String grandChild:grandChildList){
                    for (String grandParent:grandParentList){
                        context.write(new Text(grandChild),new Text(grandParent));
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();//设置MapReduce的配置
        Path outputPath = new Path(args[1]);
        FileSystem hdfs = outputPath.getFileSystem(conf);
        // 判断路径是否存在，如果存在，则删除
        if (hdfs.isDirectory(outputPath)){
            hdfs.delete(outputPath,true);
        }
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: SelfJion <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf,"SelfJion");
        //设置主类
        job.setJarByClass(SelfJion.class);
        //设置处理Mapper
        job.setMapperClass(SelfJionMapper.class);
        //设置map输出的key类型
        job.setMapOutputKeyClass(Text.class);
        //设置map输出的value类型
        job.setMapOutputValueClass(Text.class);
        //设置Reducer
        job.setReducerClass(SelfJionReducer.class);
        //设置Reducer输出的Key类型
        job.setOutputKeyClass(Text.class);
        //设置Reducer输出的value类型
        job.setOutputValueClass(Text.class);
        //设定输入路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        //设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
