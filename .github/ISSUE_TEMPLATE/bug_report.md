---
name: Report a bug
about: Report an issue to help us enhance the project.
title: "[BUG] Brief description of the issue"
labels: bug
assignees: ""
---

# Description

A clear and concise description of what the bug is.

# Steps to Reproduce

1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

# Expected Behavior

A clear and concise description of what you expected to happen.

# Screenshots

If applicable, add screenshots to help explain your problem.

# Environment

- OS: [e.g. Windows, macOS, Linux]
- Java version: [e.g. 8, 11, 16]
- Burp Suite version: [e.g. Professional 2021.4, Community 2021.4]
- GPT model used: [e.g. GPT-3, GPT-Neo, GPT-2]
- Maximum tokens used: [e.g. 2048]
- Prompt used: [e.g. "Analyze the following HTTP request and response for potential vulnerabilities..."]

# HTTP Request/Response Info

- Request:

  ```http
  POST /api/create-user HTTP/1.1
  Host: www.example.com
  Content-Type: application/json
  User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.82 Safari/537.36
  Accept: application/json
  Content-Length: 81
  Connection: keep-alive

  {
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "secure123"
  }
  ```

- Response:

  ```http
  HTTP/1.1 201 Created
  Date: Sun, 09 Apr 2023 12:00:00 GMT
  Server: Apache/2.4.41 (Ubuntu)
  Content-Type: application/json
  Content-Length: 112
  Connection: keep-alive

  {
  "status": "success",
  "message": "User created successfully.",
  "data": {
      "id": 12345,
      "username": "johndoe",
      "email": "john.doe@example.com"
  }
  }
  ```

# Error Message

Please provide the error message from the following locations:

1. Navigate to the `Dashboard` -> `Event Log` to see if there are any relevant error messages available.
2. Additionally, go to `Extensions` -> `Installed` -> `BurpGPT`, and check the `Output` and `Error` tabs for any error messages.

# Additional Context

Add any other context about the problem here.
