import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class TrumpBot {
    
    public static final String REAL_DT_TWEETS = "realdonaldtrump.csv";
    public static final String TEST_FILE = "test.csv";
    public static final String LOG_FILE = "output.log";
    
    /**
     * File to retrieve source tweets. 
     */
    public static String tweetSource = REAL_DT_TWEETS; 
    
    /**
     * Max depth of Markov chains used.
     */
    public static int depth = 3;
    
    /**
     * Number of tweets to generate.
     */
    public static int numToGenerate = 10;
    
    public static void main(String[] args) {
        TweetParser tp = new TweetParser();
        tp.load(tweetSource);

        FrequencyMap freqMap = new FrequencyMap(depth);
        freqMap.load(tp.allTweets());
        
        TweetBuilder builder = new TweetBuilder(freqMap);
        
        List<String> tweets = builder.getTweets(numToGenerate);
        checkTooLong(tweets);
        
        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            for (String t : tweets) {
                writer.println(t);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Logger.dump(LOG_FILE);
    }
    
    public static void checkTooLong(List<String> tweets) {
        List<String> tooLong = tweets.stream().filter(s -> s.length() > 140).collect(Collectors.toList());
        for (String longGuy : tooLong) {
            System.out.println(longGuy.length()+":\t"+longGuy);
        }
        if (tooLong.size() > 0) {
            System.out.println("Too long: "+tooLong.size());
        }
    }
    
}
