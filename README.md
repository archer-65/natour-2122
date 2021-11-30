# NaTour
  Progetto realizzato per l'esame di INGSW presso [UniNa](https://www.unina.it) A.A. 2021-2022

## Traccia
   > La società SoftEngUniNA ha l’obiettivo di commercializzare “NaTour21”, una moderna piattaforma social per
appassionati di escursioni.

## Funzionalità
  - Un utente può:
    - Registrarsi e autenticarsi, anche tramite piattaforme come Google e Facebook.
  - Un utente autenticato può:
    - Inserire nuovi itinerari (sentieri) in piattaforma. Un sentiero è
caratterizzato da un nome, una durata, un livello di difficoltà, un punto di inizio, una descrizione
(opzionale), e un tracciato geografico (opzionale) che lo rappresenta su una mappa. Il tracciato
geografico deve essere inseribile manualmente (interagendo con una mappa interattiva) oppure
ramite file in formato standard GPX;
    - Effettuare ricerche di itinerari tra quelli presenti in piattaforma, con possibilità di filtrare i risultati
per area geografica, per livello di difficoltà, per durata, e per accessibilità a disabili;
    -  Qualora lo ritenga necessario, un utente può anche indicare un punteggio di difficoltà e/o un tempo
di percorrenza diverso da quello indicato dall’utente che ha inserito il sentiero. In questo caso, il
punteggio di difficoltà e il tempo di percorrenza per il sentiero saranno ri-calcolati come la media
delle difficoltà e/o dei tempi indicati;
    - Un utente può creare compilation di sentieri personalizzate, caratterizzate anche da un titolo e da
una descrizione personalizzata;
    - Un utente può inviare un messaggio privato a un altro utente, per esempio per chiedere
ulteriori informazioni circa un itinerario da lui inserito. È possibile rispondere ai messaggi privati
ricevuti;
    - Un utente può segnalare informazioni inesatte/non aggiornate riguardo un sentiero. Una
segnalazione è caratterizzata da un titolo e da una descrizione. I sentieri per cui sono presenti
segnalazioni di inesattezza mostrano un warning nella schermata di dettaglio relativa;
    - Un utente può segnalare fotografie inappropriate. Le fotografie con segnalazioni non gestite non
vengono mostrate nella schermata di dettaglio di un sentiero.
  - Un amministratore può:
    - Arbitrariamente rimuovere o modificare itinerari inseriti dagli utenti. In
questo caso, la schermata di dettaglio degli itinerari modificati mostrano un warning che informa gli
utenti della modifica e della data in cui è avvenuta.  

## TODO Tecnologie
  ### IDE
  [Android Studio](https://developer.android.com/studio), per realizzare l'applicativo Android, come linguaggio è stato scelto [Java](https://www.java.com/it/).

  ### AWS
  [AWS](aws.amazon.com) insieme a [Amplify](https://aws.amazon.com/it/amplify/) per i servizi Cloud, tra questi:
  - [RDS](https://aws.amazon.com/it/rds/) per il database;
  - [Lambda](https://aws.amazon.com/it/lambda/) per calcolo su eventi serverless;
  - [S3](https://aws.amazon.com/it/s3/) per archiviare oggetti;
  - [Cognito](https://aws.amazon.com/it/cognito/) per lo user pool;
  - [API Gateway](https://aws.amazon.com/it/api-gateway/) per gestire le API.
  
## TODO Architettura e Librerie utilizzate
  
## Versionamento  
  Abbiamo utilizzato [Git](https://git-scm.com/) come VCS e il [GitHub](https://github.com/) come suo Forge.
  
## Autori
  - **Bianca Giada Chehade** 
    - Matricola: N86003209
  - **Mario Liguori**        
    - Matricola: N86003258
  - **Mattia Rossi**         
    - Matricola: N86003211
