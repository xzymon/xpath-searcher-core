package com.xzymon.xpath_searcher.core.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xpath_searcher.core.dom.representation.DocumentNodeRepresentation;
import com.xzymon.xpath_searcher.core.dom.representation.DocumentTreeRepresentation;
import com.xzymon.xpath_searcher.core.parser.HalfElementRepresentation;

public class DocumentTree {
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTree.class);
	
	private DocumentNode rootNode;
	
	private DocumentTree(DocumentNode node){
		this.rootNode = node;
	}
	
	public static DocumentTree createFromTreeRepresentation(DocumentTreeRepresentation treeRepr){
		DocumentTree tree = null;
		DocumentNodeChild nodeChild = createDocumentNodeChildFromRepresentation(treeRepr.getRootNode());
		if(nodeChild!=null && nodeChild.isNode()){
			tree = new DocumentTree((DocumentNode)nodeChild);
		} else {
			LOGGER.info("returning NULL tree");
		}
		return tree;
	}
	
	public static DocumentTree createInitialTree(String rootNodeName){
		DocumentTree tree = null;
		
		if(rootNodeName!=null && !rootNodeName.isEmpty()){
			DocumentNode rootNode = DocumentNode.createInitialDocumentNode(rootNodeName);
			tree = new DocumentTree(rootNode);
		}
		
		return tree;
	}
	
	public static DocumentNodeChild createDocumentNodeChildFromRepresentation(DocumentNodeRepresentation repr){
		DocumentNodeChild resultNode = null, subnode = null;
		DocumentNode node = null; 
		String textContent = null;
		DocumentTreeRepresentation tree = null;
		
		if(repr!=null && repr.belongsToAnyTree()){
			tree = repr.getTree();
			if(tree!=null){
				LOGGER.info("tree has rootNode = " + tree.getRootNode().getName());
			} else {
				LOGGER.info("tree is null!");
			}
			
			if(!repr.isRaw()){
				LOGGER.info("processing for repr.getName() = " + repr.getName());
				node = DocumentNode.createDocumentNodeFromAttributeRepresentationList(repr.getName(), tree, repr.getAttributes());
				if(repr.hasChildren()){
					for(DocumentNodeRepresentation child : repr.getChildren()){
						subnode = createDocumentNodeChildFromRepresentation(child);
						if(subnode==null){
							LOGGER.info("subnode is null");
						}
						subnode.setParent(node);
						node.addLastChild(subnode);
					}
				}
				resultNode = node;
			} else {
				LOGGER.info("processing for RAW repr");
				textContent = HalfElementRepresentation.fromSavedChars(tree.getSavedSource(), repr.getRawTag().getStartPosition(), repr.getRawTag().getEndPosition());
				LOGGER.info("text = [" + textContent + "]");
				resultNode = new DocumentNestedText(textContent);
			}
		} else {
			if(repr==null){
				LOGGER.info("repr==null");
			} else {
				LOGGER.info("repr is TREE-LESS");
			}
		}
		
		return resultNode;
	}
	
	public StringBuffer stringifyTree(String levelTabString, boolean selfCloseIfPossible, boolean eachTagInNewLine, boolean removeWhitespaceOnlyTextNodes){
		return this.rootNode.stringifyNode("", levelTabString, selfCloseIfPossible, eachTagInNewLine, removeWhitespaceOnlyTextNodes);
	}
}
