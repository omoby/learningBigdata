package JVM;

/**
 * FileName: StackOverFlow
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-27 下午10:00
 * Description:
 */

public class StackOverFlow {
    private int counter;
    public void count(){
        counter++;
        count();
    }
    public static void main(String[] agrs){
        System.out.println("StackOverFlow");
        StackOverFlow stackOverFlow = new StackOverFlow();
        try{
            stackOverFlow.count();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
