import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TrumpBot {
    
    public static void main(String[] args) {
        TweetParser tp = new TweetParser();
        tp.load("realdonaldtrump.csv");
//        tp.load("test.csv");
        FrequencyMap freqMap = new FrequencyMap(3);
        freqMap.load(tp.allTweets());
//        System.out.println(freqMap);
        
        TweetBuilder builder = new TweetBuilder(freqMap);
        System.out.println(builder.getTweet(3));
        
//        List<String> tweets = builder.getTweets(1000);
//        try{
//            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
//            for (String t : tweets) {
//                writer.println(t);
//            }
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
