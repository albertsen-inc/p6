import utilFunctions.Huffman;
public class huffmanTest{
    public CompressionTest(){ String initial_string ="Compression
	string test"; Huffman hf = new Huffman(); String compressed =
	hf.compress(initial_string);
	assert(compressed.equals("111100100011001000010111101101110010101001111010000011101010111110111001011110100"));
	int[] freq = generateFreq(initial_string.toCharArray());
	String decompressed = expand(compressed, freq);
	assert(compressed.equals(decompressed));
    }
}
