package com.silverpeas.tags;

import javax.servlet.jsp.tagext.*;

public class IterateTEI extends TagExtraInfo {

	public IterateTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getAttributeString("name"),
				data.getAttributeString("type"),
				true,
				VariableInfo.NESTED
			),
		};
	}
}