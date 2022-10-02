package com.example.him;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;

public class filterController implements Initializable {
    @FXML
    private ListView<String> list1,list2,list3,list4,list5,list6,list7,list8;
    @FXML
    private TextField filterName;
    @FXML
    private ComboBox cmbbox1,cmbbox2,cmbbox3,cmbbox4,filters;
    @FXML
    private CheckBox aktif;
    SQLiteJDBC sqlite = new SQLiteJDBC();
    int id;
    int sayi=0;
    boolean loaded;

    private final ObservableList<String> liste1 = FXCollections.observableArrayList();
    private final ObservableList<String> liste2 = FXCollections.observableArrayList();
    private final ObservableList<String> liste3 = FXCollections.observableArrayList();
    private final ObservableList<String> liste4 = FXCollections.observableArrayList();
    private final ObservableList<String> liste5 = FXCollections.observableArrayList();
    private final ObservableList<String> liste6 = FXCollections.observableArrayList();
    private final ObservableList<String> liste7 = FXCollections.observableArrayList();
    private final ObservableList<String> liste8 = FXCollections.observableArrayList();

    private final ObservableList<String> items1 = FXCollections.observableArrayList();
    private final ObservableList<String> items2 = FXCollections.observableArrayList();
    private final ObservableList<String> items3 = FXCollections.observableArrayList();
    private final ObservableList<String> items4 = FXCollections.observableArrayList();
    private final ObservableList<String> items5 = FXCollections.observableArrayList();
    private final ObservableList<String> items6 = FXCollections.observableArrayList();
    private final ObservableList<String> items7 = FXCollections.observableArrayList();
    private final ObservableList<String> items8 = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sirket("IGS");
        sirket_bildirim("FR");
        fon("4028328d71bc3c65017237144e7778a3");
        fon("4028328c55064295015506dc365b4c2c");
        fon_bildirim("FR");
        loaded = false;

        list1.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list2.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list3.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list4.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list5.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list6.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list7.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        list8.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        list1.addEventFilter(MouseEvent.MOUSE_PRESSED, this::rightpush1);
        list2.addEventFilter(MouseEvent.MOUSE_PRESSED, this::leftpush1);
        list3.addEventFilter(MouseEvent.MOUSE_PRESSED, this::rightpush2);
        list4.addEventFilter(MouseEvent.MOUSE_PRESSED, this::leftpush2);
        list5.addEventFilter(MouseEvent.MOUSE_PRESSED, this::rightpush3);
        list6.addEventFilter(MouseEvent.MOUSE_PRESSED, this::leftpush3);
        list7.addEventFilter(MouseEvent.MOUSE_PRESSED, this::rightpush4);
        list8.addEventFilter(MouseEvent.MOUSE_PRESSED, this::leftpush4);

