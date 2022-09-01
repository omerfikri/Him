package com.example.him;

import javafx.beans.property.SimpleStringProperty;

public class Data {
    private final SimpleStringProperty date;
    private final SimpleStringProperty bildirimSinifi;
    private final SimpleStringProperty gonderenSirket;
    private final SimpleStringProperty bildirimKonusu;
    private final SimpleStringProperty ozet;
    private final SimpleStringProperty ilgiliSirketler;
    private final SimpleStringProperty bildirimId;
    private final SimpleStringProperty ek;

    Data(String date, String bildirimSinifi,
         String gonderenSirket, String bildirimKonusu,
         String ozet, String ilgiliSirketler,
         String bildirimId, String ek)
    {
        this.date = new SimpleStringProperty(date);
        this.bildirimSinifi = new SimpleStringProperty(bildirimSinifi);
        this.gonderenSirket = new SimpleStringProperty(gonderenSirket);
        this.bildirimKonusu = new SimpleStringProperty(bildirimKonusu);
        this.ozet = new SimpleStringProperty(ozet);
        this.ilgiliSirketler = new SimpleStringProperty(ilgiliSirketler);
        this.bildirimId = new SimpleStringProperty(bildirimId);
        this.ek = new SimpleStringProperty(ek);

    }

    public String getDate(){
        return date.get();
    }

    public String getBildirimSinifi(){
        return bildirimSinifi.get();
    }

    public String getGonderenSirket(){
        return gonderenSirket.get();
    }

    public String getBildirimKonusu(){
        return bildirimKonusu.get();
    }

    public String getIlgiliSirketler(){
        return ilgiliSirketler.get();
    }

    public String getOzet(){
        return ozet.get();
    }

    public String getBildirimId(){
        return bildirimId.get();
    }

    public String getEk(){
        return ek.get();
    }
}
