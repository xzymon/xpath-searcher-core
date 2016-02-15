package com.xzymon.xpath_searcher.core.dom.representation;

public class XMLDefaultOrphanedPolicies extends OrphanedPolicies {

	public XMLDefaultOrphanedPolicies(OrphanedElementsPolicy defaultPolicy) {
		super(defaultPolicy);
	}

	public XMLDefaultOrphanedPolicies() {
		super(new OrphanedElementsPolicy(OrphanedOpeningTagMode.DEFAULT,
				OrphanedClosingTagMode.DEFAULT));
	}
}
