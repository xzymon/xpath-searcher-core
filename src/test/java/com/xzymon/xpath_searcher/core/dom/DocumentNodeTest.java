package com.xzymon.xpath_searcher.core.dom;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DocumentNodeTest {
	private String actual, expected;
	private DocumentNode aNode, bNode, cNode;
	private DocumentNestedText spaceTest, commentedSpaceTest;
	private DocumentNodeChild child;

	@Before
	public void setUp() throws Exception {
		aNode = DocumentNode.createInitialDocumentNode("a");
		bNode = DocumentNode.createInitialDocumentNode("b");
		cNode = DocumentNode.createInitialDocumentNode("c");
		spaceTest = new DocumentNestedText(" ");
		commentedSpaceTest = new DocumentNestedText("<!-- -->");
	}

	// @Test
	public void initialTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a />";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a />";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a />";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a />";
		assertEquals(expected, actual);
	}

	// @Test
	public void singleNestedTextTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		child = new DocumentNestedText(" ");
		aNode.addLastChild(child);
		assertTrue(aNode.childrenCount() == 1);
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a> </a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = new DocumentNestedText(" ");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a> </a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = new DocumentNestedText(" ");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a>\n \n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = new DocumentNestedText(" ");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n\t \n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = new DocumentNestedText(" ");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a>\t </a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = new DocumentNestedText(" ");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a>\n\t \n</a>";
		assertEquals(expected, actual);
	}

	// @Test
	public void singleNestedNodeTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		child = DocumentNode.createInitialDocumentNode("b");
		aNode.addLastChild(child);
		assertTrue(aNode.childrenCount() == 1);
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a><b></b></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = DocumentNode.createInitialDocumentNode("b");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a><b /></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = DocumentNode.createInitialDocumentNode("b");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a>\n<b />\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = DocumentNode.createInitialDocumentNode("b");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n\t<b>\n\t</b>\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = DocumentNode.createInitialDocumentNode("b");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a>\t<b /></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		child = DocumentNode.createInitialDocumentNode("b");
		aNode.addLastChild(child);
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a>\n\t<b />\n</a>";
		assertEquals(expected, actual);
	}

	// @Test
	public void twoNestedTextTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		assertTrue(aNode.childrenCount() == 2);
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a> <!-- --></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a> <!-- --></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a>\n \n<!-- -->\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n\t \n\t<!-- -->\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a>\t \t<!-- --></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a>\n\t \n\t<!-- -->\n</a>";
		assertEquals(expected, actual);
	}

	// @Test
	public void twoNestedNodeTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		assertTrue(aNode.childrenCount() == 2);
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a><b></b><c></c></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a><b /><c /></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a>\n<b />\n<c />\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n\t<b>\n\t</b>\n\t<c>\n\t</c>\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a>\t<b />\t<c /></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a>\n\t<b />\n\t<c />\n</a>";
		assertEquals(expected, actual);
	}

	//@Test
	public void threeNestedMixedTwoTextTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		assertTrue(aNode.childrenCount() == 3);
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a> <b></b><!-- --></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a> <b /><!-- --></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a>\n \n<b />\n<!-- -->\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n\t \n\t<b>\n\t</b>\n\t<!-- -->\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a>\t \t<b />\t<!-- --></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText("<!-- -->"));
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a>\n\t \n\t<b />\n\t<!-- -->\n</a>";
		assertEquals(expected, actual);
	}

	//@Test
	public void threeNestedMixedTwoNodesTest() {
		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		assertTrue(aNode.childrenCount() == 3);
		actual = aNode.stringifyNode("", "", false, false, true).toString();
		expected = "<a><b></b> <c></c></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "", true, false, true).toString();
		expected = "<a><b /> <c /></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "", true, true, true).toString();
		expected = "<a>\n<b />\n \n<c />\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "\t", false, true, true).toString();
		expected = "<a>\n\t<b>\n\t</b>\n\t \n\t<c>\n\t</c>\n</a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "\t", true, false, true).toString();
		expected = "<a>\t<b />\t \t<c /></a>";
		assertEquals(expected, actual);

		aNode = DocumentNode.createInitialDocumentNode("a");
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("b"));
		aNode.addLastChild(new DocumentNestedText(" "));
		aNode.addLastChild(DocumentNode.createInitialDocumentNode("c"));
		actual = aNode.stringifyNode("", "\t", true, true, true).toString();
		expected = "<a>\n\t<b />\n\t \n\t<c />\n</a>";
		assertEquals(expected, actual);
	}
}
