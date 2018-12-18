package DT;

/**
 * FileName: JavaThread
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午3:28
 * Description:
 */
public class JavaThread {
    public static void main(String[] args){
       Thread helloThread =  new HelloThread(); //启动线程
        helloThread.setPriority(Thread.NORM_PRIORITY);
        helloThread.start();
        helloThread.interrupt();
       new Thread(new MyLogic()).start();
        Thread thread = new Thread(new MyLogic());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.getName());

        System.out.println("Priority : "+thread.getPriority());
        System.out.println(Thread.currentThread().getName());
      TicketLogicThread saleTicket = new TicketLogicThread();
      Thread t1 = new Thread(saleTicket);
        Thread t2 = new Thread(saleTicket);
        Thread t3 = new Thread(saleTicket);
        Thread t4 = new Thread(saleTicket);
        Thread t5 = new Thread(saleTicket);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}
class HelloThread extends  Thread {
    @Override
    public void run() {
        super.run();
        for (int i = 0; i < 5; i++) {
            System.out.println(i);
            if (i == 3)
                interrupt();
        }
    }
}

class MyLogic implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i);
        }
    }
}

class TicketLogicThread implements Runnable{
    private int ticket = 10;
    @Override
    public void run() {
        for (int i = 0; i < 2;i++){
            synchronized (this){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Ticket ID: "+ticket-- +" ThreadNum: "+Thread.currentThread().getName());
            }
        }

    }
}