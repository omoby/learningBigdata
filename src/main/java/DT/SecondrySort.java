/*
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

class Fruit implements WritableComparable<Fruit>{
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

class FriutPartition extends Partitioner<Fruit, NullWritable>{
    @Override
    public int getPartition(Fruit fruit, NullWritable nullWritable, int i) {
        return Math.abs(Integer.parseInt(fruit.getDate()) * 127 ) % i;
    }
}

class GroupComparator extends WritableComparator{
    protected GroupComparator(){
        super(Fruit.class);
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

*/
