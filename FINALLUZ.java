/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import finalluz.java.Stock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
//NEW CODE
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream; 
import java.sql.PreparedStatement;
import static javafx.application.Application.launch;
//NEW CODE

public class FINALLUZ extends Application {
    private Text ItemC = new Text("Item Code:");
    private Text Desc = new Text("Description:");
    private Text Expr = new Text("Expiry Date:");
    private Text Suppp = new Text("Supplier Price:");
    private Text Qty = new Text("Quantity:");
    private Text CompID = new Text("Compartment ID:");

    private Button Save = new Button("Save");
    private Button Delete = new Button("Delete");
    private Button New = new Button("New");
    private Button Clear = new Button("Clear");
    private Button Edit = new Button("Edit");
    private Button Search = new Button("Search");
    private Button Refresh = new Button("Refresh");

    private TextField ItemCode = new TextField();
    private TextField Description = new TextField();
    private TextField ExpiryDate = new TextField();
    private TextField SupplierPrice = new TextField();
    private TextField Quantity = new TextField();
    private TextField CompartmentID = new TextField();
    @Override
    public void start(Stage primaryStage)throws Exception {
        Label connstatus = new Label();
        
        //Table
        TableView<Stock> table = new TableView<>();
        final ObservableList <Stock> data =  FXCollections.observableArrayList();
        //Item_Code
        TableColumn column1 = new TableColumn("Item Code");
        column1.setMinWidth(200);
        column1.setCellValueFactory(new PropertyValueFactory<>("Item_Code"));
        
        //Description
        TableColumn column2 = new TableColumn("Description");
        column2.setMinWidth(300);
        column2.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        //Expiry_Date
        TableColumn column3 = new TableColumn("Expiry_Date");
        column3.setMinWidth(150);
        column3.setCellValueFactory(new PropertyValueFactory<>("Expiry_Date"));
        
        //Quantity
        TableColumn column4 = new TableColumn("Quantity");
        column4.setMinWidth(100);
        column4.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        //Supplier_Price
        TableColumn column5 = new TableColumn("Supplier Price");
        column5.setMinWidth(150);
        column5.setCellValueFactory(new PropertyValueFactory<>("Supplier_Price"));
        
        //Compartment_ID
        TableColumn column6 = new TableColumn("Compartment ID");
        column6.setMinWidth(200);
        column6.setCellValueFactory(new PropertyValueFactory<>("Compartment_ID")); 
        
        
        HBox container = new HBox();
        container.getChildren().addAll(New,Search,Clear);
        container.setSpacing(5.0);
        
        HBox container2 = new HBox();
        container2.setSpacing(5.0);
        container2.getChildren().addAll(Save, Delete, Edit);
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(20);
        grid.setHgap(20);
        
        //add all
        grid.add(connstatus, 1, 0);
        grid.add(ItemC, 0, 1);
        grid.add(Desc, 0, 2);
        grid.add(Expr, 0, 3);
        grid.add(Suppp, 0, 4);
        grid.add(Qty, 0, 5);
        grid.add(CompID, 0, 6);
        grid.add(ItemCode, 1, 1);
        grid.add(Description, 1, 2);
        grid.add(ExpiryDate, 1, 3);
        grid.add(SupplierPrice, 1, 4);
        grid.add(Quantity, 1, 5);
        grid.add(CompartmentID, 1, 6);
        grid.add(container, 1, 7);
        grid.add(container2, 1, 8);

        //Initial Status
        refresh(connstatus);
        
        //New
        New.setOnAction(e -> {
            if (New.getText() == "New"){
                Clear.setDisable(false);
                Save.setDisable(false);
                Search.setDisable(true);
                Delete.setDisable(true);

                New.setText("Cancel");

                ItemCode.setEditable(true);
                Description.setEditable(true);
                ExpiryDate.setEditable(true);
                SupplierPrice.setEditable(true);
                Quantity.setEditable(true);
                CompartmentID.setEditable(true); 
                clear();
            }
            else if (New.getText() == "Cancel"){
                refresh(connstatus);
                New.setText("New");
                connstatus.setText("CONNECTED!");
            }
	});
        
        Edit.setOnAction(e -> {
            if(Edit.getText() == "Edit"){
                Edit();
                New.setText("Cancel");
                New.setDisable(false);
            }
            else if (Edit.getText() == "Update"){
                try{
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
                    update(conn, connstatus);
                    refreshtable(table);
                }
                catch(Exception x){
                }
            }
        });
        
        //Save
        Save.setOnAction(e -> {
             try{
                Class.forName("oracle.jdbc.driver.OracleDriver");

                Connection conn = null;
                conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
                save(conn, connstatus);
                Save.setDisable(true);
                
                ItemCode.setEditable(false);
                Description.setEditable(false);
                ExpiryDate.setEditable(false);
                SupplierPrice.setEditable(false);
                Quantity.setEditable(false);
                CompartmentID.setEditable(false);
                refreshtable(table);
            }
            catch(Exception x){
                x.printStackTrace();
                Save.setDisable(true);
                ItemCode.setEditable(false);
                Description.setEditable(false);
                ExpiryDate.setEditable(false);
                SupplierPrice.setEditable(false);
                Quantity.setEditable(false);
                CompartmentID.setEditable(false);
            }
             
        });
        
        //Clear
        Clear.setOnAction(e ->{
            clear();
        });
        
        //Delete
        Delete.setOnAction(e -> {
            try{
                    Class.forName("oracle.jdbc.driver.OracleDriver");

                    Connection conn = null;
                    conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
                    delete(conn, connstatus);
                    refreshtable(table);
                }
            catch(Exception x){
            }
        });
        
        //Search
        Search.setOnAction(e -> {
             try{
                Class.forName("oracle.jdbc.driver.OracleDriver");

                Connection conn = null;
                conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
                search(conn);
            }
            catch(Exception x){
                
            }
        });
        
        Refresh.setOnAction(e -> {
            refreshtable(table);
        });

        table.setOnMouseClicked(e->{
            try{
                Class.forName("oracle.jdbc.driver.OracleDriver");
                Connection conn = null;
                conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
                Stock stock = (Stock)table.getSelectionModel().getSelectedItem();
                String query = "select * from PRODUCTS where Item_Code =?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, stock.getItem_Code());
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    
                    ItemCode.setText(rs.getString("ITEM_CODE"));
                    Description.setText(rs.getString("DESCRIPTION"));
                    String expirydate = rs.getString("EXPIRY_DATE");
                    if (expirydate != null){
                        expirydate = expirydate.substring(0, 10);
                    }
                    ExpiryDate.setText(expirydate);
                    SupplierPrice.setText(rs.getString("SUPPLIER_PRICE"));
                    Quantity.setText(rs.getString("QUANTITY"));
                    CompartmentID.setText(rs.getString("COMPARTMENT_ID"));
                }
                New.setText("Cancel");
                Description.setEditable(false);
                Search.setDisable(true);
                Edit.setDisable(false);
                Clear.setDisable(true);
                Delete.setDisable(false);
            }catch(Exception x){
                
            }
        });
        
