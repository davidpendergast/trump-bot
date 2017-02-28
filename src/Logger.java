
public class Logger {
    
    public static void log(String message, String source) {
        String[] lines = message.split("\n");
        for (String line : lines) {
            System.out.println(source+":\t"+line);   
        }
    }

}
