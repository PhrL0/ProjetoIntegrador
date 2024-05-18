void setup() {
  Serial.begin(115200);
  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);


}

void loop() {
  if(Serial.available() > 0 ){// verifica dados na porta serial
    String dadosRecebido  = Serial.readStringUntil('\r');// encherga o caractere
    if(dadosRecebido.equals("ON")){
        digitalWrite(13,HIGH);
        Serial.println("Led acesso!");
    }
    if(dadosRecebido.equals("OFF")){
        digitalWrite(13,LOW);
        Serial.println("Led apagado!");
    }

  }


}
