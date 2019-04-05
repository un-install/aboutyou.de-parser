package utils;

import com.google.gson.Gson;
import dto.ProductResponse;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ProductParserUtils {

    //by mkharkhu
    //file output method
    public static void flashXML(String productsXml){
        //use pathFactory here for specify file path
    }

    //by Yanetta
    public static void productResponsesToXml(List<ProductResponse> productsResponses) {
        //List<ProductResponse> to productsXml
        try {
            File file = new File("src/main/resources/test.xml");
            //check class of instance
            JAXBContext jaxbContext = JAXBContext.newInstance(ProductResponse.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            productsResponses.forEach(pr -> {
                try {
                    jaxbMarshaller.marshal( pr, file);
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            });

        } catch (JAXBException e) {
            e.printStackTrace();
        }
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
    public static ProductResponse productHtmlParser(String productUrl) throws JSONException, IOException {
        //get html document
        Document doc = Jsoup.connect(productUrl).get();
        System.out.println(productUrl);
        //setting values to ProductResponse
        List<Element> jsons = doc.getElementsByAttributeValue("type", "application/ld+json");
        if (!jsons.stream().anyMatch(json -> json.data().contains("\"@type\":\"Product\""))) {
            return new ProductResponse();
        }
        String productJson = jsons.stream().
                filter(json -> json.data().contains("\"@type\":\"Product\"")).collect(Collectors.toList()).get(0).data();
        ProductResponse productResponse = new Gson().fromJson(productJson, ProductResponse.class);
        System.out.println(productJson);
        productResponse.setArticleNumber(doc.body().getElementsByClass("_articleNumber_1474d").get(0).text().replace("Artikel-Nr: ", ""));
        productResponse.setColor(doc.body().getElementsByAttributeValue("data-test-id", "VariantColor").text());
        productResponse.setPrice(doc.body().getElementsByAttributeValue("data-test-id", "ProductPrices").text()); //.replaceAll("[^\\d,]", "").replace(",", ".")));

        System.out.println(productResponse);
        return productResponse;
    }

    //by un-install
    public static List<String> getSerchResultUrls(String serchUrl) throws IOException {
        //get html document
        Document doc = Jsoup.connect(serchUrl).get();

        //get hrefs to products
        return doc.body().getElementsByAttributeValue("data-test-id", "ProductTileDefault")
                .stream().map(div -> "https://www.aboutyou.de" + div.getElementsByTag("a").get(0).attr("href")).collect(Collectors.toList());
    }

    //by un-install
    public static List<ProductResponse> getPrdouctResponses(String serchUrl) throws IOException, JSONException {
        return getSerchResultUrls(serchUrl).stream().map(url -> {
            try {
                return productHtmlParser(url);
            } catch (JSONException e) {
                e.printStackTrace();
                return new ProductResponse();
            } catch (IOException e) {
                e.printStackTrace();
                return new ProductResponse();
            }
        }).collect(Collectors.toList());
    }

    //by un-install
    public static String serchUrlFactory(String keyword) {
        return "https://www.aboutyou.de/maenner/bekleidung/" + keyword;
    }
}
