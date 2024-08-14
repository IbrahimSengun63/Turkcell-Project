# Proje genel bakış


---

### Uygulama fonksiyonel özellikleri

- **Kullanıcı İşlemleri**

- **Paket İşlemleri**

- **İndirim İşlemleri**

- **Satın Alma ve İade Hareketleri**

- **Ürün Değerlendirme**




# Kullanılan mimari ve teknolojiler

---

#### Mimari
    - Monolithic Architecture

#### Tools
    - Docker
    - Spring Boot
    - PostgreSQL
    - Postman
    - Swagger
    
#### Packages
    - Maven
    - Hazel Cast
    - Spring JPA
    - Spring Validation
    - Lombok
    - Map Struct
    - Mockito
    - JUnit 5
    - JaCoCo
    - Slf4j




# Veri tabanı

---

![veri tabanı](images/db/db.png)

# Hata yönetimi

#### Validasyon hataları

![validation_sample_error](images/exception/validation_sample.png)

![validation_error_management](images/exception/validation_exception_management.png)

#### İş katmanı hataları

![business_error_sample](images/exception/business_exception_sample.png)

![Business_error_management](images/exception/business-exception-management.png)

# Kullanıcı servisi

---

#### Endpoint

![user_api](images/user_service/api.png)

#### DTOs

![user_api_dtos](images/user_service/dtos.png)

# Paket servisi

---

#### Endpoint

![offer_api](images/offer_service/api.png)

#### DTOs

![offer_api_dtos](images/offer_service/dtos.png)

# İndirim servisi

---

#### Endpoint

![discount_api](images/discount_service/api.png)

#### DTOs

![discount_api_dtos](images/discount_service/dtos.png)

#### Özel sorgular

![discount_jpa_queries](images/discount_service/special_queries.png)

# İşlem servisi

---

#### Endpoint

![transaction_api](images/transaction_service/api.png)

#### DTOs

![transaction_api_dtos](images/transaction_service/dtos.png)

#### Özel sorgular

![transaction_jpa_queries](images/transaction_service/special_queries.png)


# Yorum servisi

---

#### Endpoint

![review_api](images/review_service/api.png)

#### DTOs

![review_api_dtos](images/review_service/dtos.png)

#### Özel sorgular

![review_jpa_queries](images/review_service/special_queries.png)


# Testler

---

![general_test](images/test/test_overview.png)

![service_test](images/test/test_service.png)

![api_test](images/test/test-api.png)

