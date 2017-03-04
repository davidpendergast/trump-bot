import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class TweetBuilder {
    
    public static final int CHAR_MAX_LIMIT = 140;
    public static final int CHAR_SOFT_MAX_LIMIT = CHAR_MAX_LIMIT - 20;
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
    
    private static int min(int ... values) {
        int min = values[0];
        for (int i = 1; i < values.length; i++) {
            if (min > values[i]) {
                min = values[i];
            }
        }
        return min;
    }
    
    public String combineWords(List<String> words) {
        if (words.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (i == 0) {
                if (word.startsWith("@")) {
                    sb.append("." + word); //magic twitter dot
                } else {
                    sb.append(word);
                } 
            } else {
                String prev = words.get(i-1);
                if (WordLists.NO_SURROUNDING_SPACES_PUNCTUATION.contains(prev) 
                        || WordLists.ALL_PUNCTUATION.contains(word)) {
                    sb.append(word);
                } else {
                    sb.append(" " + word);
                }
            }
        }
        return sb.toString();
    }
    
    public String getTweet() {
        List<String> tweet = new ArrayList<String>();
        List<String> memory = new ArrayList<String>();
        memory.add(FrequencyMap.START);
        
        // starts trying to finish sentence after this point
        int softCharLimit = rand.nextInt(CHAR_SOFT_MAX_LIMIT - CHAR_LOWER_LIMIT) 
                + CHAR_LOWER_LIMIT;
        
        while (true) {
            int maxDepth = min(freqMap.getDepth(), memory.size());
            
            int depth = rand.nextInt(maxDepth)+1;
            List<String> memSublist = memory.subList(memory.size()-depth, memory.size());
            Set<String> followerSet = freqMap.getFollowers(memSublist).keySet();
            
            int lengthNow = combineWords(tweet).length();
            
            List<String> followers = new ArrayList<String>(followerSet);
            if (followers.isEmpty()) {
                memSublist = memory.subList(memory.size()-1, memory.size());
                followers.addAll(freqMap.getFollowers(memSublist).keySet());
                if (followers.isEmpty()) {
                    break;
                }
            } 
            
            if (lengthNow > softCharLimit) {
                String end = getTerminalPunctuation(followers);
                if (end != null) {
                    tweet.add(end);
                    break;
                } else {
                    softCharLimit += 10;
                }
            }
            
            Collections.shuffle(followers, rand);
            String choice = followers.get(0);
            
            if (lengthNow + choice.length() + 1 < CHAR_MAX_LIMIT) {
                tweet.add(choice);
            } else {
                break;
            }
            memory.add(choice);
            if (memory.size() > depth) {
                memory.remove(0);
            }  
        }
        
        return combineWords(tweet);
    }
    
    private String getTerminalPunctuation(List<String> followers) {
        Collections.shuffle(followers, rand);
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
            result.add(tweet);
        }
        Logger.log("\ndone.", getClass().getName());
        return result;
    }

}
