package sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.jsoup.nodes.Document;


public class TextAnalyser {

    InvertedIndex myIndex = new InvertedIndex();

    //
    static ArrayList<String> listOfStopWords = new ArrayList();

    // ArrayList to store general page info, such as title, text, word count, etc
    static ArrayList<PageInfo> pagesInfo = new ArrayList<PageInfo>(10);

    public TextAnalyser() throws IOException {

    }

    // This method takes the document and splits into individual tokens */
    public void tokeniser(Document doc, int docID, int numOfLinks) {

		// Store general document info
        String pageText = doc.text();
        String pageHtml = doc.html();
        String pageTitle = doc.title();
        String pageURL = doc.baseUri().toLowerCase();

        // Load stop words
        if(listOfStopWords.isEmpty()) loadStopWords();

        // Breaks a body of text into tokens, param2 are the delimiters
        StringTokenizer st = new StringTokenizer(pageTitle + pageText,
                "\":;¬`|– —,._?!&$/*=<>[]()-\t\n");

        String token = null;
        int tokenPosition = 0;

		// Counts number of tokens in document
        int totalNumOfWords = st.countTokens();

		// Iterate through all tokens
        while (st.hasMoreTokens()) {

			// Converts current token to lower case
            token = st.nextToken().toLowerCase();

			// Increments position
            tokenPosition++;

			// Add token if it's not a stop word
            if (!isStopWord(token)) {

				// Stem token
                token = stem(token);

                // Send token to inverted index
                InvertedIndex.Indexer(token, docID, totalNumOfWords,
                        tokenPosition);
            } else {
				//System.out.print("(Stopword: " + token + ") ");
            }
        }

		// Adds all the collected info to the PageInfo class
        pagesInfo.add(docID, new PageInfo(pageURL, pageTitle, totalNumOfWords,
                docID, pageHtml, numOfLinks));

    }

    /* This tokeniser is custom built for the user's query */
    public void tokeniser(String userQuery) {

        StringTokenizer st = new StringTokenizer(userQuery, " ,.?!<>()-\t\n");

        String token = null;

        while (st.hasMoreTokens()) {

            if (isStopWord(token) == false) {

                token = stem(token);
            }
        }
    }

    // Checks if it's a stop word by checking a stop word list
    static public boolean isStopWord(String token) {

        boolean isAStopWord = false;

        if (listOfStopWords.contains(token)) {
            isAStopWord = true;
        }

        return isAStopWord;
    }

    /*
     * This stemmer calls the Stemmer class. This uses Porter's stemming
     * algorithm, which I do not own. Porter's algorithm can be obtained from
     * here: http://tartarus.org/martin/PorterStemmer/
     */
    static public String stem(String token) {

        char[] chToken = token.toCharArray();

        Stemmer st = new Stemmer();

        st.add(chToken, chToken.length);
        st.stem();
        String stemmedToken = st.toString();

        return stemmedToken;
    }

    /* Add stop words to ArrayList */
    static public void loadStopWords(){

        listOfStopWords.add("a");
        listOfStopWords.add("and");
        listOfStopWords.add("are");
        listOfStopWords.add("be");
        listOfStopWords.add("if");
        listOfStopWords.add("in");
        listOfStopWords.add("is");
        listOfStopWords.add("of");
        listOfStopWords.add("on");
        listOfStopWords.add("or");
        listOfStopWords.add("so");
        listOfStopWords.add("the");
        listOfStopWords.add("they");
        listOfStopWords.add("there");
        listOfStopWords.add("this");
        listOfStopWords.add("which");
        listOfStopWords.add("why");

    }

}
