package bookstore.util;

public class BookUtil {
    public static String getLanguage(int language) {
        switch (language){
            case 1: return "german";
            case 2: return "english";
            default: return "unknown";
        }
    }

    public static String getType(int type) {
        switch (type){
            case 1: return "intimate";
            case 2: return "great ones";
            case 3: return "better ones";
            case 4: return "funny or magic";
            default: return "unknown";
        }
    }
}
