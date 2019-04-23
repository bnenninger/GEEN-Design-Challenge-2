


/*
 * circuit setup:
 * LCD:
 *    RS pin to digital pin 12
 *    Enable pin to digital pin 11
 *    D4 pin to digital pin 6
 *    D5 pin to digital pin 5
 *    D6 pin to digital pin 4
 *    D7 pin to digital pin 2
 *    R/W pin tto ground
 *    VSS pin to ground
 *    VCC pin to 5V
 *    10K potentiometer:
 *    ends to 5V and ground
 *    wiper to LCD V0 pin
 * Flow Sensor:
 *    Data pin to digital pin 2
 */

#include <LiquidCrystal.h> //includes the liquid crystal library for display

//pins for LCD
const int rs = 12, en = 11, d4 = 6, d5 = 5, d6 = 4, d7 = 3;
LiquidCrystal lcd(rs, en, d4, d5, d6, d7);

//must be pin 2 or 3 for hardware interrupt
int flowMeterPin = 2;
//counts number of ticks from the flow sensor
//uses a long int because an int would overflow at around ~650 liters,
//which a normal user would encounter.
//long int overflows at around 9e16 liters, which is a lot of liters.
//At average annual use for a household of 5 in the US, which is approximately
//5 people * 80 gallons/day * 4 gallons/liter * 365 days/year = 7.3e5 liters per year,
//it would take 1.26e11 years for this value to overflow. That is approximately
//9 times the age of the universe. I find this to be acceptable.
unsigned long int ticks = 0;

const double LITERS_PER_TICK = 0.0055;//currently the conversion given by the flow sensor, change if calibrated


void setup() {
  //pin setup
  pinMode(flowMeterPin, INPUT);
  //sets interrupt for counting ticks of the flow meter
  attachInterrupt(digitalPinToInterrupt(flowMeterPin), incrementTick, RISING);
  //starts the serial output to the attached PC
  Serial.begin(9600);
  //starts the LCD for 16 columns and 2 rows
  lcd.begin(16, 2);
  printTwoLines("H20Saver", "initializing...");
}

unsigned long previousTicks = ticks;
unsigned long previousTime = millis();

void loop() {
  delay(1000);
  //declaring this variable will improve consistency if an interrupt triggers during this cycle.
  unsigned long newTicks = ticks;
  unsigned long newTime = millis();
  //find differences and calculates rate
  float volumeChange = getVolume(int(newTicks - previousTicks));
  float timeChange = float(newTime - previousTime) / 1000.0;//converted to seconds
  float flowRate = volumeChange / timeChange * 60.0;//*60 converts to L/min
  //saves the new values to the previous values
  previousTicks = newTicks;
  previousTime = newTime;
  //displays and outputs the results
  LCDdisplay(getVolume(ticks), flowRate);
//  LCDdisplay(587.602775, 4.9867);
  //outputs data to serial
  serialOutput(ticks, getVolume(ticks), flowRate);
}

/*
 * Outputs data to a connected PC
 */
void serialOutput(long ticks, float volume, float flowRate){
  Serial.print(ticks);
  Serial.print(" ");
  Serial.print(volume);
  Serial.print(" ");
  Serial.print(flowRate);
  Serial.print("\n");
}

/*
 * Displays the current values to the LCD
 */
void LCDdisplay(float volume, float flowRate){
  clearLCD();
  printTwoLines(formatString("volume: ", " L", volume, 1),
      formatString("rate: ", " L/min", flowRate, 1));
}

void clearLCD(){
  String clearString = "                ";
  printTwoLines(clearString, clearString);
}

/*
 * prints two lines to the screen
 */
void printTwoLines(String line1, String line2){
  lcd.setCursor(0,0);//sets cursor with column, row
  lcd.print(line1);
  lcd.setCursor(0,1);
  lcd.print(line2);
}

/*
 * Formats the string for output to LCD
 */
String formatString(String label, String units, float value, int decimalPlaces){
  char valueString[16 - (sizeof(label) + sizeof(units))];
  dtostrf(value, sizeof(valueString), decimalPlaces, valueString);
  return label + valueString + units;
}

void incrementTick() {
  ticks++;
}

/*
 * returns a volume based on a number of ticks
 */
float getVolume(int ticks){
  return ticks * LITERS_PER_TICK;
}
