package DT;

import java.util.*;

/**
 * FileName: JavaCollections
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午7:11
 * Description:
 */
public class JavaCollections {
    public static void main(String[] args){
        //listOps();
        //linkedListOps();
        //iteratorOps();
        listIteratorOps();
    }

    /**
     * ListIterator--允许程序员按任一方向遍历列表、迭代期间修改列表，并获得迭代器在列表中的当前位置
     */
    private static void listIteratorOps() {
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        linkedList.add("D");
        linkedList.addFirst("0");
        linkedList.addLast("9");
        ListIterator<String> iterator = linkedList.listIterator();
        while (iterator.hasNext()){
            String item = iterator.next();
        }
        while (iterator.hasPrevious()){
            System.out.println(iterator.previous());
        }

    }

    /**
     * 迭代器操作
     *
     */
    private static void iteratorOps() {
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        linkedList.add("D");
        linkedList.addFirst("0");
        linkedList.addLast("9");
        Iterator<String> iterator = linkedList.iterator();
        while (iterator.hasNext()){
            String item = iterator.next();
            if ("C".equals(item)){
                iterator.remove();
            }else{
                System.out.println(item);
            }
        }
        Iterator<String> iterator1 = iterator;
        while (iterator1.hasNext()){
            System.out.println(iterator1.next());
        }
    }

    /**
     * linkedList--双向列表
     */
    private static void linkedListOps() {
        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        linkedList.add("D");
        System.out.println(linkedList);
        linkedList.addFirst("0");
        linkedList.addLast("9");
        System.out.println(linkedList);
    }

    /**
     * 列表的操作
     */

    public static void listOps(){
        List<String> list = null;
        list = new ArrayList<String>();
        Collection<String> collections = new ArrayList<String>();
        //向列表添加一个元素
        list.add("Spark");
        list.add("Hadoop");
        list.add("Scala");
        list.add("Kafka");
        list.add("Techyon");
        list.add("Kafka");
        list.add("Techyon");
        collections.add("Hive");
        collections.add("MySQL");
        collections.add("Hbase");
        //向列表同时添加多个元素
        list.addAll(collections);
        System.out.println(list);

        //删除元素
        list.remove(0);//删除指定索引的元素
        System.out.println(list);
        list.remove("MySQL"); //删除指定的元素
        System.out.println(list);
        //遍历列表
        for (int i =0;i < list.size(); i++){
            System.out.print(list.get(i)+"\t");
        }
        System.out.println();
        //将列表转化为字符串数组
        String[] data =  list.toArray(new String[]{});
        for (String item:data){
            System.out.println(item);
        }
        //截取列表的子链表，包括【1,3）
        List<String> subList = list.subList(1,3);
        for (String item:subList){
            System.out.println(item);
        }
    }
}
