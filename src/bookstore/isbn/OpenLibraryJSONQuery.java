package bookstore.isbn;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class OpenLibraryJSONQuery {

    private static String URL_PREFIX = "https://openlibrary.org/";
    public static ISBNBook queryBook(String isbn)
    {
        String title = "";
        String author = "";

        JsonObject bookObj = OpenLibraryJSONQuery.queryURL(URL_PREFIX + "/isbn/" + isbn +".json");
        if(null != bookObj && !bookObj.isEmpty()) {
            title = cleanString(bookObj.get("title").toString());
            JsonArray authorArray = bookObj.has("authors") ? bookObj.get("authors").getAsJsonArray() : new JsonArray();
            if (!authorArray.isEmpty()) {
                String authorUrl = cleanString(authorArray.get(0).getAsJsonObject().get("key").toString());
                String fullAuthorURL = URL_PREFIX + authorUrl + ".json";
                JsonObject authorObj = OpenLibraryJSONQuery.queryURL(fullAuthorURL);
                if(null != authorObj && !authorObj.isEmpty()) {
                    author = cleanString(authorObj.get("name").toString());
                }
            }
        }

        return new ISBNBook(title, author, isbn);
    }

    private static JsonObject queryURL(String url)
    {
        JsonObject jsonObject = null;
        try {
            URL u = new URL(url);
            URLConnection c = u.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String line;
            String content = "";
            // reading from the urlconnection using the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content += line;
            }
            bufferedReader.close();

            JsonElement jsonElement = JsonParser.parseString(content);
            jsonObject = jsonElement.getAsJsonObject();

        }
        catch (IOException e) {
            System.out.println(e.toString());
        }

        return jsonObject;
    }

    private static String cleanString(String s)
    {
        return s.replace("\"", "");
    }
}
