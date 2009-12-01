package com.silverpeas.tags;

import javax.servlet.jsp.tagext.*;

public class AllPublicationsTEI extends TagExtraInfo {

	public AllPublicationsTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				"publication",
				"com.stratelia.webactiv.util.publication.model.PublicationDetail",
				true,
				VariableInfo.NESTED
			),
		};
	}
}