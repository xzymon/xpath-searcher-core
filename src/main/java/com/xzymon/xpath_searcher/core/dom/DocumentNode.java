package com.xzymon.xpath_searcher.core.dom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.xzymon.xpath_searcher.core.dom.representation.DocumentNodeRepresentation;
import com.xzymon.xpath_searcher.core.dom.representation.DocumentTreeRepresentation;
import com.xzymon.xpath_searcher.core.parser.AttributeRepresentation;
import com.xzymon.xpath_searcher.core.parser.HalfElementRepresentation;

public class DocumentNode implements DocumentNodeChild {
	private DocumentNode parent;
	private String name;
	private LinkedList<DocumentNodeChild> children;
	private TraversingMode traversingMode;
	private int textChilds, nodeChilds;
	private Map<String, String> attributes;

	private DocumentNode(String nodeName, Map<String, String> attributes) {
		this.name = nodeName;
		if (attributes != null) {
			this.attributes = attributes;
		} else {
			this.attributes = new HashMap<>();
		}
		this.children = new LinkedList<>();
	}
	
	public static DocumentNode createInitialDocumentNode(String name){
		if(name==null || name.isEmpty()){
			throw new NullPointerException("name parameter is null or is empty");
		}
		//TODO przydałoby się testowanie czy nazwa składa się z tu XMLowo dopuszczalnych znaków - [A-Z][a-z][0-9] i zaczyna się od [A-Z][a-z]
		//if(name)
		return new DocumentNode(name, null);
	}

	public static DocumentNode createDocumentNodeFromAttributeRepresentationList(String name, DocumentTreeRepresentation treeRepr, List<AttributeRepresentation> attrsList){
		if(attrsList!=null && !attrsList.isEmpty()){
			Map<String, String> attrs = new HashMap<String, String>();
			String value = null;
			for(AttributeRepresentation ar : attrsList){
				value = HalfElementRepresentation.fromSavedChars(treeRepr.getSavedSource(), ar.getStartQuotationMarkAt()+1, ar.getEndQuotationMarkAt()-1);
				attrs.put(ar.getName(), value);
			}
			return new DocumentNode(name, attrs);
		} else {
			return new DocumentNode(name, null);
		}
	}
	
	@Override
	public DocumentNodeType getType() {
		return DocumentNodeType.NODE;
	}

	public String getName() {
		return this.name;
	}

	public DocumentNodeChild getNthChild(int n) {
		DocumentNodeChild dnc = null, result = null;
		if (n > -1 && n < childrenCount()) {
			switch (this.traversingMode) {
			case FORWARD:
				result = this.children.get(n);
				break;
			case BACKWARD:
				Iterator<DocumentNodeChild> it = this.children.descendingIterator();
				int pos = -1;
				while (it.hasNext() && pos < n) {
					dnc = it.next();
					pos++;
				}
				if (pos == n) {
					result = dnc;
				}
				break;
			}
		}
		return result;
	}

	public DocumentNode getNthNode(int n) {
		if (n > -1 && n < this.nodeChilds) {
			Iterator<DocumentNodeChild> it = null;
			DocumentNodeChild dnc = null;
			int pos = -1;
			switch (this.traversingMode) {
			case FORWARD:
				it = this.children.iterator();
				while (it.hasNext() && pos < n) {
					dnc = it.next();
					if (DocumentNodeType.NODE.equals(dnc.getType())) {
						pos++;
					}
				}
			case BACKWARD:
				it = this.children.descendingIterator();
				while (it.hasNext() && pos < n) {
					dnc = it.next();
					if (DocumentNodeType.NODE.equals(dnc.getType())) {
						pos++;
					}
				}
			}
			if (pos == n) {
				return (DocumentNode) dnc;
			}
		} else {
			throw new IndexOutOfBoundsException(
					"parameter: n=" + n + ", but node has only " + this.nodeChilds + " NODE children");
		}
		return null;
	}

	public DocumentNestedText getNthText(int n) {
		if (n > -1 && n < this.textChilds) {
			Iterator<DocumentNodeChild> it = null;
			DocumentNodeChild dnc = null;
			int pos = -1;
			switch (this.traversingMode) {
			case FORWARD:
				it = this.children.iterator();
				while (it.hasNext() && pos < n) {
					dnc = it.next();
					if (DocumentNodeType.TEXT.equals(dnc.getType())) {
						pos++;
					}
				}
			case BACKWARD:
				it = this.children.descendingIterator();
				while (it.hasNext() && pos < n) {
					dnc = it.next();
					if (DocumentNodeType.TEXT.equals(dnc.getType())) {
						pos++;
					}
				}
			}
			if (pos == n) {
				return (DocumentNestedText) dnc;
			}
		} else {
			throw new IndexOutOfBoundsException(
					"parameter: n=" + n + ", but node has only " + this.textChilds + " TEXT children");
		}
		return null;
	}

	public DocumentNode forward() {
		this.traversingMode = TraversingMode.FORWARD;
		return this;
	}

	public DocumentNode backward() {
		this.traversingMode = TraversingMode.BACKWARD;
		return this;
	}

	public int childrenCount() {
		return this.textChilds + this.nodeChilds;
	}

