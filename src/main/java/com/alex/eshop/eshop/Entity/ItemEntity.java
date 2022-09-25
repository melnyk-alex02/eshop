package com.alex.eshop.eshop.Entity;

import javax.persistence.*;

@Entity
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @Column(name = "description")
    private String description;
    @Column(name = "image_src")
    private String image_src;
    public long getCategory_id(){
        return category.getId();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_src() {
        return image_src;
    }

    public void setImage_src(String image_src) {
        this.image_src = image_src;
    }


    public ItemEntity() {
    }

    public ItemEntity(String name, CategoryEntity category, String description, String image_src) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.image_src = image_src;
    }
}
