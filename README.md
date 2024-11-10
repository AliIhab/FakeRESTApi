
# FakeRESTApi API Test Automation Project

This repository contains automated tests for the [FakeRESTApi](https://fakerestapi.azurewebsites.net/index.html) API using **Rest Assured** and **Postman**. The tests cover all available API requests, including both functional test cases and bug reports to ensure a comprehensive validation of the API’s functionality.
This is a Project that is submitted to ITI (Information Technology Institure) Track Software Testing 4 months

## Table of Contents
- [Project Overview](#project-overview)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Test Cases & Bug Report](#test-cases-bug-report)
- [Test Completion report](#test-completion-report)

## Project Overview
This project is built to automate and verify the RESTful endpoints of FakeRESTApi using Rest Assured in Java and Postman. Each endpoint is validated against its expected responses, including status codes, response times, and data accuracy.

## Features
- Automated API testing using Rest Assured
- Collection of Postman tests
- Comprehensive test cases covering all endpoints
- Bug report documentation for tracking issues

## Getting Started

### Prerequisites
- Java 11 or higher
- Maven
- [Postman](https://www.postman.com/downloads/)
- An IDE like IntelliJ IDEA or Eclipse

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/AliIhab/FakeRESTApi.git
   ```
2. Navigate to the project directory:
   ```bash
   cd FakeRESTApi
   ```
3. Install dependencies:
   ```bash
   mvn install
   ```

## Usage
1. **Run tests with Maven**: Execute the test suite using:
   ```bash
   mvn test
   ```
2. **Run tests in Postman**: Import the Postman collection and run the tests through Postman’s Runner.

## Test Cases & Bug Report 
The detailed test cases and bug report can be found in 
https://github.com/AliIhab/FakeRESTApi/blob/master/TestCases%2BBugReport.xlsx 

## Test Completion report

Test completion report is found at 
https://github.com/AliIhab/FakeRESTApi/blob/master/Test%20Completion%20Report.pdf