        refreshtable(table);
        table.getColumns().addAll(column1, column2, column3, column5, column4, column6);  
        GridPane tablegrid = new GridPane();
        tablegrid.add(table, 0, 0);
        tablegrid.add(Refresh,0, 1);
        tablegrid.setVgap(20);
        tablegrid.setHgap(20);
        
        
        Insets insets = new Insets(12);
        Insets insets2 = new Insets(30);
        BorderPane.setMargin(grid, insets);
        BorderPane.setMargin(tablegrid, insets2);
        
        
        

        
        
        BorderPane allpane = new BorderPane();
        allpane.setCenter(grid);
        allpane.setRight(tablegrid);
        grid.setAlignment(Pos.CENTER_LEFT);
        tablegrid.setAlignment(Pos.CENTER_RIGHT);
        
         // IMAGE Moondik //NEW CODE
        Image Moonvik = new Image(new FileInputStream("C:\\Users\\John Luz\\Documents\\Codes\\Java\\JavaFXCodes\\FINALLUZ\\src\\finalluz\\Moonvik.png"));  
        Image Push = new Image(new FileInputStream("C:\\Users\\John Luz\\Documents\\Codes\\Java\\JavaFXCodes\\FINALLUZ\\src\\finalluz\\push.gif"));  

        ImageView imageView = new ImageView(Moonvik); 
        imageView.setX(50); 
        imageView.setY(20); 
        imageView.setFitHeight(140); 
        imageView.setFitWidth(140);
        imageView.setPreserveRatio(true);
        allpane.getChildren().addAll(imageView);
        // IMAGE Moondik
        ImageView imageView2 = new ImageView(Push); 
        imageView2.setX(50); 
        imageView2.setY(550); 
        imageView2.setFitHeight(140); 
        imageView2.setFitWidth(140);
        imageView2.setPreserveRatio(true);
        allpane.getChildren().addAll(imageView2);
         //NEW CODE//NEW CODE 
        
        
        Scene scene = new Scene(allpane, 1600, 900);
        connectToDB(connstatus);
        
