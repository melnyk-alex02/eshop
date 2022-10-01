package com.alex.eshop.eshop.Entity;

import javax.persistence.*;

@Entity
public class Item {
    public Item() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private Category category;
    @Column(name = "description")
    private String description;
    private String imageSrc;
    public Long getCategory_id(){
        return category.getId();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_src() {
        return imageSrc;
    }

    public void setImage_src(String image_src) {
        this.imageSrc = image_src;
    }

}
