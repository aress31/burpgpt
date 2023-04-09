# burpgpt

[![Java CI with Gradle](https://github.com/aress31/burpgpt/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/aress31/burpgpt/actions/workflows/gradle-build.yml)

`burpgpt` leverages the power of `AI` to detect security vulnerabilities that traditional scanners might miss. It sends web traffic to an `OpenAI` `model` specified by the user, enabling sophisticated analysis within the passive scanner. This extension offers customisable `prompts` that enable tailored web traffic analysis to meet the specific needs of each user. Check out the [Example Use Cases](#example-use-cases) section for inspiration.

The extension generates an automated security report that summarises potential security issues based on the user's `prompt` and real-time data from `Burp`-issued requests. By leveraging `AI` and natural language processing, the extension streamlines the security assessment process and provides security professionals with a higher-level overview of the scanned application or endpoint. This enables them to more easily identify potential security issues and prioritise their analysis, while also covering a larger potential attack surface.

> [!WARNING]
> Data traffic is sent to `OpenAI` for analysis. If you have concerns about this or are using the extension for security-critical applications, it is important to carefully consider this and review [OpenAI's Privacy Policy](https://openai.com/policies/privacy-policy) for further information.

> [!WARNING]
> While the report is automated, it still requires triaging and post-processing by security professionals, as it may contain false positives.

> [!WARNING]
> The effectiveness of this extension is heavily reliant on the [quality and precision of the prompts](#prompt-configuration) created by the user for the `GPT` model. This targeted approach will help ensure the `GPT model` generates accurate and valuable results for your security analysis.

## Features

- Provides an additional passive scan check, enabling users to submit `HTTP` request and response data to an `OpenAI`-controlled `GPT` `model` for security analysis, through the use of a placeholder system.
- Empowers users to customise `prompts` and unleash limitless possibilities for interacting with `OpenAI models`. Browse through the [Example Use Cases](#example-use-cases) for inspiration.
- Leverages the power of `OpenAI's GPT models` to detect potential security vulnerabilities in the scanned application.
- Allows the user to select the most suitable `OpenAI` `model` from the available options.
- Offers easy `API key` rotation to provide greater control over billing and usage.
- Integrates seamlessly with `Burp Suite`, allowing for easy and transparent use once configured. It also displays the analysis results directly within the `Burp UI`, enabling efficient post-processing of the scan results.

## Installation

### 1. Compilation

1. Ensure you have [Gradle](https://gradle.org/) installed and configured.

2. Download the `burpgpt` repository:

   ```bash
   git clone https://github.com/aress31/burpgpt
   cd .\burpgpt\
   ```

3. Build the standalone `jar`:

   ```bash
   gradle shadowJar
   ```

### 2. Loading the Extension Into `Burp Suite`

To install `burpgpt` in `Burp Suite`, first go to the `Extendensions` tab and click on the `Add` button. Then, select the `burpgpt-all` jar file located in the `.\lib\build\libs` folder to load the extension.

# Usage

To start using `burpgpt`, the user must first complete the following steps in the `Settings` panel accessible from the `Burp Suite` menu bar:

1. enter their `OpenAI API key`,
2. select a `model`,
3. and adjust or [create custom prompts](#2-prompt-configuration) as desired.

<img src="https://user-images.githubusercontent.com/11601622/230774262-9e7008ac-68e2-49a8-9c41-7aa1139198a3.png" alt="burpgpt UI" width="50%" height="50%">

After configuring the extension with the appropriate `API key`, `model`, and `prompt`, all passively scanned items will be analysed by the selected `OpenAI model` based on the configured settings. The results of the analysis will be displayed on a per-endpoint basis as an `Informational`-level finding.

<img src="https://user-images.githubusercontent.com/11601622/230777816-9f4c1e16-646f-4581-935f-e341f1323493.jpg" alt="burpgpt finding" width="75%" height="75%">

## Prompt Configuration

`burpgpt` allows users to customise the `prompt` for traffic-based analysis by using a system of `placeholders`. We recommend including the maximum relevant information in the prompt. The following `placeholders` are directly handled by the extension and can be used to dynamically insert specific values into the prompt:

- `{IS_TRUNCATED_PROMPT}` - A `boolean` value that indicates whether the `prompt` has been truncated to fit within the `1024 character` limit imposed by most `GPT-3.5` models' `maxTokens` value. This value is programmatically set by the extenstion.
- `{URL}` - The URL of the scanned request.
- `{METHOD}` - The HTTP request method used in the scanned request.
- `{REQUEST_HEADERS}` - The headers of the scanned request.
- `{REQUEST_BODY}` - The body of the scanned request.
- `{RESPONSE_HEADERS}` - The headers of the scanned response.
- `{RESPONSE_BODY}` - The body of the scanned response.

These `placeholders` can be used in the custom `prompt` to dynamically generate a request/response analysis `prompt` that is specific to the scanned request.

> [!NOTE] > `Burp Suite` provides the capability to support arbitrary `placeholders` through the use of [Session handling rules](https://portswigger.net/support/configuring-burp-suites-session-handling-rules) or extensions such as [Custom Parameter Handler](https://portswigger.net/bappstore/a0c0cd68ab7c4928b3bf0a9ad48ec8c7), allowing for even greater customisation of the `prompts`.

## Example Use Cases

The following list of example use cases showcases the bespoke and highly customisable nature of `burpgpt`, which enables users to tailor their web traffic analysis to meet their specific needs.

- Identifying potential vulnerabilities in web applications that use a crypto library affected by a specific CVE:

  ```
  Analyse the request and response data for potential security vulnerabilities related to the {CRYPTO_LIBRARY_NAME} crypto library affected by CVE-{CVE_NUMBER}:

  Web Application URL: {URL}
  Crypto Library Name: {CRYPTO_LIBRARY_NAME}
  CVE Number: CVE-{CVE_NUMBER}
  Request Headers: {REQUEST_HEADERS}
  Response Headers: {RESPONSE_HEADERS}
  Request Body: {REQUEST_BODY}
  Response Body: {RESPONSE_BODY}

  Identify any potential vulnerabilities related to the {CRYPTO_LIBRARY_NAME} crypto library affected by CVE-{CVE_NUMBER} in the request and response data and report them.
  ```

- Scanning for vulnerabilities in web applications that use biometric authentication by analysing request and response data related to the authentication process:

  ```
  Analyse the request and response data for potential security vulnerabilities related to the biometric authentication process:

  Web Application URL: {URL}
  Biometric Authentication Request Headers: {REQUEST_HEADERS}
  Biometric Authentication Response Headers: {RESPONSE_HEADERS}
  Biometric Authentication Request Body: {REQUEST_BODY}
  Biometric Authentication Response Body: {RESPONSE_BODY}

  Identify any potential vulnerabilities related to the biometric authentication process in the request and response data and report them.
  ```

- Analysing the request and response data exchanged between serverless functions for potential security vulnerabilities:

  ```
  Analyse the request and response data exchanged between serverless functions for potential security vulnerabilities:

  Serverless Function A URL: {URL}
  Serverless Function B URL: {URL}
  Serverless Function A Request Headers: {REQUEST_HEADERS}
  Serverless Function B Response Headers: {RESPONSE_HEADERS}
  Serverless Function A Request Body: {REQUEST_BODY}
  Serverless Function B Response Body: {RESPONSE_BODY}

  Identify any potential vulnerabilities in the data exchanged between the two serverless functions and report them.
  ```

- Analysing the request and response data for potential security vulnerabilities specific to a Single-Page Application (SPA) framework:

  ```
  Analyse the request and response data for potential security vulnerabilities specific to the {SPA_FRAMEWORK_NAME} SPA framework:

  Web Application URL: {URL}
  SPA Framework Name: {SPA_FRAMEWORK_NAME}
  Request Headers: {REQUEST_HEADERS}
  Response Headers: {RESPONSE_HEADERS}
  Request Body: {REQUEST_BODY}
  Response Body: {RESPONSE_BODY}

  Identify any potential vulnerabilities related to the {SPA_FRAMEWORK_NAME} SPA framework in the request and response data and report them.
  ```

# Roadmap

- [ ] Add a new field to the `Settings` panel that allows users to set the `maxTokens` limit for requests, thereby limiting the request size.
- [ ] Retrieve the precise `maxTokens` value for each `model` to transmit the maximum allowable data and obtain the most extensive `GPT` response possible.
- [ ] Implement persistent configuration storage to preserve settings across `Burp Suite` restarts.
- [ ] Enhance the code for accurate parsing of `GPT` responses into the `Vulnerability` `model` for improved vulnerability reporting.

## Project Information

The extension is currently under development and we welcome feedback, comments, and contributions to make it even better.

## Sponsor 💖

If this extension has saved you time and hassle during a security assessment, consider showing some love by sponsoring a cup of coffee ☕ for the developer. It's the fuel that powers development, after all. Just hit that shiny Sponsor button at the top of the page or [click here](https://github.com/sponsors/aress31) to contribute and keep the caffeine flowing. 💸

## Reporting Issues

Did you find a bug? Well, don't just let it crawl around! Let's squash it together like a couple of bug whisperers! 🐛💪

Please report any issues on the [GitHub issues tracker](https://github.com/aress31/burpgpt/issues). Together, we'll make this extension as reliable as a cockroach surviving a nuclear apocalypse! 🚀

## Contributing

Looking to make a splash with your mad coding skills? 💻

Awesome! Contributions are welcome and greatly appreciated. Please submit all PRs on the [GitHub pull requests tracker](https://github.com/aress31/burpgpt/pulls). Together we can make this extension even more amazing! 🚀

## License

See [LICENSE](LICENSE).
