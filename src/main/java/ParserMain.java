import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws IOException, JSONException {
        getPrdouctResponses("https://www.aboutyou.de/suche?term=Shuhe&source=suggest&gender=male&is_s=suggest&is_h=srp").forEach(System.out::println);
    }
}

