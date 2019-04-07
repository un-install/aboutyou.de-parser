import dto.ProductResponse;

import java.util.List;

import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws Exception {
        System.out.println(getPageList("https://www.aboutyou.de/maenner/bekleidung/jeans?is_s=typein&is_h=gz&term=jeans&category=20202"));

        System.setProperty("webdriver.gecko.driver", args.length < 2 ? "C:\\aboutyou.de-parser\\geckodriver.exe" : args[1]);
        try {
            List<ProductResponse> products = getProductResponsesWithPagination(getPageList(searchResultUrlFactory(args.length != 0 ? args[0] : "jeans")));
            productResponsesFlushXml(products);
        } catch (IllegalStateException e) {
            System.out.println("webdriver.gecko.driver not seted. Set path to the second parameter");
            return;
        }
    }
}