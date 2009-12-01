package com.silverpeas.tags.pdc;

import javax.servlet.jsp.tagext.*;

public class getPdcViewTEI extends TagExtraInfo {

	public getPdcViewTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getAttributeString("name"),
				"com.silverpeas.tags.pdc.PdcTagUtil",
				true,
				VariableInfo.NESTED
			),
		};
	}
}