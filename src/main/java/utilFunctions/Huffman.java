/*
  based on huffman.java from https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Huffman.java.html
  modified to fit this project's needs
  using strings and bitsets instead of stdin and stdout
 */

import java.util.BitSet;
import java.util.PriorityQueue;
import java.util.ArrayList;


public class Huffman {
    private static final int R = 256;
    private Huffman() { }

    public static void main(String[] args)
    {
	String initial_string ="Compression string test";
	Huffman hf = new Huffman();
	String compressed = hf.compress(initial_string);
	assert(compressed.equals("111100100011001000010111101101110010101001111010000011101010111110111001011110100"));
	int[] freq = generateFreq(initial_string.toCharArray());
	String decompressed = expand(compressed, freq);
	assert(compressed.equals(decompressed));
	
    }

    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;
	private String code;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        public int compareTo(Node that) {
            return this.freq - that.freq;
        }

	public String toString(){
	    if (this.isLeaf())
		return "[" +this.ch + ":" + this.freq+"]";
	    return "("+this.freq+":"+this.left.toString()+":"+this.right.toString()+")";
		    
	}
    }

    public static int[] generateFreq(char[] str)
    {
        int[] freq = new int[R];
        for (int i = 0; i < str.length; i++){
            freq[str[i]]++;
	}
	return freq;
    }


    private static ArrayList<Node> getLeafNodes(Node root){
	if (root.isLeaf()){
	    ArrayList<Node> ret = new ArrayList<Node>();
	    ret.add(root);
	    return ret;
	}
	ArrayList<Node> ret = new ArrayList<Node>();
	ret.addAll(getLeafNodes(root.left));
	ret.addAll(getLeafNodes(root.right));
	return ret;
    }
    
    public static String compress(String str) {
	char[] input = str.toCharArray();
        int[] freq = generateFreq(input);
        Node root = buildTrie(freq);
        String[] st = new String[R];
        buildCode(st, root, "");
        writeTrie(root);
	StringBuilder binary = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
	    ArrayList<Node> leaves = getLeafNodes(root);
	    for (int x = 0; x < leaves.size(); x++){
		if (leaves.get(x).ch == input[i])
		    binary.append(leaves.get(x).code);
		}
	}
        
	
	String binaryString = binary.toString();
	return binaryString;
    }

    public static BitSet stringToBitSet(String str)
    {
	BitSet bitSet = new BitSet(str.length());
	for (int i = 0; i < str.length(); i++) {
	    if (str.charAt(i) == '1') {
		bitSet.set(i);
	    }
	}
	return bitSet;
    }
    
    private static Node buildTrie(int[] freq) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        for (char c = 0; c < R; c++){
            if (freq[c] > 0)
                pq.add(new Node(c, freq[c], null, null));
	}

        while (pq.size() > 1) {
	    Node left  = pq.poll();
            Node right = pq.poll();
	    
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.add(parent);
	   
        }
        return pq.poll();
    }

    private static void writeTrie(Node x) {
        if (x.isLeaf()) {
            System.out.println(x.ch + ": " + x.code);
            return;
        }
        writeTrie(x.left);
        writeTrie(x.right);
    }
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
	   
	    x.code = s;
            st[x.ch] = s;
        }
    }

    public static String expand(String Compressed, int[] freq) {
	StringBuilder expanded = new StringBuilder();
	StringBuilder tmp = new StringBuilder();
        Node root = buildTrie(freq);
	String[] st = new String[R];
	buildCode(st, root, "");
        int length = Compressed.length();
	char[] chars = Compressed.toCharArray();
	ArrayList<Node> leaves = getLeafNodes(root);
        for (int i = 0; i < length; i++) {
	    tmp.append(chars[i]);
            for (int j = 0; j < leaves.size(); j++){
		if(leaves.get(j).code.equals(tmp.toString())){
		    tmp.setLength(0);
		    expanded.append(leaves.get(j).ch);
		    break;
		}
	    }
        }
	return expanded.toString();
    }


    // private static Node readTrie() {
    //     boolean isLeaf = BinaryStdIn.readBoolean();
    //     if (isLeaf) {
    //         return new Node(BinaryStdIn.readChar(), -1, null, null);
    //     }
    //     else {
    //         return new Node('\0', -1, readTrie(), readTrie());
    //     }
    // }
}
