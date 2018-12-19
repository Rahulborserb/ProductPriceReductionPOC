# ProductPriceReductionPOC
It is a restful web-service which returns products that have a price reduction.

## Running the application
1) You can run a Spring Boot application from your IDE as spring boot app.

2) You can run your application using java -jar. For example, you need to change directory to the current project directory and run following command in cmd.

   #### java -jar target/ProductPriceReductionPOC-1.0-SNAPSHOT.jar

3) You can use below maven command to quickly compile and run your application.
   #### mvn spring-boot:run
 
## Sample Endpoint 
###### http://localhost:8092/v2/product
###### http://localhost:8092/v2/product?labelType=ShowPercDscount
###### http://localhost:8092/v2/product?labelType=ShowWasThenNow
 
## Special Instructions
 
1) RestAPI to obtain a list of products was not accessible in our company's network.So I took liberty to use HttpComponentsClientHttpRequestFactory for RestTemplet implementation to configure proxy, even though there are multiple flavours available for RestTemplet implementation.
 
    I have added below properties for proxy in application.properties.You have to change the proxy accordingly.
    #### jlp.proxy.host=172.21.0.17 
    #### jlp.proxy.port=8080
    #### jlp.proxy.proxy-enabled=true

2) For some products "now" price is coming as object instead of String from API, we are skipping that product in product response.

## Assumption/Presumption:
1) While accessing /v2/product endpoint of service, query param "labelType" is case sensetive. If it is passed, then it must be passed as "labelType" only otherwise it will take by default value for query param "labelType".

2) If "was" price is available and it has value "0",then we are returning 0 while calculating discount percentage otherwise it will throw Arithmatic Exception.

3) Product response may come in Json format or XML format.This will be depend on ACCEPT header.
