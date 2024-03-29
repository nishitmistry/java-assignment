# Steps to run
1. pull the Project 
2. create Database in mysql with name shopping_app
3. change the database url according your server application.properties under resources folder
4. run ShoppingAppApplication.java SpringBoot Application
5. call Post localhost:8080/api/User to create a User
6. call Post localhost:8080/api/Inventory to create a Inventory 
7. call Post localhost:8080/api/Coupon with {"CouponCode":"<code in String>","Discount": <discount in double> }
8. you can test other apis spcified in the assignment
