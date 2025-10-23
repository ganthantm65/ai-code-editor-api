# AI Code Assistant & Compiler Service

This project provides a backend system for AI-assisted code operations and code execution. It includes endpoints for **explaining, fixing, optimizing code**, and **running code** in multiple programming languages using Docker-based isolated execution.

---

## Features

1. **AI Code Services**

   * **Explain Code**: Provides line-by-line explanations of code with time and space complexity.
   * **Fix Code**: Fixes code based on given error messages and returns corrected code.
   * **Optimize Code**: Optimizes code for performance and readability with explanations.

2. **Code Execution Service**

   * Supports multiple languages: **Python, Java, C++, JavaScript**.
   * Executes code safely inside **Docker containers**.
   * Provides output, errors, and handles execution timeouts.

3. **Security**

   * Docker isolation for code execution.
   * Limited CPU and memory per container.
   * No network access for running code to prevent security risks.

---

## Technologies

* **Backend Framework**: Spring Boot
* **AI Integration**: Google Gemini API
* **Language Support**: Python, Java, C++, JavaScript
* **Containerization**: Docker
* **JSON Parsing**: Jackson (`ObjectMapper`)
* **HTTP Client**: Java `HttpClient`

---

## Project Structure

```
src/main/java/com/example/server/
├─ services/
│  ├─ AiService.java          # Handles AI code explain, fix, optimize
│  ├─ CompilerService.java    # Handles code execution in Docker containers
├─ controller/
│  ├─ AiController.java       # Exposes AI endpoints: /ai/explain, /ai/fix, /ai/optimize
│  ├─ CompilerController.java # Exposes code execution endpoint: /code/run
├─ Model/
│  ├─ RequestModel.java       # Input for code execution
│  ├─ ResponseModel.java      # Output of code execution
```

---

## API Endpoints

### AI Services

#### 1. Explain Code

* **URL**: `/ai/explain`
* **Method**: POST
* **Request Body**:

```json
{
  "code": "public class Main { ... }",
  "language": "java"
}
```

* **Response**:

```json
{
  "explanation": "Line by line explanation with time and space complexity..."
}
```

---

#### 2. Fix Code

* **URL**: `/ai/fix`
* **Method**: POST
* **Request Body**:

```json
{
  "code": "public class Main { ... }",
  "output": "Compilation error message",
  "language": "java"
}
```

* **Response**:

```json
{
  "fixedCode": "Corrected Java code here"
}
```

---

#### 3. Optimize Code

* **URL**: `/ai/optimize`
* **Method**: POST
* **Request Body**:

```json
{
  "code": "public class Main { ... }",
  "language": "java"
}
```

* **Response**:

```json
{
  "optimizedCode": "Optimized code with explanation and complexities"
}
```

---

### Code Execution

#### Run Code

* **URL**: `/code/run?language=<language>`
* **Method**: POST
* **Request Body**:

```json
{
  "code": "print('Hello World')"
}
```

* **Response**:

```json
{
  "output": "Hello World\n",
  "error": null
}
```

* **Supported Languages**: `python`, `java`, `cpp`, `js`
* **Timeout**: 10 seconds per execution
* **Resource Limits**: 1 CPU, 512MB RAM

---

## Environment Variables

* `GEMINI_API_KEY` – API key for Google Gemini AI.

---

## Setup & Run

1. **Clone the repository**

```bash
git clone <repository_url>
cd <repository_directory>
```

2. **Set environment variable**

```bash
export GEMINI_API_KEY="your_api_key_here"
```

3. **Build and run Spring Boot application**

```bash
./mvnw clean install
./mvnw spring-boot:run
```

4. **Ensure Docker is running** for code execution.

---

## Notes

* AI services rely on Google Gemini API; ensure the key is valid.
* Code execution is isolated in Docker; any unsupported language will return an error.
* Outputs include both standard output and error streams.

---

## Example Usage (Curl)

**Explain Code:**

```bash
curl -X POST http://localhost:8080/ai/explain \
-H "Content-Type: application/json" \
-d '{"code": "print(\"Hello World\")", "language": "python"}'
```

**Run Code:**

```bash
curl -X POST "http://localhost:8080/code/run?language=python" \
-H "Content-Type: application/json" \
-d '{"code": "print(\"Hello World\")"}'
```

---


