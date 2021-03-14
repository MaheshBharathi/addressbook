#### Address Book API

 **Assumptions:**

    - Address book will have only one contact with a given name.
    - Multiple contacts can have contact number.
    - Only contact name is compared to determine unique contacts(case sensitive)
    - Not much emphasis on defining valid phone no format. Accepts numbers, space and and hyphens
    - Log file available at <project_root_dir>/logs/address-book.log
    - H2 databse datafile created at <project_root_dir>/data
 
 ----
 #### Local Setup/Development
 
 **Technologies use:** Java 8, Spring boot 2.4, H2 database, JPA, Maven 3
 
 **Swagger UI :** http://localhost:8080/addressbook/swagger-ui.html
   
 Steps to run locally or setup.
 
 1. compile and build address book application
    ```mvn clean install```
    
 2. Run com.pwc.addressbook.AddressBookApplication class to start the application.
 
 3. The application is configured to runs on port 8080. If port 8080 is not available, please update port in application.yml file.
 
 4. Hit the API endpoints from swagger UI or REST clients such as postman.
 
 5. Get all contacts API accepts a query parameter "sortOrder". By default contacts are returned in ascending order. Set sort order to desc to sort the contacts in descending order.(http://localhost:8080/addressbook/contacts?sortOrder=desc)
  
 
    
    