package it.polito.justorder_framework.model;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String key;
    private String name;
    private Double cost;
    private String notes;
    private List<String> ingredients;
    private String category;
}
