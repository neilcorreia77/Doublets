import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Iterator;

import java.util.stream.Collectors;

/**
 * Provides an implementation of the WordLadderGame interface. 
 *
 * @author Your Name (you@auburn.edu)
 */
public class Doublets implements WordLadderGame {
   HashSet<String> lexicon;
   private static final List<String> nulll = new ArrayList<String>();
   

    // The word list used to validate words.
    // Must be instantiated and populated in the constructor.
    /////////////////////////////////////////////////////////////////////////////
    // DECLARE A FIELD NAMED lexicon HERE. THIS FIELD IS USED TO STORE ALL THE //
    // WORDS IN THE WORD LIST. YOU CAN CREATE YOUR OWN COLLECTION FOR THIS     //
    // PURPOSE OF YOU CAN USE ONE OF THE JCF COLLECTIONS. SUGGESTED CHOICES    //
    // ARE TreeSet (a red-black tree) OR HashSet (a closed addressed hash      //
    // table with chaining).
    /////////////////////////////////////////////////////////////////////////////

    /**
     * Instantiates a new instance of Doublets with the lexicon populated with
     * the strings in the provided InputStream. The InputStream can be formatted
     * in different ways as long as the first string on each line is a word to be
     * stored in the lexicon.
     */
   public Doublets(InputStream in) {
      try {
         lexicon = new HashSet<String>();
             
         Scanner s =
                new Scanner(new BufferedReader(new InputStreamReader(in)));
         while (s.hasNext()) {
            String str = s.next();
            lexicon.add(str.toUpperCase());
            s.nextLine();
         }
         in.close();
      }
      catch (java.io.IOException e) {
         System.err.println("Error reading from InputStream.");
         System.exit(1);
      }
   }


    /**
     * Returns the total number of words in the current lexicon.
     *
     * @return number of words in the lexicon
     */
   public int getWordCount() {
      return lexicon.size();
   }
    
    /**
     * Checks to see if the given string is a word.
     *
     * @param  str the string to check
     * @return     true if str is a word, false otherwise
     */
   public boolean isWord(String str) {
      str = str.toUpperCase();
      return lexicon.contains(str);
    
   }
    
    /**
     * Returns the Hamming distance between two strings, str1 and str2. The
     * Hamming distance between two strings of equal length is defined as the
     * number of positions at which the corresponding symbols are different. The
     * Hamming distance is undefined if the strings have different length, and
     * this method returns -1 in that case. See the following link for
     * reference: https://en.wikipedia.org/wiki/Hamming_distance
     *
     * @param  str1 the first string
     * @param  str2 the second string
     * @return      the Hamming distance between str1 and str2 if they are the
     *                  same length, -1 otherwise
     */
   public int getHammingDistance(String str1, String str2) {
      str1 = str1.toUpperCase();
      str2 = str2.toUpperCase();
      if (str1.length() != str2.length()) {
         return -1;
      }
      int count = 0;
      for (int i = 0; i < str1.length(); i++) {
         if (str1.charAt(i) != str2.charAt(i)) {
            count++;
         }
      
      }
      return count;
    
   }
    
    /**
     * Returns all the words that have a Hamming distance of one relative to the
     * given word.
     *
     * @param  word the given word
     * @return      the neighbors of the given word
     */
   public List<String> getNeighbors(String word) { 
      List<String> relative = new ArrayList<String>();
      if (!isWord(word)) {
         return relative;
      }
      Iterator<String> itr = lexicon.iterator();
      String rel;
      while (itr.hasNext()) {
         rel = itr.next();
         if (getHammingDistance(word, rel) == 1) {
            relative.add(rel);
         }
      
      }
      return relative;
   }
    
    /**
     * Checks to see if the given sequence of strings is a valid word ladder.
     *
     * @param  sequence the given sequence of strings
     * @return          true if the given sequence is a valid word ladder,
     *                       false otherwise
     */
   public boolean isWordLadder(List<String> sequence) {
      if (sequence == null) {
         return false;
      }
      
      if (sequence.isEmpty()) {
         return false;
      }
      
      for (String element: sequence) {
         if (!isWord(element)) {
            return false;
         }
      }
      for (int i = 0; i < sequence.size() - 1; i++) {
         if (getHammingDistance(sequence.get(i), sequence.get(i+1)) != 1) {
            return false;
         }
         
      }
      return true;
   }
   
   public List<String> getMinLadder(String start, String end) {
      start = start.toUpperCase();
      end = end.toUpperCase();
      ArrayList<String> s = new ArrayList<String>();
      List<String> minLadder = new ArrayList<String>();
      
      if (start.equals(end)) {
         minLadder.add(start);
         return minLadder;
      }
      
      if (getHammingDistance(start, end) == -1) {
         return nulll;
      }
      
      if (isWord(start) && isWord(end)) {
         s = breathFirstSearch(start, end);
         
      }
      if (s.isEmpty()) {
         return nulll;
      }
      
      for (int i = s.size() - 1; i >= 0; i--) {
         minLadder.add(s.get(i));
      }
      return minLadder;
   
   }
   
   private ArrayList<String> breathFirstSearch(String start, String end) {
      Deque<Node> queue = new ArrayDeque<Node>();
      HashSet<String> bus = new HashSet<String>();
      ArrayList back = new ArrayList<String>();
      bus.add(start);
      queue.addLast(new Node(start, null));
      Node endOfNode = new Node(end, null);
      outerloop:
      while (!queue.isEmpty()) {
         Node node = queue.removeFirst();
         String element = node.element;
         List<String> neighbors = getNeighbors(element);
         for (String elementt: neighbors) {
            if (!bus.contains(elementt)) {
               bus.add(elementt);
               queue.addLast(new Node(elementt, node));
               if (elementt.equals(end)) {
                  endOfNode.previous = node;
                  break outerloop;
               }
            
            }
         
         }
      }
      if (endOfNode.previous == null) {
         return back;
      }
      Node n = endOfNode;
      while (n != null) {
         back.add(n.element);
         n = n.previous;
      }
      return back;
   
   
   }
   
   private class Node {
      String element;
      Node previous;
   
      public Node(String e, Node prev) {
         element = e;
         previous = prev;
      
      }
   
   }
    


}


