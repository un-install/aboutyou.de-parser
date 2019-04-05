import org.json.JSONException;

import java.io.IOException;

import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws IOException, JSONException {
        productResponsesToXml(getPrdouctResponses(serchUrlFactory("jeans")));
    }
}

