# Haber İzleme Modülü(HİM)

Haber İzleme Modülü, Kamuyu Aydınlatma Platformu(KAP)'nda yayınlanan duyuruları gösteren bir masaüstü uygulamasıdır.

## Bağımlıklar
  - Java SDK v17.0.4
  - java.sql v3.36.0
  - json.simple v1.1.1
  - java.desktop
  - javafx.fxml
  - javafx.controls
  - org.xerial.sqlitejdbc

  Bağımlılıklar önce projenin pom.xml sayfasında kütüphane olarak eklenir. Daha sonrasında module-info.java dosyasına requires olarak eklenir.
  Bunların hepsi projede içermektedir kütüphaneler indirilmemiş ise pom.xml'den indirebilirsiniz.

## Class yapıları
  - MainApplication,
    projeyi çalıştırıp ayağa kaldıran sınıftır.
  - MainController,
    ana sayfada görüntü işlemleri ve butonların işlevlerini düzenlenen sınıftır.
  - Data,
    apiden gelen resource modeli.
  - filterController,
    filtreleme işlemlerinin yapıldığı sınıftır.
  - SQLiteJDBC,
    filtreleme seçeneklerinin veritabanı işlemlerini yapan sınıftır.
  - notificationController,
    yeni duyuru geldiğinde bildirim işlmelerini yapan sınıftır.
  - aboutController,
    menü kısmındaki hakkında sayfasının işlemlerini yapan sınıftır.
  - confirmController,
    çıkış yapılmak istendiğinde kullanıcı karşısına çıkan ekranın işlevlerini yerine getiren sınıftır.
  - confirm1Controller,
    aynı filtre ismi ile kaydedilmek istendiğinde hata mesajını vermeyi sağlayan sınıftır.
  
  
  
  
  
  
  
  
 
