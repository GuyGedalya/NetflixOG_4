# NetflixOG_4 - Exercise 4

# Instructions to compile and run the project  

To run and compile the project, run: docker-compose up --build -d  
You should run in from the main folder of the project (where the file "docker-compose.yml" is).  

Once the project is up, you can access the website at "localhost:3000" in the URL line.  
![url](https://github.com/user-attachments/assets/c1f8de2b-74fc-4b53-98e4-823f7d0ce9c9)  

To access the andriod app, you need to go the a file called ApiClient and change the IP in lines 6 and 7 to your IP.  
The file is in NOG_Android/app/src/main/java/com/example/nog/connectionClasses/ApiClient.java  
![ip](https://github.com/user-attachments/assets/b98b3a55-0a75-4d54-95a0-717b96398075)  

You Can find your IP in your Network properties  
![ip2](https://github.com/user-attachments/assets/67d0fd34-1d39-4604-a2c5-86057cab4eea)  

After that you can enter the NOG_Android folder and run the project with the play button.  

## Describing the project  

Work Process on Assignment No. 4  
When we received Assignment No. 4, we decided to start early. We divided it into two main parts: Web and Android, ensuring that both of us worked on each part to gain experience with all technologies.  

Web:  
Development was split into frontend design and backend integration with the server from previous assignments. Each of us worked on both aspects—design and connection to the backend. We created three main application pages, login/registration, and a maintenance page.  

We learned about UI design, server-side integration, and merging everything. For example, displaying movies on the site was a frontend task, while retrieving movie data required backend integration. We aimed for experience in both areas.  

Android:  
After completing the web part, we had a clear picture of how to approach Android. The main difference was the use of XML for UI and Java for functionality, whereas in the web version, Node.js files also handled structure.  

We developed pages equivalent to the web ones but adapted for mobile:  

A side menu instead of a top menu.  
A scrollable movie list instead of a grid layout.  
A simpler, cleaner design for the app.  

Challenges:  

Connecting the app to a remote server – Unlike the web, where the server was local, we had to configure port forwarding and an API for external communication.  
Implementing MVVM architecture – Unlike the MVC-based web app, here we built the architecture from scratch, learning LiveData and repository structures.  
Due to the interconnected nature of the work, we updated each other frequently. Finally, we documented all functionalities of both the web and mobile applications.  















