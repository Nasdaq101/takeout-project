Takeout Project
#
The project is an online ordering system (for food, flowers, daily necessities, etc.) developed using the Spring Boot framework. The system is divided into two main components: the admin portal and the user interface. Key features include menu management, order processing, employee management, and functionalities for browsing products, placing orders, and making payments.
#
Technology Stack

Backend:

	•	Spring Boot
	•	Spring Task
	•	Spring Cache
	•	MyBatis
    •	HttpClient
    •	WebSocket
    •	JWT
    •	Nginx
	•	Lombok

Database

	•	MySQL
	•	Redis  

Frontend:

	•	Vue
    •	Wechat-mini
	•	Uniapp
	•	ElementUI
    •	EChart

API:

	•	RESTful API
#
Development Environment Setup

1. Install Java JDK and configure the environment variables.


2. Install MySQL and Redis databases and create the corresponding databases.
   #Create the MySQL database and tables: execute the mysql.sql file.


3. Install Maven build tool.


4. Install and configure Nginx with the following setup:
#
    Add the following configuration under the "http" section
    
    map $http_upgrade $connection_upgrade {
    default upgrade;
    '' close;
    }
    
    upstream webservers {
    server 127.0.0.1:8081 weight=50;
    #server 127.0.0.1:8088 weight=50;
    }
    
    server {
    listen 80;
    server_name localhost;

    location / {
        root html/sky;
        index index.html index.htm;
    }

    # Reverse proxy for admin requests
    location /api/ {
        proxy_pass http://localhost:8081/admin/;
        #proxy_pass http://webservers/admin/;
    }

    # Reverse proxy for user requests
    location /user/ {
        proxy_pass http://webservers/user/;
    }

    # WebSocket
    location /ws/ {
        proxy_pass http://webservers/ws/;
        proxy_http_version 1.1;
        proxy_read_timeout 3600s;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "$connection_upgrade";
    }

    location /media {
        root configure_media_file_location; # e.g.: D:/static
        # Note: Create a media folder under the D:/static directory
    }
    }

5. Clone the project locally: git clone https://github.com/Nasdaq101/sky-take-out.git


6. Modify the configuration file application.yml:
#
    spring:
        datasource:
            url: jdbc:mysql://url
            username: root
            password: database_password
        data:
            redis:
                password: redis_database_password

7. Create a new file named application-env.yml in the resources directory, and add the following configuration:
#
    sky:
        wechat:
            appid: obtain_from_wechat_miniprogram_application
            secret: obtain_from_wechat_miniprogram_application
            mchid: merchant_id
            mchSerialNo:
            privateKeyFilePath:
            apiV3Key:
            weChatPayCertFilePath:
            notifyUrl:
            refundNotifyUrl:

8. Run the Project

   Once the configuration is complete, you can start the project.
	