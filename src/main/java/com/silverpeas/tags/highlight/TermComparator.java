package com.silverpeas.tags.highlight;

import java.util.*;

public class TermComparator implements Comparator
{
	static public TermComparator comparator = new TermComparator();

	public int compare(Object o1, Object o2)
	{
		String t1 = (String) o1;
		String t2 = (String) o2;
		
		if (t1.length() == t2.length())
			return 0;
		else if (t1.length() > t2.length())
			return -1;
		else
			return 1;	
	}

	/**
	 * This comparator equals self only.
	 * 
	 * Use the shared comparator GSCNameComparator.comparator
	 * if multiples comparators are used.
	 */
	public boolean equals(Object o)
	{
		return o == this;
	}
}