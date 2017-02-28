import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TweetParser {
    
    public List<String> allTweets;
    public boolean allowAndroid = true;
    public boolean allowWebClient = false;
    public boolean allowIphone = false;
    public boolean allowOther = false;
    public boolean allowRetweets = false;
    
    public static final int RETWEET_COUNT_IDX = 0;
    public static final int SOURCE_IDX = 1;
    public static final int TWEET_TEXT_IDX = 2;
    
    public static final int IS_RETWEET_IDX = 4;
    public static final int DATE_IDX = 5;
    
    public boolean includeRetweets = false;
    
    private static final String QUOTE = "~!!~";
    private static final String COMMA = "~!~";
    
    public TweetParser() {
        allTweets = new ArrayList<String>();
    }
    
    public int size() {
        return allTweets.size();
    }
    
    public String getTweet(int i) {
        return allTweets.get(i);
    }
    
    public List<String> allTweets() {
        return allTweets;
    }
    
    public void load(String filename) {
        
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            List<String> fileContents = lines.collect(Collectors.toList());
            Logger.log("Parsing "+fileContents.size()+" tweets...",
                    getClass().getName());
            
            for (String line : fileContents) {
                String[] lineData = splitByComma(line);
                if (isValid(lineData)) {
                    allTweets.add(lineData[TWEET_TEXT_IDX]);
                }
            }
            Logger.log("Found "+allTweets.size()+" valid tweets", 
                    getClass().getName());
            
        } catch (IOException e) {
            System.err.println("Failed to load "+filename);
            e.printStackTrace();
            return;
        }
        
        int i = 0;
        for (String tweet : allTweets) {
            //System.out.println(i+":\t"+tweet);
            i++;
        }
        
       
    }
    
    private String[] splitByComma(String csvEntry) {
        if (csvEntry.contains("\"")) {
            //System.out.println(csvEntry);
            int start = csvEntry.indexOf("\"");
            int end = csvEntry.lastIndexOf('\"');
            String tweet;
            if (end == start) {
                //some tweets are pretty boo
                return null;
            } else {
                tweet = csvEntry.substring(start+1, end); //slices off
            }
            tweet = tweet.replaceAll("\"\"", QUOTE);
            tweet = tweet.replaceAll(",", COMMA);
            csvEntry = csvEntry.substring(0, start) + tweet + csvEntry.substring(end+1);
        }
        
        String[] result = csvEntry.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].replaceAll(COMMA, ",");
            result[i] = result[i].replaceAll(QUOTE, "\"");
        }
        
        return result;
    }
    
    private boolean isValid(String[] lineData) {
        if (lineData == null || lineData.length != 8) {
            return false;
        } 
        if (!allowRetweets && lineData[IS_RETWEET_IDX].equals("True")) {
            return false;
        }
        
        String source = lineData[SOURCE_IDX];
        if ("Twitter for iPhone".equals(source) && !allowIphone) {
            return false;
        } else if ("Twitter Web Client".equals(source) && !allowWebClient) {
            return false;
        } else if ("Twitter for Android".equals(source) && !allowAndroid) {
            return false;
        }
        
        if (!lineData[TWEET_TEXT_IDX].contains(" ")) {
            // sometimes tweets will just be a single @ mention of someone
            return false;
        }
        
        return true;
    }
}
