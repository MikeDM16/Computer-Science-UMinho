

//Wifi Connection
#include <ESP8266WiFi.h>

//Wifi server
#include <ESP8266WebServer.h>

// Example testing sketch for various DHT humidity/temperature sensors
// Written by ladyada, public domain

#include "DHT.h"
// Define a web server at port 80 for HTTP
ESP8266WebServer server(80);

static const uint8_t D2   = 4;

#define DHTPIN D2     // what digital pin we're connected to

// Uncomment whatever type you're using!
#define DHTTYPE DHT11   // DHT 11

// Connect pin 1 (on the left) of the sensor to +5V
// NOTE: If using a board with 3.3V logic like an Arduino Due connect pin 1
// to 3.3V instead of 5V!
// Connect pin 2 of the sensor to whatever your DHTPIN is
// Connect pin 4 (on the right) of the sensor to GROUND
// Connect a 10K resistor from pin 2 (data) to pin 1 (power) of the sensor

// Initialize DHT sensor.
// Note that older versions of this library took an optional third parameter to
// tweak the timings for faster processors.  This parameter is no longer needed
// as the current DHT reading algorithm adjusts itself to work on faster procs.
DHT dht(DHTPIN, DHTTYPE);


//Functions
void handleNotFound() {
  digitalWrite ( LED_BUILTIN, 0 );
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += ( server.method() == HTTP_GET ) ? "GET" : "POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";

  for ( uint8_t i = 0; i < server.args(); i++ ) {
    message += " " + server.argName ( i ) + ": " + server.arg ( i ) + "\n";
  }

  server.send ( 404, "text/plain", message );
}

void handleTemperature() {

  char html[1000];
  int sec = millis() / 1000;
  int min = sec / 60;
  int hr = min / 60;

  float t = dht.readTemperature();
  if (isnan(t)) {
      t=-1;
  }

// Build an HTML page to display on the web-server root address
  snprintf( html, 1000,

"<html>\
  <head>\
    <meta http-equiv='refresh' content='10'/>\
    <title>ESP8266 WiFi Sensor</title>\
    <style>\
      body { background-color: #cccccc; font-family: Arial, Helvetica, Sans-Serif; font-size: 1.5em; Color: #000000; }\
      h1 { Color: #AA0000; }\
    </style>\
  </head>\
  <body>\
    <h1>ESP8266 Web Sensor</h1>\
    <p>Uptime: %02d:%02d:%02d</p>\
    <p>Temperature: %d.%02d C</p>\
    <p>%s<p>\
    <p>This page refreshes every 10 seconds. Click <a href=\"javascript:window.location.reload();\">here</a> to refresh the page now.</p>\
  </body>\
</html>",

    hr, min % 60, sec % 60,
    (int)t, (int)(t*100)%100
  );
  server.send ( 200, "text/html", html );
}

void handleHumidity() {

  char html[1000];
  int sec = millis() / 1000;
  int min = sec / 60;
  int hr = min / 60;

  float h = dht.readHumidity();
  if (isnan(h)) {
      h=-1;
  }
  
// Build an HTML page to display on the web-server root address
  snprintf( html, 1000,

"<html>\
  <head>\
    <meta http-equiv='refresh' content='10'/>\
    <title>ESP8266 WiFi Sensor</title>\
    <style>\
      body { background-color: #cccccc; font-family: Arial, Helvetica, Sans-Serif; font-size: 1.5em; Color: #000000; }\
      h1 { Color: #AA0000; }\
    </style>\
  </head>\
  <body>\
    <h1>ESP8266 Web Sensor</h1>\
    <p>Uptime: %02d:%02d:%02d</p>\
    <p>Humidity: %d.%02d%</p>\
    <p>%s<p>\
    <p>This page refreshes every 10 seconds. Click <a href=\"javascript:window.location.reload();\">here</a> to refresh the page now.</p>\
  </body>\
</html>",

    hr, min % 60, sec % 60,
    (int)h, (int)(h*100)%100
  );
  server.send ( 200, "text/html", html );
}

void setup() {
  Serial.begin(9600);
  delay(100);

  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  //Serial.println(ssid);
  WiFi.mode(WIFI_AP);
  WiFi.softAP("MYSensor", "12345678");
  

  Serial.println("");
  Serial.println("WiFi connected");  
  Serial.println("IP address: ");
  Serial.println(WiFi.softAPIP());
  Serial.println(WiFi.localIP());
  Serial.print("Netmask: ");
  Serial.println(WiFi.subnetMask());
  Serial.print("Gateway: ");
  Serial.println(WiFi.gatewayIP());

  server.on ( "/", []() {
    server.send ( 200, "text/plain", "this works well!" );
  } );
  server.on ( "/temperature", handleTemperature);
  server.on ( "/humidity", handleHumidity);
  server.onNotFound ( handleNotFound );
  
  server.begin();
  Serial.println("HTTP server started");

  dht.begin();
  Serial.println("dth11 sensor started!");
}

void loop() {
  server.handleClient();
  delay(2000);
}
