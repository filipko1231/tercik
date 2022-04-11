package sample;

import com.sun.javafx.geom.Point2D;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
    @FXML
    Circle circ0,circ1,circ2,circ3,circ4,circ5,circ6,circ7,circ8,circ9;
    @FXML
    Line sipka;
    @FXML
    Button vynulovat;
    @FXML
    Button start;
    @FXML
    TextArea text;
    @FXML
    TextField pocetStriel;
    @FXML
    TextField meno;




    int perioda=0;
    double xmysi,ymysi,xmysiFix,ymysiFix,xmysilast,ymysilast;
    Random rn=new Random();
    int cas_do_zatrasenia;
    Casovac c = new Casovac();
    int body = 0;
    int pokusy = 0;
    vektor v;
    boolean hrame = false;



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
        v= new vektor(nahodnost_vetra.getValue(),smer_vetra.getValue(),sila_vetra.getValue());
        v.start();
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
                setSipka();
                cas_do_zatrasenia--;
            }
        }.start();
        pocetStriel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    pocetStriel.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        pocetStriel.setText("");
    }
    public void vector_update(){
        v.sh();
        v= new vektor(nahodnost_vetra.getValue(),smer_vetra.getValue(),sila_vetra.getValue());
        v.start();
        System.out.println("xdxdxdxdxdxdx");
    }

    public void setSipka(){
        sipka.setStartX(sipka.getEndX()+v.x/4.5);
        sipka.setStartY(sipka.getEndY()+v.y/4.5);
    }
    public void pifpaf(MouseEvent mouseEvent) throws IOException {
        if (hrame){
            Circle c = new Circle(kriz.getX()+kriz.getFitWidth()/2,kriz.getY()+kriz.getFitHeight()/2,5);
            c=vietor(c);
            c=penalizacia(500,c);
            System.out.println("pocet bodov = "+pocetb(c));
            body += pocetb(c);
            pokusy--;
            System.out.println(c.getCenterX()+" "+c.getCenterY());
            Spetny_raz(100);
            panel.getChildren().add(c);
        }
        if (hrame && pokusy == 0){
            hrame = false;
            Files.write(Paths.get("tabulka.txt"), (meno.getText()+";" + Integer.parseInt(pocetStriel.getText()) + ";" + body+"\n").getBytes(), StandardOpenOption.APPEND);
        }
        updateScore();
    }
    public Circle vietor(Circle c){
        Circle vystup =c;

        vystup.setCenterY(vystup.getCenterY()+v.y);
        vystup.setCenterX(vystup.getCenterX()+v.x);

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
    public int pocetb(Circle stred){
        Circle vystup =stred;
        int sucet=0;

        if (contain(vystup,circ0)){
            sucet+=10;
        }
        if (contain(vystup,circ1)){
            sucet+=10;
        }
        if (contain(vystup,circ2)){
            sucet+=10;
        }
        if (contain(vystup,circ3)){
            sucet+=10;
        }
        if (contain(vystup,circ4)){
            sucet+=10;
        }
        if (contain(vystup,circ5)){
            sucet+=10;
        }
        if (contain(vystup,circ6)){
            sucet+=10;
        }
        if (contain(vystup,circ7)){
            sucet+=10;
        }
        if (contain(vystup,circ8)){
            sucet+=10;
        }
        if (contain(vystup,circ9)){
            sucet+=10;
        }



        return sucet;
    }
    public boolean contain(Circle vystup,Circle kruhy){
        return Math.sqrt((288 - vystup.getCenterY()) * (288 - vystup.getCenterY()) + (326 - vystup.getCenterX()) * (326 - vystup.getCenterX()))<=kruhy.getRadius();
    }


    public void start(){
        if (!pocetStriel.getText().isEmpty() && !meno.getText().isEmpty()){
            if (pocetStriel.getText().charAt(0) != '0'){
                hrame = true;
                start.setDisable(true);
                pocetStriel.setDisable(true);
                meno.setDisable(true);
                pokusy = Integer.parseInt(pocetStriel.getText());
                updateScore();
            }
        }
    }
    public void updateScore(){
        text.setText("Pokusy : "+pokusy+"\n" +
                     "Body : "+body);
    }
    public void vynulovatScore(){
        pokusy = 0;
        body = 0;
        hrame = false;
        updateScore();
        pocetStriel.setDisable(false);
        start.setDisable(false);
        meno.setDisable(false);
    }

}

