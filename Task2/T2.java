package Task2;

import java.util.concurrent.Semaphore;

public class T2 {
    static Semaphore sA = new Semaphore(1);
    static Semaphore sB = new Semaphore(0);
    static Semaphore sC = new Semaphore(0);
    static Semaphore sD = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        // Create and run the threads.
        Thread tA = new Thread(new threadA());
        Thread tB = new Thread(new threadB());
        Thread tC = new Thread(new threadC());
        Thread tD = new Thread(new threadD());

        System.out.println("\nThreads starting!\n~~~~~~~~~~~~~~");

        tA.start();
        tB.start();
        tC.start();
        tD.start();

        tA.join();
        tB.join();
        tC.join();
        tD.join();
    }

    static class threadA extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    sA.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Somethings Brokey");
                    e.printStackTrace();
                }
                System.out.print("A");
                if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8) {
                    sB.release();
                } else {
                    sC.release();
                }
            }
        }
    }

    static class threadB extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    sB.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Somethings Brokey");
                    e.printStackTrace();
                }
                System.out.print("B");
                sA.release();
            }
        }
    }

    static class threadC extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    sC.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Somethings Brokey");
                    e.printStackTrace();
                }
                System.out.print("C");
                if (i == 0 || i == 2 || i == 4 || i == 6 || i == 8) {
                     sD.release();
                } else {
                    sA.release();
                }
                if (i == 9) System.out.println("\n");
            }
        }
    }

    static class threadD extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    sD.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Somethings Brokey");
                    e.printStackTrace();
                }
                System.out.print("D");
                sC.release();
            }
        }
    }
}
