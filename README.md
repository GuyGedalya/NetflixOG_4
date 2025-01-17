# NetflixOG_3 - Exercise 3

# Instructions to compile and run the project   

## BUILD AND COMPILE THE SERVER  

The server requires ports 3000, 4000 and 27017 to be available.  
You can interact with the server using port 3000.  

Command to build and run the server: docker-compose up --build  
You should run the project from the NetflixOG_3 folder, the Docker dasktop should be open.  
The command: "docker-compose down" will stop the program. To start it again use "docker-compose up".   
 

# Here are the commands the program supports  
You can input instruction through the command line. The commands I'm showing work on linux.  In windows you should add \ before every " for example:  
A command in linux:  curl -i -X POST http://localhost:3000/api/users -H "Content-Type: application/json" -d '{ "UserName": "exampleUser", "Email": "exampleuser@example.com", "Password": "yourPassword","Phone": "+123456789" }'  

A command in windows:  curl -i -X POST http://localhost:3000/api/users -H "Content-Type: application/json" -d "{ \"UserName\": \"exampleUser\", \"Email\": \"exampleuser@example.com\", \"Password\": \"yourPassword\", \"Phone\": \"+123456789\" }"  

* Note that the .md formatting of the README doesn't show the windows command properly. To see it you should open the README in a text editor.  


## LOG IN and SIGN IN  
### Add user command  
User must have a unique Email, UserName, and Phone.  
you can also add a ProfileImage which should be a valid URL. You must also enter a Password.  

example command to create a user:  
curl -i -X POST http://localhost:3000/api/users -H "Content-Type: application/json" -d '{ "UserName": "exampleUser", "Email": "exampleuser@example.com", "Password": "yourPassword","Phone": "+123456789" }'  

