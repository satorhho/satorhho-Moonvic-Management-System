package finalluz.java;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class Stock{
    private final SimpleStringProperty Item_Code;
    private final SimpleStringProperty Description;
    private final SimpleStringProperty Expiry_Date;
    private int Quantity;
    private double Supplier_Price;
    private final SimpleStringProperty Compartment_ID;
    
    
    public Stock(String item_code, String description, String expiry_date, double supplier_price, int quantity, String compartment_id){
        this.Item_Code = new SimpleStringProperty(item_code);
        this.Description = new SimpleStringProperty(description);
        this.Expiry_Date = new SimpleStringProperty(expiry_date);
        this.Quantity = quantity;
        this.Supplier_Price = supplier_price;
        this.Compartment_ID = new SimpleStringProperty(compartment_id);
    }

    public String getItem_Code() {
        return Item_Code.get();
    }

    public void setItem_Code(String item_code) {
        Item_Code.set(item_code);
    }

    public String getDescription() {
        return Description.get();
    }

    public void setDescription(String description) {
        Description.set(description);
    }

    public String getExpiry_Date(){
        return Expiry_Date.get();
    }
    
    public void setExpiry_Date(String expiry_date){
        Expiry_Date.set(expiry_date);
    }
    
    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getSupplier_Price() {
        return Supplier_Price;
    }

    public void setSupplier_Price(double supplier_price) {
        Supplier_Price = supplier_price;
    }

    public String getCompartment_ID() {
        return Compartment_ID.get();
    }

    public void setCompartment_ID(String compartment_id) {
        Compartment_ID.set(compartment_id);
    }
    
    
}