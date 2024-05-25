# Online Quiz App

This is an Online Quiz Application with a frontend built using HTML, CSS, and JavaScript, and a backend developed using Java SDK 22. The app utilizes MySQL for database management, with the MySQL Connector and JSON libraries for database connectivity and data handling.

## Features

- **Topic Selection:** Users can choose from a list of quiz topics.
- **Difficulty Levels:** Each topic has multiple levels of difficulty.
- **Quiz Interface:** Users are presented with questions one by one, with options for each question.
- **Navigation:** Users can navigate between questions using "Previous," "Next," and question number buttons.
- **Answered Question Indicators:** Answered questions have a dark blue border around the question number buttons.
- **Timer:** Each question has a timer. Once the timer runs out, the options are disabled.
- **Submit Answers:** Users can submit their answers by pressing the "Finish" button.
- **Results:** After submission, users see their results, including the number of attempted questions and correct answers.
  - Correct answers are highlighted in green.
  - Incorrect selections are highlighted in red, with correct answers shown in green.
  - Feedback text indicates whether the answer was correct or wrong.
  - Question button borders change color to indicate correctness (red for incorrect, green for correct).
- **Retry or Home:** Users can retry the quiz or return to the home page to select a new quiz.

## Folder Structure

```plaintext
online-quiz-app/
├───backend
│   ├───.vscode
│   ├───bin
│   │   ├───config
│   │   ├───dao
│   │   ├───handler
│   │   ├───models
│   │   └───service
│   ├───lib
│   └───src
│       ├───config
│       ├───dao
│       ├───handler
│       └───service
├───database
└───frontend
    ├───components
    ├───css
    ├───images
    └───js
```

## Setup Instructions

### Prerequisites

- Java SDK 22
- MySQL server
- MySQL Connector for Java
- JSON library for Java

### Steps

#### Clone the repository:

```bash
git clone <repository-url>
cd online-quiz-app
```

## Set up the database:

1. Import the `quizapptable.sql` file into your MySQL server.
2. Alternatively, you can add sample data by importing `quizapptablesampledata.sql`.

## Configure the backend:

- Update the database connection details in `backend/src/config/DatabaseConfig.java`.

## Build and run the backend:

```bash
cd backend
```
 - Start `App.java`

## Serve the frontend:

You can use a local server like `http-server` for serving the HTML files.

```bash
cd frontend
```
- Start web server


## Open the application:

- Open your browser and navigate to `http://localhost:<port>`.

## Usage

1. Open the web application.
2. Select a topic from the list.
3. Choose the difficulty level.
4. Answer the questions within the time limit.
5. Submit your answers to view the results.
6. Retry the quiz or return to the home page for a new quiz.

## Contributing

If you wish to contribute to the project, feel free to fork the repository and submit pull requests.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

