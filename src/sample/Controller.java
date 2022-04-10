package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    ImageView kriz;
    @FXML
    Slider unava;
    @FXML
    AnchorPane panel;
    @FXML
    Slider sila_vetra;
    @FXML
    Slider smer_vetra;
    @FXML
    Slider nahodnost_vetra;

    int perioda=0;
    double xmysi,ymysi,xmysiFix,ymysiFix,xmysilast,ymysilast;
    Random rn=new Random();
    int cas_do_zatrasenia;
    Casovac c = new Casovac();


    int tolerancia_pohybu=5;
    int penalizacia_za_pohyb = 30;

    public int galtonBoard(int pozicia, int hladina){
        if (hladina==0) return pozicia;
        else if (rn.nextInt(2)==1) return galtonBoard(pozicia+1,hladina-1);
        return galtonBoard(pozicia,hladina-1);
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

    public void mouseMove(MouseEvent mouseEvent) {
        //x_nová = x_stará*0.9+x_nová_nameraná*0.1;      filtrácia
        //x_nová = x_stará+(x_nová_nameraná-x_stará)*0.1

        xmysi=mouseEvent.getSceneX()-kriz.getFitWidth()/2;           //xmysi
        ymysi=mouseEvent.getSceneY()-kriz.getFitHeight()/2;//ymysi


        if (!(xmysi>=xmysilast-tolerancia_pohybu && xmysi<=xmysilast+tolerancia_pohybu && ymysi>=ymysilast-tolerancia_pohybu && ymysi<=ymysilast+tolerancia_pohybu )){
            xmysilast = xmysi;
            ymysilast = ymysi;
            c.count=0;


        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        c.start();
        xmysilast = 0;
        ymysilast = 0;
        cas_do_zatrasenia = short_galton(15,0);
        new AnimationTimer() {
            @Override
            public void handle(long l) {

                if (cas_do_zatrasenia == 0){
    /*              int rozptyl = (int) (10 * (unava.getValue()+1));
                    xmysi +=  rn.nextInt(2*rozptyl)-rozptyl;
                    ymysi +=  rn.nextInt(2*rozptyl)-rozptyl;
    */
                    xmysiFix = xmysi + short_galton(5,-5) * ((unava.getValue()*3)+1);

                    ymysiFix = ymysi + short_galton(5,-5) * ((unava.getValue()*3)+1);

                    cas_do_zatrasenia = short_galton(15,0);
                }
                kriz.setX(kriz.getX()*0.97+xmysiFix*0.03);
                kriz.setY(kriz.getY()*0.97+ymysiFix*0.03);

                cas_do_zatrasenia--;
            }
        }.start();
    }

    public void pifpaf(MouseEvent mouseEvent) {
        Circle c = new Circle(kriz.getX()+kriz.getFitWidth()/2,kriz.getY()+kriz.getFitHeight()/2,5);
        c=vietor(c);
        c=penalizacia(500,c);

        Spetny_raz(100);
        panel.getChildren().add(c);
    }
    public Circle vietor(Circle c){
        Circle vystup =c;

        vystup.setCenterY(vystup.getCenterY()+(Math.sin(Math.toRadians(smer_vetra.getValue()-nahodnost_vetra.getValue()+short_galton((int) nahodnost_vetra.getValue(),0))))*short_galton((int) sila_vetra.getValue(),0));
        vystup.setCenterX(vystup.getCenterX()+(Math.cos(Math.toRadians(smer_vetra.getValue()-nahodnost_vetra.getValue()+short_galton((int) nahodnost_vetra.getValue(),0))))*short_galton((int) sila_vetra.getValue(),0));

        return vystup;
    }
    public Circle penalizacia(int cas ,Circle stred){
        Circle vystup =stred;
        if (c.count<=cas) {
            vystup.setCenterY(vystup.getCenterY()+short_galton(penalizacia_za_pohyb,0)-penalizacia_za_pohyb);
            vystup.setCenterX(vystup.getCenterX()+short_galton(penalizacia_za_pohyb,0)-penalizacia_za_pohyb);
        }
        return vystup;
    }

    public void Spetny_raz(int Power){
        ymysiFix=ymysiFix-short_galton(Power,0);

    }


}

