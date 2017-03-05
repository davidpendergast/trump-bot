import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    
    public static StringBuilder log = new StringBuilder();

    public static void log(String message, String source) {
        String[] lines = message.split("\n");
        for (String line : lines) {
            String msg = source + ":\t" + line;
            log.append(msg + System.lineSeparator());
        }
    }
    
    public static void log(String message) {
        String[] lines = message.split("\n");
        for (String line : lines) {
            log.append(line + System.lineSeparator());
        }
    }
    
    public static void clear() {
        log.setLength(0);
    }
    
    public static void dump(String filename) {
        try{
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.print(log.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
