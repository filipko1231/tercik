package sample;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;


public class Controler_Tabulka implements Initializable {
    @FXML
    TableView tabulka;
    @FXML
    TextArea text;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File file = new File("tabulka.txt");
        try (Scanner input = new Scanner(file))
        {
            while (input.hasNextLine())
            {
                text.appendText(input.nextLine() + "\n");
            }
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
        }

        /*try {
            BufferedReader br = new BufferedReader(new FileReader("tabulka.txt"));
            String str = "";
            /*while ((str = br.readLine()) != ""){
                String [] pole = str.split(";");
                Tabulka tab = new Tabulka(pole[0],pole[1],pole[2]);
                tabulka.getItems().add(tab);
            }


            str = br.readLine();
            System.out.println(str);
            String [] pole = str.split(";");
            //Tabulka tab = new Tabulka(pole[0],pole[1],pole[2]);


            ObservableList data = FXCollections.observableArrayList(
                    new Tabulka(pole[0],pole[1],pole[2])
            );
            //tabulka.setItems(data);
            /*System.out.println(tab.getMeno()+" "+tab.getStrely()+" "+tab.getSkore());
            tabulka.getItems().set(1,tab);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
