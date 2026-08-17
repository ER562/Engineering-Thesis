# Water level measurement system

A comprehensive water level monitoring system designed as an engineering thesis project. The system measures water levels using an ultrasonic sensor, collects data on a central Java server, and provides a graphical user interface (GUI) for data visualization and analysis.

## System Architecture

* Water height sensor - Measures water level in real-time and transmits data wirelessly to the central server.
* Server - Stores sensor readings, fetches and archives historical data from external weather APIs, and handles authentication for both sensors and user accounts.
* User application - Desktop GUI application that allows users to view, compare, and analyze historical and real-time data retrieved from the server.

## Used technologies

* Java - server logic and user application Gui
* C++ - Arduino firmware & sensor communication logic

## Requirements

* Eclipse IDE for Java Developers (v2025-03)
* Arduino IDE (v2.3.6)

## Hardware requirements

* Microcontroller - Arduino UNO R4 WiFi
* Ultrasonic distance sensor - HC-SR04

## Building & running

1. **Clone this repository with following command:**
```bash
git clone https://github.com/ER562/Engineering-Thesis.git
```

2. **Oppening sensor project**

_Arduino project can be oppened by double clicking following file:_
```bash
./Sensor/Sensor.ino
```

3. **Oppening server and user application projects**

_Launch Eclipse IDE and open any workspace_

_Navigate to: File/Import..._

_Select: Git/Projects from Git/Existing local repository and click Next_

_click Add..., then Browse... and select root folder of this repository_

_click next and choose "Import from existing Eclipse projects", and click next_

_select "server..." and "User-Application..." projects, than click "Finish"_