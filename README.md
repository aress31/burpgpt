# burpgpt

[![Java CI with Gradle](https://github.com/aress31/burpgpt/actions/workflows/gradle-build.yml/badge.svg)](https://github.com/aress31/burpgpt/actions/workflows/gradle-build.yml)

`burpgpt` utilises the power of artificial intelligence to identify potential security vulnerabilities that traditional "dumb" scanners might overlook. It sends traffic to a user-specified `OpenAI` model for analysis within the passive scanner. By providing the ability to customise prompts, this extension offers limitless possibilities for analysing web traffic according to specific user requirements.

The extension's objective is to provide a context-relevant security report that delivers an automated yet human-readable summary of potential security issues in the application. As such, it presents a fast and convenient approach to integrating the capabilities of AI and natural language processing into security assessments. It also alleviates the workload on security professionals while offering them a higher-level overview of the scanned application/endpoints.

## Features

- Provides an additional passive scan check to submit HTTP request and response data to a user-controlled GPT model for security analysis.
- Allows for customisation of prompts to unlock infinite ways of interacting with OpenAI models.
- Leverages the power of OpenAI's GPT-3 API to detect potential security vulnerabilities in the scanned application.
- Allows the user to select the most suitable OpenAI model from the available options.
- Offers easy API key rotation to provide greater control over billing and usage.
- Integrates seamlessly with Burp Suite, making it easy to use and transparent once configured.

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

### 2. Loading the Extension Into the `Burp Suite`

To install `burpgpt` in `Burp Suite`, go to the `Extendensions` tab and click on the `Add` button. Then, load the `burpgpt-all` jar file located in the `.\lib\build\libs` folder.

# Usage

Before using `burpgpt`, the user needs to fill in their `OpenAI API key` and select/change a `model` within the settings panel available on the `Burp Suite` menu bar. The user can also tweak or use custom `prompts` within the settings panel.

Once the `OpenAI` model is configured, all passively scanned items would be sent to the selected `OpenAI model` for analysis based on the user's `prompt`. The `prompt` accepts placeholders that are post-processed prior to sending to the `OpenAI model` to replace with the relevant request/response values.

Here is a list of the supported placeholders in the burpgpt extension:

- `{IS_TRUNCATED_PROMPT}` - A `boolean` value that indicates whether the prompt has been truncated to fit within the `2048 character` limit imposed by most `GPT-3.5` models' `maxTokens` value. This value is programmatically set by the extenstion.
- `{URL}` - The URL of the scanned request.
- `{METHOD}` - The HTTP request method used in the scanned request.
- `{REQUEST_HEADERS}` - The headers of the scanned request.
- `{REQUEST_BODY}` - The body of the scanned request.
- `{RESPONSE_HEADERS}` - The headers of the scanned response.
- `{RESPONSE_BODY}` - The body of the scanned response.

These placeholders can be used in the custom prompt to dynamically generate a request/response analysis prompt that is specific to the scanned request.

# Roadmap

- [ ] Retrieve the precise `maxTokens` value for each model to transmit the maximum allowable data and obtain the most extensive `GPT` response possible.
- [ ] Implement persistent configuration storage to preserve settings across `Burp Suite` restarts.
- [ ] Enhance the code for accurate parsing of `GPT` responses into the `Vulnerability` model for improved vulnerability reporting.

## Project Information

The extension is currently under development and we welcome feedback, comments, and contributions to make it even better.

## Sponsor 💖

If this extension has saved you time and hassle during a security assessment, consider showing some love by sponsoring a cup of coffee ☕ for the developer. It's the fuel that powers development, after all. Just hit that shiny Sponsor button at the top of the page or [click here](https://github.com/sponsors/aress31) to contribute and keep the caffeine flowing. 💸

## Reporting Issues

Did you find a bug? Well, don't just let it crawl around! Let's squash it together like a couple of bug whisperers! 🐛💪

Please report any issues on the [GitHub issues tracker](https://github.com/aress31/burp-gpt/issues). Together, we'll make this extension as reliable as a cockroach surviving a nuclear apocalypse! 🚀

## Contributing

Looking to make a splash with your mad coding skills? 💻

Awesome! Contributions are welcome and greatly appreciated. Please submit all PRs on the [GitHub pull requests tracker](https://github.com/aress31/swurg/pulls). Together we can make this extension even more amazing! 🚀

## License

See [LICENSE](LICENSE).
