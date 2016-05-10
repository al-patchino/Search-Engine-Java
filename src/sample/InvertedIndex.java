package sample;


/* This is my Inverted index class which is responsible for storing my tokens and mapping it to their respective IDs.
 * Basic information such as term frequency is also stored. This inverted index may be visualised as the following:
 *
          Tokens:		 docID:			Statistics (Object):
 *			cat ---------->	1				TF: 2, etc.
 *					|----->	3				TF: 5, etc.
 *					|----->	7				TF: 1, etc.
 *					|----->	8				TF: 4, etc.
 *
 *			dog ---------->	2				TF: 7, etc.
 *					|----->	5				TF: 8, etc.
 *
 *			lizard ------->	1				TF: 1, etc.
 *					|----->	6				TF: 5, etc.
 *					|-----> 9				TF: 3, etc.
 *			...more entries...

 Such that token "cat" is found at documents 1, 3, 7, 8, with term frequencies of 2, 5, 1 and 4, respectively. */

import java.util.Map;
import java.util.HashMap;

public class InvertedIndex {

    /*
     * Declaring the inverted index as HashMap Strings (tokens) mapped to
     * another HashMap containing Intergers (docID) mapped to Statistics (Object
     * to hold stats) Token DocID Object
     */


    public static Map<String, Map<Integer, Statistics>> invertedIndex = new HashMap<String, Map<Integer, Statistics>>();

    /*
     * This indexer will take the incoming tokens are store them to the inverted
     * index as described above.
     */
    static void Indexer(String token, int docID, int totalNumOfWords,
                        int tokenPosition) {

        if (invertedIndex.containsKey(token)) {

			/*
			 * If token has already been found in the current document, then
			 * increment term frequency.
			 */
            if (invertedIndex.get(token).containsKey(docID)) {

				/* Increment term frequency */
                invertedIndex
                        .get(token)
                        .get(docID)
                        .setTermFreq(
                                (invertedIndex.get(token).get(docID)
                                        .getTermFreq()) + 1);

            } else { /*
					 * If the token already exists but found again in new
					 * document, then add new docID to it. Set TF = 1, since
					 * it's the first token
					 */

                invertedIndex.get(token).put(docID,
                        new Statistics(1, 0.0, 0.0, tokenPosition));
            }
        }

        else {

			/*
			 * If it's a new token, then add said token, create a hashmap and
			 * link it to its docID
			 */

            HashMap<Integer, Statistics> innerMap = new HashMap<Integer, Statistics>();

            innerMap.put(docID, new Statistics(1, 0.0, 0.0, tokenPosition));

            invertedIndex.put(token, innerMap);

            // JTree token update
            //GUI.eTree.addNodeInSortedOrder(token, "Inverted Index");

            //GUI.eTree.addNodeTo(Main.currentURL, token);
        }
    }
}
