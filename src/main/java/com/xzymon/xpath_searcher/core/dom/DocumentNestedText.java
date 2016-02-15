package com.xzymon.xpath_searcher.core.dom;

public class DocumentNestedText implements DocumentNodeChild {
	private String text;
	private DocumentNode parent;

	public DocumentNestedText(String text) {
		this.text = text;
	}

	@Override
	public DocumentNodeType getType() {
		return DocumentNodeType.TEXT;
	}

	@Override
	public boolean isNode() {
		return false;
	}

	@Override
	public boolean isText() {
		return true;
	}

	public String toString() {
		return "DocumentNestedText text:[" + this.text + "]";
	}

	@Override
	public void setParent(DocumentNode parent) {
		this.parent = parent;
	}

	@Override
	public DocumentNode getParent() {
		return this.parent;
	}

	@Override
	public StringBuffer stringifyNode(String baseTabString, String nextTabLevelString, boolean selfCloseIfPossible,
			boolean eachTagInNewLine, boolean removeWhitespaceOnlyTextNodes) {
		String tempText = this.text;
		if (removeWhitespaceOnlyTextNodes) {
			tempText = tempText.trim();
		}
		int len = tempText.length();
		if (len == 0) {
			return null;
		} else {
			StringBuffer sb = new StringBuffer(len + baseTabString.length());
			sb.append(baseTabString).append(tempText);
			return sb;
		}
	}

	@Override
	public DocumentNodeChild copyInstance() {
		String text = new String(this.text);
		return new DocumentNestedText(text);
	}
}
