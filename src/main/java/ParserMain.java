import com.google.gson.Gson;
import dto.ProductContainer;
import dto.ProductResponse;
import org.json.JSONException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static utils.ProductParserUtils.*;

//by Daniluko
public class ParserMain {
    public static void main(String[] args) throws IOException, JSONException {
        List<ProductResponse> products = getPrdouctResponses(serchUrlFactory("jeans"));
        productResponsesToXml(products);

        // System.out.println(serchUrlFactory("jeans"));
        BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/test.json"));
        writer.write(new Gson().toJson(new ProductContainer(products)));
    }
}

