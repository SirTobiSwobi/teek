package org.SirTobiSwobi.c3.teek.core;

import static org.junit.Assert.*;

import org.SirTobiSwobi.c3.teek.core.WordMoverDistance;
import org.SirTobiSwobi.c3.teek.db.WordEmbedding;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestWordMoverDistance {
	
	private WordEmbedding wordEmbedding;
	private WordMoverDistance wmd;

	@Before
	public void setUp() throws Exception {
		wordEmbedding = WordEmbedding.buildFromLocalFile("/opt/wordembeddings/skip-gram-wiki1stbill.txt");
		wmd = new WordMoverDistance(wordEmbedding);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		String a="Test string";
		String b="string test";
		assertTrue("Distance between '"+a+" and '"+b+"' is "+wmd.getDistance(a, b)+" but should be 0.0",wmd.getDistance(a, b)==0.0);
		b = "glass";
		double b1 = wmd.getDistance("Test", b);
		double b2 = wmd.getDistance("string", b);
		assertTrue("WMD of one term to one text should be minimum term distance. WMD = "+wmd.getDistance(a, b)+" b1="+b1+" b2="+b2,wmd.getDistance(a, b)==Math.min(b1, b2));
		assertTrue("WMD of one term to one text should be minimum term distance. WMD = "+wmd.getDistance(b, a)+" b1="+b1+" b2="+b2,wmd.getDistance(b, a)==Math.min(b1, b2));
		b = "the quick brown fox jumps over the lazy rabbit";
		assertTrue("WMD of text is sum of individual wmds: ",wmd.getDistance("Test", b)+wmd.getDistance("string", b)==wmd.getDistance(a, b));
		
		
	}

}
