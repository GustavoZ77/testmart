Testmart

Execute application and use the REST api 

build the project

./gradlew build

run the application 

./gradlew bootRun

endpoint to test the application by rest/web

http://localhost:8080/testmart/products/lowrating/{rating} -> returns the title of products down of the specified rating 

http://localhost:8080/testmart/cart/highest -> return the carts with the highest total

http://localhost:8080/testmart/cart/lowest -> return the carts with the lowest total

http://localhost:8080/testmart/cart/{userId} -> return the cart of the specified user and add images

execute test 

./gradlew test
