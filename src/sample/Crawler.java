package sample;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.plugin.javascript.JSClassLoader;
import java.util.ArrayList;

import java.io.IOException;

public class Crawler {

    // Master list of URL to crawl
    private static ArrayList<String> linksToVisit = new ArrayList<>();
    private static ArrayList<String> linksAlreadyVisited = new ArrayList();
    private static ArrayList<String> docIDtoURL = new ArrayList<>();
    private static ArrayList<String> hostsAlreadyVisited = new ArrayList<>();
    private static ArrayList<String> hostBlackList = new ArrayList<String>();

    String pageText;
    Elements links;



    private static int discardedLinks = 0;
    int numOfLinks = 0;

    static int docID = 0;

    private static int maxPagesToCrawl = 0;


    public Crawler(){}

    public boolean crawl(){

        boolean done = false;

        // Connect to URL and store HTML content
        String currentURL = linksToVisit.get(0);
        System.out.print("Crawling... " + currentURL);
        Document doc = null;

        // Try to connect to URL
        try {
            doc = Jsoup.connect(currentURL).get();
            System.out.print(" .......Done! ");
            done = true;

            // Remove link from queue
            linksToVisit.remove(0);

            // Scrape all links from HTML
            links = doc.getElementsByTag("a");

            // Add current URL to list of links already visited
            linksAlreadyVisited.add(currentURL);

            // Maps the docID to their URL counterparts for fast retrieval later
            docIDtoURL.add(docID, currentURL);

            // Iterate through obtained links, check if they are valid
            for (Element link : links) {

                numOfLinks++;

                // Convert from relative URL to absolute URL
                String tempLink = link.attr("abs:href").toString().toLowerCase();

                //System.out.println(tempLink);

                // Check if URL is valid, if yes then add to queue
                if ((!isValidURL(tempLink)) || linksToVisit.contains(tempLink)) {
                    discardedLinks++;
                } else {
                    linksToVisit.add(tempLink);
                }
            }

            // Send HTML document to text analysis
            TextAnalyser tA = new TextAnalyser();
            tA.tokeniser(doc, docID, numOfLinks);

            System.out.println(" Found " + links.size() + " links");

            // Increment docID
            docID++;

        } catch (IOException e) {
            System.out.println(".....Not crawled! (IOException)");
            done = false;
            linksToVisit.remove(0);
        } catch (IllegalArgumentException f){
            System.out.println(".....Not crawled! (IllegalArgumentException)");
            done = false;
            linksToVisit.remove(0);
        }

        return done;

    }

    public boolean isValidURL(String tempLink) {

        boolean isValidURL = true;

		/* Gets host name */
        URI uri = null;
        String hostName = null;
        try {
            uri = new URI(tempLink);
            hostName = uri.getHost();
        } catch (URISyntaxException e) {

            System.out.print("Bad URL: " + hostName);
            isValidURL = false;

        }

        if (tempLink == "" || tempLink == " ") {
            // System.out.println("Error! Blank URL: " + tempLink);
            isValidURL = false;
        }
		/* Checks if URLs lead to undownloadable content */
        else if (tempLink.contains(".jpg") || tempLink.contains(".jpeg")
                || tempLink.contains(".png") || tempLink.contains(".svg")
                || tempLink.contains(".gif") || tempLink.contains(".bmp")
                || tempLink.contains(".pdf") || tempLink.contains(".xml")
                || tempLink.contains("irc:") || tempLink.contains("https:")
                || tempLink.contains("ftp:") || tempLink.contains("mailto:")
                || tempLink.contains("skype:") || tempLink.contains("ssh:")) {

            isValidURL = false;
        }

		/* Checks if the url has already been visited */
        else if (linksAlreadyVisited.contains(tempLink)) {
            // System.out.println("Error! URL already visited: " + tempLink);
            isValidURL = false;
        }

		/*
		 * Checks if link contains a framgment identifier. For example,
		 * "http://www.example.org/foo.html#bar" is the same page as
		 * "http://www.example.org/foo.html". The hash mark merely points the
		 * browser to the header with id="bar".
		 */
        else if (tempLink.contains("#")) {
            // System.out.println("Error! URL leads to same page");
            isValidURL = false;
        }

		/* Checks if host name is in black list */
        else if (hostBlackList.contains(hostName)) {

            isValidURL = false;

            System.out.println(hostName + " Blacklisted!");
        }

        return isValidURL;
    }

    public void addToQueue(String url){

        linksToVisit.add(url);
    }

    public int getMaxPagesToCrawl() { return maxPagesToCrawl;}

    public static void setMaxPagesToCrawl(int maxPagesToCrawl) { Crawler.maxPagesToCrawl = maxPagesToCrawl;}

    public static int getTotalNumOfPagesCrawled (){ return docIDtoURL.size();}

    public static int getIndexOfURL(String url){ return docIDtoURL.indexOf(url);}

    public String getLastLinkVisited() {

        if(linksAlreadyVisited != null & !linksAlreadyVisited.isEmpty()){
            return linksAlreadyVisited.get(linksAlreadyVisited.size() - 1);
        }else return "Queue is empty!";

    }

    public int getNumberOfLinksAlreadyVisited(){
        return linksAlreadyVisited.size();
    }

    public int getNumberOfDiscardedLinks(){
        return discardedLinks;
    }

    public ArrayList getDocIDArrayList(){
        return docIDtoURL;
    }

    public int getNumberOfLinksStillToVisit(){return linksToVisit.size();}

    public void printDocIDToURL(){

        for(int i = 0; i < docIDtoURL.size(); i++){
            System.out.println("DocID: " + i + " URL " + docIDtoURL.get(i));
        }
    }
}