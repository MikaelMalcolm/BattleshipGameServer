# BattleshipGameServer

Connection Phase
Client connects to server
Server waits for initial action from client:
Initial Action Phase
Client will send “QUIT”, which disconnects the client
Or
“Play &lt;username&gt;” ,which will add the client to the games queue with a username of their choosing.
That client then takes no further action until it receives the following message from the server:
“PLAY &lt;opponent’s name&gt;”.
This indicates that the server has matched two players, and has placed them both in theSet-up Stage
Set-up Stage
This is the phase where clients place their ships on the board. After a client is satisfied with their ship
placement, they send the following message to the server:
“REDY &lt;gameboard&gt;”
Note: For the &lt;gameboard&gt; portion of the message, it will just be an abstraction of the placement of
ships on that client’s 10x10 grid. *****
After this message is sent, the client waits for either a “TURN” or “WAIT” message.
After receiving this message, this starts theBattle Phase of the game.
Battle Phase:
If the Client had received “TURN”, they make the first move. Otherwise, they wait to receive another
message from the server.
This is the game loop:
Attacker: send “BOM1 &lt;xy&gt;” to the server. &lt;xy&gt; is the co-ordinates of where the Attacker hit.
The server processes the attacker’s move and determines the outcome.
Server then sends “RES1 &lt;xyresult&gt;” to both clients. X and Y are the co-ordinates where the Attacker
made their move. Result will be 1 or 0, and will communicate whether the Attacker’s move resulted in a
HIT or a MISS.

if the attacker’s move does not determine a winner:

The Attacker will then receive the msg “WAIT”, while the Defender will receive the message
“TURN”, effectively switching the roles of the Attacker and Defender. The protocol then goes back
to the Battle Phase.

Else If the attacker’s move has determined a winner (All of the opponent’s ships are destroyed),
then:
The server will send the following message to the clients:
Attacker: “WINN”
Defender: ”LOSE”
At this point, the Clients then have the option to play against each other again. To indicate that
he/she wants to play against the same person again, the Client sends “AGAN,1”. Otherwise, they will
send “AGAN,0”.
Then the clients will wait to hear a response, indicating if the match will restart or not. The message
“AGAN,1” means the match will restart. “AGAN,0” means it will not.
If the match does not restart:
Both Clients then have the option to Quit the game or re-enter the Matchmaking Queue. This essentially
restarts the protocol back to Initial Action Stage.
If the match does restart:
Both clients start a new match, re-entering the Set-up Stage of the protocol.

Other notes:
Clients can disconnect from the server or surrender (Quit) a match at any time. In this case:
If the Attacker disconnects or sends a “SURR” message, the server will notify the Defender that it was
won (with the msg “WINN”) and take client back to the Initial Action phase.

If the Defender disconnects or surrenders (sends a “SURR” msg), the server will notify the Attacker that
it won (with the msg “WINN”) and take client back to the Initial Action phase.
