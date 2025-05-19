Merhaba 

Bu proje Spring Boot framework kullanılarak Java Dilinde yazılmıştır.

Projedeki Java Compiler versiyonu 21.0.6 dır.
Proje içerisinde lombok kütüphaneleri kullanılmıştır bu yüzden projeyi çalıştırmadan önce herhangi bir problem ile karşılaşmamak için lombok kurulumu önerilir.

Projede database olarak H2 kullanılmıştır ve doğrudan proje dosyalarına eklenmiştir bu yüzden herhangi bir path tanımı yapılamasına gerek yoktur.

Projedeki güvenlik yapısı login ve rol bazlı oluşturulmuştur, böylece kullanıcılar ilk oluşturuldukları anda yetki sınırları belirlenir.

Projenin başlatılmasıyla birlikte 3 yetki grubu (Admin , Employee, Customer) otomatik olarak oluşturulur. Buna ek olarak ilk ayağa kaldırma işlemiyle beraber projedeki tam yetkili admin kullanıcısı bir defaya mahsus yaratılır.

adminTckn: 12345678910
adminPass:1234

Projede statik olarak database parolası ve adı da oluşturulur.

Username: test
Pass: test123


Projenin db paneline ulaşmak için : http://localhost:8080/h2-console


Projenin login sayfasına ulaşmak için : http://localhost:8080/login


İlk login işleminden sonra admin ana sayfasına yöneliyoruz. Bunun ardından yaratılan her kullanıcı kendi yetki alanına uygun ana sayfalara yönlendiriliyor.

Admin ve Employee kullanıcıları customer kullanıcılarının yaptığı tüm işlemleri kendi panellerinde kullancılar üzerinde uygulayabilirler.

Kullanıcılar sadece kendi sayfaları üzerinden , kendilerine ait cüzdanlar yaratmak, yaratılar cüzdanlardan para gönderme-alma ve buna bağlı oluşan işlemleri görüntüleme yetkilerine sahiptirler.
Ayrıca bazı durumlarda kullanıcılar yaptıkları onaya takılan işlemlerin sonuçlanması için de çalışan tarafından yapılacak işlemi beklemek zorundadılar buna yetkileri yoktur.

Kullancılar kendilerine ait olmayan cüzdanlar üzerinen işlem yapmaya çalıştıkları takdirde bu yetiksiz işlemi de gerçekleştiremeyeceklerdir.

Çalışanlar bütün kullanıcı hesapları üzerinde tam yetkiye sahiptir ve yeni kullanıcı yaratma işlemi de yalnızca çalışanlar tarafından sağlanır.(Admin çalışan yetkilerini de içerdiği için o da bu işleme yetkilidir)
