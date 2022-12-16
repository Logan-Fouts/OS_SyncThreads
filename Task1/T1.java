package Task1;

import java.util.concurrent.Semaphore;

// Since sA has one avaiable instance and sB has none sA will run first. When it is done sA's count is 0 and sB is released adding one to the count of sB allowing sB to run.
// Each A is printed 10 times but after each time the B is printed.
public class T1 {
    static Semaphore sA = new Semaphore(1);
    static Semaphore sB = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        // Create and run the threads.
        Thread tA = new Thread(new threadA());
        Thread tB = new Thread(new threadB());
        System.out.println("\nThreads starting!\n~~~~~~~~~~~~~~");
        tA.start();
        tB.start();

        tA.join();
        tB.join();
    }

    static class threadA extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // Try to acuire a permit from sA if not wait.
                try {
                    sA.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Somethings Brokey: sA count:" + sA.availablePermits() + "  sB count:"
                            + sB.availablePermits());
                    e.printStackTrace();
                }
                // Print A then release a permit for sB.
                System.out.print("A");
                sB.release();
            }
        }
    }

    static class threadB extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                // Try to acuire a permit from sB if not wait.
                try {
                    sB.acquire();
                } catch (InterruptedException e) {
                    System.out.println("Somethings Brokey: sA count:" + sA.availablePermits() + "  sB count:"
                            + sB.availablePermits());
                    e.printStackTrace();
                }
                // Print B then release a permit for sA.
                System.out.print("B");
                sA.release();
                // Prettier spacing.
                if (i == 9)
                    System.out.println("\n");
            }
        }
    }
}
