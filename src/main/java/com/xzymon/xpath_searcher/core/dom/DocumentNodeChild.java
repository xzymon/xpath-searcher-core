package com.xzymon.xpath_searcher.core.dom;

public interface DocumentNodeChild {
	DocumentNodeType getType();
	boolean isNode();
	boolean isText();
	void setParent(DocumentNode parent);
	DocumentNode getParent();
	StringBuffer stringifyNode(String baseTabString, String nextTabLevelString, boolean selfCloseIfPossible, boolean eachTagInNewLine, boolean removeWhitespaceOnlyTextNodes);
	
	/**
	 * Tworzy i zwraca kopię głęboką instancji z której wywołano metodę, wszakże z jednym wyjątkiem
	 * - uzyskana kopia nie ma ustawionej właściwości parent.
	 * @return kopia głęboka instancji z parent==null
	 */
	DocumentNodeChild copyInstance();
}
