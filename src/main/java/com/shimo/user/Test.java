package com.shimo.user;

import com.shimo.spring.ShiMoApplicationContext;
import com.shimo.user.service.UserService;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * <p>
 *
 * </p>
 *
 * @author 世墨
 * @since 2022/7/25 9:38
 */
public class Test {

    public static void main(String[] args) {
        final ShiMoApplicationContext shiMoApplicationContext = new ShiMoApplicationContext(AppConfig.class);
        final UserService userService = (UserService) shiMoApplicationContext.getBean("userService");
        System.out.println(userService);
        userService.print();
    }



//		public static void main(String[] args) {
//			final CountDownLatch latch = new CountDownLatch(2);
//
//			new Thread(() -> {
////				try {
//					System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
////					Thread.sleep(3000);
//					System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
//					latch.countDown();
////				} catch (InterruptedException e) {
////					e.printStackTrace();
////				}
//			}).start();
//
//			new Thread(() -> {
////				try {
//					System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
////					Thread.sleep(3000);
//					System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
//					latch.countDown();
////				} catch (InterruptedException e) {
////					e.printStackTrace();
////				}
//			}).start();
//
//			try {
//				System.out.println("等待2个子线程执行完毕...");
//				latch.await();
//				System.out.println("2个子线程已经执行完毕");
//				System.out.println("继续执行主线程");
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}


//    public static void main(String[] args) {
//        int N = 4;
//        CyclicBarrier barrier  = new CyclicBarrier(N);
//        for(int i=0;i<N;i++)
//            new Writer(barrier).start();
//    }
//    static class Writer extends Thread{
//        private CyclicBarrier cyclicBarrier;
//        public Writer(CyclicBarrier cyclicBarrier) {
//            this.cyclicBarrier = cyclicBarrier;
//        }
//
//        @Override
//        public void run() {
//            System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
//            try {
////					Thread.sleep(5000);      //以睡眠来模拟写入数据操作
//                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
//                cyclicBarrier.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }catch(BrokenBarrierException e){
//                e.printStackTrace();
//            }
//            System.out.println("所有线程写入完毕，继续处理其他任务...");
//        }
//    }


//		public static void main(String[] args) {
//			int N = 8;            //工人数
//			Semaphore semaphore = new Semaphore(5); //机器数目
//			for(int i=0;i<N;i++)
//				new Worker(i,semaphore).start();
//		}
//
//		static class Worker extends Thread{
//			private int num;
//			private Semaphore semaphore;
//			public Worker(int num,Semaphore semaphore){
//				this.num = num;
//				this.semaphore = semaphore;
//			}
//
//			@Override
//			public void run() {
//				try {
//					semaphore.acquire();
//					System.out.println("工人"+this.num+"占用一个机器在生产...");
//					Thread.sleep(2000);
//					System.out.println("工人"+this.num+"释放出机器");
//					semaphore.release();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//		}

    /**
     * 1）CountDownLatch和CyclicBarrier都能够实现线程之间的等待，只不过它们侧重点不同：
     *
     * CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
     *
     * 而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
     *
     * 另外，CountDownLatch是不能够重用的，而 CyclicBarrier（c k 白 瑞 奥） 是可以重用的。
     *
     * 2）Semaphore（撒 么 否 ） 其实和锁有点类似，它一般用于控制对某组资源的访问权限。
     */
}
