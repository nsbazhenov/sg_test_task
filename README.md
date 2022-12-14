API for working with clan and player gold.
======
+ Tasks

  + To implement logic of adding/decreasing gold to clan, providing that gold may be added from hundred(100) different threads at the same moment. By different users for different reasons.

  + To implement the tracking of different transactions of adding gold, so that the support team could identify when and for what reason in the treasury has changed the amount of gold, how much was, how much became, etc.
------
+ Using IDE
    + Clean &#8594; Package &#8594; Working through the IDE;
    + Command line &#8594; mvnw clean install &#8594; java -jar skytec_games_test-1.0-jar-with-dependencies.jar;
------
+ Endpoint
    + Get clan by id: GET http://localhost:8080/clan?id=1
    + Get all clans: GET http://localhost:8080/clans
    + Add gold to the clan: POST http://localhost:808/clan/add-gold
    ```javascript
    {"clanId" : "1", "playerId" : "1", "value" : "100"}
    ```
    + Take gold to the clan: POST http://localhost:808/clan/take-gold
    ```javascript
    {"clanId" : "1", "playerId" : "1", "value" : "100"}
    ```
    + Get player by id: GET http://localhost:8080/player?id=1