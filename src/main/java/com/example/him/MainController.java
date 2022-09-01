package com.example.him;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
public class MainController implements Initializable {

    @FXML
    private CheckMenuItem detay;
    @FXML
    private Label lbl1,lbl2;
    @FXML
    private TableView<Data> table1,table2;
    @FXML
    private TableColumn<Data,String> bildirimTarihi,bildirimSinifi,gonderenSirket,bildirimKonusu,ozet,ilgiliSirketler,bildirimId,ek;
    @FXML
    private TableColumn<Data,String> bildirimTarihi1,bildirimSinifi1,gonderenSirket1,bildirimKonusu1,ozet1,ilgiliSirketler1,bildirimId1,ek1;

    @FXML
    private ComboBox choice;
    @FXML
    private Label names;

    private String zaman="";
    private int sayi1;
    private int sayi2=0;
    JSONArray data_obj;
    SQLiteJDBC sqlite = new SQLiteJDBC();
    private final ObservableList<Data> list = FXCollections.observableArrayList();
    private final ObservableList<Data> list2 = FXCollections.observableArrayList();

    private final List<String> liste1 = new ArrayList<String>();
    private final List<String> liste2 = new ArrayList<String>();
    private final List<String> liste3 = new ArrayList<String>();
    private final List<String> liste4 = new ArrayList<String>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        sqlite.create();
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        long nextsaniye = currentDate.getTime() -(24*60*60*1000) ;
        Date nextdate = new Date(nextsaniye);

