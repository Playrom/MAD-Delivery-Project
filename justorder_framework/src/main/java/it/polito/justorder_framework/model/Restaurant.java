package it.polito.justorder_framework.model;

import android.net.Uri;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private String key;
    private String name;
    private Uri imageUri;
    private String telephone;
    private String address;
    private String fiscalCode;
    private String vatCode;
    private String iban;
    private String openingHour;
    private String closingHour;
    private List<String> openingDays;
    private List<String> type;

    private String owner;
    private HashMap<String, Boolean> managers;
}
