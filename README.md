# Digital EcoHome

## Overview

Digital EcoHome is an Android application designed to help users monitor and control their smart home devices, track energy consumption, and receive alerts and tips for energy savings.

## Features

- Real-time energy consumption monitoring
- Device control for smart home devices
- Alerts for high energy consumption and device status
- Energy-saving tips
- Data visualization using charts

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Hamdan-1/Digital-EcoHome.git
   ```
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.

## SAXParseException Error

### Explanation

The `SAXParseException` error in `content_main.xml` is caused by an invalid character or whitespace before the XML declaration. The error message indicates that "Content is not allowed in prolog," which means there is an unexpected character before the XML declaration.

### Steps to Fix

1. Open the `app/src/main/res/layout/content_main.xml` file.
2. Remove any extra spaces or characters before the XML declaration.
3. Ensure the file starts with `<?xml version="1.0" encoding="utf-8"?>` on the very first line.
4. Save the file and try building the project again.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
