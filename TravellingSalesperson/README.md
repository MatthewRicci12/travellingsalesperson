heuristic: cost = 3.3969307170000005, 6 milliseconds
backtrack: cost = 1.3566775349999998, 513 milliseconds
mine: cost = 2.695585887, 0 milliseconds

heuristic: cost = 3.3969307170000005, 5 milliseconds
backtrack: cost = 1.3566775349999998, 504 milliseconds
mine: cost = 2.323382221, 1 milliseconds

heuristic: cost = 3.3969307170000005, 2 milliseconds
backtrack: cost = 1.3566775349999998, 409 milliseconds
mine: cost = 2.042652264, 0 milliseconds

heuristic: cost = 3.3969307170000005, 2 milliseconds
backtrack: cost = 1.3566775349999998, 466 milliseconds
mine: cost = 1.967638847, 0 milliseconds

heuristic: cost = 3.3969307170000005, 9 milliseconds
backtrack: cost = 1.3566775349999998, 527 milliseconds
mine: cost = 1.753522045, 0 milliseconds

heuristic: cost = 183.86, 1 milliseconds
backtrack: cost = 183.86, 17 milliseconds
mine: cost = 130.01999999999998, 0 milliseconds

heuristic: cost = 1.8271134757999998, 2 milliseconds
backtrack: cost = 1.3611004867999998, 892 milliseconds
mine: cost = 1.5441425278, 0 milliseconds

My algorithm was an improvement upon the heuristic algorithm. Based on doing experiments, it gives a path that is better than heuristic about 8/10 of the time, which is significant. In rare cases it may be worse, but for the most part it is a huge improvement. 

The reason that my algorithm performs better than the original heuristic algorithm is because I used random chance. What I did was pick a random number from 0 to the number of nodes casted to an int. If it was not 0, it would pick the closest city like normal. If it were 0 though, then it would pick a neighbor completely at random. Every time it picked a random neighbor, the chances became steeper. Therefore, it was actually quite rare that the algorithm would pick a random neighbor. The strength comes from the fact that it actually behaves exactly like heuristic most of the time, and it only occasionally deviates at random. This random deviation more than often leads to optimal results since heuristic inherently leads to non-optimal configurations. 
