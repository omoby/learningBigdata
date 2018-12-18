package DT;

/**
 * FileName: javaGenerics
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午11:36
 * Description:
 */
public class javaGenerics {
    public static void main(String[] args){
        /*Empty<String,Integer> empty = null;
        empty = new Empty<String,Integer>("Spark",1);
        //System.out.println(empty.getKey() +" "+empty.getValue());
        log(empty);*/

        ITNews<String> itNews = new ITNews<>("Spark is Faster");
        Integer[] data = {1,2,3,4,5,6,7};
        arrayGenerics(data);

    }

    private static <T> void arrayGenerics(T[] data) {
        for (T item : data){
            System.out.println(item);
        }
    }


    private static void log(Empty<? extends String, ? extends Number> empty) {
        System.out.println(empty.getKey() +" "+empty.getValue());
    }
}
 interface News<T>{

    public T getContent();
 }
 class  ITNews<T> implements News<T>{
    private T content;

     public ITNews(T spark_is_faster) {
         this.content = spark_is_faster;
     }

     public void setContent(T content) {
         this.content = content;
     }

     @Override
     public T getContent(){
        return content;
    }
 }
class Empty<K extends String,V extends Number> {
    private K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Empty(K key, V value) {
        this.key = key;
        this.value = value;
    }
}

/*class Empty<T>{
    private  T item;

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }

    public Empty(T item) {
        this.item = item;
    }
}*/
