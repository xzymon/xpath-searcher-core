package com.xzymon.xpath_searcher.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.xzymon.xpath_searcher.core.dom.DocumentTree;
import com.xzymon.xpath_searcher.core.dom.representation.DocumentTreeRepresentation;
import com.xzymon.xpath_searcher.core.dom.representation.HTMLDefaultOrphanedPolicies;
import com.xzymon.xpath_searcher.core.dom.representation.NodeRepresentation;
import com.xzymon.xpath_searcher.core.dom.representation.OrphanedPolicies;
import com.xzymon.xpath_searcher.core.dom.representation.XMLDefaultOrphanedPolicies;
import com.xzymon.xpath_searcher.core.exception.BuildingDOMException;
import com.xzymon.xpath_searcher.core.exception.ParserException;
import com.xzymon.xpath_searcher.core.listener.BindingListener;
import com.xzymon.xpath_searcher.core.listener.ParserListener;
import com.xzymon.xpath_searcher.core.listener.XPathSearchingListener;
import com.xzymon.xpath_searcher.core.parser.AttributeRepresentation;
import com.xzymon.xpath_searcher.core.parser.HalfElementsParser;

/**
 * Obiekt zawierający wewnątrz stan - w postaci reprezentacji powiązanej ze strumieniem
 * @author root
 *
 */
public class XPathSearcher implements StateHolder{
	private static final Logger LOGGER = LoggerFactory.getLogger(XPathSearcher.class);
	
	// przeniesione z XMLStateHoldera
	private Map<Node, NodeRepresentation> elementsMap = null;
	private Map<Node, AttributeRepresentation> attributesMap = null;
	private XPathProcessor engine = null;
	private HalfElementsParser heParser = null;
	private boolean empty;
	private List<BindingListener> bindingListeners;
	
	private DocumentTreeRepresentation treeBoundToStream;
	
