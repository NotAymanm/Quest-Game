# 📂 Quest Game

Welcome to the **Quest Game** repository! This project features a Java-based backend application for an exciting and interactive adventure card game.

## 📋 Table of Contents

- [About the Project](#-about-the-project)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#-usage)
- [Features](#-features)
- [Contributing](#-contributing)

---

## 💡 About the Project

The Quest Game is a backend application designed to manage an engaging card-based adventure game. With a robust Java backend, it provides a foundation for gameplay mechanics, deck management, and game logic.

## 🚀 Getting Started

Follow these instructions to set up and run the project locally.

### Prerequisites

- **Java Development Kit (JDK) 17 or higher**
- **Maven** (to manage dependencies)
- **Docker** (optional, for containerized deployment)

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/notaymanm/quest-game.git
   ```

2. Build the project using Maven:

   ```bash
   cd backend
   mvn clean install -DskipTests
   ```

## 📖 Usage

### Running Locally

Run the application using the Maven wrapper:

```bash
cd backend
mvn spring-boot:run
```

Open a new terminal to run frontend:
```bash
cd frontend
npx http-server
```
### Configuring the Frontend

To ensure the frontend correctly communicates with the local backend, you'll need to update the `frontend/script.js` file:

1. **Uncomment** the line that sets the `apiBaseUrl` to your local backend URL:

   ```javascript
   const apiBaseUrl = "http://localhost:8080";
   ```

2. **Comment out** the line that refers to the environment variable:

   ```javascript
   // const apiBaseUrl = import.meta.env.VITE_API_URL;
   ```

After making these changes, you can access the application by visiting `http://localhost:8080` in your browser.

---

## ✨ Features

- **Adventure Card Mechanics**: Includes a framework for managing cards and decks.
- **Spring Boot Backend**: Built with a modern Java framework.
- **Docker Support**: Easily deployable with containerization.

---

## 🤝 Contributing

Contributions are welcome! To get started:

1. Fork the repository.
2. Create a new branch:

   ```bash
   git checkout -b feature/your-feature-name
   ```

3. Make your changes and commit:

   ```bash
   git commit -m "Add your message here"
   ```

4. Push your branch and submit a pull request.

---

Enjoy playing and developing with Quest Game! If you have any questions or suggestions, feel free to open an issue.
