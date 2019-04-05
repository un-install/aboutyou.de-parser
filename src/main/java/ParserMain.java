import dto.ProductResponse;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws IOException, JSONException {
        productResponsesToXml(getPrdouctResponses(getSerchResultUrls(args[0])));
    }
}

