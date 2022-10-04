/*
* AUTHOR: Matthew Ricci
* FILE: PA11Main.java
* ASSIGNMENT: Programming Assignment 11 - Traveling Salesperson
* COURSE: CSC 210; Section C; Spring 2021
* PURPOSE: This program contains main as well as the 3 methods that solve this
* problem: backtracking, heuristic, and my own method, as well as a method that
* compares costs and times of these.
*
USAGE:
java file [HEURISTIC, BACKTRACK, MINE, TIME]

where file is the name of an mtx file in the following format:
*

* ----------- EXAMPLE INPUT -------------
* Input file:
* -------------------------------------------
* | %%MatrixMarket matrix coordinate real general
* | %-------------------------------------------------------------------------------
* | % 1: Tucson
* | % 2: Phoenix
* | % 3: Prescott
* | % 4: Show Low
* | % 5: Flagstaff
* | % author: Michelle Strout
* | % modified by: Dyana Muller
* | %-------------------------------------------------------------------------------
* | 5 5 20
* | 5 3 42.0
* | 3 5 7.0
* | 2 1 113.0
* | 1 5 209.48
* -------------------------------------------

and [HEURISTIC, BACKTRACK, MINE, TIME] is a set of commands that trigger 
different methods of the same name.

* The commands shown above are all of the commands that are supported
* by this program. It is assumed that (except for some specific errors), 
* the input is well-formed, and matches the format shown above.

*/


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class PA11Main {
	
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(args[0]));
         
        int totalSize = extractInfo(scanner);
        
        DGraph graph = new DGraph(totalSize);
        
        while (scanner.hasNext()) {
        	String[] edgeLine = scanner.nextLine().split("\\s+");
        	int node1 = Integer.valueOf(edgeLine[0].trim());
        	int node2 = Integer.valueOf(edgeLine[1].trim());
        	Double weight = Double.valueOf(edgeLine[2].trim());
        	graph.addEdge(node1, node2, weight);
        }

    	switch (args[1].toLowerCase()) {
    	
    	case "heuristic":
    		Trip trip = heuristic(graph);
    		System.out.println(trip.toString(graph));
    		break;
    		
    	case "backtrack":
    		trip = backtrack(graph);
    		System.out.println(trip.toString(graph));
    		break;
    		
    	case "mine":
    		trip = mine(graph);
    		System.out.println(trip.toString(graph));
    		break;
    		
    	case "time":
    		time(graph);
    		break;
    	
    	}
        	
        scanner.close();

}
    
    /*
    * A method that returns the number of nodes in the graph.
    *
    * @param scanner, a Scanner object passed from main.
    *
    * @return size, the number of nodes in the graph.

    */
    private static int extractInfo(Scanner scanner) {
        String line = scanner.nextLine(); //Get rid of title
        
        boolean commentLine = true;
        while (commentLine) {
            line = scanner.nextLine();
            commentLine = line.startsWith("%");
        }
       
        String[] sizeInfo = line.split("( )+");
        
        int rows = Integer.valueOf(sizeInfo[0].trim());
        int columns = Integer.valueOf(sizeInfo[1].trim());
        
        int size = Math.max(rows, columns); 
        
        return size;
    }
    
    /*
    * A method that performs a heuristic search, always going for the lowest
    * cost node.
    *
    * @param graph, any DGraph.
    *
    * @return trip, a Trip object representing the trip found by the algorithm.

    */
    private static Trip heuristic(DGraph graph) {
    	Trip trip = new Trip(graph.numNodes);
    	int curCity = 1;
    	trip.chooseNextCity(curCity);
    	for (int i = 2; i <= graph.numNodes; i++) {
    		double minDistance = Double.MAX_VALUE;
    		int closestCity = graph.numNodes + 1;
    		for (int city : graph.getNeighbors(curCity)) {
    			if (trip.isCityAvailable(city)) {
    				double distance = graph.getWeight(curCity, city);
    				if (distance < minDistance) {
    					minDistance = distance;
    					closestCity = city;
    				}
    				
    			}
    		}
    		trip.chooseNextCity(closestCity);
    		curCity = closestCity;
    	}
    	return trip;
    }
    
    /*
    * A method that performs a backtrack search, trying every possible 
    * permutation.
    *
    * @param graph, any DGraph.
    *
    * @return trip, a Trip object representing the trip found by the algorithm.

    */
    private static Trip backtrack(DGraph graph) {
    	Trip trip = new Trip(graph.numNodes);
    	Trip smallestTrip = new Trip(0);
    	trip.chooseNextCity(1);
    	Trip result = backtrackHelper(graph, trip, smallestTrip);	
    	return result;
    }
    
    /*
    * A helper function for backtrack. Follows the classic choosing/unchoosing
    * algorithm and continually updates the smallestTrip.
    *
    * @param graph, any DGraph.
    * 
    * @param curTrip, the current Trip.
    * 
    * @param smallestTrip, the smallest Trip found so far.
    *
    * @return Trip, a Trip object representing the smallest trip found by the 
    * algorithm.

    */
    private static Trip backtrackHelper(DGraph graph, Trip curTrip, Trip smallestTrip) { 
    	double smallestTripCost = smallestTrip.tripCost(graph);
    	double curTripCost = curTrip.tripCost(graph);
    	if (curTrip.citiesLeft().size() == 0) {
    		if (curTripCost < smallestTripCost) {
    			smallestTrip.copyOtherIntoSelf(curTrip);
    		}
    	}
    	if (curTripCost < smallestTripCost) {
    		for (int city : curTrip.citiesLeft()) {
    			curTrip.chooseNextCity(city);
    			backtrackHelper(graph, curTrip, smallestTrip);
    			curTrip.unchooseLastCity();
    		}
    	}
    	return smallestTrip;
     }
    
    /*
    * An improved heuristic search that very rarely picks a random node, but
    * is heuristic most of the time.
    *
    * @param graph, any DGraph.
    *
    * @return trip, a Trip object representing the trip found by the algorithm.

    */
    private static Trip mine(DGraph graph) {
    	Trip trip = new Trip(graph.numNodes);
    	int curCity = 1;
    	Random random = new Random();
    	int randomFactor = graph.numNodes;
    	trip.chooseNextCity(curCity);
    	
    	for (int i = 2; i <= graph.numNodes; i++) {
    		double minDistance = Double.MAX_VALUE;
    		int closestCity = graph.numNodes + 1;
    		for (int city : graph.getNeighbors(curCity)) {
    			if (trip.isCityAvailable(city)) {
    				double distance = graph.getWeight(curCity, city);
    				if (distance < minDistance) {
    					minDistance = distance;
    					closestCity = city;
    				}
    				
    			}
    		}
    		
    		int randint = random.nextInt(randomFactor);
    		if (randint != 0) {
        		trip.chooseNextCity(closestCity);
        		curCity = closestCity;
    		} else {
    			int item = random.nextInt(graph.getNeighbors(curCity).size()); // In real life, the Random object should be rather more shared than this
    			int index = 0;
    			int chosenCity = 0;
    			for (int city: graph.getNeighbors(curCity))
    			{
    			    if (index == item) {
    			        chosenCity = city;
    			    }
    			    index++;
    			}
        		trip.chooseNextCity(chosenCity);
        		curCity = chosenCity;
    		}
    		randomFactor++;
    	}
    	return trip;
    }
    
    
    /*
    * A method that shows the cost and time of all 3 methods.
    *
    * @param graph, any DGraph.
    *

    */
    private static void time(DGraph graph) {	
    	long startTime = System.nanoTime();
    	Trip trip = heuristic(graph);
    	long endTime = System.nanoTime();
    	long duration = (endTime - startTime) / 1000000;
    	System.out.println("heuristic: cost = " + trip.tripCost(graph) + ", " + duration +" milliseconds");
    	
    	startTime = System.nanoTime();
    	trip = backtrack(graph);
    	endTime = System.nanoTime();
    	duration = (endTime - startTime) / 1000000;
    	System.out.println("backtrack: cost = " + trip.tripCost(graph) + ", " + duration +" milliseconds");
    	
    	startTime = System.nanoTime();
    	trip = mine(graph);
    	endTime = System.nanoTime();
    	duration = (endTime - startTime) / 1000000;
    	System.out.println("mine: cost = " + trip.tripCost(graph) + ", " + duration +" milliseconds");
    }

    

}
