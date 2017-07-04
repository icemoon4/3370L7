package edu.wit.cs.comp3370;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/* Finds the longest common substring in two text files 
 * 
 * Wentworth Institute of Technology
 * COMP 3370
 * Lab Assignment 7
 * Rachel Palmer
 */

public class LAB7 {
	
	//TODO Document this method
	public static String[] findLCSdyn(String text1, String text2) {
		// TODO Implement this method
		int m = text1.length()+1;
		int n = text2.length()+1;
		int[][] c = new int[m][n];
		int[][] b = new int[m][n];
		String[] alignment = new String[]{"",""};
		for(int i=0;i<m;i++){
			c[i][0] = 0;
		}
		for(int j=0;j<n;j++){
			c[0][j] = 0;
		}
		for(int i=1;i<m;i++){
			for(int j=1;j<n;j++){
				if(text1.charAt(i-1)==text2.charAt(j-1)){
					c[i][j] = c[i-1][j-1] + 1;
					b[i][j] = 'd'; //d for diagonal
				}
				else if(c[i-1][j] >= c[i][j-1]){
					c[i][j] = c[i-1][j];
					b[i][j] = 'u'; //u for up
				}
				else{
					c[i][j] = c[i][j-1];
					b[i][j] = 'l'; //l for left
				}
			}
		}
		longest = c[m-1][n-1];
		print(b,alignment,text1,text2,m-1,n-1);
		StringBuffer a0 = new StringBuffer(alignment[0]);
	    a0.reverse();
	    alignment[0] = a0.toString();
	    StringBuffer a1 = new StringBuffer(alignment[1]);
	    a1.reverse();
	    alignment[1] = a1.toString();
		return alignment;
	}
	
	public static void print(int b[][], String[] alignment, String text1, String text2, int i, int j){
		while(i>=0 && j>=0){
			if(i==0){
				while(j!=0){
					alignment[0] += '-';
					alignment[1] += text2.charAt(j-1);
					j--;
				}
				return;
			}
			else if(j==0){
				while(i!=0){
					alignment[0] += text1.charAt(i-1);
					alignment[1] += '-';
					i--;
				}
				return;
			}
			else{
				if(b[i][j] == 'd'){
					alignment[0] += text1.charAt(i-1);
					alignment[1] += text2.charAt(j-1);
					i--;
					j--;
				}
				else if(b[i][j] == 'u'){
					alignment[0] += text1.charAt(i-1);
					alignment[1] += '-';
					i--;
				}
				else{
					alignment[0] += '-';
					alignment[1] += text2.charAt(j-1);
					j--;
				}
			}
		}
	}
	
	/********************************************
	 * 
	 * You shouldn't modify anything past here
	 * 
	 ********************************************/
	
	private static int longest = 0;
	
	// recursive helper for DFS
	private static void dfs_solve(int i1, int i2, String s1, String s2, char[] out1, char[] out2, int score, int index) {
	  
	  if ((i1 >= s1.length()) && (i2 >= s2.length())) {
	    if (score > longest) {
	      out1[index] = '\0';
	      out2[index] = '\0';
	      longest = score;
	      sol1 = String.valueOf(out1).substring(0, String.valueOf(out1).indexOf('\0'));
	      sol2 = String.valueOf(out2).substring(0, String.valueOf(out2).indexOf('\0'));
	    }
	  }
	  else if ((i1 >= s1.length()) && (i2 < s2.length())) {	// at the end of first string
	    out1[index] = '-';
	    out2[index] = s2.charAt(i2);
	    dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index+1);
	  }
	  else if ((i1 < s1.length()) && (i2 >= s2.length())) {	// at the end of second string
	    out1[index] = s1.charAt(i1);
	    out2[index] = '-';
	    dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index+1);
	  }
	  else {
	    if (s1.charAt(i1) == s2.charAt(i2)) {	// matching next character
	      out1[index] = s1.charAt(i1);
	      out2[index] = s2.charAt(i2);
	      dfs_solve(i1 + 1, i2 + 1, s1, s2, out1, out2, score + 1, index + 1);
	    }
	    
	    out1[index] = '-';
	    out2[index] = s2.charAt(i2);
	    dfs_solve(i1, i2 + 1, s1, s2, out1, out2, score, index + 1);
	    
	    out1[index] = s1.charAt(i1);
	    out2[index] = '-';
	    dfs_solve(i1 + 1, i2, s1, s2, out1, out2, score, index + 1);
	  }

	}

	// Used for DFS solution
	private static String sol1, sol2;

	// recursively searches for longest substring, checking all possible alignments
	public static String[] findLCSdfs(String text1, String text2) {
	  int max_len = text1.length() + text2.length() + 1;
	  char[] out1 = new char[max_len];
	  char[] out2 = new char[max_len];
	  
	  dfs_solve(0, 0, text1, text2, out1, out2, 0, 0);
	  
	  String[] ret = new String[2];
	  ret[0] = sol1; ret[1] = sol2;
	  return ret; 
	}	
	
	// returns the length of the longest string
	public static int getLongest() {
		return longest;
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String file1, file2, text1 = "", text2 = "";
		System.out.printf("Enter <text1> <text2> <algorithm>, ([dfs] - depth first search, [dyn] - dynamic programming): ");
		System.out.printf("(e.g: text/a.txt text/b.txt dfs)\n");
		file1 = s.next();
		file2 = s.next();
		
		try {
			text1 = new String(Files.readAllBytes(Paths.get(file1)));
			text2 = new String(Files.readAllBytes(Paths.get(file2)));
		} catch (IOException e) {
			System.err.println("Cannot open files " + file1 + " and " + file2 + ". Exiting.");
			System.exit(0);
		}
		
		String algo = s.next();
		String[] result = {""};
		
		switch (algo) {
		case "dfs":
			result = findLCSdfs(text1, text2);
			break;
		case "dyn":
			result = findLCSdyn(text1, text2);
			break;
		default:
			System.out.println("Invalid algorithm");
			System.exit(0);
			break;
		}
		
		s.close();
		
		System.out.printf("Best cost: %d\nLongest string alignment:\n%s\n\n%s\n", longest, result[0], result[1]);	
	}
}
