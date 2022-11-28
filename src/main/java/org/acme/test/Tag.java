package org.acme.test;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag extends PanacheEntity {
    public String name;
    @ManyToMany(fetch = FetchType.LAZY,
    cascade = {
            CascadeType.PERSIST, // use for insert data to parent and child
            CascadeType.MERGE, // use for update data to parent and child
            CascadeType.REMOVE
    })
    @JsonbTransient
    public Set<Tutorial> tutorials = new HashSet<>();


    public void  removeTutorial(long tutorialId) {
        Tutorial tutorial = tutorials.stream().filter(t -> t.id == tutorialId).findAny().orElse(null);
        if (tutorial != null) {
            tutorials.remove(tutorial);
            tutorial.tags.remove(this);
        }
    }
}