        String date = dateFormat.format(nextdate);
        item_of_list(date);
        table1.setItems(list);
        sayi1=list.size();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(7), e-> {
            if(zaman==""){
                zaman= date;
            }
            item_of_list(zaman);
        }));
        timeline.setCycleCount(7);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void item_of_list(String zaman){
        bildirimTarihi.setCellValueFactory(new PropertyValueFactory<>("date"));
        bildirimSinifi.setCellValueFactory(new PropertyValueFactory<>("bildirimSinifi"));
        gonderenSirket.setCellValueFactory(new PropertyValueFactory<>("gonderenSirket"));
        bildirimKonusu.setCellValueFactory(new PropertyValueFactory<>("bildirimKonusu"));
        ozet.setCellValueFactory(new PropertyValueFactory<>("ilgiliSirketler"));
        ilgiliSirketler.setCellValueFactory(new PropertyValueFactory<>("ozet"));
        bildirimId.setCellValueFactory(new PropertyValueFactory<>("bildirimId"));
        ek.setCellValueFactory(new PropertyValueFactory<>("ek"));

        try {
            String url ="https://kap.org.tr/tr/api/kapi/him/disclosure/list?fromDate="+zaman;

            URL link = new URL(url);
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

                //Close the scanner
                scanner.close();
                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                data_obj = (JSONArray) parse.parse(inline.toString());

                if(data_obj != null){
                    //Get the required data using its key
                    System.out.println("No Problem");
                }else{
                    System.out.println((Object) null);
                }

                if(data_obj.size() != sayi1){
                    list.clear();

                    for(int i = 0; i < data_obj.size(); i++){
                        JSONObject new_obj = (JSONObject) data_obj.get(i);
                        String tarih = (String) new_obj.get("publishDate");

                        String[] dizi = tarih.split(" ");
                        String gun,ay,yil;
                        yil = dizi[0].substring(0,4);
                        ay = dizi[0].substring(4,8);
                        gun = dizi[0].substring(8,10);
                        tarih=gun+ay+yil+" "+dizi[1];

                        String sinif = (String) new_obj.get("disclosureClass");
                        String sirket = (String) new_obj.get("companyTitle");
                        String bildirimKonusu = (String) new_obj.get("title");

                        String ozet = (String) new_obj.get("summary");
                        if(ozet == null){
                            ozet = "";
                        }

                        String ilgiliSirket = (String) new_obj.get("relatedStocks");
                        if(ilgiliSirket == null){
                            ilgiliSirket = "";
                        }

                        String bildirimId = new_obj.get("disclosureIndex").toString();
                        String ek = new_obj.get("attachmentCount").toString();
                        list.add(new Data(tarih,sinif,sirket,bildirimKonusu,ilgiliSirket,ozet,bildirimId,ek));
                        sayi1=list.size();
                    }sayi2++;
                    if(sayi2 > 1){
                        notification();
                    }
                }
                select();
                lbl1.setText("Tüm Bildirimler - ["+list.size()+" adet]");
            }
        }   catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public void notification() {
        try {
            Parent part = FXMLLoader.load(getClass().getResource("notification.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(part);
            stage.setScene(scene);
            stage.setTitle("Yeni Bildirim");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.show();

            String title = "Yeni Bildirim Geldi";
            String message = list.get(0).getBildirimKonusu();
            Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("info.png")));

            String os = System.getProperty("os.name");
            if (os.contains("Linux")) {
                ProcessBuilder builder = new ProcessBuilder(
                        "zenity",
                        "--notification",
                        "--title=" + title,
                        "--text=" + message);
                builder.inheritIO().start();
            } else if (os.contains("Mac")) {
                ProcessBuilder builder = new ProcessBuilder(
                        "osascript", "-e",
                        "display notification \"" + message + "\""
                                + " with title \"" + title + "\"");
                builder.inheritIO().start();
            } else if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();

                TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
                trayIcon.setImageAutoSize(true);
                tray.add(trayIcon);

                trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void getir(){
        String secim = (String) choice.getSelectionModel().getSelectedItem();
        sayi2=0;

        if(secim==null){
            secim = "";
        }

        char[] chars = secim.toCharArray();
        StringBuilder sb= new StringBuilder();

        if(chars.length == 0){
            sb= new StringBuilder("1");
        }else {
            for (char c : chars) {
                if (Character.isDigit(c)) {
                    sb.append(c);
                }
            }
        }

        long saniye = 24*60*60*1000;
        int i = Integer.parseInt(String.valueOf(sb));
        String DATE_FORMAT = "dd.MM.yyyy";

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        long nextsaniye = currentDate.getTime() -(saniye*i) ;
        Date nextdate = new Date(nextsaniye);

        zaman= dateFormat.format(nextdate);
        System.out.println(zaman);
        item_of_list(zaman);
    }
    @FXML
    public void click(){
        table1.setOnMouseClicked(e->{
            if(e.getClickCount() == 2 && table1.getSelectionModel().getSelectedItem()!=null ){
                String link ="http://www.kap.org.tr/tr/Bildirim/"+table1.getSelectionModel().getSelectedItem().getBildirimId();
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(link));
                } catch (Exception event) {
                    throw new RuntimeException(event);
                }
            }
        });
    }
    @FXML
    public void click1(){
        table2.setOnMouseClicked(e->{
            if(e.getClickCount() == 2 && table2.getSelectionModel().getSelectedItem()!=null ){
                String link ="http://www.kap.org.tr/tr/Bildirim/"+table2.getSelectionModel().getSelectedItem().getBildirimId();
                try {
                    java.awt.Desktop.getDesktop().browse(new URI(link));
                } catch (Exception event) {
                    throw new RuntimeException(event);
                }
            }
        });
    }
    @FXML
    public void filter(){
        try {
            Parent part = FXMLLoader.load(getClass().getResource("filter.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(part);
            stage.setScene(scene);
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("filter.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Filtre Yönetimi");
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.show();
            scene.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.ESCAPE){
                    stage.close();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void down(){
        try {
            Parent part = FXMLLoader.load(getClass().getResource("confirm.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(part);
            stage.setScene(scene);
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("off.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Çıkış");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void help(){
        try {
            Parent part = FXMLLoader.load(getClass().getResource("about.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(part);
            stage.setScene(scene);
            javafx.scene.image.Image icon = new javafx.scene.image.Image(getClass().getResourceAsStream("about.png"));
            stage.getIcons().add(icon);
            stage.setTitle("Haber İzleme Modülü Hakkında");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.show();

            scene.setOnKeyPressed(e->{
                if(e.getCode() == KeyCode.ESCAPE){
                    stage.close();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void summary(){
        boolean is = detay.isSelected();
        ozet.setVisible(is);
        ozet1.setVisible(is);
    }

    public void select(){
        liste1.clear();
        liste2.clear();
        liste3.clear();
        liste4.clear();
        Connection connection = null;
        Statement statement = null;
        StringBuilder isim= new StringBuilder();

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:database.db");
            connection.setAutoCommit(false);
            System.out.println("Opened database succesfully ");

            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from Him;");

            while(rs.next()){
                int id = rs.getInt("id");
                String filterName = rs.getString("FilterName");
                String aktif = rs.getString("Aktif");
                boolean b1 = Boolean.parseBoolean(aktif);
                String companies = rs.getString("Companies");
                String fons = rs.getString("Fons");
                String companyNotifications = rs.getString("CompanyNotifications");
                String fonNotifications = rs.getString("FonNotification");

                if(b1){
                    isim.append(filterName+" ");
                    String company = companies.substring(1, companies.length() - 1);
                    String[] dizi1= company.split(",");

                    for(int i = 0; i < dizi1.length; i++){
                        dizi1[i] = dizi1[i].trim();
                        liste1.add(dizi1[i]);
                    }

                    String fon = fons.substring(1, fons.length() - 1);
                    String[] dizi2 = fon.split(",");

                    for(int i = 0; i < dizi2.length; i++){
                        dizi2[i] = dizi2[i].trim();
                        liste2.add(dizi2[i]);
                    }

                    String companyNotification = companyNotifications.substring(1, companyNotifications.length() - 1);
                    String[] dizi3 = companyNotification.split(",");

                    for(int i = 0; i < dizi3.length; i++){
                        dizi3[i] = dizi3[i].trim();
                        liste3.add(dizi3[i]);
                    }

                    String fonNotification = fonNotifications.substring(1, fonNotifications.length() - 1);
                    String[] dizi4 = fonNotification.split(",");

                    for(int i = 0; i < dizi4.length; i++){
                        dizi4[i] = dizi4[i].trim();
                        liste4.add(dizi1[i]);
                    }
                }
                names.setText("[ "+(isim)+"]");
            }
            rs.close();
            statement.close();
            connection.close();

        }catch (Exception e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage()+"hata burada");
        }
        filterList();
    }
    public void filterList(){
        list2.clear();

        bildirimTarihi1.setCellValueFactory(new PropertyValueFactory<>("date"));
        bildirimSinifi1.setCellValueFactory(new PropertyValueFactory<>("bildirimSinifi"));
        gonderenSirket1.setCellValueFactory(new PropertyValueFactory<>("gonderenSirket"));
        bildirimKonusu1.setCellValueFactory(new PropertyValueFactory<>("bildirimKonusu"));
        ozet1.setCellValueFactory(new PropertyValueFactory<>("ilgiliSirketler"));
        ilgiliSirketler1.setCellValueFactory(new PropertyValueFactory<>("ozet"));
        bildirimId1.setCellValueFactory(new PropertyValueFactory<>("bildirimId"));
        ek1.setCellValueFactory(new PropertyValueFactory<>("ek"));

        for (int i = 0; i < list.size(); i++) {    //Şirket Filtresi
            for (int j = 0; j < liste1.size(); j++) {
                if (liste1.get(j).equals(list.get(i).getGonderenSirket())) {
                    list2.add(new Data(list.get(i).getDate(), list.get(i).getBildirimSinifi(),
                            list.get(i).getGonderenSirket(), list.get(i).getBildirimKonusu(),
                            list.get(i).getOzet(), list.get(i).getIlgiliSirketler(),
                            list.get(i).getBildirimId(), list.get(i).getEk()));
                }
            }
            for (int j = 0; j < liste2.size(); j++) {         //Fon Filtresi
                if (liste2.get(j).equals(list.get(i).getGonderenSirket())) {
                    list2.add(new Data(list.get(i).getDate(), list.get(i).getBildirimSinifi(),
                            list.get(i).getGonderenSirket(), list.get(i).getBildirimKonusu(),
                            list.get(i).getOzet(), list.get(i).getIlgiliSirketler(),
                            list.get(i).getBildirimId(), list.get(i).getEk()));
                }
            }
            for (int j = 0; j < liste3.size(); j++) {        //Şirket Bildirim Filtresi
                if (liste3.get(j).equals(list.get(i).getBildirimKonusu())) {
                    list2.add(new Data(list.get(i).getDate(), list.get(i).getBildirimSinifi(),
                            list.get(i).getGonderenSirket(), list.get(i).getBildirimKonusu(),
                            list.get(i).getOzet(), list.get(i).getIlgiliSirketler(),
                            list.get(i).getBildirimId(), list.get(i).getEk()));
                }
            }
            for(int j = 0; j < liste4.size(); j++){      //Fon Bildirim Konusu
                if(liste4.get(j).equals(list.get(i).getBildirimKonusu())){
                    list2.add(new Data(list.get(i).getDate(), list.get(i).getBildirimSinifi(),
                            list.get(i).getGonderenSirket(), list.get(i).getBildirimKonusu(),
                            list.get(i).getOzet(), list.get(i).getIlgiliSirketler(),
                            list.get(i).getBildirimId(), list.get(i).getEk()));
                }
            }
        }

        table2.setItems(list2);
        lbl2.setText("Filtrelenmiş Bildirimler - ["+list2.size()+" adet]");
    }
}