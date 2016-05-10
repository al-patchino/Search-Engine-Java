package sample;


public class PageInfo {

    private String title;
    private int totalWords;
    private int docID;
    private String URL;
    private int votes;
    private String html;
    private double pageScore;
    private double cosSim;
    private double accumulatedTFIDF = 0.0;
    private int numOfLinks = 0;

    public PageInfo(String URL, String title, int totalWords, int docID,
                    String html, int numOfLinks) {

		// This stores document information
        this.setURL(URL);
        this.setTitle(title);
        this.setTotalWords(totalWords);
        this.setDocID(docID);
        this.setHtml(html);
        this.setNumOfLinks(numOfLinks);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public double getPageScore() {
        return pageScore;
    }

    public void setPageScore(double pageScore) {
        this.pageScore = this.pageScore + pageScore;
    }

    public double getCosSim() {
        return cosSim;
    }

    public void setCosSim() {

        this.cosSim = (accumulatedTFIDF / Math.log(totalWords)) * 100;

        System.out.println("Sim: " + cosSim + ", AccTFIDF: " + accumulatedTFIDF
                + ", totalWords:  " + totalWords + " DocID: " + docID
                + " URL: " + URL);
    }

    public double getAccumulatedTFIDF() {
        return accumulatedTFIDF;
    }

    public void setAccumulatedTFIDF(double accumulatedTFIDF) {

        this.accumulatedTFIDF = +(accumulatedTFIDF / Math.log(totalWords) * 100);

        System.out.println(this.accumulatedTFIDF + " = " + accumulatedTFIDF
                + "/ log" + totalWords + " title: " + this.title);
    }

    public int getNumOfLinks() {
        return numOfLinks;
    }

    public void setNumOfLinks(int numOfLinks) {
        this.numOfLinks = numOfLinks;
    }

    public void clearScores() {

        this.pageScore = 0;
        this.accumulatedTFIDF = 0;
        this.cosSim = 0;
    }
}
