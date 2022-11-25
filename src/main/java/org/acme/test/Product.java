package org.acme.test;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Product extends PanacheEntityBase {
    @Id
    @SequenceGenerator(
            name = "productSequence",
            sequenceName = "product_id_seq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productSequence")
    public Integer productId;
    public String name;
    public String detail;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="personid", nullable=false)
    @JsonbTransient
    public Person person;

    @JsonbTransient
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Product() {
    }

    @JsonbCreator
    public Product(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public static Set<PanacheEntityBase> findByPersonId(Integer personId) {
       return Product.list("personid", personId).stream().collect(Collectors.toSet());
    }
}
