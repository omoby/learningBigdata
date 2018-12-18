package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FileName: JoinWorkersInformation
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-7 上午8:06
 * Description:
 */
public class JoinWorkersInformation {
    /**
     * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
     */
    public static class JoinWorkersInformationMapper extends Mapper<LongWritable, Text, LongWritable, WorkersInformation> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String inputData = value.toString();
            String[] data = inputData.split("\t");
            if (data.length <= 3) {
                WorkersInformation department = new WorkersInformation();
                department.setDepartmentNo(data[0]);
                department.setDepartmentName(data[1]);
                department.setFlag(0);
                context.write(new LongWritable(Integer.parseInt(data[0])), department);
            } else if (data.length ==8 ) {
                WorkersInformation worker = new WorkersInformation();
                worker.setWorkerNo(data[0]);
                worker.setWorkerName(data[1]);
                worker.setDepartmentNo(data[7]);
                worker.setFlag(1);
                context.write(new LongWritable(Integer.valueOf(data[7])), worker);

            }else if (data.length == 7){
                WorkersInformation worker = new WorkersInformation();
                worker.setWorkerNo(data[0]);
                worker.setWorkerName(data[1]);
                worker.setDepartmentNo(data[6]);
                worker.setFlag(1);
                context.write(new LongWritable(Integer.valueOf(data[6])),worker);
            }
        }
    }

    /**
     * 使用Reducer将输入的key本身作为key直接输出
     */



    public static class JoinWorkersInformationReducer extends Reducer<LongWritable, WorkersInformation, NullWritable,Text> {

        @Override
        protected void reduce(LongWritable key, Iterable<WorkersInformation> values, Context context) throws IOException, InterruptedException {
            WorkersInformation department = null;

            List<WorkersInformation> workerList = new ArrayList<WorkersInformation>();
            for(WorkersInformation item : values)
                if (0 == item.getFlag()) {
                    department = new WorkersInformation(item);
                } else {
                    workerList.add(new WorkersInformation(item));
                }
            for (WorkersInformation worker : workerList){
               // worker.setDepartmentNo(department.getDepartmentNo());
                worker.setDepartmentName(department.getDepartmentName());
                context.write(NullWritable.get(),new Text(worker.toString()));
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();//设置MapReduce的配置
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: JoinWorkersInformation <in> [<in>...] <out>");
            System.exit(2);
        }

        //设置作业
        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(JoinWorkersInformation.class);
        job.setJobName("JoinWorkersInformation");
        //设置处理map,reduce的类JoinWorkersInformationMapper
        job.setMapperClass(JoinWorkersInformationMapper.class);
        job.setReducerClass(JoinWorkersInformationReducer.class);

        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(WorkersInformation.class);
        //设置输入输出格式的处理
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(Text.class);
        //设定输入输出路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}

 class WorkersInformation implements WritableComparable{
    private String workerNo = "";
    private String workerName = "";
    private String departmentNo = "";
    private String departmentName = "";
    private int flag = 0; //0代表department，1代表Worker

    public WorkersInformation(){

    }

    public WorkersInformation(String workerNo, String workerName, String departmentNo, String departmentName, int flag) {
        this.workerNo = workerNo;
        this.workerName = workerName;
        this.departmentNo = departmentNo;
        this.departmentName = departmentName;
        this.flag = flag;
    }

    public WorkersInformation(WorkersInformation information){
        this.workerNo = information.workerNo;
        this.workerName = information.workerName;
        this.departmentNo = information.departmentNo;
        this.departmentName = information.departmentName;
        this.flag = information.flag;
    }
    public String getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(String workerNo) {
        this.workerNo = workerNo;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getDepartmentNo() {
        return departmentNo;
    }

    public void setDepartmentNo(String departmentNo) {
        this.departmentNo = departmentNo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(workerNo);
        dataOutput.writeUTF(workerName);
        dataOutput.writeUTF(departmentNo);
        dataOutput.writeUTF(departmentName);
        dataOutput.writeInt(flag);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.workerNo = dataInput.readUTF();
        this.workerName = dataInput.readUTF();
        this.departmentNo = dataInput.readUTF();
        this.departmentName = dataInput.readUTF();
        this.flag = dataInput.readInt();

    }

    @Override
    public String toString() {
        return this.workerNo + " "+ this.workerName + " "+ this.departmentNo+ " "+ this.departmentName;
    }
}
