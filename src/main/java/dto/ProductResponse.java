package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.math.BigDecimal;

//by Yanetta and un-install
//Product response pojo
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ProductResponse")
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

    @Override
    public String toString() {
        return "ProductResponse{" +
                "name='" + name + '\'' +
                ", brand=" + brand +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", articleNumber='" + articleNumber + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
