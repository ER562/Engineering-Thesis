#include <WiFiS3.h>
#include "wifi_password.h"

int Trig = 2;
int Echo = 3;

char ssid[] = SSID;
char pass[] = PASS;

int status = WL_IDLE_STATUS;

char packetBuffer[256];
char sendpacketBuffer[256];

char server_address[] = "192.168.88.68"; //to do
int server_port = 49200;

char password[11] = "abcd1     ";
char serial_number[11] = "1abcd     ";

bool loggedin = false;
char identitycode[5];

WiFiUDP Udp;

void setup()
{
  Serial.begin(9600);
  pinMode(Trig, OUTPUT);
  pinMode(Echo, INPUT);

  //wifi
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);

    delay(10000);
  }
  Serial.println("Connected to WiFi");
  Serial.println(WiFi.localIP());

  Udp.begin(49200);
}

int pomiar_odleglosci ()
{
  digitalWrite(Trig, LOW);
  delayMicroseconds(2);
  digitalWrite(Trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(Trig, LOW);
  digitalWrite(Echo, HIGH); 
  long CZAS = pulseIn(Echo, HIGH);
  return CZAS / 58;
}
  
void loop()
{
  //connecting to the server
  while(loggedin == false){
    sprintf(sendpacketBuffer, "020000%s%s", serial_number, password);
    
    Udp.beginPacket(server_address, server_port);
    Udp.write(sendpacketBuffer);
    Udp.endPacket();

    uint32_t start = millis();
    while(millis() - start < 5000){
      int packetSize = Udp.parsePacket();
      if(packetSize){
        Serial.println("dostano pakiet");

        int len = Udp.read(packetBuffer,256);
        if(len >= 6){
          char code[3];
          char identyfier[5];
          strncpy(code, packetBuffer, 2);
          code[2] = '\0';
          strncpy(identyfier, packetBuffer+2, 4);
          identyfier[4] = '\0';

          if(strcmp(code, "02") == 0 && strcmp(identyfier, "0000") != 0){
            strcpy(identitycode, identyfier);
            Serial.println("zalogowano");
            loggedin = true;
            break;
          }
        }
      }
    }
    delay(5000);
  }

  while(loggedin == true){
    Serial.println("logged in");
    int cm = pomiar_odleglosci();
    Serial.print("Odleglosc: ");
    Serial.print(cm);
    Serial.println(" cm");

    sprintf(sendpacketBuffer, "06%s%d", identitycode, cm);
    Serial.println(sendpacketBuffer);
    Serial.println(strlen(sendpacketBuffer));
    
    Udp.beginPacket(server_address, server_port);
    Udp.write(sendpacketBuffer, strlen(sendpacketBuffer));
    Udp.endPacket();

    uint32_t start = millis();
    while(millis() - start < 5000){
      int packetSize = Udp.parsePacket();
      if(packetSize){
        Serial.println("dostano pakiet");

        int len = Udp.read(packetBuffer,256);
        if(len >= 6){
          char code[3];
          char identyfier[5];
          strncpy(code, packetBuffer, 2);
          code[2] = '\0';
          strncpy(identyfier, packetBuffer+2, 4);
          identyfier[4] = '\0';

          if(strcmp(code, "03") == 0 && strcmp(identyfier, "0000") == 0){
            loggedin = false;
            Serial.println("logged out");
            break;
          }else if(strcmp(code, "03") == 0 && strcmp(identyfier, identitycode) == 0){
            Serial.println("still loggedin");
            break;
          }
        }
      }
    }
    delay(60000);
  }
}