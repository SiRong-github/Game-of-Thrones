# University Subject
This is a SWEN30006 Software Modelling and Design Project of the University of Melbourne created by a three-person team.

# Demo

https://github.com/user-attachments/assets/db37e9b9-1471-4aad-af7f-2f11ed42b861

# File Purpose
The program refactors an existing card game framework based on a custom-made game called Spill med Troner or Game of Thrones (GoT), where character abilities can be amplified or reduced, and battles are determine by their attack and defence stats.

# File-Level Documentation
The rules of Game of Thrones are as follows:  

A. Basics
   
  1. The game is played with a standard 52 card deck without aces.  
  2. All court cards (J, Q, K) have rank 10.  
  3. Each suit represents different aspects  
    a. Heart: character  
    b. Club: attack  
    c. Spade: defence  
    d. Diamond: magic, which modify attack and defence
  4. An "effected" character is represented by its character in addition to a sequence of effect cards.  
    \- The card sequence [6H, 3C, 7D] represents the character 6h, with the sequence of attack effect card 3C and magic effect card 7D applied.

 B. Attack and Defence  
  1. Without any effect cards, the attack and defence for a character are both equal to the card rank.  
    \- The character \[6H] has attack 6 and defence 6.  
  2. Club adds to attack, while Spade adds to defence.  
    \- \[6H, 3C] has attack 9 and defence 6.  
    \- \[6H, 3S] has attack 6 and defence 9.  
  3. Diamond reduces either attack or defence by its rank depending on the card underneath, which cannot be a heart. If the card is another diamond, it reduces the already reduced non-magic effect card. An "effected" character can never drop below 0.  
    \- The card sequence \[6H, 3C, 5D] represents attack 4 (= 6 + 3 - 4) and defence 6.  
    \- The card sequence \[6H, 3S, 5D] represents attack 6 and defence 4 (= 6 + 3 - 5).  
    \- The card sequence \[6H, 5D] is invalid.  
    \- The card sequence \[6H, 3C, 10D] has attack 0 and defence 6.  
  4. If the rank of an effect card is the same rank as the card on which it is directly played, the effect is doubled.  
    \- The card sequence \[6H, 6C] has attack 18 and defence 6.  
    \- The card sequence \[6H, 6C, 6D] has attack 6 (= 6 + 12 - 12) and defence 6.  
    \- The card sequence \[6H, 10C, KD] has attack 0 (= 6 + 10 - 20 ) and defence 6.

C. The Game
  1. Dealing is done by providing each player with first 3 hearts, then 9 effect cards.  
  2. All cards are dealt face down and played face up. While the software version deals the cards face up for developers' use, automated players are unable to see the cards of other players.  
  3. Play proceeds clockwise.  
  4. Players 0 ad 2 form a team, using the top character pile in the GUI, and players 1 and 3 form another, using the bottom character pile.  
  5. Each play consists of 3 rounds plus battle:  
    a. The first round starts with a randomly selected player. Subsequent rounds start with the player one position clockwise from the previous starting player.  
    b. The first 2 players (starting and next) must play a character to their pile for their team, For the remaining 2 1/2 rounds, each player can either pass or play an effect card. Diamonds cannot be played above hearts.  
    c. The battle round consists of each "effected" character attacking the other. For each of the two attacks, the attacker wins only if their attack is strictly higher than the defence of the defender.  
      \- If the attacker wins, the rank of the character (heart) is added to the attacking team's score, else it is added to the defending team's score.  
      \- The possible outcomes include:  
        i. One side scores from both characters  
        ii. Each team scores from their own characters  
        iii. Each team scores from the opposing team's character  
    \- Example 1  
      * Team 1 (Players 0 and 2) ends up with EC0 = \[6H, ...] with attack 11 and defence 9.  
      * Team 2 (Players 1 and 3) ends up with EC1 = \[QH, ...] with attack 9 and defence 9.
      * EC0 attacks EC1, with team1 attack 11 > team2 defence 9, so team1 receives a score of 10 (rank of QH).  
      * EC1 attacks EC0, with team0 attack 9 == team2 defence 9, so team2 does not receive any score.  
      * Overall outcome: The teams have a score of 10 and 0, respectively.  
     \- Example 2  
      * Team 1 ends up with EC0 = \[2H, ...] with attack 9 and defence 2.  
      * Team 2 ends up with EC1 = \[9H, ...] with attack 3 and defence 8.
      * EC0 attacks EC1, with team1 attack 9 > team2 defence 8, so team1 receives a score of 9 (rank of 9H).  
      * EC1 attacks EC0, with team2 attack 3 == team1 defence 2, so team2 receives a score of 2 (rank of 2H).
      * Overall outcome: The teams have a score of 9 and 2, respectively. 

  6. At the end of all six plays, the pair with the highest score wins.

E. Strategy  

  1. Players will want to add positive effects to their character and negative to their opponent's.  
  2. Players will need to decide when to play effect cards and when to pass. Effect cards left at the end of all plays are wasted, and some played effect cards may not have an impact on the battle and are thus wasted.

F. Specification for Extended GoT  

  1. Calculate the attack and defence values in addition to the outcomes of the battles correctly based on the full sequence of cards.  
  2. Ensure that all player types follow the rules listed above, by playing a character only if they are taking one of two moves, and by not playing a magic on a character. This should be checked independently of the player's actions with BrokeRuleException being thrown if a rule is violated, in which case this should never be thrown if the program is behaving correctly.
  3. Support 4 player types:  
    a. Human: should not change from provided framework, other than ensuring they follow the rules
    b. Random: same as Human  
    c. Simple: play like Random, but will pass if the randomly chosen card and pile directly help the opposition or hinder their own team
    d. Smart: keep track of cards played and use this information along with the knowledge of their own hand to ensure that they will only play a randomly selected card where both conditions are true  
       i. It would change the battle outcome in their team's favour if the battle took place straight after playing the card  
       ii. There is no magic card in another player's hand which could double reduce (if same rank) the effect of the card  
  4. Load appropriate game parameters from the property files and must support configuring the player types in the property file using properties of the form "players.0 = smart" for the full set of player types (human, random, simple, smart) and players {players.0, players.1, players.2, players.3} along with two other parameters (seed, watchingTime) appearing in the sample property files.

  

# Provided Template and Driver Program by the Subject

1. lib folder

Contains the JGameGrid library

2. properties 

Contains the test cases. 

    a. got.properties

    Configuration for 1 human and 3 random players.

    b. original.properties
		
    Configuration for 4 random players.

    c. smart.properties

    Tests extended version, 1 human, 1 smart and 2 simple players.
    

3. sprites

Contains the gif images of the game objects.

4. src.thrones/game
   
    a. GameOfThrones

    Runs the project based on the given command line arguments which are based on the selected test case in the properties folder and contains the main logic of the game.

    b. BrokeRuleException

    Exception thrown when a player breaks a rule.
     

# Testing

Steps prior to compiling and running:
1. Download (or clone) the project.
2. Ensure that the Java version is Java 17.
3. Head to the project directory by writing.

	cd *projectFileLocation*
 
 For example
 	
  	cd Desktop/GameOfThrones  
   

## Compiling

	javac -cp lib/JGameGrid.jar -d out src/*/*.java

## Running

 java -cp out:lib/JGameGrid.jar:properties:sprites utility.GameOfThrones properties/*test file*


For example:

	java -cp out:lib/JGameGrid.jar:properties:sprites utility.GameOfThrones properties/verySimple.properties
