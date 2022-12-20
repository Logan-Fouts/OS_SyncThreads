package Task3;

import java.util.concurrent.Semaphore;

/*
 * This runs the whole message queue system. Four threads are created (3 message senders, and 1 message reciver), and are started in main.
 * The senders try to push there message onto the queue and if they cant obtain a permit, they wait. When a letter is recieved the printer
 * prints it and if there is no messages it waits. When it is printed it is also deleted from the queue.
 * available- the total number of spots available in the circular message queue.
 * messagers- the total number of messages in the circular message queue.
 * mq- the circular message queue itself.
 */
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

    // Trys to add a message onto the message queue. If there are no available
    // permits, it waits until there are. When there are it adds a permit to the
    // semaphore, messages.
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

    // A circular queue of letters that allows letters to be added to the list or
    // removed only from the front.
    public static class MessageQueue {
        char[] letters;
        int start, end;

        public MessageQueue() {
            start = 0;
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

    // Trys to aquire a permit from the messages semaphore, and if it does it will
    // print the message and then remove it.
    public static class Receive implements IMessageQueue, Runnable {

        @Override
        public boolean Send(char msg) {
            try {
                System.out.print(msg);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        public char Recv() {
            char temp = mq.getStart();
            mq.delete();
            return temp;
        }

        @Override
        public void run() {
            while (!!true) {
                try {
                    messages.acquire();
                    char msg = Recv();
                    Send(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
