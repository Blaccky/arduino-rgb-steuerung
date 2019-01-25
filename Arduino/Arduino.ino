#define R 3
#define B 5
#define G 6
String input;
int aold = 0;
int bold = 0;
int cold = 0;


void setup() {
   pinMode(R, OUTPUT);
   pinMode(G, OUTPUT);
   pinMode(B, OUTPUT);
   digitalWrite(R, LOW);
   digitalWrite(G, LOW);
   digitalWrite(B, LOW);
   Serial.begin(9600);
   Serial.setTimeout(50);
}

void loop() {
while(Serial.available()) {
  input = Serial.readString();
  Serial.println(input);
  int a = input.substring(0, 3).toInt();
  int b = input.substring(3, 6).toInt();
  int c = input.substring(6, 9).toInt();
  if(a != aold || b != bold || c != cold){
  aold = a;
  bold = b;
  cold = c;
  Serial.println(a);
  Serial.println(b);
  Serial.println(c);
  setColor(a, b, c); 
  }
  

  
}
}

void setColor(int r,int g,int b) {
   analogWrite(R, r);
   analogWrite(G, g);
   analogWrite(B, b);
}
