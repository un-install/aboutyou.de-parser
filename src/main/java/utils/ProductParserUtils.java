package utils;

import com.google.gson.Gson;
import dto.ProductContainer;
import dto.ProductResponse;
import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProductParserUtils {

    //by Yanetta and mkharkhu
    public static void productResponsesToXml(List<ProductResponse> productsResponses) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ProductContainer.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(new ProductContainer(productsResponses), fileFactory("xml"));

        } catch (JAXBException | IOException  e) {
            e.printStackTrace();
        }
    }

    //by un-install
    public static File fileFactory(String type) throws IOException {
        if (System.getProperty("os.name").contains("Windows")) {
            return checkedCreate("C:\\aboutyou.de-parser\\output\\out." + type);
        } else {
            return checkedCreate("/etc/aboutyou.de-parser/output/out." + type);
        }
    }

    public static File checkedCreate(String filePath) throws IOException {
        File outFile = new File(filePath);
        if (!outFile.exists()) {
            File outDir = new File(outFile.getParent());
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            outFile.createNewFile();
        }
        return outFile;
    }

    //by un-install
    public static ProductResponse productHtmlParser(String productUrl) throws JSONException, IOException {
        //get html document
        Document doc = Jsoup.connect(productUrl).get();
        ProductResponse productResponse;

        //check json
        List<Element> jsons = doc.getElementsByAttributeValue("type", "application/ld+json");
        if (jsons.stream().anyMatch(json -> json.data().contains("\"@type\":\"Product\""))) {

            //setting values to ProductResponse from json
            String productJson = jsons.stream().
                    filter(json -> json.data().contains("\"@type\":\"Product\"")).collect(Collectors.toList()).get(0).data();
            productResponse = new Gson().fromJson(productJson, ProductResponse.class);

            //setting values to ProductResponse from html
            productResponse.setArticleNumber(doc.body().getElementsByClass("_articleNumber_1474d").get(0).text().replace("Artikel-Nr: ", ""));
            productResponse.setColor(doc.body().getElementsByAttributeValue("data-test-id", "VariantColor").text());
            productResponse.setPrice(doc.body().getElementsByAttributeValue("data-test-id", "ProductPrices").text()); //.replaceAll("[^\\d,]", "").replace(",", ".")));
        } else {
            productResponse = new ProductResponse();
        }

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
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return new ProductResponse();
            }
        }).collect(Collectors.toList());
    }

    //by un-install
    public static String serchUrlFactory(String keyword) throws IOException {
        Document doc = Jsoup.connect("https://www.aboutyou.de").followRedirects(true).data("_textInput_f8463 _textInputActive_f8463", keyword).post();

        FormElement serchForm = doc.getElementsByClass("_searchBarForm_f8463").forms().get(0);
        serchForm.val(keyword);
        Connection conn = serchForm.submit();
        conn.followRedirects(true);

        return "https://www.aboutyou.de/maenner/bekleidung/jeans?is_s=suggest&is_h=gz&term=jeans&category=20202";
    }

}
