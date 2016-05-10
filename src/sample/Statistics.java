package sample;
/* This class stores token-specific information */

public class Statistics {

    private double invDocFreq;
    private double termFreq;
    private String pageURL;
    private double tfIdf;
    private int tokenPosition;

    public Statistics(int termFreq, double invDocFreq, double tfIdf,
                      int tokenPosition) {

        this.setInvDocFreq(invDocFreq);
        this.setTermFreq(termFreq);
        this.setTokenPosition(tokenPosition);
    }

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    public double getInvDocFreq() {
        return invDocFreq;
    }

    public void setInvDocFreq(double invDocFreq) {
        this.invDocFreq = invDocFreq;
    }

    public double getTermFreq() {
        return termFreq;
    }

    public void setTermFreq(double termFreq) {

		/* logged term frequency */
        this.termFreq = (2 * (1 + Math.log(termFreq)));

    }

    public double getTfIdf() {
        return tfIdf;
    }

    public void setTfIdf(double tfIdf) {
        this.tfIdf = tfIdf;
    }

    public int getTokenPosition() {
        return tokenPosition;
    }

    public void setTokenPosition(int tokenPosition) {
        this.tokenPosition = tokenPosition;
    }

    public String toString() {

        String s = "\nTF: " + termFreq + "\n " + "IDF: " + invDocFreq + "\n"
                + "TF.IDF: " + tfIdf + "\n";

        return s;

    }
}
