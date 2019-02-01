import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class FrequencyCount {

    private static final int NUMBER_OF_WORDS_TO_PRINT = 10;
    public static void main(String[] args) {
    /*
    The following code was derived from my previous project: Adventure
     */
        // Initializing variables to be used for reading the Json file
        boolean urlIsValid = true;
        StringBuilder docString = new StringBuilder("");
        String urlToUse = "";
        URL docURL;
        InputStream inStream;
        InputStreamReader inStreamReader;
        Scanner userInputReader = new Scanner(System.in);

        do {
            // Prompt for and read input from the user
            urlIsValid = true;
            System.out.println("enter \"default\" to read the default document, or enter a URL to use a different one");
            String userInput = userInputReader.nextLine().trim();

            if (userInput.equals("default")) {
                urlToUse = "http://www.gutenberg.org/files/17921/17921-0.txt";
            } else {
                urlToUse = userInput;
            }

            // The following code turns the online Json file into a String
            try {
                docURL = new URL(urlToUse);
                inStream = docURL.openStream();
                inStreamReader = new InputStreamReader(inStream, Charset.forName("US-ASCII"));
                boolean isReading = true;

                // Individually reads characters from the input stream and appends them to the Json string
                while (isReading) {
                    int streamReturn = inStreamReader.read();
                    char charReturn;
                    if (streamReturn != -1) {
                        charReturn = (char) (streamReturn);
                        docString.append(charReturn);
                    } else {
                        isReading = false;
                    }
                }

            } catch (IOException e) {
                System.out.println(urlToUse + " isn't a valid filepath");
                urlIsValid = false;
            }

            // Prompt user again if the URL isn't valid
        } while (!urlIsValid);

        /*
        The following code is no longer derived from Adventure
         */

        System.out.print(getMostCommonWords(docString.toString()));

    }

    /**
     * Returns a string that consists of up to the 10 most common words within the passed in string
     *
     * @param docString String to count the words from
     * @return String containing up to the 10 most common words
     */
    public static String getMostCommonWords(String docString) {
        StringBuilder output = new StringBuilder();
        Map<String, Integer> wordFrequencies = new HashMap<>();
        String[] docStringWords = docString.split("[ /!?,.:\n\t\r]+");
        int numWordsToPrint = NUMBER_OF_WORDS_TO_PRINT;

        // Puts everything in the array of words into the map. Duplicate words increment their pre-existing associated values.
        for (String word : docStringWords) {
            if (wordFrequencies.containsKey(word)) {
                wordFrequencies.put(word, wordFrequencies.get(word) + 1);
            } else {
                wordFrequencies.put(word, 1);
            }
        }

        // Since we regex by punctuation, there will be a lot of empty strings.
        wordFrequencies.remove("");

        // Sorts the entries by their value (which is word count)
        List<Map.Entry<String, Integer>> sortedWordFrequencies = sortByValue(wordFrequencies);

        if (sortedWordFrequencies.size() < numWordsToPrint) {
            numWordsToPrint = sortedWordFrequencies.size();
        }

        // Iterate over the last numWordsToPrint words (which will be the most frequent ones) and print their key & value
        output.append("Most common words:");

        // Appends each of the strings to the output
        for (int i = sortedWordFrequencies.size() - 1;
             i > sortedWordFrequencies.size() - (numWordsToPrint + 1);
             i--) {
            output.append(sortedWordFrequencies.get(i).getKey());
            output.append("\n");
        }

        return output.toString();
    }

    /**
     * Some of the following code is derived from: https://www.mkyong.com/java/how-to-sort-a-map-in-java/
     *
     * @param unsortMap Map to be sorted
     * @return Sorted map
     */
    public static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Integer>> list =
                new LinkedList<>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        list.sort(new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        return list;
    }

}
