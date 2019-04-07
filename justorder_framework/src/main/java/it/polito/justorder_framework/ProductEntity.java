package it.polito.justorder_framework;

public class ProductEntity {
    private String name;
    private String imageFileName;
    private String cost;
    private String notes;
    private String ingredients;
    private String category;

    public ProductEntity(String name, String imageFileName, String cost, String notes, String ingredients, String category) {
        this.name = name;
        this.imageFileName = imageFileName;
        this.cost = cost;
        this.notes = notes;
        this.ingredients = ingredients;
        this.category = category;
    }

    public ProductEntity(String name, String cost, String category) {
        this.name = name;
        this.cost = cost;
        this.category = category;
    }

    public ProductEntity() {
        this("", null, "", "", "", "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
