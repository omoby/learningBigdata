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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.StringTokenizer;

/**
 * FileName: InvertedIndex
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-9 下午9:04
 * Description:
 */
public class InvertedIndex {
    //使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
    public static class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {
        final Text number = new Text("1"); //标记每一个key
        private  String fileName; //存取每一个文件的名称
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            if (line.trim().length() >0){
                StringTokenizer stringTokenizer = new StringTokenizer(line); //对输入的句子进行切分单词
                while (stringTokenizer.hasMoreTokens()){
                    String keyForCombiner = stringTokenizer.nextToken()+":"+fileName; //将key设置为word：filename
                    context.write(new Text(keyForCombiner),number);

                }
            }
        }

        /**
         * 初始化，获得当前输入文件的名称
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
           FileSplit inputSplit = (FileSplit)context.getInputSplit();
           fileName = inputSplit.getPath().getName();
        }
    }


    //现在本地进行归并，在进行Shuffle
    public static class InvertedIndexCombiner extends Reducer<Text, Text, Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (Text item : values){
                sum += Integer.valueOf(item.toString());
            }
            String[] keyArray = key.toString().split(":");
            context.write(new Text(keyArray[0]),new Text(keyArray[1]+":"+sum));
        }
    }
    public static class InvertedIndexReducer extends Reducer<Text, Text, Text,Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer result = new StringBuffer();
            for (Text item: values){
                result.append(item+";");
            }
            context.write(key,new Text(result.toString().substring(0,result.toString().length()-1)));
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
            System.out.println("Usage: InvertedIndex <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf,"InvertedIndex");
        //设置主类
        job.setJarByClass(InvertedIndex.class);
        job.setCombinerClass(InvertedIndexCombiner.class);
        //设置处理Mapper
        job.setMapperClass(InvertedIndexMapper.class);
        //设置map输出的key类型
        job.setMapOutputKeyClass(Text.class);
        //设置map输出的value类型
        job.setMapOutputValueClass(Text.class);
        //设置Reducer
        job.setReducerClass(InvertedIndexReducer.class);
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
