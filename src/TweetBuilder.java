import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TweetBuilder {
    
    public static final int CHAR_MAX_LIMIT = 140;
    public static final int CHAR_LOWER_LIMIT = 50;
    private FrequencyMap freqMap;
    private Random rand;
    
    public TweetBuilder(FrequencyMap freqMap, long seed) {
        this.rand = new Random(seed);
        this.freqMap = freqMap;
    }
    
    public TweetBuilder(FrequencyMap freqMap) {
        this(freqMap, System.currentTimeMillis());
    }
    
    public String getTweet() {
        StringBuilder res = new StringBuilder();
        String currentWord = FrequencyMap.START;
        int CHAR_LIMIT = rand.nextInt(CHAR_MAX_LIMIT - CHAR_LOWER_LIMIT) 
                + CHAR_LOWER_LIMIT;
        
        while (res.length() < CHAR_LIMIT) {
            List<String> followers = freqMap.getFollowers(currentWord);
            if (followers.isEmpty()) {
                break;
            } else if (res.length() > CHAR_LIMIT - 15) {
                String end = getTerminalPunctuation(followers);
                if (end != null) {
                    res.append(end);
                    break;
                }
            }
            
            Collections.shuffle(followers, rand);
            String choice = followers.get(0);
            
            String addition = "";
            if (res.length() == 0 && choice.length() > 0 
                    && choice.charAt(0) == '@') {
                res.append(".");    // add magic twitter dot
            } else if (res.length() > 0 && !WordLists.ALL_PUNCTUATION.contains(choice)) {
                addition = " ";
            }
            
            addition = addition + choice;
            if (res.length() + addition.length() <= CHAR_LIMIT) {
                res.append(addition);
            } else {
                return res.toString();
            }
            currentWord = choice;
            
        }
        return res.toString();
    }
    
    private String getTerminalPunctuation(List<String> followers) {
        for (String x : followers) {
            if (WordLists.TERMINAL_PUNCTUATION.contains(x)) {
                return x;
            }
        }
        return null;
    }
    
    public List<String> getTweets(int n) {
        List<String> result = new ArrayList<String>();
        Logger.log("Creating "+n+" tweets...\n ", getClass().getName());
        for (int i = 0; i < n; i++) {
            String tweet = getTweet();
            if (i < 10 || n < 20) {
                Logger.log(tweet, getClass().getName());
            } else if (i == 10) {
                Logger.log("<creating "+(n-10)+" more...>", getClass().getName());
            }
            result.add(getTweet());
        }
        Logger.log("\ndone.", getClass().getName());
        return result;
    }

}
