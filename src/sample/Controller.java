package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.TableView;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Controller implements Initializable {

    // Search tab components
    @FXML private TextField search_searchBar;
    @FXML private WebView webBrowser;
    @FXML private Button search_backToResults;


    // Crawler Tab components
    @FXML private Button crawl_startButton;
    @FXML private TextField crawl_seedURL;
    @FXML private TextField crawl_maxPages;
    @FXML private TextArea crawl_textArea;
    @FXML private ProgressBar crawl_progressBar;
    @FXML private Label crawl_pagesCrawled;
    @FXML private Label crawl_pagesDiscarded;
    @FXML private Label crawl_pagesToCrawl;
    @FXML private TextArea crawl_pagesInfo;
    @FXML private ListView crawl_docList;

    // Test

    private Service<Void> backgroundThread;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Handler for crawl_start button
    public void handleStartCrawl(ActionEvent actionEvent) {

        Platform.setImplicitExit(false);

        backgroundThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        // Get seed URL from text field
                        System.out.println("Seed URL: " + crawl_seedURL.getText());
                        String seedURL = crawl_seedURL.getText().toLowerCase();

                        // Create crawler object
                        Crawler spider = new Crawler();

                        // Adds seed URL to queue
                        if(spider.isValidURL(seedURL) == true) spider.addToQueue(seedURL);

                        // Set max pages
                        spider.setMaxPagesToCrawl(Integer.parseInt(crawl_maxPages.getText()));
                        int maxPages = Integer.parseInt(crawl_maxPages.getText());

                        // Crawl until it reaches max number of pages
                        while(spider.getNumberOfLinksAlreadyVisited() < maxPages){

                            // Start crawler
                            spider.crawl();

                            // Update GUI elements
                            crawl_textArea.appendText("Crawled.. " + spider.getLastLinkVisited() + "\n");
                            float progress = (float)spider.getNumberOfLinksAlreadyVisited() / (float)maxPages;
                            crawl_progressBar.setProgress(progress);
                            crawl_pagesInfo.clear();
                            crawl_pagesInfo.appendText("Links already crawled: \t" +
                                    Integer.toString(spider.getNumberOfLinksAlreadyVisited()) + "\n");
                            crawl_pagesInfo.appendText("Links in queue: \t" +
                                    Integer.toString(spider.getNumberOfLinksStillToVisit()) + "\n");
                            crawl_pagesInfo.appendText("Links discarded: \t" +
                                    Integer.toString(spider.getNumberOfDiscardedLinks()) + "\n");

                            ObservableList<String> oList = FXCollections.observableArrayList(spider.getDocIDArrayList());

                            crawl_docList.getItems().addAll(oList);
                            //crawl_pagesCrawled.setText(Integer.toString(spider.getNumberOfLinksAlreadyVisited()));
                            //crawl_pagesDiscarded.setText(Integer.toString(spider.getNumberOfDiscardedLinks()));
                            //crawl_pagesCrawled.setText("hello");
                            //crawl_pagesDiscarded.setText("Hello");
                        }
                        spider.printDocIDToURL();

                        return null;
                    }
                };
            }
        };

        backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Thread Done!");
            }
        });

        crawl_textArea.textProperty().bind(backgroundThread.messageProperty());
        //Testing commit 2
        //crawl_pagesCrawled.textProperty().bind(backgroundThread.messageProperty());
        //crawl_pagesDiscarded.textProperty().bind(backgroundThread.messageProperty());



        backgroundThread.restart();


    }

    public void handleSearchButton(ActionEvent actionEvent){

       // Creates new instance of Ranking
        Ranking rank = new Ranking();

        final WebEngine webEngine = webBrowser.getEngine();

		// Clears search results if isn't empty
        if (!Ranking.searchResults.isEmpty()) {
            rank.clearPageScores();
            System.out.println("Scores cleared!");
        }

        // Checks if text box is empty. */
        if (search_searchBar.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "Please enter a search term or an URL!", "Error",
                    JOptionPane.ERROR_MESSAGE);

        }else {

            // Checks if it's an URL in search bar. URL correction if needed
            if (search_searchBar.getText().contains(".")) {
                if ((!search_searchBar.getText().contains("http://"))) {
						/* Display HTML to JEditorPane. */
                   // loadPage("http://" + userQueryTextbox.getText());
                    webEngine.load("http://" + search_searchBar.getText());

                } else {
						/* Display HTML to JEditorPane */
                    //loadPage(userQueryTextbox.getText());
                    webEngine.load(search_searchBar.getText());
                }
            }

		    // Checks if there are documents in the inverted index to search
            else if (InvertedIndex.invertedIndex.isEmpty()) {
                JOptionPane.showMessageDialog(
                                null,
                                "Error: Inverted index is empty. You must first crawl and index webpages first. ",
                                "Error", JOptionPane.ERROR_MESSAGE);
            } else {

					/* Converts user query to lower case */
                String userQuery = search_searchBar.getText().toLowerCase();

					/* Create instance of StringTokenizer */
                StringTokenizer st = new StringTokenizer(userQuery,
                        " ,.?!<>()-\t\n");

                String token = null;

				// Tokenise user's query
                while (st.hasMoreTokens()) {

                    token = st.nextToken();

					// Remove stop words
                    if (TextAnalyser.isStopWord(token) == false) {

                        token = TextAnalyser.stem(token);

                        System.out.println("Token: " + token);

                        // Checks if token in in index. Useless to calculate scores
                        // for non-existent tokens
                        if (InvertedIndex.invertedIndex.containsKey(token)) {

                            // Calculates ranking
                            try {
                                rank.SetTFIDF(token);
                                rank.SetAccumulatedTFIDF(token);

								// GUI update */
                                //indexSizeLabelValue.setText(Integer.toString(
                                       // InvertedIndex.invertedIndex.size()));

                            } catch (BadLocationException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

            // Display results in the web browser
            String results = Ranking.showSearchResults();
            webEngine.loadContent(results);


        }
        System.out.println("Size: " + Ranking.searchResults.size());

    }

    // Handler back to search results
    public void handleBackToSearchResults(ActionEvent actionEvent) {

        final WebEngine webEngine = webBrowser.getEngine();

       // webBrowser.



        if(!Ranking.searchResults.isEmpty()){
            String results = Ranking.showSearchResults();
            webEngine.loadContent(results);
        }
    }


}
