package sample;

import javafx.scene.control.Slider;

import java.util.Random;

import static java.lang.Thread.sleep;
class vektor extends Thread{


    public double nahodnost_vetra;
    public double smer_vetra;
    public double sila_vetra;
    public double x;
    public double y;
    boolean sh = true;

    public vektor(double nahodnost_vetra, double smer_vetra, double sila_vetra)
    {
        this.nahodnost_vetra = nahodnost_vetra;
        this.smer_vetra = smer_vetra;
        this.sila_vetra = sila_vetra;
    }

    @Override
    public void run() {
        while (sh){
            try {

                sleep(short_galton(15,0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            x = Math.sin(Math.toRadians(smer_vetra-nahodnost_vetra+short_galton((int) nahodnost_vetra,0)))*short_galton((int) sila_vetra,0);
            y = Math.cos(Math.toRadians(smer_vetra-nahodnost_vetra+short_galton((int) nahodnost_vetra,0)))*short_galton((int) sila_vetra,0);



        }


    }
    public int short_galton(int najpravdepodobnejsi_vysledok,int zaciatok){
        return galton(najpravdepodobnejsi_vysledok*2,zaciatok);
    }
    public int galton(int hladina , int pozicia){
        Random rd = new Random();
        if (hladina==0){
            return pozicia;
        }else if (rd.nextBoolean()){
            return galton(hladina-1,pozicia);
        }else {
            return galton(hladina-1,pozicia+1);
        }
    }

    public void setNahodnost_vetra(double nahodnost_vetra)
    {
        this.nahodnost_vetra = nahodnost_vetra;
    }

    public void setSmer_vetra(double smer_vetra)
    {
        this.smer_vetra = smer_vetra;
    }

    public void setSila_vetra(double sila_vetra)
    {
        this.sila_vetra = sila_vetra;
    }
    public void sh(){
        sh=false;
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double getY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}