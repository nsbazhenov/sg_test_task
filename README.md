API for working with clan and player gold.
======
Tasks

+ To implement logic of adding/decreasing gold to clan, providing that gold may be added from hundred(100) different threads at the same moment. By different users for different reasons.

+ To implement the tracking of different transactions of adding gold, so that the support team could identify when and for what reason in the treasury has changed the amount of gold, how much was, how much became, etc.
Installation:
------
+ Using IDE
    + Clean > Package;
    + Working through the IDE;
    + Else, working through a console: java -jar ...;
------
+ API


  http://localhost:8080/clan?id=1
  http://localhost:8080/clans

  http://localhost:808/clan/add-gold
 ```javascript
  {"clanId" : "1","playerId" : "1","value" : "100"}
```

http://localhost:808/clan/take-gold
 ```javascript
  {"clanId" : "1","playerId" : "1","value" : "100"}
```

http://localhost:8080/player?id=1

