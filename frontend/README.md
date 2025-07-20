# React Application Setup

This guide walks you through setting up and running a React application locally using npm.

## Prerequisites

Before you begin, ensure you have the following requirements met:

- **Node** React requires Node.js and npm (Node Package Manager) to be installed on your system. You can verify by running the following command:
```shell
node -v
npm -v
```
If not installed, download Node.js from here, and it will automatically install npm along with it [Node](https://nodejs.org/en/download/package-manager)

## Getting Started

## 1. Configure Environments

In the frontend directory, you'll need to create `.env` file for the application configuration.

You can start by copying the provided `.env.example` file:
```shell
cp .env.example .env
```

- Make sure to customize values in the `.env.example` file to suit your local setup.

## 2. Install packages

```shell
npm i
```

## 3. Run the backend application

Make sure you have running backend application. In the `backend` folder there is detailed instruction of how to run the app.

## 4. Run the frontend application
To run the React application, use the following Maven command:
```
npm run dev
```

## 5. Usage

The app by default should start at http://localhost:3000.

## Additional Notes

### Used Technologies
- React
- TypeScript
- Vite
- Tailwind
