# Miro Widget API

### Summary
This is a small API where basic CRUD operations can be done through Widgets.
Two different repository implementation exist in the service:
 * ConcurrentHashMap
 * H2

Operation results of widgets are same, they do not depend on the storage type.

This app is written with Java 11 & SpringBoot framework.
 
### Build the app
    $ mvn clean install


### Run the app with 2 different profiles
This will use ConcurrentHashMap to store widgets:

    $ mvn spring-boot:run -Dspring-boot.run.profiles=in-memory

This will use H2 in memory DB to store widgets:
    
    $ mvn spring-boot:run -Dspring-boot.run.profiles=database

If you would like to query data in H2, go to http://localhost:8080/h2-console and use these credentials to login:

    username: sa
    password:
    
As default, the application will start at port 8080.

### Functionalities of API on widgets
* **POST** - **/v1/widgets** -> Create a brand new widget. Mandatory fields: **xIndex**, **yIndex**, **height**, **weight**
* **PUT** - **/v1/widgets** -> Update an existing widget. Mandatory fields: **id**, **xIndex**, **yIndex**, **height**, **weight**
* **DELETE** - **/v1/widgets/{id}** -> Delete an existing widget
* **GET** - **/v1/widgets/{id}** -> Get a widget by id
* **GET** - **/v1/widgets** -> Get widgets. Optional request parameters are **limit**, **x0**, **y0**, **x1**, **y0**. Limit should be between [1-500], **default limit = 10** 
**[x0,y0]** & **[x1,y1]** represents the coordinates(two points) to filter which widgets are located in it.