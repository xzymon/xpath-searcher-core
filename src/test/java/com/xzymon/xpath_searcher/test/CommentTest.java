package com.xzymon.xpath_searcher.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channels;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xpath_searcher.core.XPathSearcher;
import com.xzymon.xpath_searcher.core.dom.DocumentTree;

public class CommentTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommentTest.class);
	
	//private String bigFile = "/test-nocomment.xml";
	//private String bigFile = "/comment-test.xml";
	private String bigFile = "/nocomment-test.xml";
	private XPathSearcher cSearcher;
	
	private InputStream srcStream;
	
	String source;
	String expected, actual;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		expected = "<root>\n</root>";
		source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n</root>";
		srcStream = new ByteArrayInputStream(source.getBytes());
		cSearcher = XPathSearcher.createFromXML(srcStream, null, true);
		
		DocumentTree tree = cSearcher.getDocumentTree();
		assertNotNull(tree);
		actual = tree.stringifyTree("\t", true, true, true).toString();
		assertEquals(expected, actual);
	}

}
