package DT;

/**
 * FileName: DesignPatternOps
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-1 下午9:47
 * Description:
 */
public class DesignPatternOps {
    public static void main(String[] args) {
      /*  Network network = null;//定义接口对象
        network = new NetWorkProxy(new NetWorkStub());
        network.browse();
*/
      Friut f = null;
      f = FriutFactory.getInstance("pairs");
      if (f != null){
          f.eat();
      }

    }
}


interface Network{
    public void browse();
}
class  NetWorkStub implements Network{
    @Override
    public  void browse(){
        System.out.println("Information from NetWork!");
    }
}
class NetWorkProxy implements Network{
    private Network network;
    public NetWorkProxy(Network network){
        this.network = network;
    }
    @Override
    public void browse(){
        network.browse();
    }
    public void login(){
        System.out.println("Login....");
    }
}

interface Friut{
    public void eat();
}
class Apple implements Friut{
    @Override
    public void eat(){
        System.out.println("Eat an Apple");
    }
}
class Pair implements Friut{
    @Override
    public void eat(){
        System.out.println("Eat a pairs");
    }
}

class FriutFactory{
    public static Friut getInstance(String friut){
        Friut f = null;
        if ("apple".equals(friut)){
            f = new Apple();
        }
        if ("pairs".equals(friut)){
            f = new Pair();
        }
        return f;
    }
}