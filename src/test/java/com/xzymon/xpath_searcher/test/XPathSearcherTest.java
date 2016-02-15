package com.xzymon.xpath_searcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;

import javax.xml.xpath.XPathExpressionException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xpath_searcher.core.XPathSearcher;

public class XPathSearcherTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(XPathSearcherTest.class);

	private String xmlFile = "/comment-test.xml";
	private String htmlFile = "/test.html";
	
	private XPathSearcher xmlSearcher;
	private XPathSearcher htmlSearcher;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		InputStream xmlIs = Object.class.getResourceAsStream(xmlFile);
		//InputStream htmlIs = Object.class.getResourceAsStream(htmlFile);
		
		xmlSearcher = XPathSearcher.createFromXML(xmlIs, null, true);
		//htmlSearcher = XPathSearcher.createFromHTML(htmlIs, null);
	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testHasExpression() {
		String expression = "//*";
		
		try {
			xmlSearcher.newSearch(expression);
			assertTrue(xmlSearcher.hasExpression());
		} catch (XPathExpressionException e) {
			fail("XPathExpressionException occured on xml");
			e.printStackTrace();
		} catch (NullPointerException ne){
			ne.printStackTrace();
			fail("NullPointer on xml");
		}
		
		try {
			htmlSearcher.newSearch(expression);
			assertTrue(htmlSearcher.hasExpression());
		} catch (XPathExpressionException e) {
			fail("XPathExpressionException occured on html");
			e.printStackTrace();
		} catch (NullPointerException ne){
			ne.printStackTrace();
			fail("NullPointer on html");
		}
	}

	@Test
	public void testGetExpression() {
		String expression = "//*";
		
		try {
			xmlSearcher.newSearch(expression);
			assertEquals(expression, xmlSearcher.getExpression());
		} catch (XPathExpressionException e) {
			fail("XPathExpressionException occured on xml");
			//LOGGER.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (NullPointerException ne){
			ne.printStackTrace();
			fail("NullPointer on xml");
		}
		
		/*
		try {
			htmlSearcher.newSearch(expression);
			assertEquals(expression, htmlSearcher.getExpression());
		} catch (XPathExpressionException e) {
			fail("XPathExpressionException occured on html");
			e.printStackTrace();
		} catch (NullPointerException ne){
			ne.printStackTrace();
			fail("NullPointer on html");
		}
		*/
	}

	//@Test
	public void testNewSearch() {
		try {
			assertEquals(11, xmlSearcher.newSearch("//property"));
		} catch (XPathExpressionException e) {
			fail("XPathExpressionException occured on xml");
			e.printStackTrace();
		} catch (NullPointerException ne){
			ne.printStackTrace();
			fail("NullPointer on xml");
		}
		
		try {
			assertEquals(1, htmlSearcher.newSearch("//body"));
		} catch (XPathExpressionException e) {
			fail("XPathExpressionException occured on html");
			e.printStackTrace();
		} catch (NullPointerException ne){
			ne.printStackTrace();
			fail("NullPointer on html");
		}
	}

	//@Test
	public void testNextNode() {
		
	}

	//@Test
	public void testReset() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testClear() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testIsEmpty() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testGetData() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testGetFoundNodeList() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testGetSearchingListeners() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testAddSearchingListener() {
		//fail("Not yet implemented"); // TODO
	}

	//@Test
	public void testRemoveSearchingListener() {
		//fail("Not yet implemented"); // TODO
	}

}
