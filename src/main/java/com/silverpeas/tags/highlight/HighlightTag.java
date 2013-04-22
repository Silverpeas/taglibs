/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.tags.highlight;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class HighlightTag extends BodyTagSupport {
  private String style;
  private String className;
  private Hashtable glossary;
  private String url;
  private String javaScript;
  private String onlyFirst;

  private final String delimiter = "##";

  public String getJavaScript() {
    return javaScript;
  }

  public void setJavaScript(String javaScript) {
    this.javaScript = javaScript;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public HighlightTag() {
    style = null;
    className = null;
    glossary = null;
    url = null;
  }

  public void setStyle(String s) {
    style = s;
  }

  public String getStyle() {
    return style;
  }

  public void setClassName(String s) {
    className = s;
  }

  public Hashtable getGlossary() {
    return glossary;
  }

  public void setGlossary(Hashtable glossary) {
    this.glossary = glossary;
  }

  public int doAfterBody()
      throws JspException {
    BodyContent bodycontent = getBodyContent();
    String s;
    if (bodycontent != null) {
      if ((s = bodycontent.getString()) == null) {
        s = "";
      }
      bodycontent.clearBody();
    } else {
      s = "";
    }

    Enumeration terms = getGlossary().keys();
    List lTerms = new ArrayList();
    while (terms.hasMoreElements()) {
      String term = (String) terms.nextElement();
      lTerms.add(term);
    }

    Collections.sort(lTerms, new TermComparator());

    for (int t = 0; t < lTerms.size(); t++) {
      String term = (String) lTerms.get(t);
      // System.out.println("-------------------");
      // System.out.println("term = "+term);
      String id = (String) getGlossary().get(term);

      s = highlightTerm(s, term, id);
    }

    s = clean(s, delimiter);

    try {
      JspWriter jspwriter = bodycontent.getEnclosingWriter();
      jspwriter.print(s);
    } catch (Exception exception) {
      System.out.println("highlight: could not save body");
    }
    return 0;
  }

  public int doEndTag()
      throws JspException {
    dropData();
    return 6;
  }

  public void release() {
    dropData();
  }

  private String highlightTerm(String s, String term, String id) {
    String lowerS = s.toLowerCase();
    // Transform special chars in html code
    term = encodeSpecialChar(term);
    String lowerT = term.toLowerCase();

    boolean find = false;

    StringBuffer stringbuffer = new StringBuffer("");
    int i;
    while ((i = lowerS.indexOf(lowerT)) >= 0) {
      if (i != 0) {
        stringbuffer.append(s.substring(0, i));
      }

      // le mot retrouvé est-il bien le terme
      // et non pas un fragment du mot. Ex : terme = Carcinogen et mot = carcinogenenis
      char before = lowerS.charAt(i - 1);
      char after = lowerS.charAt(i + term.length());
      String sBefore = stringbuffer.toString();
      /*
       * if ((before != ' ' && before != '>' && before != '<' && before != '.' && before != ',' &&
       * before != '"' && before != '\'' && before != '(' && before != ')' && before != '!' &&
       * before != '?' && before != ':') || (after != ' ' && after != '>' && after != '<' && after
       * != '.' && after != ',' && after != '"' && after != '\'' && after != '(' && after != ')' &&
       * after != '!' && after != '?' && after != ':') ||
       * (sBefore.lastIndexOf("<")>sBefore.lastIndexOf(">")))
       */
      if ((before == ';' || after == '&' || Character.isLetter(before) || Character.isLetter(after)) ||
          sBefore.lastIndexOf("<") > sBefore.lastIndexOf(">")) {
        stringbuffer.append(s.substring(i, i + term.length()));

        lowerS = lowerS.substring(i + term.length());
        s = s.substring(i + term.length());
      } else {
        // le mot retrouvé fait il partie d'un terme composé (ie : cancer vs cervical cancer)
        // Test s'il n'y a pas déjà "</a>" en fin de mot
        String start = "";
        String end = "";
        if (i + term.length() + 2 <= lowerS.length())
          end = lowerS.substring(i + term.length(), i + term.length() + 2);
        if (i >= 2)
          start = lowerS.substring(i - 2, i);
        // System.out.println("start = "+start);
        // System.out.println("end = "+end);
        if (end.equalsIgnoreCase("##") || start.equals("##")) {
          stringbuffer.append(s.substring(i, i + term.length()));

          lowerS = lowerS.substring(i + term.length());
          s = s.substring(i + term.length());
        } else {
          if (!find) {
            if (style != null) {
              stringbuffer.append("<span ");
              stringbuffer.append("style=\"");
              stringbuffer.append(style);
              stringbuffer.append("\">");
            }

            if (getUrl() != null || getJavaScript() != null) {
              if (getUrl() != null)
                stringbuffer.append("<a href=\"" + getUrl() + id + "\" target=\"_blank\"");
              else
                stringbuffer
                    .append("<a href=\"javaScript:" + getJavaScript() + "('" + id + "');\"");

              if (className != null)
                stringbuffer.append(" class=\"" + className + "\">");
              else
                stringbuffer.append(">");
            }
          }
          stringbuffer.append(delimiter);

          // laisse le terme du texte
          stringbuffer.append(s.substring(i, i + term.length()));

          stringbuffer.append(delimiter);

          if (!find) {
            if (getUrl() != null || getJavaScript() != null)
              stringbuffer.append("</a>");

            if (style != null)
              stringbuffer.append("</span>");

            if ("true".equals(getOnlyFirst()))
              find = true;
          }

          if (i + term.length() < s.length()) {
            lowerS = lowerS.substring(i + term.length());
            s = s.substring(i + term.length());
          } else {
            lowerS = "";
            s = "";
          }
        }
      }
    }
    if (lowerS.length() > 0) {
      stringbuffer.append(s);
    }
    return stringbuffer.toString();
  }

  private String clean(String s, String toFind) {
    StringBuffer buffer = new StringBuffer();
    int i;
    while ((i = s.indexOf(toFind)) >= 0) {
      buffer.append(s.substring(0, i));
      s = s.substring(i + toFind.length());
    }
    if (s.length() > 0)
      buffer.append(s);

    return buffer.toString();
  }

  private void dropData() {
    style = null;
    className = null;
    glossary = null;
    url = null;
    javaScript = null;
  }

  public String getOnlyFirst() {
    return onlyFirst;
  }

  public void setOnlyFirst(String onlyFirst) {
    this.onlyFirst = onlyFirst;
  }

  /**
   * Transform special chars in its html code
   * @param javastring
   * @return String with code html
   */
  public String encodeSpecialChar(String javastring) {
    StringBuffer res = new StringBuffer();

    if (javastring == null) {
      return res.toString();
    }
    for (int i = 0; i < javastring.length(); i++) {
      switch (javastring.charAt(i)) {
        case 'á':
          res.append("&aacute;"); // aacute
          break;
        case '\u010C':
          res.append("&#268;"); // Ccaron
          break;
        case '\u010D':
          res.append("&#269;"); // ccaron
          break;
        case 'é':
          res.append("&eacute;"); // eacute
          break;
        case '\u011A':
          res.append("&#282;"); // Ecaron
          break;
        case '\u011B':
          res.append("&#283;"); // ecaron
          break;
        case 'í':
          res.append("&iacute;"); // í
          break;
        case '\u0147':
          res.append("&#327;"); // Ncaron
          break;
        case '\u0148':
          res.append("&#328;"); // ncaron
          break;
        case '\u0158':
          res.append("&#344;"); // Rcaron
          break;
        case '\u0159':
          res.append("&#345;"); // rcaron
          break;
        case '\u0160':
          res.append("&#352;"); // Scaron
          break;
        case '\u0161':
          res.append("&#353;"); // scaron
          break;
        case 'ú':
          res.append("&uacute;"); // uacute
          break;
        case '\u016F':
          res.append("&#367;"); // udegre
          break;
        case '\u017D':
          res.append("&#381;"); // Zcaron
          break;
        case '\u017E':
          res.append("&#382;"); // zcaron
          break;
        default:
          res.append(javastring.charAt(i));
      }
    }
    return res.toString();
  }
}