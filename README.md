# A demo Jetty web service

This project briefly demonstrates how to set up and configure Jetty, with some security included.

For SLF4J set up, refer to [this useful guide](http://saltnlight5.blogspot.com/2013/08/how-to-configure-slf4j-with-different.html).


## Serlvet endpoints

GET or POST /demo - use this to confirm connection to the web service. If Ok, then returns the current Unix timestamp.

GET /resource - prints a list of all files according the query param (or form data) "dir"; if the param is not provided 
then expect an HTTP 400.

GET /download - attempts to download a (text) file according to the path parameter following ```/download```, 
e.g. ```/download/pub/test1.txt```. If a valid folder is given then nothing is streamed and an HTTP 200 is sent. 
If a valid (text) file at the path given (in the fat JAR) exists then this is streamed with an HTTP 200. Otherwise, 
returns an HTTP 404 if the file or folder does not exist.

The download servlet endpoint is suffixed with a ```*``` wildcard to allow for path parameters.

Attached is a [POSTMAN export](./JettyDemo.postman_collection.json) for convenience.