        primaryStage.setTitle("Moonvic Inventory Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void connectToDB(Label connstatus){  // CODE NI MAAM JELLYLYLYLY
        
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
            if(conn == null){
                connstatus.setText("NOT CONNECTED!");
                 connstatus.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
                 //connstatus.setTextFill(Color.GREEN);
            }
            else{
                connstatus.setText("CONNECTED!");
                connstatus.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
                //connstatus.setTextFill(Color.RED);
            }
        }
        catch(SQLException e){
            connstatus.setText("NOT CONNECTED!");
            connstatus.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void refresh(Label connstatus){
        Save.setDisable(true);
        Clear.setDisable(false);
        Edit.setDisable(true);
        Delete.setDisable(true);
        Search.setDisable(false);
        Edit.setText("Edit");
        New.setText("New");
        Description.setPromptText("ENTER DESCRIPTION");
        ItemCode.setEditable(false);
        Description.setEditable(true);
        ExpiryDate.setEditable(false);
        SupplierPrice.setEditable(false);
        Quantity.setEditable(false);
        CompartmentID.setEditable(false);
        clear();
    }
    
    public void refreshtable(TableView table){
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = null;
            conn = DriverManager.getConnection("jdbc:oracle:thin:@DESKTOP-SS6V5N6:1521:xe", "moonvick", "moonvick");
            String query = "select * from PRODUCTS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            table.getItems().clear();

            while(rs.next()){
                String itemcode = rs.getString("ITEM_CODE");
                String description = rs.getString("DESCRIPTION");
                String expiry_date = rs.getString("EXPIRY_DATE");
                float Price = rs.getFloat("SUPPLIER_PRICE");
                Double storeprice = BigDecimal.valueOf(Price).setScale(3, RoundingMode.HALF_UP).doubleValue();
                int Qty = rs.getInt("QUANTITY");
                String compartment = rs.getString("COMPARTMENT_ID");
                if (expiry_date != null){
                    expiry_date = expiry_date.substring(0, 10);
                }
                table.getItems().add(new Stock(itemcode, description, expiry_date, storeprice, Qty, compartment));
            }
            }catch(Exception e2){
                System.err.println(e2);
            }
    }
    
