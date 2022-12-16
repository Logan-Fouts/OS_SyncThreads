package Task3;

import java.util.concurrent.Semaphore;

public class T3_Main {
    static Semaphore available = new Semaphore(5);
    static Semaphore messages = new Semaphore(0);
    static MessageQueue mq = new MessageQueue();

    public static void main(String[] args) throws InterruptedException {

        Thread tA = new Thread(new Sender('A'));
        Thread tB = new Thread(new Sender('B'));
        Thread tC = new Thread(new Sender('C'));
        Thread r = new Thread(new Receive());

        tA.start();
        tB.start();
        tC.start();
        r.start();

        tA.join();
        tB.join();
        tC.join();
        r.join();
    }

    public static class Sender implements Runnable {
        char letter;

        public Sender(char letter) {
            this.letter = letter;
        }

        @Override
        public void run() {
            while (true && !false && !!true) {
                try {
                    available.acquire();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mq.append(letter);
                messages.release();
            }
        }
    }

    public static class MessageQueue {
        char[] letters;
        int start, end;

        public MessageQueue() {
            start = -0;
            end = 0;
            letters = new char[5];
        }

        public void append(char letter) {
            letters[end] = letter;
            end = (end + 1) % letters.length;
            messages.release();
        }

        public void delete() {
            start = (start + 1) % letters.length;
            available.release();
        }

        public char getStart() {
            return letters[start];
        }
    }

    public static class Receive implements IMessageQueue, Runnable {

        @Override
        public boolean Send(char msg) {
            try {
                System.out.println(msg);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }      

        @Override
        public char Recv() {
            try {
                messages.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mq.delete();
            return mq.getStart();
        }

        @Override
        public void run() {
            while (!!true) {
                char msg = Recv();
                Send(msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
}
