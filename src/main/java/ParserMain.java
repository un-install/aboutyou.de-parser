import dto.ProductResponse;

import java.util.List;

import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws Exception {
        long currentTime = System.currentTimeMillis();
        System.setProperty("webdriver.gecko.driver", args.length < 2 ? "C:\\aboutyou.de-parser\\geckodriver.exe" : args[1]);
        try {
            List<ProductResponse> products = getProductResponsesWithPagination(getPageList(searchResultUrlFactory(args.length != 0 ? args[0] : "jeans")));
            productResponsesFlushXml(products);
            System.out.println(System.currentTimeMillis() - currentTime);
        } catch (IllegalStateException e) {
            System.out.println("webdriver.gecko.driver not seted. Set path to the second parameter\n" +
                    "Dawnload: https://github.com/mozilla/geckodriver/releases");
        }
    }
}