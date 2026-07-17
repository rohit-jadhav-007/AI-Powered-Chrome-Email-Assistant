# AI-Powered Chrome Email Assistant

A Chrome extension that generates context-aware Gmail replies in one click, powered by a Spring Boot backend and Google's Gemini API.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-brightgreen)
![Manifest V3](https://img.shields.io/badge/Chrome%20Extension-Manifest%20V3-blue)
![Gemini API](https://img.shields.io/badge/AI-Google%20Gemini-8E44AD)

## Overview

AI-Powered Chrome Email Assistant adds an **AI Reply** button directly into Gmail's compose toolbar. When clicked, it reads the current email thread, sends it to a Spring Boot backend, and generates a context-aware reply using Google's Gemini API — inserting the result straight into the compose box.

The project has two parts:
- **`email-writer-ext/`** — a Chrome Extension (Manifest V3) that injects the button and handles the Gmail UI
- **`emialWriter-sb/`** — a Spring Boot REST API that talks to the Gemini API and returns the generated reply

## Features

- One-click AI-generated replies inside Gmail's native compose window
- Context-aware responses generated from the original email thread
- Automatic detection of Gmail's dynamically loaded compose toolbar via `MutationObserver`
- Clean separation between frontend (extension) and backend (API), communicating over REST
- API key kept server-side, never exposed to the browser

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | JavaScript, Chrome Extension (Manifest V3), MutationObserver API |
| Backend | Java 17, Spring Boot 3.5, Spring WebFlux (WebClient) |
| AI | Google Gemini API (`gemini-2.5-flash`) |
| JSON Parsing | Jackson |

## How It Works

1. The content script watches the Gmail DOM for the compose/reply toolbar.
2. Once detected, it injects an **AI Reply** button into the toolbar.
3. On click, the extension extracts the visible email thread text and sends a `POST` request to the backend at `/api/email/generate`.
4. The backend builds a prompt from the email content and tone, then calls the Gemini API via a reactive `WebClient`.
5. The generated reply is parsed out of Gemini's response and returned as plain text.
6. The extension inserts that text directly into Gmail's compose box.

## Project Structure

```
AI-Powered-Chrome-Email-Assistant/
├── email-writer-ext/                 # Chrome extension (frontend)
│   ├── manifest.json
│   ├── content-script.js
│   └── content.css
└── emialWriter-sb/                   # Spring Boot backend
    ├── src/main/java/com/email/writer/
    │   ├── EmialWriterSbApplication.java
    │   └── App/
    │       ├── EmailGeneretorController.java
    │       ├── EmailGeneratorService.java
    │       └── EmailRequest.java
    ├── src/main/resources/application.properties
    └── pom.xml
```

## Prerequisites

- Java 17+
- Google Chrome
- A [Google Gemini API key](https://aistudio.google.com/app/apikey)
- Maven is not required — the project includes the `mvnw` wrapper

## Setup & Installation

### 1. Clone the repository
```bash
git clone https://github.com/rohit-jadhav-007/AI-Powered-Chrome-Email-Assistant.git
cd AI-Powered-Chrome-Email-Assistant
```

### 2. Configure your Gemini API key
Open `emialWriter-sb/src/main/resources/application.properties` and set your key:
```properties
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=
gemini.api.key=YOUR_GEMINI_API_KEY
```
> **Don't commit real API keys.** For a public repo, load `gemini.api.key` from an environment variable instead of hardcoding it.

### 3. Run the backend
```bash
cd emialWriter-sb
./mvnw spring-boot:run
```
The API starts on `http://localhost:8080`.

### 4. Load the Chrome extension
1. Open `chrome://extensions` in Chrome
2. Enable **Developer mode** (top right)
3. Click **Load unpacked**
4. Select the `email-writer-ext` folder
5. Open [Gmail](https://mail.google.com) — the extension is now active

## Usage

1. Open Gmail and click **Reply** on any email
2. Click the **AI Reply** button in the compose toolbar
3. Wait a moment while the reply is generated
4. Review and edit the generated text before sending

## API Reference

**POST** `/api/email/generate`

Request body:
```json
{
  "emailContent": "The original email thread text",
  "tone": "professional"
}
```

Response: `200 OK` with the generated reply as plain text.

## Limitations & Future Improvements

- Backend URL is currently hardcoded to `localhost:8080`; making it configurable would ease deployment.
- Reply tone is fixed to `"professional"` in the extension UI, though the backend already accepts any tone string — a tone selector is a natural next step.
- Email content extraction relies on specific Gmail DOM selectors, which may need updates if Gmail changes its markup.
- API key is currently read from `application.properties`; environment-variable based config is recommended before making the repo public.

## Author

**Rohit Jadhav**
[LinkedIn](https://www.linkedin.com/in/rohit-jadhav-0000cr7/) · [GitHub](https://github.com/rohit-jadhav-007/)
