//Ping sensor reading sequence taken from Arduino Examples
//Signal pin of PING sensor
const int ping_sigpin = 7;
//Setup loop
void setup() {
  //Start serial communication@9600 baud
  Serial.begin(9600);
}

//Main loop
void loop()
{
  //variables for PING: time and distance in centimeters
  long time, distcm;
  //send short trigger signal
  pinMode(ping_sigpin,OUTPUT);
  digitalWrite(ping_sigpin,LOW);
  delayMicroseconds(5);
  digitalWrite(ping_sigpin, HIGH);
  delayMicroseconds(5);
  digitalWrite(ping_sigpin, LOW);
  //read
  pinMode(ping_sigpin, INPUT);
  time=pulseIn(ping_sigpin, HIGH);
  //compute distance
  distcm=ms_to_cm(time);
  Serial.println(distcm);
  delay(200);
}

long ms_to_cm(long time){
  return time/29/2;
}
