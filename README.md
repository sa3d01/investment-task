# FinFlx Technical Assessment

Investment service using Spring Boot, Swagger API, nginx, and Docker. The service provide endpoints to create an investment account and create investment orders. 

## Technologies Used

- Java 11
- Spring Boot 2.7.13
- MySQL Database
- Nginx
- Swagger for API documentation

## Getting Started

To run the project, make sure you have the following prerequisites installed on your system:

- Java Development Kit (JDK) 11
- MySQL Database

### Step 1: Clone the Repository

Clone this repository to your local machine using the following command:

```bash
git clone https://github.com/sa3d01/investment-task.git
```

### Step 2: Build and Run the Project

### (Set Up the .env)

Run the following commands in order in the project directory

```bash
cp .env.example .env
```
and update .env values to be suit with your environment.

Run the following command to build and start the project using Docker Compose:

```bash
docker-compose up -d --build
```
This will create Docker containers for the application and the MySQL database. The application will start, and you can access the APIs and Swagger documentation at `http://localhost:70/swagger-ui.html`.

### Step 3: Explore the APIs

You can explore and test the APIs using the Swagger documentation. Navigate to `http://localhost:70/swagger-ui.html` in your web browser.
This will display the available endpoints and allow you to interact with them throw API gateway.

## Contact

For any questions or feedback, please feel free to contact us at sa3dsalem01@gmail.com.