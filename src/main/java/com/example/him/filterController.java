package com.example.him;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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

    private final ObservableList<String> liste1 = FXCollections.observableArrayList();
    private final ObservableList<String> liste2 = FXCollections.observableArrayList();
    private final ObservableList<String> liste3 = FXCollections.observableArrayList();
    private final ObservableList<String> liste4 = FXCollections.observableArrayList();
    private final ObservableList<String> liste5 = FXCollections.observableArrayList();
    private final ObservableList<String> liste6 = FXCollections.observableArrayList();
    private final ObservableList<String> liste7 = FXCollections.observableArrayList();
    private final ObservableList<String> liste8 = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sirket("IGS");
        sirket_bildirim("FR");
        fon("4028328d71bc3c65017237144e7778a3");
        fon("4028328c55064295015506dc365b4c2c");
        fon_bildirim("FR");
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
        }else{
            if(sayi==1) {
                String name = filterName.getText();
                String check = String.valueOf(aktif.isSelected());
                sqlite.update(id,name,check,liste2,liste4,liste6,liste8);
                sayi = 0;
            }else{
                String filtres= filters.getItems().toString();
                String filtre = filtres.substring(1, filtres.length() - 1);
                String[] dizi = filtre.split(",");
                for(int i = 0; i < dizi.length; i++){
                    dizi[i] = dizi[i].trim();
                    if(dizi[i].equals(filterName.getText())){
                        count++;
                    }
                }
                if(count == 0){

                    String name = filterName.getText();
                    String check = String.valueOf(aktif.isSelected());

                    sqlite.insert(name, check, liste2, liste4, liste6, liste8, id);

                    filters.getItems().addAll(name);
                }else{
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
                    System.out.println("Bu isimde filtre bulunmaktadır");
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
                        liste2.add(dizi1[i]);
                        liste1.remove(dizi1[i]);
                    }
                    list1.setItems(liste1);
                    list2.setItems(liste2);

                    String fon = fons.substring(1, fons.length() - 1);
                    String[] dizi2 = fon.split(",");

                    for(int i = 0; i < dizi2.length; i++){
                        dizi2[i] = dizi2[i].trim();
                        liste4.add(dizi2[i]);
                        liste3.remove(dizi1[i]);
                    }
                    list3.setItems(liste3);
                    list4.setItems(liste4);

                    String companyNotification = companyNotifications.substring(1, companyNotifications.length() - 1);
                    String[] dizi3 = companyNotification.split(",");

                    for(int i = 0; i < dizi3.length; i++){
                        dizi3[i] = dizi3[i].trim();
                        liste6.add(dizi3[i]);
                        liste5.remove(dizi1[i]);
                    }
                    list5.setItems(liste5);
                    list6.setItems(liste6);

                    String fonNotification = fonNotifications.substring(1, fonNotifications.length() - 1);
                    String[] dizi4 = fonNotification.split(",");

                    for(int i = 0; i < dizi4.length; i++){
                        dizi4[i] = dizi4[i].trim();
                        liste8.add(dizi1[i]);
                        liste7.remove(dizi4[i]);
                    }
                    list7.setItems(liste7);
                    list8.setItems(liste8);
                }
            }
            rs.close();
            statement.close();
            connection.close();

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
        if(list1.getSelectionModel().getSelectedItem()!=null) {
            liste2.add(list1.getSelectionModel().getSelectedItem());
            list1.getItems().remove(list1.getSelectionModel().getSelectedItem());
            list2.setItems(liste2);
        }
    }
    public void left1(){
        if (list2.getSelectionModel().getSelectedItem() != null) {
            liste1.add(list2.getSelectionModel().getSelectedItem());
            list2.getItems().remove(list2.getSelectionModel().getSelectedItem());
            list1.setItems(liste1);
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
        if(list3.getSelectionModel().getSelectedItem()!=null) {
            liste4.add(list3.getSelectionModel().getSelectedItem());
            list3.getItems().remove(list3.getSelectionModel().getSelectedItem());
            list4.setItems(liste4);
        }
    }
    public void left2(){
        if (list4.getSelectionModel().getSelectedItem() != null) {
            liste3.add(list4.getSelectionModel().getSelectedItem());
            list4.getItems().remove(list4.getSelectionModel().getSelectedItem());
            list3.setItems(liste3);
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
        if(list5.getSelectionModel().getSelectedItem()!=null) {
            liste6.add(list5.getSelectionModel().getSelectedItem());
            list5.getItems().remove(list5.getSelectionModel().getSelectedItem());
            list6.setItems(liste6);
        }
    }
    public void left3(){
        if (list6.getSelectionModel().getSelectedItem() != null) {
            liste5.add(list6.getSelectionModel().getSelectedItem());
            list6.getItems().remove(list6.getSelectionModel().getSelectedItem());
            list5.setItems(liste5);
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
        if(list7.getSelectionModel().getSelectedItem()!=null) {
            liste8.add(list7.getSelectionModel().getSelectedItem());
            list7.getItems().remove(list7.getSelectionModel().getSelectedItem());
            list8.setItems(liste8);
        }
    }
    public void left4(){
        if (list8.getSelectionModel().getSelectedItem() != null) {
            liste7.add(list8.getSelectionModel().getSelectedItem());
            list8.getItems().remove(list8.getSelectionModel().getSelectedItem());
            list7.setItems(liste7);
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
                    liste1.add((bist));
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
                    liste3.add((bist));
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
                    liste5.add((bist));
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
                    liste7.add((bist));
                }
                Collections.sort(liste7);
                list7.setItems(liste7);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        } /*else if (secim.equals("Varlık Finansman Fonları")) {
            word="ALL";
            fon(word);
        } else if (secim.equals("Konut Finansman Fonları")) {
            word="ALL";
            fon(word);*
        }*/ else if (secim.equals("Gayrimenkul Yatırım Fonları")) {
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
