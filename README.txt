Config notes:
- Inside Server.java, change String serverHostName inside main to the computers ip address.
- Inside both Server.java and Client.java, change int port inside main to the port you would like to use.

To compile:
>make all

To start the server:
>java Server

To start the client:
>java Client

Side note:
You will be given stdout instructions for what is to be expected of stdin.

Example of what to expect on the command-line:

>java Server
How many players? 1 for playing against AI
>3
Number of Draws before skipping to next person?  num <= 0 : for unlimited
>2
Waiting for connection.....
Player 0 connected.
Player 1 connected.
Player 2 connected.
All connection successful
Setting up I/O
Assigning player 0
Assigning player 1
Assigning player 2
Applying options to player 0
Applying options to player 1
Applying options to player 2
Distributing cards
The top card of the discard pile is: Six of Spades
Round 1
Player 0's turn to play
...

>java Client
Attemping to connect to host 192.12.69.186 on port 10007.
You have successfully connected to the server!
Your player id is 0
Draw option is set to 2.  num < 0 means unlimited draw
Distributing cards
Current top card from the discard pile is: Six of Spades
Your hand is currently:
Five of Diamonds    Three of Diamonds    Jack of Clubs    Ten of Clubs    Five
of Hearts    
Waiting for your turn to play
...

Current top card from the discard pile is: Six of Spades
Your turn to pick
Your hand is currently:
Five of Diamonds    Three of Diamonds    Jack of Clubs    Ten of Clubs    Five
of Hearts    
Don't have any card to play
Drawing
Your hand is currently:
Five of Diamonds    Three of Diamonds    Jack of Clubs    Ten of Clubs    Five
of Hearts    Eight of Diamonds    
Your hand is currently:
Five of Diamonds    Three of Diamonds    Jack of Clubs    Ten of Clubs    Five
of Hearts    Eight of Diamonds    
Your options are: 
0: Eight of Diamonds    
Please select an option by entering its corresponding number
>0
...

>java Client
Attemping to connect to host 192.12.69.186 on port 10007.
You have successfully connected to the server!
Your player id is 1
Draw option is set to 2.  num < 0 means unlimited draw
Distributing cards
Current top card from the discard pile is: Six of Spades
Your hand is currently:
Two of Clubs    Two of Hearts    Ace of Diamonds    Three of Clubs    Six of
Clubs
Waiting for your turn to play
...

>java Client
Attemping to connect to host 192.12.69.186 on port 10007.
You have successfully connected to the server!
Your player id is 2
Draw option is set to 2.  num < 0 means unlimited draw
Distributing cards
Current top card from the discard pile is: Six of Spades
Your hand is currently:
Four of Diamonds    Queen of Spades    Nine of Spades    Four of Clubs    Two
of Diamonds    
Waiting for your turn to play
...
