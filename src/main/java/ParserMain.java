import dto.ProductResponse;
import java.util.List;
import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws Exception {
        System.setProperty("webdriver.gecko.driver", args.length < 2 ? "" : args[1]);
        try {
            List<ProductResponse> products = getProductResponses(searchResultUrlFactory(args.length != 0 ? args[0] : "Kleider"));
            productResponsesFlushXml(products);
        }catch (IllegalStateException e){
            System.out.println("webdriver.gecko.driver not seted. Set path to the second parameter");
            return;
        }
    }
}

