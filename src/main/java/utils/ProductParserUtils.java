package utils;

import com.google.gson.Gson;
import dto.ProductResponse;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class ProductParserUtils {

    //by mkharkhu
    //file output method
    public static void flashXML(String productsXml) {
        //use pathFactory here for specify file path
    }

    //by Yanetta
    public static String productsJsonToXml(List<ProductResponse> productsResponses) {
        //List<ProductResponse> to productsXml
        return null;
    }

    //by un-install
    public static String pathFactory() {
        if (System.getProperty("os.name").equals("Windows 10")) {
            return "C:\\apbutyou.de-parser\\xml-out";
        } else {
            return "/etc/xml-out";
        }
    }

    //by un-install
    public static ProductResponse productHtmlParser() throws JSONException, IOException {
        //get html document
        Document doc = Jsoup.connect("https://www.aboutyou.de/p/converse/chuck-taylor-as-core-ox-sneaker-3565780").get();

        //setting values to ProductResponse
        String productJson = doc.body().getElementById("app").getElementsByTag("script").get(1).data();
        ProductResponse productResponse = new Gson().fromJson(productJson, ProductResponse.class);
        productResponse.setArticleNumber(doc.body().getElementsByClass("_articleNumber_1474d").get(0).text());
        productResponse.setColor(doc.body().getElementsByAttributeValue("data-test-id", "VariantColor").text());
        productResponse.setPrice(Double.parseDouble(doc.body().getElementsByAttributeValue("data-test-id", "ProductPrices").text().replaceAll("[^\\d,]", "").replace(",", ".")));

        System.out.println(productResponse);

        return productResponse;
    }






}
