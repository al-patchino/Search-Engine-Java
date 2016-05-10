package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/************************************************************************************************
 * Project : A Search Engine in Java 4.0.1
 *
 *
 * Owner : Patrick A. Machado, (M.Eng) Computer Engineering School of
 * Engineering, University of Portsmouth. up631136@myport.ac.uk
 *
 * Description : A search engine based on common information retrieval
 * techniques found in Introduction to Information Retriaval by Christopher
 * Manning. This project is intended for the unit ENG554: Collaborative and
 * Pervasive Network Applications. This software uses Jsoup parser and Porter's
 * stemming algorithm, which I do not own.
 *
 ***************************************************************************************/



public class Main extends Application {
   // public class Main {

    @Override
   public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Search Engine 1.4");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
        primaryStage.setResizable(false);
     }

    public static void main(String[] args) {
        launch(args);

        System.out.println("Hello pls");
    }
}