![adding a user](https://github.com/user-attachments/assets/3efeee5e-6c56-4e5b-8ef4-8c3eccd20a4c)  
With a ProfileImage:  
![create user](https://github.com/user-attachments/assets/e8e9baff-4037-4eca-af32-7548b5345af3)

### Getting user's token  
Some commands require a user to be signed in. In which case, the user needs to add the user's token (ID) to the header of the request.  
To get this token, you enter the command:  
http://localhost:3000/api/tokens  
You should add the UserName and the Password to the body of the request.  

example command to get the token:  
curl -i -X POST http://localhost:3000/api/tokens -H "Content-Type: application/json" -d '{ "UserName": "exampleUser", "Password": "yourPassword" }'  

![token](https://github.com/user-attachments/assets/b8ac8948-4946-4761-a12b-2cf892aec39f)


### Getting a user information by Id  
You can send a request to get a user's information with the users id.  

Example command to get the user's info (omit the : before the id).  
curl -i -X GET http://localhost:3000/api/users/:id  

![get user by id](https://github.com/user-attachments/assets/4ecc77f8-7cbb-4362-bd2c-14e4e29c435b)


## HOME PAGE - Modifying operation require user to be signed in   
### Category commands:   
* You can see all the available categories using the command: curl -i http://localhost:3000/api/categories  

![get categories](https://github.com/user-attachments/assets/e5de5e8f-4fe9-42fb-9c32-2bee32476e36)

* You can add categories using a command like:  
curl -i -X POST http://localhost:3000/api/categories -H "Content-Type: application/json" -H "user-id: ID" -d '{"name": "some category", "promoted": true}'  
Instead of ID you write the token. Promotion should be boolean.    

This command is allowed only for signed users. You should add the token(user-id) to the header of the request.  

![create category1](https://github.com/user-attachments/assets/015ba04b-d474-45e2-bbb3-6da4a672cbc8)
![create category2](https://github.com/user-attachments/assets/9f7012e4-74bb-4592-b72d-8ace93ae5ea0)

* You can get a specific category details using the command:  
curl -i http://localhost:3000/api/categories/:id  

![get category by id](https://github.com/user-attachments/assets/60dcdb64-b8f4-4f99-8cd5-e130ef9fc0a9)

* You can update category details using the command:  
curl -i -X PATCH http://localhost:3000/api/categories/:id -H "Content-Type: application/json" -H "user-id: ID" -d '{"name": "updated category", "promoted": false}' 
   
You should add the user-id in the header for this to work. The id in the URL is the id of the category you want to patch.  

![patch category1](https://github.com/user-attachments/assets/4b957e0c-db38-4990-aca0-b84993a4d040)
![patch category2](https://github.com/user-attachments/assets/bf41f493-0e50-40b0-b87a-6989ab32e226)

* You can delete category using the command: curl -i -X DELETE http://localhost:3000/api/categories/:id -H "user-id: ID"  
  
You should add the user-id in the header for this to work. The id in the URL is the id of the category you want to delete.  

![delete category1](https://github.com/user-attachments/assets/867fe8fa-e388-4dac-87fe-d52ab6152a1f)
![delete category2](https://github.com/user-attachments/assets/bc10aa6e-f193-40b3-92ff-1c5f16dc6f27)

### Movie commands:
* You can add movies to the system using the command:  
curl -i -X POST http://localhost:3000/api/movies -H "Content-Type: application/json" -H "user-id: ID" -d '{"Title": "movie title", "ReleaseDate": "2025-05-01", "Categories": [a list of categories]}'

Instead of ID you write the user token. You must enter a Title, ReleaseDate (YYYY-MM-DD) and a list of existing category name in a string form to the Categories. A movie must have at least one category. You can also enter an Image (should be a valid URL) but it's optional.  

![adding movies](https://github.com/user-attachments/assets/724e9d87-fe56-4b6f-8d2c-43e04bca9f3b)  
With an Image:  
![ceate movie](https://github.com/user-attachments/assets/607adcef-8b1f-4fa2-a132-ae75318c5d73)

* You can get a list of random unseen movies form promoted categories. You will also get a list the 20 last movies the user watched:  
curl -i http://localhost:3000/api/movies -H "user-id: ID"  
Instead of ID you write the user token.

![get movies1](https://github.com/user-attachments/assets/754accd5-877f-4100-aa7a-823883af86c6)
![get movies2](https://github.com/user-attachments/assets/e492120a-1a07-4311-abf8-02afd6cd9f8b)
![get movies3](https://github.com/user-attachments/assets/b8ee0849-8b94-4777-a3c6-e080fb261734)
![get movies4](https://github.com/user-attachments/assets/96045235-3295-4fb4-9c70-9c351fd373dc)

* You can get a movie information given an ID using the command:  
curl -i http://localhost:3000/api/movies/:id  
Where id is the movie ID you want info about (omit the :).

![geting movie by id](https://github.com/user-attachments/assets/dce4ef1e-9531-468c-a326-baed62d5e6c0)

* You can replace movie's information given a movie ID using the command:  
curl -i -X PUT http://localhost:3000/api/movies/:id -H "Content-Type: application/json" -H "user-id: ID" -d '{"Title": "new movie title", "ReleaseDate": "2025-05-01", "Categories": [a list of categories]}'  
Instead of ID you write the user token. You must enter a Title, ReleaseDate (YYYY-MM-DD) and a list of existing category name in a string form to the Categories. The id in the URL if the id of the movie you want to replace (omit the :).  

![put movie](https://github.com/user-attachments/assets/14db22a9-9319-495d-88a2-149a3857f0c3)

* You can delete a movie form the system using the command:  
 curl -i -X DELETE http://localhost:3000/api/movies/:id -H "user-id: ID"  
 Instead of ID you write the user token. The id in the URL if the id of the movie you want to replace (omit the :).  

![delete movie](https://github.com/user-attachments/assets/0d47b607-421c-48f7-b315-c12671b99801)

* You can add movies to a user's watch list:  
curl -i -X POST http://localhost:3000/api/movies/:id/recommend -H "user-id: ID"  
Instead of ID you write the user token. The id in the URL if the id of the movie you want to add to the user's watch list (omit the :).  

![add movie to user1](https://github.com/user-attachments/assets/9647b172-f1a0-4883-a7e9-8cbe70515119)
![add movie to user2](https://github.com/user-attachments/assets/91a9ce51-b34a-47e5-9f18-5a4c01635b74)

* You can get recommendations based on the user's taste and other users given a movie:  
curl -i http://localhost:3000/api/movies/:id/recommend -H "user-id: ID"  
Instead of ID you write the user token. The id in the URL if the id of the movie you want recommendation by (omit the :).  

![recommend1](https://github.com/user-attachments/assets/a777349a-ace4-4d92-85d9-9f1b447b2d6f)
![recommend2](https://github.com/user-attachments/assets/5b426bae-0de4-4fc7-ba32-7fb655920258)

* You can search using a query:  
curl -i http://localhost:3000/api/movies/search/:query  

![query](https://github.com/user-attachments/assets/8246b5e7-b816-4857-a89f-8e057cc2383d)













