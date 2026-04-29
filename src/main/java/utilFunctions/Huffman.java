/*
  based on huffman.java from https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/Huffman.java.html
  modified to fit this project's needs
  using strings and bitsets instead of stdin and stdout
 */

import java.util.BitSet;
import java.util.PriorityQueue;


public class Huffman {
    private static final int R = 256;
    private Huffman() { }


    private static class Node implements Comparable<Node> {
        private final char ch;
        private final int freq;
        private final Node left, right;

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
    }

    public static void compress(String str) {
        char[] input = str.toCharArray();
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++){
            freq[input[i]]++;
	}
        Node root = buildTrie(freq);
        String[] st = new String[R];
        buildCode(st, root, "");
        writeTrie(root);
	StringBuilder binary = new StringBuilder();
        for (int i = 0; i < input.length; i++) {
            String code = st[input[i]];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    binary.append("0");
                }
                else if (code.charAt(j) == '1') {
                    binary.append("1");
                }
                else throw new IllegalStateException("Illegal state");
            }
        }
	
	String binaryString = binary.toString();
	BitSet bitSet = new BitSet(binaryString.length());
	for (int i = 0; i < binaryString.length(); i++) {
	    if (binaryString.charAt(i) == '1') {
		bitSet.set(i);
	    }
	}  
	System.out.println(bitSet);
    }

    private static Node buildTrie(int[] freq) {
        PriorityQueue<Node> pq = new PriorityQueue<Node>();
        for (char c = 0; c < R; c++)
            if (freq[c] > 0)
                pq.add(new Node(c, freq[c], null, null));
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
            System.out.println("1: " + x.ch);
            return;
        }
        System.out.printf("0");
        writeTrie(x.left);
        writeTrie(x.right);
    }
    private static void buildCode(String[] st, Node x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else {
            st[x.ch] = s;
        }
    }

    public static String expand(String Compressed, int[] freq) {
	StringBuilder expanded = new StringBuilder();
        Node root = buildTrie(freq);
        int length = Compressed.length();
	char[] chars = Compressed.toCharArray();
        for (int i = 0; i < length; i++) {
            Node x = root;
            while (!x.isLeaf()) {
                boolean bit = chars[i] == '1';
                if (bit) x = x.right;
                else     x = x.left;
            }
            expanded.append(x.ch);
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
