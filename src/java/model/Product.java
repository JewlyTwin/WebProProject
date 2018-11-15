/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author JewlyTwin
 */
@Entity
@Table(name = "PRODUCT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
    @NamedQuery(name = "Product.findByProductid", query = "SELECT p FROM Product p WHERE p.productid = :productid"),
    @NamedQuery(name = "Product.findByProductname", query = "SELECT p FROM Product p WHERE p.productname = :productname"),
    @NamedQuery(name = "Product.findByProducttype", query = "SELECT p FROM Product p WHERE p.producttype = :producttype"),
    @NamedQuery(name = "Product.findByUnitprice", query = "SELECT p FROM Product p WHERE p.unitprice = :unitprice"),
    @NamedQuery(name = "Product.findByWeight", query = "SELECT p FROM Product p WHERE p.weight = :weight"),
    @NamedQuery(name = "Product.findByStorability", query = "SELECT p FROM Product p WHERE p.storability = :storability"),
    @NamedQuery(name = "Product.findByDetail", query = "SELECT p FROM Product p WHERE p.detail = :detail"),
})
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "PRODUCTID")
    private Integer productid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "PRODUCTNAME")
    private String productname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PRODUCTTYPE")
    private String producttype;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UNITPRICE")
    private int unitprice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WEIGHT")
    private int weight;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "STORABILITY")
    private String storability;
    @Size(max = 150)
    @Column(name = "DETAIL")
    private String detail;
    @OneToMany(mappedBy = "productid")
    private List<Orderdetail> orderdetailList;

    public Product() {
    }

    public Product(Integer productid) {
        this.productid = productid;
    }

    public Product(Integer productid, String productname, String producttype, int unitprice, int weight, String storability) {
        this.productid = productid;
        this.productname = productname;
        this.producttype = producttype;
        this.unitprice = unitprice;
        this.weight = weight;
        this.storability = storability;
    }

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype;
    }

    public int getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(int unitprice) {
        this.unitprice = unitprice;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getStorability() {
        return storability;
    }

    public void setStorability(String storability) {
        this.storability = storability;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @XmlTransient
    public List<Orderdetail> getOrderdetailList() {
        return orderdetailList;
    }

    public void setOrderdetailList(List<Orderdetail> orderdetailList) {
        this.orderdetailList = orderdetailList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (productid != null ? productid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
        if ((this.productid == null && other.productid != null) || (this.productid != null && !this.productid.equals(other.productid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Product[ productid=" + productid + " ]";
    }
    
}
