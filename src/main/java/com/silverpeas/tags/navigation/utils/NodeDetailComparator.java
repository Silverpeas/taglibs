package com.silverpeas.tags.navigation.utils;

import java.util.Comparator;

import com.stratelia.webactiv.util.node.model.NodeDetail;

public class NodeDetailComparator implements Comparator<NodeDetail> {

	@Override
	public int compare(NodeDetail o1, NodeDetail o2) {
		
		if (o1.getOrder() == o2.getOrder()) {
			return 0;
		} else if (o1.getOrder() > o2.getOrder()) {
			return 1;
		} else {
			return -1;
		}
	}

}
