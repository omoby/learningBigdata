package DT;

import scala.math.Ordered;

import java.io.Serializable;
import java.util.Objects;

/**
 * 自定义二次排序的Key
 */
public class SecondarySort implements Ordered<SecondarySort>, Serializable
{
    //需要二次排序的Key
    private int first;
    private int second;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecondarySort that = (SecondarySort) o;
        return first == that.first && second == that.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }



    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }


    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }


    //二次排序公开构造器
    public SecondarySort(int first,int second){
        this.first = first;
        this.second = second;
    }

    @Override
    public int compare(SecondarySort that) {
        if(this.first - that.getFirst() != 0){
            return this.first - that.getFirst();
        }else{
            return this.second - that.getSecond();
        }

    }

    @Override
    public boolean $less(SecondarySort that) {
        if (this.first < that.getFirst()){
            return true;
        }else if(this.first == that.getFirst() && this.second < that.getSecond()){
            return true;
        }
        return false;
    }

    @Override
    public boolean $greater(SecondarySort other) {
        if (this.first > other.getFirst()){
            return true;
        }else if(this.first == other.getFirst() && this.second > other.getSecond()){
            return true;
        }
        return false;
    }

    @Override
    public boolean $less$eq(SecondarySort that) {
        if(this.$less(that)){
            return true;
        }else if(this.first == that.getFirst() && this.second == that.getSecond()){
            return true;
        }
        return false;
    }

    @Override
    public boolean $greater$eq(SecondarySort that) {
        if (this.$greater(that)){
            return true;
        }else if (this.first == that.getFirst() && this.second == that.getSecond()){
            return true;
        }
            return false;
    }

    @Override
    public int compareTo(SecondarySort that) {
        if(this.first - that.getFirst() != 0){
            return this.first - that.getFirst();
        }else{
            return this.second - that.getSecond();
        }
    }



}