    public void save(Connection conn, Label connstatus){
        try{
            
            String itemc = ItemCode.getText(); 
            String desc = Description.getText(); 
            String expr = ExpiryDate.getText(); 
            String supplierprice = SupplierPrice.getText(); 
            String quantity = Quantity.getText();
            String compart = CompartmentID.getText();
            String query;
            if (itemc.isEmpty() || desc.isEmpty() || supplierprice.isEmpty() || quantity.isEmpty() || compart.isEmpty()){
                refresh(connstatus);
                connstatus.setText("PLEASE COMPLETE ALL FIELDS.");
            }
            else if (expr.isEmpty()){
                query = "INSERT INTO PRODUCTS VALUES('" + itemc + "', '" + desc + "', NULL, " + Double.parseDouble(supplierprice) + ", " + Integer.parseInt(quantity) + ", '" + compart + "')";
                Statement stmt = conn.createStatement(); //create connection statement
                int rs = stmt.executeUpdate(query); // execute statement which is the insertion command
                if(rs > 0){//if insertion successful
                    Save.setDisable(true);
                    ItemCode.setEditable(false);
                    Description.setEditable(false);
                    ExpiryDate.setEditable(false);
                    SupplierPrice.setEditable(false);
                    Quantity.setEditable(false);
                    CompartmentID.setEditable(false);
                    refresh(connstatus);
                    connstatus.setText("RECORD SAVED!");
                }
            }
            else{
                query = "INSERT INTO PRODUCTS VALUES('" + itemc + "', '" + desc + "', DATE '" + expr + "', " + Double.parseDouble(supplierprice) + ", " + Integer.parseInt(quantity) + ", '" + compart + "')";
                Statement stmt = conn.createStatement(); //create connection statement
                int rs = stmt.executeUpdate(query); // execute statement which is the insertion command
                if(rs > 0){//if insertion successful
                    Save.setDisable(true);
                    ItemCode.setEditable(false);
                    Description.setEditable(false);
                    ExpiryDate.setEditable(false);
                    SupplierPrice.setEditable(false);
                    Quantity.setEditable(false);
                    CompartmentID.setEditable(false);
                    refresh(connstatus);
                    connstatus.setText("RECORD SAVED!");
                }
            }
            
        }
        //handle possible errors
        catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void clear(){
        ItemCode.clear();
        Description.clear();        
        ExpiryDate.clear();
        SupplierPrice.clear();  
        Quantity.clear();
        CompartmentID.clear();
    }
   
    
    public void Edit(){
        ItemCode.setEditable(false);
        Description.setEditable(true);
        ExpiryDate.setEditable(true);
        SupplierPrice.setEditable(true);
        Quantity.setEditable(true);
        CompartmentID.setEditable(true);
        
        Edit.setText("Update");
        
        Save.setDisable(true);
        Delete.setDisable(true);
        Edit.setDisable(false);
        New.setDisable(true);
        Search.setDisable(true);
        
    }
    
    public void search(Connection conn){
        String desc = Description.getText();
        if(!desc.trim().isEmpty()){
            //create selection query
            String query = "SELECT * FROM PRODUCTS WHERE LOWER(DESCRIPTION) LIKE LOWER('" + desc + "')";
            try{
                //create connection statement
                Statement stmt = conn.createStatement();
                //execute query
                ResultSet rs = stmt.executeQuery(query);
                
                //read the result and display to textfields
                while (rs.next()){
                    String itemcode = rs.getString("ITEM_CODE");
                    String description = rs.getString("DESCRIPTION");
                    String expiry_date = rs.getString("EXPIRY_DATE");
                    float Price = rs.getFloat("SUPPLIER_PRICE");
                    int Qty = rs.getInt("QUANTITY");
                    String compartment = rs.getString("COMPARTMENT_ID");
                    if (expiry_date == null)
                        expiry_date = "";
                    else
                        expiry_date = expiry_date.substring(0, 10);
                    ItemCode.setText(itemcode);
                    Description.setText(description);
                    ExpiryDate.setText(expiry_date);
                    SupplierPrice.setText(Float.toString(Price));
                    Quantity.setText(Integer.toString(Qty));
                    CompartmentID.setText(compartment);
                    Delete.setDisable(false);
                    Edit.setDisable(false);
                }
                int rf = stmt.executeUpdate(query);
                if(rf == 0){
                    Alert alertagain = new Alert(Alert.AlertType.ERROR, "No record found.");
                    alertagain.setHeaderText(null);
                    alertagain.showAndWait();
                }
            }
            catch (SQLException e){
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void update(Connection conn, Label connstatus){
        try{
            String Itemc = ItemCode.getText(); 
            String Desc = Description.getText(); 
            String Expr = ExpiryDate.getText(); 
            String Supplierprice = SupplierPrice.getText(); 
            String qtyy = Quantity.getText();
            String Compart = CompartmentID.getText();
            
            String query;
            if (Expr.isEmpty()){
                query = "UPDATE PRODUCTS SET DESCRIPTION = '" + Desc + "', EXPIRY_DATE = NULL, SUPPLIER_PRICE = " + Double.parseDouble(Supplierprice) + ", QUANTITY = " + Integer.parseInt(qtyy) + ", COMPARTMENT_ID = '" + Compart + "' WHERE ITEM_CODE = '" + Itemc +"'";
            }
            else{
                query = "UPDATE PRODUCTS SET DESCRIPTION = '" + Desc + "', EXPIRY_DATE =  DATE '" + Expr + "', SUPPLIER_PRICE = " + Double.parseDouble(Supplierprice) + ", QUANTITY = " + Integer.parseInt(qtyy) + ", COMPARTMENT_ID = '" + Compart + "' WHERE ITEM_CODE = '" + Itemc +"'";
            }
            Statement stmt = conn.createStatement(); //create connection statement
            int rs = stmt.executeUpdate(query); //execute statement which is the update command
            if(rs > 0){ //if update successful
                connstatus.setText("RECORD UPDATED!");
                if(connstatus.getText() == "RECORD UPDATED!"){
                   refresh(connstatus);                     
                }

            }
        }
        //handle possible errors
        catch (SQLException e){
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void delete(Connection conn, Label connstatus){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("You are about to delete a record.");
        alert.showAndWait();
        
        if(alert.getResult() == ButtonType.YES){
            try{
                String desc = Description.getText(); //get content of product code text field
                //create the deletion query
                String query = "DELETE FROM PRODUCTS WHERE DESCRIPTION = '" + desc + "'";
                
                Statement stmt = conn.createStatement(); //create connection statement
                int rs = stmt.executeUpdate(query); //execute statement which is the delete command
                if(rs > 0){
                    Alert alertagain = new Alert(Alert.AlertType.INFORMATION, "Record Deleted.");
                    alertagain.setHeaderText(null);
                    alertagain.showAndWait();
                    refresh(connstatus);
                    connstatus.setText("CONNECTED!");
                }
            }
            //handle possible errors
            catch (SQLException e){
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}