	public static XPathSearcher createFromXML(InputStream is, List<BindingListener> bindingListeners, boolean ignoreNamespaces){
		boolean empty = true;
		char[] chars = null;
		HalfElementsParser parser = null;
		
		XPathSearcher xps = null;
		
		if(is!=null){
			try{
				int avail = is.available();
				if(avail>0){
					byte[] savedStream = new byte[avail];
					is.read(savedStream);
					ByteArrayInputStream bais = new ByteArrayInputStream(savedStream);
					InputStreamReader reader = new InputStreamReader(bais);
					char[] charBuf = new char[savedStream.length];
					int read = reader.read(charBuf);
					chars = new char[read];
					System.arraycopy(charBuf, 0, chars, 0, read);
					parser = new HalfElementsParser(chars);
					empty = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!empty){
			LOGGER.debug(String.format("Building fixing DocumentTreeRepresentation ..."));
			OrphanedPolicies firstPolicies = new XMLDefaultOrphanedPolicies();
			DocumentTreeRepresentation fixingTree = DocumentTreeRepresentation.createFromHalfElementsParser(parser, XmlPreprocessingMode.ASSUME_XML, firstPolicies);
			LOGGER.debug("Fixing DocumentTreeRepresentation built successfully.");
			LOGGER.debug("Reconstructing from fixed stream ...");
			String stringifiedTree = fixingTree.stringifyTree();
			String corrected = stringifiedTree.replaceAll("&nbsp;", "&#160;");
			
			HalfElementsParser heParser;
			try {
				heParser = new HalfElementsParser(corrected.toCharArray());
				OrphanedPolicies xmlOrphanedPolicies = new XMLDefaultOrphanedPolicies();
				DocumentTreeRepresentation tree = DocumentTreeRepresentation.createFromHalfElementsParser(heParser, XmlPreprocessingMode.ASSUME_XML, xmlOrphanedPolicies);
				XPathProcessor engine = new XPathProcessorImpl(new ByteArrayInputStream(corrected.getBytes()), bindingListeners);
				
				xps = new XPathSearcher(heParser, engine, bindingListeners, tree);
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return xps;
	}
	
	public static XPathSearcher createFromHTML(InputStream is, List<BindingListener> bindingListeners){
		boolean empty = true;
		char[] chars = null;
		HalfElementsParser parser = null;
		
		XPathSearcher xps = null;
		
		if(is!=null){
			try{
				int avail = is.available();
				if(avail>0){
					byte[] savedStream = new byte[avail];
					is.read(savedStream);
					ByteArrayInputStream bais = new ByteArrayInputStream(savedStream);
					InputStreamReader reader = new InputStreamReader(bais);
					char[] charBuf = new char[savedStream.length];
					int read = reader.read(charBuf);
					chars = new char[read];
					System.arraycopy(charBuf, 0, chars, 0, read);
					parser = new HalfElementsParser(chars);
					empty = false;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!empty){
			LOGGER.debug(String.format("Building fixing DocumentTreeRepresentation ..."));
			OrphanedPolicies firstPolicies = new HTMLDefaultOrphanedPolicies();
			DocumentTreeRepresentation fixingTree = DocumentTreeRepresentation.createFromHalfElementsParser(parser, XmlPreprocessingMode.ASSUME_HTML, firstPolicies);
			LOGGER.debug("Fixing DocumentTreeRepresentation built successfully.");
			LOGGER.debug("Reconstructing from fixed stream ...");
			String stringifiedTree = fixingTree.stringifyTree();
			String corrected = stringifiedTree.replaceAll("&nbsp;", "&#160;");
			
			HalfElementsParser heParser;
			try {
				heParser = new HalfElementsParser(corrected.toCharArray());
				OrphanedPolicies xmlOrphanedPolicies = new XMLDefaultOrphanedPolicies();
				DocumentTreeRepresentation tree = DocumentTreeRepresentation.createFromHalfElementsParser(heParser, XmlPreprocessingMode.ASSUME_XML, xmlOrphanedPolicies);
				XPathProcessor engine = new XPathProcessorImpl(new ByteArrayInputStream(corrected.getBytes()), bindingListeners);
				
				xps = new XPathSearcher(heParser, engine, bindingListeners, tree);
			} catch (ParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return xps;
	}
	
	private XPathSearcher(HalfElementsParser heParser, XPathProcessor engine, List<BindingListener> bindingListeners, DocumentTreeRepresentation tree){
		this.heParser = heParser;
		this.engine = engine;
		this.bindingListeners = bindingListeners;
		this.treeBoundToStream = tree;
		
		int boundElements = bindElementsToText();
		int boundAttributes = bindAttributesToText();
		if(bindingListeners!=null){
			for(BindingListener pl : bindingListeners){
				pl.nodesBound(boundElements, boundAttributes);
			}
		}
	}
	
	/**
	 * Umożliwia dostęp do reprezentacji dokumentu oderwanej od strumienia
	 * - każdorazowo zwraca nową strukturę - zbudowaną w oparciu o strukturę 
	 * uzyskaną przy parsowaniu strumienia
	 * @return
	 */
	public DocumentTree getDocumentTree(){
		if(this.treeBoundToStream==null){
			LOGGER.error("this.treeBoundToStream is NULL!");
		} else {
			LOGGER.error("this.treeBoundToStream is not null");
		}
		return DocumentTree.createFromTreeRepresentation(treeBoundToStream);
	}
	
	public boolean hasExpression(){
		return engine.hasExpression();
	}
	
	public String getExpression(){
		return engine.getExpression();
	}
	
	public int newSearch(String expression) throws XPathExpressionException{
		return engine.findNodes(expression);
	}
	
	public void nextNode(){
		engine.nextNode();
	}
	
	public void reset(){
		engine.reset();
	}
	
	public void clear(){
		engine.clear();
	}
	
	public boolean isEmpty(){
		return empty;
	}
	
	public char[] getData(){
		return this.heParser.getSavedChars();
	}
	
	public NodeList getFoundNodeList(){
		return engine.getAllFoundNodes();
	}
	
	public NodeRepresentation getBoundSlicedNode(Node node){
		return elementsMap.get(node);
	}
	
	public AttributeRepresentation getBoundAttributeRepresentation(Node node){
		return attributesMap.get(node);
	}
	
	public void invokeParserListeners(){
		this.heParser.invokeListeners();
	}
	
	public List<ParserListener> getParserListeners(){
		return this.heParser.getParserListeners();
	}
	
	public void addParserListener(ParserListener listener){
		this.heParser.addParserListener(listener);
	}
	
	public void removeParserListener(ParserListener listener){
		this.heParser.removeParserListener(listener);
	}
	
	public List<XPathSearchingListener> getSearchingListeners() {
		return engine.getSearchingListeners();
	}
	
	public void addSearchingListener(XPathSearchingListener listener){
		engine.addSearchingListener(listener);
	}
	
	public void removeSearchingListener(XPathSearchingListener listener){
		engine.removeSearchingListener(listener);
	}
	
	/**
	 * Dokonuje powiązania wszystkich węzłów drzewa dokumentu z ich położeniem w strumieniu
	 * @return
	 */
	private int bindElementsToText(){
		int boundElements = 0;
		Node xPathNode = null;
		NodeRepresentation slicedNode = null;
		elementsMap = new HashMap<Node, NodeRepresentation>();
		try {
			engine.findNodes("//*");
			NodeList nodeList = engine.getAllFoundNodes();
			NodeRepresentation rootSlicedNode = this.heParser.buildStructure();
			NodeRepresentation.SlicedNodeIterator snIt = rootSlicedNode.iterator();
			for(int nodeLoop=0; nodeLoop<nodeList.getLength(); nodeLoop++){
				xPathNode = nodeList.item(nodeLoop);
				if(snIt.hasNext()){
					slicedNode = snIt.next();
					LOGGER.info(String.format("parallel nodes: xpath=%2$s(%1$d), sliced=%3$s", nodeLoop, xPathNode.getNodeName(), slicedNode.getName()));
					if(xPathNode.getNodeName().equals(slicedNode.getName())){
						elementsMap.put(xPathNode, slicedNode);
						boundElements++;
					} else {
						LOGGER.info(String.format("names NOT EQUAL: xpath=%2$s(%1$d), sliced=%3$s", nodeLoop, xPathNode.getNodeName(), slicedNode.getName()));
					}
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BuildingDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return boundElements;
	}
	
	private int bindAttributesToText(){
		int boundAttributes = 0;
		Node xPathNode = null;
		NodeRepresentation slicedNode = null;
		attributesMap = new HashMap<Node, AttributeRepresentation>();
		Iterator<AttributeRepresentation> attrIt = null;
		int attrCount;
		int processedAttrs;
		List<AttributeRepresentation> attrs = null;
		try {
			engine.findNodes("//*/@*");
			NodeList nodeList = engine.getAllFoundNodes();
			NodeRepresentation rootSlicedNode = this.heParser.buildStructure();
			NodeRepresentation.SlicedNodeIterator snIt = rootSlicedNode.iterator();
			for(int i=0; i<nodeList.getLength(); i++){
				xPathNode = nodeList.item(i);
				LOGGER.info(String.format("xpath node(%1$d)=%2$s", i, xPathNode.getNodeName()));
			}
			for(int nodeLoop=0; nodeLoop<nodeList.getLength(); /*nodeLoop++*/){
				if(snIt.hasNext()){
					slicedNode = snIt.next();
					LOGGER.info("processing node element " + slicedNode.getName());
					if(slicedNode.hasAttributes()){
						attrs = slicedNode.getAttributes();
						attrCount = attrs.size();
						attrIt = attrs.iterator();
						for(AttributeRepresentation ar : attrs){
							LOGGER.info(String.format("known attribute: %1$s", ar.getName()));
						}
						processedAttrs = 0;
						while(processedAttrs<attrCount){
							xPathNode = nodeList.item(nodeLoop);
							for(AttributeRepresentation attrNode : attrs){
								LOGGER.info(String.format("parallel nodes: xpath=%2$s(%1$d), attr=%3$s", nodeLoop, xPathNode.getNodeName(), attrNode.getName()));
								if(xPathNode.getNodeName().equals(attrNode.getName())){
									attributesMap.put(xPathNode, attrNode);
									boundAttributes++;
									nodeLoop++;
									processedAttrs++;
									break;
								} else {
									LOGGER.info(String.format("names NOT EQUAL: xpath=%2$s(%1$d), attr=%3$s", nodeLoop, xPathNode.getNodeName(), attrNode.getName()));
								}
							}
						}
					}
					
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BuildingDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return boundAttributes;
	}
}
