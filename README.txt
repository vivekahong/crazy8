Crazy 8
==============

Synopsis
--------------

Crazy project is a distributed project that is programmed to demonstrate the message passing by playing a game of crazy eight against multiple players or AI.

Installation
--------------

1.Download the code into a directory  
2.Build the files by using the following command:  
`make all`
3.The program is now ready to run  

Code Example
--------------
	
On the server side:
`java Server {port}`

Example of what to expect on the command-line:

>java Server 10007
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

On the Client side:
`java Client {hostname} {port}`

>java Client 192.12.69.186 10007
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

>java Client 192.12.69.186 10007
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

>java Client 192.12.69.186 10007
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


Contributors
--------------

Shien Hong			honghsien5@gmail.com
Kegan Schaub		kshaub@email.arizona.edu