        select();
    }
    public void select(){

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            System.out.println("Opened database succesfully ");

            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Him;");

            while(rs.next()){
                id = rs.getInt("id");
                String filterName = rs.getString("FilterName");
                String aktif = rs.getString("Aktif");
                boolean b1 = Boolean.parseBoolean(aktif);
                String companies = rs.getString("Companies");
                String fons = rs.getString("Fons");
                String companyNotifications = rs.getString("CompanyNotifications");
                String fonNotification = rs.getString("FonNotification");

                filters.getItems().add(filterName);

                System.out.println("id = " + id);
                System.out.println("Filter Name: " + filterName);
                System.out.println("Aktif: " + aktif);
                System.out.println("Companies: " + companies);
                System.out.println("Fons: " + fons);
                System.out.println("CompanyNotifications: " + companyNotifications);
                System.out.println("FonNotification: " + fonNotification);
            }
            rs.close();
            statement.close();
            connection.close();
            System.out.println("showed company");
        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }

    }
    @FXML
    public void save(){
        int count = 0;
        if(filterName.getText()==""){
            System.out.println("boş değer girilemez");
        }else{
            if(loaded){
                System.out.println("update");
                String name = filterName.getText();
                String check = String.valueOf(aktif.isSelected());
                sqlite.update(id,name,check,liste2,liste4,liste6,liste8);
            }else{
                System.out.println("save");

                String filtres= filters.getItems().toString();
                String filtre = filtres.substring(1, filtres.length() - 1);
                String[] dizi = filtre.split(",");
                for(int i = 0; i < dizi.length; i++) {
                    dizi[i] = dizi[i].trim();
                    if(dizi[i].equals(filterName.getText())) {
                        count++;
                    }
                }
                if(count != 0){
                    try {
                        Parent part = FXMLLoader.load(getClass().getResource("confirm1.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(part);
                        stage.setScene(scene);
                        stage.setTitle("Filtre Adı");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.setResizable(false);
                        stage.setAlwaysOnTop(true);
                        stage.show();
                        scene.setOnKeyPressed(e->{
                            if(e.getCode() == KeyCode.ESCAPE){
                                stage.close();
                            }
                        });
                    } catch (Exception e) {
                        System.out.println(e.getClass().getName() + ": " + e.getMessage());
                    }
                }else{
                    String name = filterName.getText();
                    String check = String.valueOf(aktif.isSelected());
                    //System.out.println(liste4);
                    sqlite.insert(name, check, liste2, liste4, liste6, liste8, id);

                    filters.getItems().addAll(name);
                }
            }
        }
    }
    @FXML
    public void load(){
        list2.getItems().clear();
        list4.getItems().clear();
        list6.getItems().clear();
        list8.getItems().clear();
        sayi=1;
        String name = (String) filters.getSelectionModel().getSelectedItem();

        Connection connection = null;
        Statement statement = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            System.out.println("Opened database succesfully ");

            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Him;");

            while(rs.next()){
                int id = rs.getInt("id");
                String filtername = rs.getString("FilterName");
                String check = rs.getString("Aktif");
                boolean b1 = Boolean.parseBoolean(check);
                String companies = rs.getString("Companies");
                String fons = rs.getString("Fons");
                String companyNotifications = rs.getString("CompanyNotifications");
                String fonNotifications = rs.getString("FonNotification");

                if(name.equals(filtername)){
                    filterName.setText(name);
                    aktif.setSelected(b1);

                    String company = companies.substring(1, companies.length() - 1);
                    String[] dizi1= company.split(",");

                    for (int i = 0; i < dizi1.length; i++) {
                        dizi1[i] = dizi1[i].trim();
                        if(!dizi1[i].equals("")) {
                            liste2.add(dizi1[i]);
                            liste1.remove(dizi1[i]);
                        }
                    }
                    list1.setItems(liste1);
                    list2.setItems(liste2);

                    String fon = fons.substring(1, fons.length() - 1);
                    String[] dizi2 = fon.split(",");

                    for(int i = 0; i < dizi2.length; i++){
                        dizi2[i] = dizi2[i].trim();
                        if(!dizi2[i].equals("")) {
                            liste4.add(dizi2[i]);
                            liste3.remove(dizi2[i]);
                        }
                    }
                    list3.setItems(liste3);
                    list4.setItems(liste4);

                    String companyNotification = companyNotifications.substring(1, companyNotifications.length() - 1);
                    String[] dizi3 = companyNotification.split(",");

                    for(int i = 0; i < dizi3.length; i++){
                        dizi3[i] = dizi3[i].trim();
                        if(!dizi3[i].equals("")) {
                            liste6.add(dizi3[i]);
                            liste5.remove(dizi3[i]);
                        }
                    }
                    list5.setItems(liste5);
                    list6.setItems(liste6);

                    String fonNotification = fonNotifications.substring(1, fonNotifications.length() - 1);
                    String[] dizi4 = fonNotification.split(",");

                    for(int i = 0; i < dizi4.length; i++){
                        dizi4[i] = dizi4[i].trim();
                        if(!dizi4[i].equals("")) {
                            liste8.add(dizi4[i]);
                            liste7.remove(dizi4[i]);
                        }
                    }
                    list7.setItems(liste7);
                    list8.setItems(liste8);
                }
            }
            rs.close();
            statement.close();
            connection.close();
            loaded = true;

        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    @FXML
    public void delete(){
        String name = (String) filters.getSelectionModel().getSelectedItem();
        sqlite.delete(name);
        filters.getItems().remove(name);
        filterName.setText("");
        aktif.setSelected(false);
        list2.getItems().clear();
        list4.getItems().clear();
        list6.getItems().clear();
        list8.getItems().clear();
    }
    public void right1(){
        if(list1.getSelectionModel().getSelectedItem()!=null) {   //items2 2.geçici liste görevinde  liste2 1. geçici liste görevinde
            if(!items2.isEmpty()) {
                liste2.addAll(items2);
                list1.getItems().removeAll(liste2);
                list2.setItems(liste2);
            }
            items2.clear();
        }
    }
    public void left1(){
        if(list2.getSelectionModel().getSelectedItem()!=null) {
            if(!items1.isEmpty()) {
                liste1.addAll(items1);
                list2.getItems().removeAll(liste1);
                list1.setItems(liste1);
            }
            items1.clear();
        }
    }
    public void rightall1(){
        liste2.addAll(list1.getItems());
        list1.getItems().removeAll(list1.getItems());
        list2.setItems(liste2);
    }
    public void leftall1(){
        liste1.addAll(list2.getItems());
        list2.getItems().removeAll(list2.getItems());
        list1.setItems(liste1);
    }
    public void right2(){
        if(list3.getSelectionModel().getSelectedItem()!=null) {   //items2 2.geçici liste görevinde  liste2 1. geçici liste görevinde
            if(!items4.isEmpty()) {
                liste4.addAll(items4);
                list3.getItems().removeAll(liste4);
                list4.setItems(liste4);
            }
            items4.clear();
        }
    }
    public void left2(){
        if(list4.getSelectionModel().getSelectedItem()!=null) {
            if(!items3.isEmpty()) {
                liste3.addAll(items3);
                list4.getItems().removeAll(liste3);
                list3.setItems(liste3);
            }
            items3.clear();
        }
    }
    public void rightall2(){
        liste4.addAll(list3.getItems());
        list3.getItems().removeAll(list3.getItems());
        list4.setItems(liste4);
    }
    public void leftall2(){
        liste3.addAll(list4.getItems());
        list4.getItems().removeAll(list4.getItems());
        list3.setItems(liste3);
    }
    public void right3(){
        if(list5.getSelectionModel().getSelectedItem()!=null) {   //items2 2.geçici liste görevinde  liste2 1. geçici liste görevinde
            if(!items6.isEmpty()) {
                liste6.addAll(items6);
                list5.getItems().removeAll(liste6);
                list6.setItems(liste6);
            }
            items6.clear();
        }
    }
    public void left3(){
        if(list6.getSelectionModel().getSelectedItem()!=null) {
            if(!items5.isEmpty()) {
                liste5.addAll(items5);
                list6.getItems().removeAll(liste5);
                list5.setItems(liste5);
            }
            items5.clear();
        }
    }
    public void rightall3(){
        liste6.addAll(list5.getItems());
        list5.getItems().removeAll(list5.getItems());
        list6.setItems(liste6);
    }
    public void leftall3(){
        liste5.addAll(list6.getItems());
        list6.getItems().removeAll(list6.getItems());
        list5.setItems(liste5);
    }
    public void right4(){
        if(list7.getSelectionModel().getSelectedItem()!=null) {   //items2 2.geçici liste görevinde  liste2 1. geçici liste görevinde
            if(!items8.isEmpty()) {
                liste8.addAll(items8);
                list7.getItems().removeAll(liste8);
                list8.setItems(liste8);
            }
            items8.clear();
        }
    }
    public void left4(){
        if(list8.getSelectionModel().getSelectedItem()!=null) {
            if(!items7.isEmpty()) {
                liste7.addAll(items7);
                list8.getItems().removeAll(liste7);
                list7.setItems(liste7);
            }
            items7.clear();
        }
    }
    public void rightall4(){
        liste8.addAll(list7.getItems());
        list7.getItems().removeAll(list7.getItems());
        list8.setItems(liste8);
    }
    public void leftall4(){
        liste7.addAll(list8.getItems());
        list8.getItems().removeAll(list8.getItems());
        list7.setItems(liste7);
    }
    public void sirket(String type){
        try {
            URL link = new URL("https://www.kap.org.tr/tr/api/kapmembers/"+type+"/A");
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(link.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }

                JSONParser parse = new JSONParser();
                JSONArray data_obj = (JSONArray) parse.parse(inline.toString());

                if(data_obj != null){
                    //Get the required data using its key
                    System.out.println("No Problem");
                }else{
                    System.out.println((Object) null);
                }

                for(int i = 0; i < data_obj.size(); i++){
                    JSONObject new_obj = (JSONObject) data_obj.get(i);
                    String bist = (String) new_obj.get("kapMemberTitle");
                    if(!liste1.contains(bist)) {
                        liste1.add((bist));
                    }
                }
                Collections.sort(liste1);
                list1.setItems(liste1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void fon(String type){
        try {
            URL link = new URL("https://www.kap.org.tr/tr/api/kapi/him/fund/filterByGroupOid/"+type);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(link.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }

                JSONParser parse = new JSONParser();
                JSONArray data_obj = (JSONArray) parse.parse(inline.toString());

                if(data_obj != null){
                    //Get the required data using its key
                    System.out.println("No Problem");
                }else{
                    System.out.println((Object) null);
                }

                for(int i = 0; i < data_obj.size(); i++){
                    JSONObject new_obj = (JSONObject) data_obj.get(i);
                    String bist = (String) new_obj.get("title");
                    if(!liste3.contains(bist)) {
                        liste3.add((bist));
                    }
                }
                Collections.sort(liste3);
                list3.setItems(liste3);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void sirket_bildirim(String type){
        try {
            URL link = new URL("https://www.kap.org.tr/tr/api/kapi/him/disclosureTopic/filterByClassType/"+type);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(link.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }

                JSONParser parse = new JSONParser();
                JSONArray data_obj = (JSONArray) parse.parse(inline.toString());

                if(data_obj != null){
                    //Get the required data using its key
                    System.out.println("No Problem");
                }else{
                    System.out.println((Object) null);
                }

                for(int i = 0; i < data_obj.size(); i++){
                    JSONObject new_obj = (JSONObject) data_obj.get(i);
                    String bist = (String) new_obj.get("title");
                    if(!liste5.contains(bist)) {
                        liste5.add((bist));
                    }
                }
                Collections.sort(liste5);
                list5.setItems(liste5);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void fon_bildirim(String type){
        try {
            URL link = new URL("https://www.kap.org.tr/tr/api/kapi/him/fundDisclosureTopic/filterByClassType/"+type);
            HttpURLConnection conn = (HttpURLConnection) link.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                StringBuilder inline = new StringBuilder();
                Scanner scanner = new Scanner(link.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline.append(scanner.nextLine());
                }

                JSONParser parse = new JSONParser();
                JSONArray data_obj = (JSONArray) parse.parse(inline.toString());

                if(data_obj != null){
                    //Get the required data using its key
                    System.out.println("No Problem");
                }else{
                    System.out.println((Object) null);
                }

                for(int i = 0; i < data_obj.size(); i++){
                    JSONObject new_obj = (JSONObject) data_obj.get(i);
                    String bist = (String) new_obj.get("title");
                    if (!liste7.contains(bist)) {
                        liste7.add((bist));
                    }
                }
                Collections.sort(liste7);
                list7.setItems(liste7);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void rightpush1(MouseEvent evt){

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list1 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items2.size(); i++) {
                        if(items2.get(i).equals(lv.getItems().get(index))){
                            items2.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items2.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void leftpush1(MouseEvent evt){

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list2 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items1.size(); i++) {
                        if(items1.get(i).equals(lv.getItems().get(index))){
                            items1.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items1.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void rightpush2(MouseEvent evt) {

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list3 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items4.size(); i++) {
                        if(items4.get(i).equals(lv.getItems().get(index))){
                            items4.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items4.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void leftpush2(MouseEvent evt){

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list4 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items3.size(); i++) {
                        if(items3.get(i).equals(lv.getItems().get(index))){
                            items3.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items3.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void rightpush3(MouseEvent evt) {

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list5 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items6.size(); i++) {
                        if(items6.get(i).equals(lv.getItems().get(index))){
                            items6.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items6.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void leftpush3(MouseEvent evt){

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list6 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items5.size(); i++) {
                        if(items5.get(i).equals(lv.getItems().get(index))){
                            items5.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items5.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void rightpush4(MouseEvent evt) {

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list7 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items8.size(); i++) {
                        if(items8.get(i).equals(lv.getItems().get(index))){
                            items8.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items8.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    public void leftpush4(MouseEvent evt){

        Node node = evt.getPickResult().getIntersectedNode();

        while (node != null && node != list8 && !(node instanceof ListCell)) {
            node = node.getParent();
        }

        if (node instanceof ListCell) {
            evt.consume();

            ListCell cell = (ListCell) node;
            ListView lv = cell.getListView();

            lv.requestFocus();

            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (cell.isSelected()) {
                    lv.getSelectionModel().clearSelection(index);
                    for(int i = 0; i < items7.size(); i++) {
                        if(items7.get(i).equals(lv.getItems().get(index))){
                            items7.remove(i);
                        }
                    }
                } else {
                    lv.getSelectionModel().select(index);
                    items7.add((String) lv.getSelectionModel().getSelectedItem());
                }
            }
        }
    }
    @FXML
    public void cmbbox1(){
        String word="";
        liste1.clear();
        String secim = (String)cmbbox1.getSelectionModel().getSelectedItem();

        if(secim == null || secim.equals("BIST Şirketleri")){
            word="IGS";
            sirket(word);
        } else if (secim.equals("Yatırım Kuruluşları")) {
            word="YK";
            sirket(word);
        } else if (secim.equals("Portföy Yönetim Şirketleri")) {
            word="PYS";
            sirket(word);
        } else if (secim.equals("Bağımsız Denetim Kuruluşları")) {
            word="BDK";
            sirket(word);
        } else if (secim.equals("Derecelendirme Şirketleri")) {
            word="DCS";
            sirket(word);
        }else if (secim.equals("Değerleme Şirketleri")){
            word="DS";
            sirket(word);
        }else if (secim.equals("Diğer Kap Üyeleri ve İşlem Görmeyen Şirketler")){
            word="DK";
            sirket(word);
        }
    }
    @FXML
    public void cmbbox2(){
        String word="";
        liste3.clear();
        String secim = (String)cmbbox2.getSelectionModel().getSelectedItem();

        if(secim == null || secim.equals("Yatırım Fonu")){
            word="4028328c55064295015506dc365b4c2c";
            fon(word);
            word="4028328d71bc3c65017237144e7778a3";
            fon(word);
        } else if (secim.equals("Emeklilik Yatırım Fonu")) {
            word="4028328c55064295015506de3edf4f98";
            fon(word);
        } else if (secim.equals("Yabancı Yatırım Fonu")) {
            word="4028328c55064295015506decc1b50d1";
            fon(word);
        } else if (secim.equals("Borsa Yatırım Fonu")) {
            word="4028328c55064295015506db980a4bec";
            fon(word);
        }  else if (secim.equals("Gayrimenkul Yatırım Fonları")) {
            word="4028328c5fc60da7015fe285cb2c63a6";
            fon(word);
        } else if (secim.equals("Girişim Sermayesi Yatırım Fonları")) {
            word="4028328c7436d8ce01744eef65f14601";
            fon(word);
        }
    }
    @FXML
    public void cmbbox3(){
        String word="";
        liste5.clear();
        String secim = (String)cmbbox3.getSelectionModel().getSelectedItem();

        if(secim == null || secim.equals("Finansal Rapor Bildirim Konuları")){
            word="FR";
            sirket_bildirim(word);
        } else if (secim.equals("Özel Durum Açıklaması Konuları")) {
            word="ODA";
            sirket_bildirim(word);
        } else if (secim.equals("Duyuru Bildirimi Konuları")) {
            word="DUY";
            sirket_bildirim(word);
        } else if (secim.equals("Diğer Bildirim Konuları")) {
            word="DG";
            sirket_bildirim(word);
        } else if (secim.equals("Tümü")) {
            word="ALL";
            sirket_bildirim(word);
        }
    }
    @FXML
    public void cmbbox4(){
        String word="";
        liste7.clear();
        String secim = (String)cmbbox4.getSelectionModel().getSelectedItem();

        if(secim == null || secim.equals("Finansal Rapor Bildirim Konuları")){
            word="FR";
            fon_bildirim(word);
        } else if (secim.equals("Özel Durum Açıklaması Konuları")) {
            word="ODA";
            fon_bildirim(word);
        } else if (secim.equals("Duyuru Bildirimi Konuları")) {
            word="DUY";
            fon_bildirim(word);
        } else if (secim.equals("Diğer Bildirim Konuları")) {
            word="DG";
            fon_bildirim(word);
        } else if (secim.equals("Tümü")) {
            word="ALL";
            fon_bildirim(word);
        }
    }
}
