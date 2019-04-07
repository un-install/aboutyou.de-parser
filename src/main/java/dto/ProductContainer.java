package dto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;


@XmlRootElement(name = "ProductContainer")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductContainer implements Serializable {
    @XmlElementWrapper(name = "products")
    @XmlElement(name = "product")
    private List<ProductResponse> products;

    public ProductContainer() {
    }

    public ProductContainer(List<ProductResponse> products) {
        this.products = products;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProductContainer{");
        sb.append("products=").append(products);
        sb.append('}');
        return sb.toString();
    }
}
