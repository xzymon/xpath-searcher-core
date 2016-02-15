package com.xzymon.xpath_searcher.test;

import static org.junit.Assert.fail;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.xzymon.xpath_searcher.core.XPathSearcher;

public class XPathSearcher_BigTest {
	private String bigFile = "/bigFile.html";
	private XPathSearcher bigSearcher;
	
	@Before
	public void setUp() throws Exception {
		InputStream bigIs = Object.class.getResourceAsStream(bigFile);
		
		bigSearcher = XPathSearcher.createFromHTML(bigIs, null);
	}

	@Test
	public void test() {
		
	}

}
