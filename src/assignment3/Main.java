/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * Mina Abbassian
 * mea2947
 * 16170
 * Abdullah Haris
 * ah52897
 * 16185
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/fall-2020-pr3-fa20-pr3-pair-35.git
 * Fall 2020
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	public static HashSet<String> visited = new HashSet<String>();
	public static Set<String> dict;
	public static String firstCopy;
	public static String secondCopy;
	
	
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		
		ArrayList<String> inputs = parse(kb);
		if(inputs != null) {
			String first = inputs.get(0);
			String second = inputs.get(1);
			
			//Printing the Word Ladder 
			//printLadder(getWordLadderBFS(first, second));
			printLadder(getWordLadderDFS(first, second));
		}
	}
	
	
	/**
	 * initialize 
	 * Initializes static variables or constants 
	 * Will call this method before running our JUNIT tests. 
	 * So call it only once at the start of main.
	 */
	public static void initialize() {
		dict = makeDictionary();
		firstCopy = "";
		secondCopy = "";
		visited.clear();
		//dict.add("*.*.*");
	}
	
	
	/**
	 * parse
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String start = keyboard.next(); //first input word
		
		//the command /quit must result in your program terminating with no further input
		if(start.equals("/quit")) {
			keyboard.close();
			return null;
		}
		
		String end = keyboard.next(); //second input word
		ArrayList<String> input = new ArrayList<String>(); 
		
		input.add(start.toUpperCase());
		input.add(end.toUpperCase());
		
		return input; //input contains start word and end word 
	}
	
	
	/**
	 * findNeighbors (Added)
	 * @param first String representing a word 
	 * @param second String representing the final word in the word ladder
	 * @return a Queue of Strings holding all words in the dictionary that differ 
	 * 		  	from the inputted word parameter by only one letter 
	 */
	public static Queue<String> findNeighbors(String first, String second) {
		//this will be returned, a Queue holding all of the neighbors of first word
		Queue<String> neighbors = new LinkedList<String>();
		
		//first check direct comparisons from changing letters of first word
		//	to letters of second word
		for(int j = 0; j < 5; j++) {
			String word = first;
			String last = second; 
			
			//changing the last letter
			if(j == 4) {
				word = word.substring(0, 4) + last.charAt(4);
			}
			
			//changing the first letter
			else if(j == 0) {
				word = last.charAt(0) + word.substring(1, 5);
			}
			
			//changing all letters in between
			else {
				word = word.substring(0, j) + last.charAt(j) + word.substring(j+1, 5);
			}
			
			//if it is a valid word and if the word has not already been visited
			if(dict.contains(word) && !visited.contains(word)) {
				//add the word to the neighbors queue
				neighbors.add(word);
			}
			
		}
		
		//checking the rest of the words in the dictionary
		for(int j = 0; j < 5; j++) {
			for(char letter = 'A'; letter <= 'Z'; letter++) {
				//a neighboring word 
				String n;
				
				//changing the last letter
				if(j == 4) {
					n = first.substring(0, 4) + letter;
				}
				
				//changing the first letter
				else if(j == 0) {
					n = letter + first.substring(1, 5);
				}
				
				//changing all letters in between
				else {
					n = first.substring(0, j) + letter + first.substring(j+1, 5);
				}
				
				//if it is a valid word and if the word has not already been visited
				if(dict.contains(n) && !visited.contains(n)) {
					//add the word to the neighbors queue
					neighbors.add(n);	
				}
				
			}
		}
		
		return neighbors;
	}
	
	
	/**
	 * getWordLadderDFSHelper (Added)
	 * Recursive implementation of DFS to find a Word Ladder between start and end words 
	 * @param word String representing a word
	 * @param otherWord String representing the end word in the Word Ladder
	 * @return ArrayList<String> holding the Word Ladder between the start and end words, if any
	 */
	private static ArrayList<String> getWordLadderDFSHelper(String word, String otherWord) {
	
		visited.add(word);
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(word);
		if(word.equals(otherWord)) {
			return ladder;
		}
		
		Queue<String> neighbors = findNeighbors(word, otherWord);
		if(neighbors.isEmpty()) {
			return null;
		}
		
		while(!neighbors.isEmpty()) {
			String neighbor = neighbors.remove();
			ArrayList<String> rec = getWordLadderDFSHelper(neighbor, otherWord); 
			
			if (rec != null) {
				ArrayList<String> output = ladder;
				output.addAll(rec);
				return output;
			}
	
		}
		return null;
	
	}
	
	
	/**
	 * getWordLadderDFS
	 * Gets a Word Ladder between the start and end words using DFS
	 * @param start String representing the start word in the Word Ladder
	 * @param end String representing the end word in the Word Ladder
	 * @return an ArrayList of Strings holding the Word Ladder between (and including)
	 * 			the start and end words
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		start = start.toUpperCase();
		end = end.toUpperCase();
		
		firstCopy = start.toUpperCase();
		secondCopy = end.toUpperCase();
		
		return getWordLadderDFSHelper(start, end);
	}
	
	
	/**
	 * getWordLadderBFS 
	 * Gets a Word Ladder between the start and end words using BFS
	 * @param start String representing the start word in the Word Ladder
	 * @param end String representing the end word in the Word Ladder
	 * @return an ArrayList of Strings holding the Word Ladder between (and including) 
	 * 			the start and end words 
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
 
    	start = start.toUpperCase();
    	end = end.toUpperCase();
    	
    	firstCopy = start;
    	secondCopy = end;
    	
    	visited.add(start);
    	
    	Queue<ArrayList<String>> q = new LinkedList<ArrayList<String>>();
    	q.add(new ArrayList<String>());
    	q.peek().add(start);
    	
    	while(!q.isEmpty()) {
    		ArrayList<String> current = q.remove();
    		String word = current.get(current.size()-1);
    		visited.add(word);
    		Queue<String> neighbors = findNeighbors(word, end); 
    		while(!neighbors.isEmpty()) {
    			String neighbor = neighbors.remove();
    			if(visited.contains(neighbor))
    				continue;
    			if (neighbor.equals(end)) {
    				ArrayList <String> ladder = new ArrayList<String>();
    				for (String w: current) {
    					ladder.add(w);
    				}
    				ladder.add(neighbor); //neighbor and end are the same thing here
    				visited.clear();
    				return ladder; 
    			}
    			ArrayList<String> nextIteration = new ArrayList<String>();
				for (String w: current) {
					nextIteration.add(w);
				}
				nextIteration.add(neighbor);
    			q.add(nextIteration);
    		}
    		
    	}
    	visited.clear();
    	return null; // replace this line later with real return
	}
    
	
    /**
     * printLadder
     * Prints the corresponding Word Ladder between the user's entered words in the correct format
     * @param ladder ArrayList<String> holding the Word Ladder 
     */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder == null) {
			System.out.println("no word ladder can be found between " + firstCopy.toLowerCase() + " and " + secondCopy.toLowerCase() + ".");
		} else {
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between " + firstCopy.toLowerCase() + " and " + secondCopy.toLowerCase() + ".");
			for (String w: ladder) {
				System.out.println(w.toLowerCase());
			}
		}
	}
	
	
	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
