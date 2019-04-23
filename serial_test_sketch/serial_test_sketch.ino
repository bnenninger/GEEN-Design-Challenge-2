void setup(){
  Serial.begin(9600);
}

float counter = 0.0;

void loop(){
  counter += 0.5;
  Serial.println(counter, OCT);
  delay(1000);
}
