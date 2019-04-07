package dto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

//by Yanetta and un-install
//Product response pojo
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductResponse implements Serializable {
    @XmlElement(name = "name")
    private String name;

    //@XmlElementWrapper(name = "brand")
    @XmlElement(name = "brand")
    private Brand brand;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "price")
    private String price;

    @XmlElement(name = "articleNumber")
    private String articleNumber;

    @XmlElement(name = "color")
    private String color;

    @XmlElement(name = "url")
    private String url;

    @XmlElementWrapper(name = "images")
    @XmlElement(name = "image")
    private List<String> image;

    public ProductResponse() {
    }

    public ProductResponse(String name, Brand brand, String description, String price, String articleNumber, String color) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.articleNumber = articleNumber;
        this.color = color;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ProductResponse{");
        sb.append("name='").append(name).append('\'');
        sb.append(", brand=").append(brand);
        sb.append(", description='").append(description).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append(", articleNumber='").append(articleNumber).append('\'');
        sb.append(", color='").append(color).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
