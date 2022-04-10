package sample;

import static java.lang.Thread.sleep;
class Casovac extends Thread{


    public int count=0;
    @Override
    public void run() {
        while (true){
            try {

                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            count++;


        }


    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
