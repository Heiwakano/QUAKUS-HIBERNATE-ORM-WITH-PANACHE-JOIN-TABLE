package org.acme.test;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
public class Person extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "personSequence",
            sequenceName = "person_id_seq",
            allocationSize = 1,
            initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personSequence")
    public Integer personId;
    // @Column("BBL_Name")
    public String name;
    public LocalDate birth;
    public Status status;
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL,mappedBy = "person")
//    @Fetch(FetchMode.JOIN)
    public Set<Product> products;

    @JsonbTransient
    public void setProducts(Set<Product> products) {
        this.products = products;
    }
    @JsonbTransient
    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Person() {
    }

    @JsonbCreator
    public Person(String name, LocalDate birth, Status status) {
        this.name = name;
        this.birth = birth;
        this.status = status;
    }

    public static Set<PanacheEntityBase> findAllPerson() {
        return Person.list("select distinct p from Person p join fetch p.products").stream().collect(Collectors.toSet());
    }

    public static Person findByName(String name){
        return find("name", name).firstResult();
    }

    public static List<Person> findAlive(){
        return list("status", Status.Alive);
    }

    public static void deleteStefs(){
        delete("name", "Stef");
    }

}