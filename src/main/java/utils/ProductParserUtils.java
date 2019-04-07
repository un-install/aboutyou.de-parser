package utils;

import com.google.gson.Gson;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import dto.ProductContainer;
import dto.ProductResponse;
import exception.EmptySearchResultException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductParserUtils {
    //Loging by un-install
    private static final Logger LOG = LogManager.getLogger(ProductParserUtils.class);

    //by Yanetta and mkharkhu
    public static void productResponsesFlushXml(List<ProductResponse> productsResponses) {
        try {
            LOG.debug("start productResponsesFlushXml, productResponses size =" + productsResponses.size());
            //setup marshaller
            JAXBContext jaxbContext = JAXBContext.newInstance(ProductContainer.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            //flash xml
            jaxbMarshaller.marshal(new ProductContainer(productsResponses), fileFactory("xml"));
            LOG.debug("end productResponsesFlushXml");
        } catch (JAXBException | IOException e) {
            LOG.debug("productResponseFlushXml failed!!!");
            e.printStackTrace();
        }
    }

    //by un-install
    public static File fileFactory(String type) throws IOException {
        if (System.getProperty("os.name").contains("Windows")) {
            return checkedCreateFile("C:\\aboutyou.de-parser\\output\\out." + type);
        } else {
            return checkedCreateFile("/etc/aboutyou.de-parser/output/out." + type);
        }
    }

    //by un-install
    public static File checkedCreateFile(String filePath) throws IOException {
        LOG.debug("start checkedCreateFile, filePath=" + filePath);
        File outFile = new File(filePath);
        if (!outFile.exists()) {
            File outDir = new File(outFile.getParent());
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            outFile.createNewFile();
        }
        LOG.debug("end checkedCreateFile, outFile path=" + outFile.getPath());
        return outFile;
    }

    //by un-install
    public static ProductResponse productHtmlParser(String productUrl) throws IOException, InterruptedException {
        LOG.debug("start productHtmlParser, productUrl=" + productUrl);
        //get html document
        Document doc = Jsoup.connect(productUrl).get();
        ProductResponse productResponse;

        //collecting jsons
        List<Element> productJson = doc.body().getElementsByAttributeValue("type", "application/ld+json").stream().
                filter(json -> json.data().contains("\"Product\"")).collect(Collectors.toList());

        //check json
        if (!productJson.isEmpty()) {
            //setting values to ProductResponse from json
            productResponse = new Gson().fromJson(productJson.get(0).data(), ProductResponse.class);

            //setting values to ProductResponse from html
            productResponse.setArticleNumber(doc.body().getElementsByClass("_articleNumber_1474d").get(0).text().replace("Artikel-Nr: ", ""));
            productResponse.setColor(doc.body().getElementsByAttributeValue("data-test-id", "VariantColor").text());
            productResponse.setPrice(doc.body().getElementsByAttributeValue("data-test-id", "ProductPrices").text()); //.replaceAll("[^\\d,]", "").replace(",", ".")));
        } else {
            productResponse = new ProductResponse();
        }
        LOG.debug("end productHtmlParser, productResponse=" + productResponse);
        return productResponse;
    }

    //by un-install
    public static List<String> getSearchResultUrls(String searchUrl) throws IOException {
        LOG.debug("start getSearchResultUrls, searchUrl=" + searchUrl);
        //get html document
        Document doc = Jsoup.connect(searchUrl).followRedirects(false).get();

        try {
            if (!doc.getElementsByClass("_subline_54217").isEmpty()) {
                throw new EmptySearchResultException("");
            }

            //map hrefs to products
            List<String> productUrls = doc.body().getElementsByAttributeValue("data-test-id", "ProductTileDefault")
                    .stream().map(div -> "https://www.aboutyou.de" + div.getElementsByTag("a").get(0).attr("href")).collect(Collectors.toList());

            LOG.debug("end getSearchResultUrls, productUrls size=" + productUrls.size());
            return productUrls;

        } catch (EmptySearchResultException e) {
            LOG.debug("Terminating program: no results");
            System.out.println("No Results");
            System.exit(0);
            return null;
        }
    }

    //by un-install
    public static List<ProductResponse> getProductResponses(String searchUrl) throws IOException {
        LOG.debug("start getProductResponses, searchUrl=" + searchUrl);
        //map productUrl to productResponse
        List<ProductResponse> productResponses = getSearchResultUrls(searchUrl).stream().parallel()
                .map(url -> {
                    try {
                        return productHtmlParser(url);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                        return new ProductResponse();
                    }
                }).collect(Collectors.toList());

        LOG.debug("end getProductResponses, productResponses list size=" + productResponses.size());
        return productResponses;
    }

    //by un-install
    public static String searchResultUrlFactory(String keyword) throws IOException, InterruptedException {
        LOG.debug("start searchResultUrlFactory, keyword=" + keyword);
        //load page
        WebDriver driver = new FirefoxDriver();
        driver.get("https://www.aboutyou.de/");

        //submitting keyword to search form
        driver.findElement(By.className("_searchBarWrapper_f8463")).click();
        driver.findElement(By.className("_textInput_f8463")).sendKeys(keyword, Keys.ENTER);

        //getting url after redirection
        while (driver.getCurrentUrl().equals("https://www.aboutyou.de/dein-shop")) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        String searchUrl = driver.getCurrentUrl();
        driver.close();

        LOG.debug("end searchResultUrlFactory, searchUrl=" + searchUrl);
        return searchUrl;
    }

    //needs refactoring
    //by un-install
    public static List<String> getPageList(String searchResultUrl) throws IOException {
        LOG.debug("start getPageList, searchResultUrl={}", searchResultUrl);
        Document doc = Jsoup.connect(searchResultUrl).get();
        List<String> pageUrls = new ArrayList<>();
        pageUrls.add(searchResultUrl);

        List<Element> paginationWrapper = doc.body().getElementsByClass("_paginationWrapper_fab66");
        if (paginationWrapper.isEmpty()) {
            return pageUrls;
        }
        List<Element> paggButtonsHrefs = paginationWrapper.get(0).getElementsByTag("a");
        Element lastPaggButton = paggButtonsHrefs.get(paggButtonsHrefs.size() - 2);
        int pcount = Integer.parseInt(lastPaggButton.text());
        String url = lastPaggButton.attr("href");

        AtomicInteger count = new AtomicInteger(2);
        pageUrls.addAll(Stream.generate(() -> "https://www.aboutyou.de" + url.split("=")[0] + "=" + count.getAndIncrement()).limit(1).collect(Collectors.toList()));
        LOG.debug("end getPageList, pageUrls={}", pageUrls);
        return pageUrls;
    }

    //by un-install
    public static List<ProductResponse> getProductResponsesWithPagination(List<String> pageUrls) {
        LOG.debug("start getProductResponsesWithPagination, pageUrls", pageUrls);
        return pageUrls.stream().map(url -> {
            try {
                return getProductResponses(url);
            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<ProductResponse>();
            }
        }).flatMap(prs -> prs.stream()).collect(Collectors.toList());
    }
}
