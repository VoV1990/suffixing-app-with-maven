Description

Use maven to build and control a simple Java program with dependencies.

App Specification

It is a Suffixing App - a small java application that refers to a config file and renames a set of files and renames them adding a suffix specified in the same config.

Details:

Application should read a config file on the startup
Then it should ensure that all of files from the config exist
Then it should rename each file adding a suffix from the config to its name
It should print the results of the renaming like:
old_name -> new_name
Changes: config file now should be an XML file.
Steps

Create a maven project and specify its GAV settings, encoding, language level, etc.
Add a dependency to some library for reading and parsing JSON files. (for instance, GSON)
Write the code implementing the app specification.
Configure maven project to build a runnable jar containing application and its dependencies.
Show the mentor your results.

Logging Specification:

Application should log startup information.
Application should log information on config read.
Application should log renaming process information.
Application should log summary information.
Application should log shutdown information.
Application should handle and log possible errors.

Use different logging level. All log entries should contain a date and time information as well.

When renaming is finished the application should print a document of completed actions.
Document should be XML-based. It should contain:
-config file name
-execution time
-list of files with old and new names
All the logging entries from previous exercise should become JSON document of some structure. They should contain:
-date and time
-message
-severity label
-error info, if its error