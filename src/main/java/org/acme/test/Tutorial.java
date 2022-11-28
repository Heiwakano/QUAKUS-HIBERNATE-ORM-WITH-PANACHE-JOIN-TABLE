package org.acme.test;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tutorials")
public class Tutorial extends PanacheEntity {

    public String title;
    public String description;
    public boolean published;
    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
            CascadeType.PERSIST,
                    CascadeType.MERGE,
                    CascadeType.REMOVE
    }
    )
    @JoinTable(name = "tutorials_tags",
            joinColumns = { @JoinColumn(name = "tutorial_id") },
            inverseJoinColumns = { @JoinColumn(name = "tag_id") })
    public Set<Tag> tags = new HashSet<>();

    @JsonbTransient
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Tutorial() {
    }

    @JsonbCreator
    public Tutorial(String title, String description, boolean published) {
        this.title = title;
        this.description = description;
        this.published = published;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.tutorials.add(this);
    }

    public void removeTag(long tagId) {
        Tag tag = tags.stream().filter(t -> t.id == tagId).findFirst().orElse(null);
        if (tag != null) {
            tags.remove(tag);
            tag.tutorials.remove(this);
        }
    }
}
