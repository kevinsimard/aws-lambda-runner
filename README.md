# AWS Lambda Runner

This is a AWS Lambda Java simulator that allows running and debugging Lambda functions authored in Java locally. You can add this tool to your Lambda project as a provided dependency and enjoy testing locally your Lambda functions in Java.

## Usage

Use `$ mvn install` to install the JAR locally.

See [aws-lambda-example](https://github.com/kevinsimard/aws-lambda-example) project for a real implementation.

## Code Structure

    ├── src
    │   └── main
    │       └── java
    │           └── com
    │               └── kevinsimard
    │                   └── aws
    │                       └── lambda
    │                           └── Runner.java
    ├── .editorconfig
    ├── .gitattributes
    ├── .gitignore
    ├── LICENSE.md
    ├── README.md
    └── pom.xml

## License

This package is open-sourced software licensed under the [MIT license](http://opensource.org/licenses/MIT).
