# Mental User

# Dependency
- Postgresql

### Run postgresql with docker
docker run --name cont_postgresql -itd -p 5432:5432 --restart always -e DB_NAME=user-dev,user-qa -e DB_USER=dbuser -e DB_PASS=12345 sameersbn/postgresql:9.6-2

>Note: Locale makinenizi hem developer hem de qa makinesi olarak düşünebilirsiniz.
Qa makineniz varsa user-qa databasei oluşturmanıza gerek yok.

#### Environment
3 tane environmentimiz vardır. 
- [Default](../user/src/main/resources/config/application-default.yml)
- [Quality Assurance](../user/src/main/resources/config/application-qa.yml)
- [Production](../user/src/main/resources/config/application-prod.yml)

# Build
Gradle build işlemi için

<code>gradlew build</code>

# Run

#### Gradle üzerinden run etmek için

<code>gradlew bootRun</code>

###### Environment
İstediğiniz environment için komutun sonuna -Dspring.profiles.active={profile-name} ekleyin.

Örneğin

<code>gradlew bootRun -Dspring.profiles.active=qa</code>


#### Java üzerinden run etmek için

<code>java -jar build/libs/user.jar</code>

###### Environment
İstediğiniz environment için komutun sonuna --spring.profiles.active={profile-name} ekleyin.

Örneğin

<code>java -jar build/libs/user.jar --spring.profiles.active=qa</code>

# Docker

## Projeyi docker komutundan çalıştırma
>Docker run ile çalıştırırken dependent olan containerları ayağa kaldırmalısınız.

<code>docker run -it --name cont_user -p 8080:10050 --link cont_postgresql mental/user</code>

# Docker compose dan çalıştırma
> Compose ile çalıştırırken dependent olan başka containerlar var ise durdurunuz. Yoksa port çakışması olur.

<code>docker-compose up</code>


## Up yaparken build etmek için
<code>docker-compose up --build</code>


## Build docker
> Imagei değiştirmek isterseniz.

Projeyi gradle ile build ettikten sonra docker 
imageini oluşturmak için docker build etmelisiniz.

<code>docker build -t mental/user .</code>


# Browse

[http://localhost:10050/user](http://localhost:10050/user)

