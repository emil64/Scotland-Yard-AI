# Scoland Yard - AI

This is an AI capable of making the best move for MrX for the ScotlandYard game, implemented in here: https://github.com/emil64/Scotland-Yard

## Scoring Function
The main objective of the scoring function is to take a ScotlandYardView and to generate
a score mainly based on the distance between MrX and the detectives. To do that, the obvious
choice was the BFS (Breadth-first search) algorithm which provided us with the minimum
distance between two nodes on the graph. We used a slightly modified version of it accounting
for necessary tickets to make the moves. Finally, the getCost method from Distances class
returns an object of type Cost that contains the minimum distance between nodes, and the cost
of getting from one node to the other (no. of Taxi tickets, Bus tickets and Underground tickets).
This method is used to compute the score (cost) of each detective, by adding value to the
components above (moves has 1000, taxi has 2, bus has 3 and underground has 5). Then, the
closest detective gets 40% of the total score and the sum of the others, 60%.
Moreover, we made an implementation of ScotlandYardView in order to generate possible
game configurations, called ScotMask . This class allows us to quickly retrieve data form specific
configurations, and, also, change its parameters by calling makeMove method.
## MiniMax Algorithm
In order to make our AI choose the best move, we decided to implement a MiniMax
algorithm. In this algorithm we use the scoring function as a static evaluation. Basically, the
algorithm iterates through a tree of moves and when it reaches a leaf (depth = 0) it makes the
static evaluation by calling the scoring function. We have chosen a depth of 4, meaning that MrX
can see 5 piles ahead. The algorithm is designed for 2 players (MrX and the detectives). One
player makes a move then the depth is reduced by one in the recursive call and the players are
switched. In order to optimize our AI(MrX takes less time to make the move) we used alpha-beta
pruning. This way, the algorithm is more efficient because it keeps track of the current score in a
certain node. Therefore, depending on which player moves (maximizing or minimizing) the
algorithm is capable of pruning all the children of that node which provide a smaller/bigger score
respectively. We also had to determine the set of valid moves from the current position in order
to create the tree. We did this by adding the validMove and possibleDoubleMoves in the ScotMask
class.
