package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * FileName: MRSecondarySort
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-8 下午12:36
 * Description:
 */

public class MRSecondarySort {

    //使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
    public static class MRSecondarySortMapper extends Mapper<LongWritable, Text, Fruit, NullWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] data = line.split("\t");
            Fruit fruit = null;
            if (data.length == 3){
                fruit = new Fruit(data[0],data[1],new Integer(data[2]));
            }
            context.write(fruit,NullWritable.get());
        }
    }

    //使用Reducer将输入的key本身作为key直接输出


    public static class MRSecondarySortReducer extends Reducer<Fruit, NullWritable, Text,NullWritable> {

        @Override
        protected void reduce(Fruit key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            String string = key.getDate()+" "+key.getName()+ " " + key.getSales();
            context.write(new Text(string),NullWritable.get());
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
            System.out.println("Usage: MRSecondarySort <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf,"MRSecondarySort");
        //设置主类
        job.setJarByClass(MRSecondarySort.class);
        //设置处理Mapper
        job.setMapperClass(MRSecondarySortMapper.class);
        //设置map输出的key类型
        job.setMapOutputKeyClass(Fruit.class);
        //设置map输出的value类型
        job.setMapOutputValueClass(NullWritable.class);
        //设置Reducer
        job.setReducerClass(MRSecondarySortReducer.class);
        //设置Reducer输出的Key类型
        job.setOutputKeyClass(Text.class);
        //设置Reducer输出的value类型
        job.setOutputValueClass(NullWritable.class);
        //设置分区数
        job.setPartitionerClass(FriutPartition.class);
        //设置分组函数
        job.setGroupingComparatorClass(GroupComparator.class);
        //设定输入路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        //设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
class Fruit implements WritableComparable<Fruit> {
    private static final Logger logger = LoggerFactory.getLogger(Fruit.class);
    private String date;
    private String name;
    private Integer sales;

    public Fruit() {
    }

    public Fruit(String date, String name, Integer sales) {
        this.date = date;
        this.name = name;
        this.sales = sales;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (this == o)
            return true;
        if (o instanceof Fruit){
            Fruit fruit = (Fruit) o;
            return fruit.getDate().equals(this.getDate()) && fruit.getName().equals(this.getName())&&(this.getSales()==fruit.getSales());
        }else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        return this.date.hashCode() * 157 + this.sales + this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.date+" "+ this.name+" "+ this.sales;
    }

    @Override
    public int compareTo(Fruit fruit) {
        int result = this.date.compareTo(fruit.getDate());
        if (result == 0){
            int result1 = this.sales - fruit.getSales();
            if (result1 == 0){
                double result2 = this.name.compareTo(fruit.getName());
                if (result2 > 0){
                    return  -1;
                }else if (result2 < 0){
                    return 1;
                }else {
                    return 0;
                }
            }else if(result1 > 0){
                return -1;

            }else if (result1 < 0){
                return 1;
            }
        }else if (result >0){
            return -1;
        }else {
            return 1;
        }
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.date);
        dataOutput.writeUTF(this.name);
        dataOutput.writeInt(this.sales);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.date  = dataInput.readUTF();
        this.name = dataInput.readUTF();
        this.sales = dataInput.readInt();

    }
}

class FriutPartition extends Partitioner<Fruit, NullWritable> {
    @Override
    public int getPartition(Fruit fruit, NullWritable nullWritable, int i) {
        return Math.abs(Integer.parseInt(fruit.getDate()) * 127 ) % i;
    }
}

class GroupComparator extends WritableComparator {
    protected GroupComparator(){
        super(Fruit.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        Fruit x = (Fruit)a;
        Fruit y = (Fruit)b;
        if (!x.getDate().equals(y.getDate())){
            return x.getDate().compareTo(y.getDate());
        }else{
            return x.getSales().compareTo(y.getSales());
        }
    }
}

