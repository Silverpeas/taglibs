package com.silverpeas.tags.searchEngine;

import javax.servlet.jsp.tagext.*;

public class getSearchEngineTEI extends TagExtraInfo {

	public getSearchEngineTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getAttributeString("name"),
				"com.silverpeas.tags.searchEngine.SearchEngineTagUtil",
				true,
				VariableInfo.NESTED
			),
		};
	}
}