	/**
	 * Uwaga - ta metoda nie dokonuje ustawienia parent dla child - jedynie ustawia child jako dziecko tego węzła
	 * @param n
	 * @param child
	 * @return
	 */
	public boolean addNthChild(int n, DocumentNodeChild child) {
		if (child != null && n > -1 && n < childrenCount() + 1) {
			this.children.add(n, child);
			switch (child.getType()) {
			case NODE:
				this.nodeChilds++;
				break;
			case TEXT:
				this.textChilds++;
				break;
			}
			return true;
		}
		return false;
	}

	/**
	 * Uwaga - ta metoda nie dokonuje ustawienia parent dla child - jedynie ustawia child jako dziecko tego węzła
	 * @param child
	 * @return
	 */
	public boolean addFirstChild(DocumentNodeChild child) {
		return addNthChild(0, child);
	}

	/**
	 * Uwaga - ta metoda nie dokonuje ustawienia parent dla child - jedynie ustawia child jako dziecko tego węzła
	 * @param child
	 * @return
	 */
	public boolean addLastChild(DocumentNodeChild child) {
		return addNthChild(childrenCount(), child);
	}

	public void removeNthChild(int n) {
		if (n > -1 && n < childrenCount()) {
			Iterator<DocumentNodeChild> it = null;
			int pos = -1;
			DocumentNodeChild dnc = null;
			switch (this.traversingMode) {
			case FORWARD:
				it = this.children.iterator();
				break;
			case BACKWARD:
				it = this.children.descendingIterator();
				break;
			}
			while (it.hasNext() && pos < n) {
				dnc = it.next();
				pos++;
			}
			if (dnc!=null && pos == n) {
				it.remove();
				switch (dnc.getType()) {
				case NODE:
					this.nodeChilds--;
					break;
				case TEXT:
					this.textChilds--;
					break;
				}
			}
		}
	}

	public void removeNthNode(int n) {
		if (n > -1 && n < this.nodeChilds) {
			Iterator<DocumentNodeChild> it = null;
			int pos = -1;
			DocumentNodeChild dnc = null;
			switch (this.traversingMode) {
			case FORWARD:
				it = this.children.iterator();
				break;
			case BACKWARD:
				it = this.children.descendingIterator();
				break;
			}
			while (it.hasNext() && pos < n) {
				dnc = it.next();
				if(dnc.isNode()){
					pos++;
				}
			}
			if (dnc!=null && pos == n) {
				it.remove();
				this.nodeChilds--;
			}
		}
	}

	public void removeNthText(int n) {
		if (n > -1 && n < this.textChilds) {
			Iterator<DocumentNodeChild> it = null;
			int pos = -1;
			DocumentNodeChild dnc = null;
			switch (this.traversingMode) {
			case FORWARD:
				it = this.children.iterator();
				break;
			case BACKWARD:
				it = this.children.descendingIterator();
				break;
			}
			while (it.hasNext() && pos < n) {
				dnc = it.next();
				if(dnc.isText()){
					pos++;
				}
			}
			if (dnc!=null && pos == n) {
				it.remove();
				this.textChilds--;
			}
		}
	}

	public String getAttributeValue(String key) {
		return attributes.get(key);
	}

	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

	public void putAttribute(String key, String value) {
		attributes.put(key, value);
	}

	public int attributesCount() {
		return attributes.size();
	}

	@Override
	public void setParent(DocumentNode parentNode) {
		this.parent = parentNode;
	}

	@Override
	public DocumentNode getParent() {
		return this.parent;
	}

	@Override
	public boolean isNode() {
		return true;
	}

	@Override
	public boolean isText() {
		return false;
	}
	
	@Override
	public String toString(){
		return "DocumentNode name:" + this.getName() + " attributes:" + this.attributesCount() + " children::nodes:" + this.nodeChilds + " children::texts:" + this.textChilds;
	}

	@Override
	public StringBuffer stringifyNode(String baseTabString, String nextTabLevelString, boolean selfCloseIfPossible, boolean eachTagInNewLine, boolean removeWhitespaceOnlyTextNodes) {
		StringBuffer sb = new StringBuffer();
		StringBuffer textSb = null;
		boolean canBeSelfClosed = this.childrenCount()==0 && selfCloseIfPossible;
		sb.append(baseTabString).append('<').append(this.name);
		if(!this.attributes.isEmpty()){
			for(Entry<String, String> ent : this.attributes.entrySet()){
				sb.append(" ").append(ent.getKey()).append('=').append('\"').append(ent.getValue()).append('\"');
			}
		}
		if(canBeSelfClosed){
			sb.append(" /");
		}
		sb.append('>');
		if(!canBeSelfClosed){
			if(eachTagInNewLine){
				sb.append('\n');
			}
			for(DocumentNodeChild child : this.children){
				textSb = child.stringifyNode(baseTabString + nextTabLevelString, nextTabLevelString, selfCloseIfPossible, eachTagInNewLine, removeWhitespaceOnlyTextNodes);
				if(textSb!=null){
					sb.append(textSb);
					if(eachTagInNewLine){
						sb.append('\n');
					}
				}
			}
			sb.append(baseTabString).append('<').append('/').append(this.name).append('>');
		}
		return sb;
	}

	@Override
	public DocumentNodeChild copyInstance() {
		//skopiować kolekcje i wartości z wyjątkiem parent
		return null;
	}
}
