package sample;


import org.jsoup.Jsoup;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.util.Map;
import java.util.TreeMap;

public class Ranking {

    /* Results will be stored in this map. */
    static Map<Double, String> searchResults = new TreeMap<Double, String>()
            .descendingMap();

    /* Calculates IDF, and thus TF.IDF weighting */
    public void SetTFIDF(String token) throws BadLocationException {

		/* This does not not change */
        int totalNumOfDocuments = Crawler.getTotalNumOfPagesCrawled();

		/* How many documents contain the token? */
        int numOfDocumentsContainingToken = InvertedIndex.invertedIndex.get(
                token).size();

		/* Calculate IDF */
        double IDF = Math.log(totalNumOfDocuments
                / numOfDocumentsContainingToken);

		/* Setting the IDF & Tf.IDF for all relevant documents */
        for (Integer docIDKey : InvertedIndex.invertedIndex.get(token).keySet()) {

			/* To avoid division by 0, overwrite it to a small number. */
            if (IDF == 0) {
                IDF = 0.000001;
            }

			/* Iterating through all documents containing token, and setting IDF */
            InvertedIndex.invertedIndex.get(token).get(docIDKey)
                    .setInvDocFreq(IDF);

			/* Calculating TF.IDF weighting for all entries in inverted file */
            double currentTf = InvertedIndex.invertedIndex.get(token)
                    .get(docIDKey).getTermFreq();

            int tokenPosition = InvertedIndex.invertedIndex.get(token)
                    .get(docIDKey).getTokenPosition();

            System.out.println("token position " + tokenPosition);

			/* Calculates a modifies tf*idf with a weighted token positon */
            double currentTfIdf = currentTf * IDF
                    * ((1000 / tokenPosition) + 1);

			/* Sets TF*IDF */
            InvertedIndex.invertedIndex.get(token).get(docIDKey)
                    .setTfIdf(currentTfIdf);

        }
    }

    public void SetAccumulatedTFIDF(String token) {

		/* Calculates page score relative to query */
        for (Integer docIDKey : InvertedIndex.invertedIndex.get(token).keySet()) {

            double currentTfIdf = InvertedIndex.invertedIndex.get(token)
                    .get(docIDKey).getTfIdf();

            try{
				/* Sets accumulated scores and divides by document length */
                TextAnalyser.pagesInfo.get(docIDKey).setAccumulatedTFIDF(
                        currentTfIdf);

            }catch(IndexOutOfBoundsException e){
                JOptionPane.showMessageDialog(null,
                        "Oops! Something went wrong.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }


			// Add to search results if not already
            if (!Ranking.searchResults.containsValue(TextAnalyser.pagesInfo
                    .get(docIDKey).getURL())) {
                Ranking.searchResults.put(TextAnalyser.pagesInfo.get(docIDKey)
                                .getAccumulatedTFIDF(),
                        TextAnalyser.pagesInfo.get(docIDKey).getURL());
            }
        }
    }

    // Clears all pagescores and tf-idf weights.
    public void clearPageScores() {

		/* Iterates through all documents and clears it. */
        for (int docIDKey = 0; docIDKey < TextAnalyser.pagesInfo.size(); docIDKey++) {

            TextAnalyser.pagesInfo.get(docIDKey).clearScores();
        }
        searchResults.clear();
    }

    /* This method is responsible for formatting the search results. */
    static String showSearchResults() {

        String result = "<HTML>";
        for (Double key : Ranking.searchResults.keySet()) {

            String url = Ranking.searchResults.get(key);

			/* Retrieve docID of URL */
            int index = Crawler.getIndexOfURL(url);

            String title = null;
            String firstLine = "No description available";
            int score = 0;


            // Checks if there is a title available, if not then use URL as title
            if (index == -1) {
                title = Ranking.searchResults.get(key);
            } else {

				// Tries to load page description if available. */
                try {
                    title = TextAnalyser.pagesInfo.get(index).getTitle();

                    String html = TextAnalyser.pagesInfo.get(index).getHtml();

					/* Retrieve page score. */
                    score = (int) TextAnalyser.pagesInfo.get(index)
                            .getAccumulatedTFIDF();

					/* Cuts paragraph on web page to 100 characters */
                    firstLine = Jsoup.parse(html).getElementsByTag("p").text()
                            .toString().substring(0, 300)
                            + "...";

                } catch (StringIndexOutOfBoundsException e) {

                    firstLine = "No description available.";
                }
            }

            result += "<a href=\"" + Ranking.searchResults.get(key) + "\">"
                    + title + "</a><p>" + firstLine + "</p><br><hr>";

        }
        result += "</HTML>";

		/* GUI update */
        //GUI.resultsPane.setText(result);
        //GUI.resultsPane.setCaretPosition(0);

        return result;

    }
}