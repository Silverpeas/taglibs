/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.silverpeas.tags.kmelia;

import com.silverpeas.tags.util.SiteTagUtil;
import static org.junit.Assert.*;

/**
 *
 * @author ehugonnet
 */
public class KmeliaTagUtilTest {

  static KmeliaTagUtil instance;

  public KmeliaTagUtilTest() {
  }

  @org.junit.BeforeClass
  public static void setUpClass() throws Exception {
    instance = new KmeliaTagUtil("WA1", "kmelia560", "9", false);
  }

  @org.junit.AfterClass
  public static void tearDownClass() throws Exception {
  }

  @org.junit.Before
  public void setUp() throws Exception {
  }

  @org.junit.After
  public void tearDown() throws Exception {
  }

  /**
   * Test of parseHtmlContent method, of class KmeliaTagUtil.
   */
  @org.junit.Test
  public void testParseHtmlContent() {
    SiteTagUtil.setServerContext("/webSilverpeas");
    SiteTagUtil.setFileServerName("/attached_file");
    String htmlContent = "<p style=\"text-align: center;\">Silverpeas V5 est disponible! "
        + "Cette nouvelle version apporte de nouvelles fonctionnalit&eacute;s et une ergonomie "
        + "am&eacute;lior&eacute;e. Pour plus de d&eacute;tails sur ces &eacute;volutions: "
        + "<a href=\"http://www.silverpeas.com/SilverpeasWebFileServer/componentId/toolbox69/"
        + "attachmentId/14115/lang/fr/name/SilverpeasV5_Features1.3.pdf\" target=\"_blank\">"
        + "Evolutions v5</a></p><p style=\"text-align: center;\"><span style=\"color: rgb(0, 102, 0);\">"
        + "<span><strong><span style=\"font-size: medium;\">Votre plateforme Xnet pr&ecirc;te &agrave; "
        + "l'emploi <br /><br type=\"_moz\" /></span></strong></span></span></p><p style=\"text-align: center;_\">"
        + "<img width=\"428\" height=\"264\" alt=\"\" src=\"/silverpeas/FileServer/schemaMailing_mid.jpg?"
        + "ComponentId=kmelia560&amp;SourceFile=1227102639270.jpg&amp;MimeType=image/jpeg&amp;Directory=Attachment/1225Images/\" />"
        + "</p><p>Silverpeas permet de b&acirc;tir un Intranet, un Extranet ou d'alimenter des sites "
        + "WEB 2.0, avec des fonctionnalit&eacute;s pr&ecirc;tes &agrave; l'emploi.</p><p>"
        + "Silverpeas&nbsp;est utilis&eacute; pour partager des documents (GED), pour faciliter "
        + "la gestion des projets, pour organiser la gestion de contenu (CMS), et favoriser la "
        + "capitalisation des connaissances.</p><p>Silverpeas offre une ergonomie intuitive, "
        + "bas&eacute;e sur les standards ergonomiques du web.</p><p>Silverpeas offre la "
        + "possibilit&eacute;, pour des utilisateurs non techniciens, de d&eacute;ployer &agrave; "
        + "volont&eacute; des outils pr&ecirc;ts &agrave; l'emploi : GED, annuaires, calendriers, "
        + "workflows, formulaires, blog, wiki, forum, gestion de projet, etc.&nbsp;</p><p>Le&nbsp;"
        + "moteur de workflow de Silverpeas permet d'organiser la circulation de l'information, et "
        + "le traitement de services en ligne.</p><p>Silverpeas offre de puissants m&eacute;canismes "
        + "de gestion et de d&eacute;lagaton des droits, &agrave; la port&eacute;e d'utilisateurs non "
        + "techniciens, &eacute;vitant ainsi, un goulot d'&eacute;tranglement au niveau du service IT."
        + "</p><p>La <strong>taxonomie</strong> transverse &agrave; la plateforme, coupl&eacute;e au "
        + "<strong>moteur de recherche </strong>g&eacute;n&eacute;ral, procure aux utilisateurs un "
        + "moyen de retrouver rapidement l'information qu'ils recherchent.</p><p>Silverpeas est "
        + "d&eacute;velopp&eacute; en Java et&nbsp;disponible sous licence Gnu Affero GPL V3. "
        + "<img width=\"60\" height=\"51\" border=\"0\" alt=\"\" "
        + "src=\"/silverpeas/FileServer/OpenSource2.gif?ComponentId=kmelia560&amp;SourceFile="
        + "1166795078206.gif&amp;MimeType=image/gif&amp;Directory=Attachment/1225Images\" />"
        + "</p><p>Silverpeas est aussi distribu&eacute;e dans une version commerciale dot&eacute;e "
        + "de modules additionnels et qui b&eacute;n&eacute;ficie du support de l'&eacute;diteur."
        + "<br /><br /><a target=\"_blank\" href=\"http://www.jboss.com/\"><img width=\"150\" "
        + "height=\"47\" border=\"0\" alt=\"Logo Jboss\" "
        + "src=\"/silverpeas/FileServer/Jboss.gif?ComponentId=kmelia560&amp;SourceFile=1201537314113"
        + ".gif&amp;MimeType=image/gif&amp;Directory=Attachment/1225Images/\" />"
        + "</a><a target=\"_blank\" href=\"http://www.postgresql.org/\"> <img width=\"90\" "
        + "height=\"48\" border=\"0\" alt=\"Logo PostgreSQL\" src=\"/silverpeas/FileServer/"
        + "PostgreSQL.jpg?ComponentId=kmelia560&amp;SourceFile=1201537347371.jpg&amp;MimeType="
        + "image/gif&amp;Directory=Attachment/1225Images/\" /></a><a target=\"_blank\" "
        + "href=\"http://lucene.apache.org/\"> <img width=\"110\" height=\"17\" border=\"0\" "
        + "alt=\"Logo Lucene\" src=\"/silverpeas/FileServer/lucene_green_300.gif?ComponentId="
        + "kmelia560&amp;SourceFile=1201537365091.gif&amp;MimeType=image/gif&amp;Directory=Attachment/1225Images/\" />"
        + "</a><a href=\"http://www.fckeditor.net/\"> <img width=\"100\" height=\"23\" border=\"0\" "
        + "alt=\"Logo FCK Editor\" src=\"/silverpeas/FileServer/FCK.jpg?ComponentId=kmelia560&amp;"
        + "SourceFile=1201538274570.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" />"
        + "</a><br /><br />Silverpeas peut utiliser des composants propri&eacute;taires (Weblogic, "
        + "Websphere, Microsoft SQL Server, Oracle...).</p><p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/silverpeas/FileServer/BrowserOs.jpg?ComponentId=kmelia560&amp;SourceFile"
        + "=1221139304307.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" /></p>";
    String expResult = "<p style=\"text-align: center;\">Silverpeas V5 est disponible! "
        + "Cette nouvelle version apporte de nouvelles fonctionnalit&eacute;s et une ergonomie "
        + "am&eacute;lior&eacute;e. Pour plus de d&eacute;tails sur ces &eacute;volutions: "
        + "<a href=\"http://www.silverpeas.com/SilverpeasWebFileServer/componentId/toolbox69/"
        + "attachmentId/14115/lang/fr/name/SilverpeasV5_Features1.3.pdf\" target=\"_blank\">"
        + "Evolutions v5</a></p><p style=\"text-align: center;\"><span style=\"color: rgb(0, 102, 0);\">"
        + "<span><strong><span style=\"font-size: medium;\">Votre plateforme Xnet pr&ecirc;te &agrave; "
        + "l'emploi <br /><br type=\"_moz\" /></span></strong></span></span></p><p style=\"text-align: center;_\">"
        + "<img width=\"428\" height=\"264\" alt=\"\" src=\"/webSilverpeas/attached_file/schemaMailing_mid.jpg?"
        + "ComponentId=kmelia560&SourceFile=1227102639270.jpg&MimeType=image/jpeg&Directory=Attachment/1225Images/\" />"
        + "</p><p>Silverpeas permet de b&acirc;tir un Intranet, un Extranet ou d'alimenter des sites "
        + "WEB 2.0, avec des fonctionnalit&eacute;s pr&ecirc;tes &agrave; l'emploi.</p><p>"
        + "Silverpeas&nbsp;est utilis&eacute; pour partager des documents (GED), pour faciliter "
        + "la gestion des projets, pour organiser la gestion de contenu (CMS), et favoriser la "
        + "capitalisation des connaissances.</p><p>Silverpeas offre une ergonomie intuitive, "
        + "bas&eacute;e sur les standards ergonomiques du web.</p><p>Silverpeas offre la "
        + "possibilit&eacute;, pour des utilisateurs non techniciens, de d&eacute;ployer &agrave; "
        + "volont&eacute; des outils pr&ecirc;ts &agrave; l'emploi : GED, annuaires, calendriers, "
        + "workflows, formulaires, blog, wiki, forum, gestion de projet, etc.&nbsp;</p><p>Le&nbsp;"
        + "moteur de workflow de Silverpeas permet d'organiser la circulation de l'information, et "
        + "le traitement de services en ligne.</p><p>Silverpeas offre de puissants m&eacute;canismes "
        + "de gestion et de d&eacute;lagaton des droits, &agrave; la port&eacute;e d'utilisateurs non "
        + "techniciens, &eacute;vitant ainsi, un goulot d'&eacute;tranglement au niveau du service IT."
        + "</p><p>La <strong>taxonomie</strong> transverse &agrave; la plateforme, coupl&eacute;e au "
        + "<strong>moteur de recherche </strong>g&eacute;n&eacute;ral, procure aux utilisateurs un "
        + "moyen de retrouver rapidement l'information qu'ils recherchent.</p><p>Silverpeas est "
        + "d&eacute;velopp&eacute; en Java et&nbsp;disponible sous licence Gnu Affero GPL V3. "
        + "<img width=\"60\" height=\"51\" border=\"0\" alt=\"\" "
        + "src=\"/webSilverpeas/attached_file/OpenSource2.gif?ComponentId=kmelia560&SourceFile="
        + "1166795078206.gif&MimeType=image/gif&Directory=Attachment/1225Images\" />"
        + "</p><p>Silverpeas est aussi distribu&eacute;e dans une version commerciale dot&eacute;e "
        + "de modules additionnels et qui b&eacute;n&eacute;ficie du support de l'&eacute;diteur."
        + "<br /><br /><a target=\"_blank\" href=\"http://www.jboss.com/\"><img width=\"150\" "
        + "height=\"47\" border=\"0\" alt=\"Logo Jboss\" "
        + "src=\"/webSilverpeas/attached_file/Jboss.gif?ComponentId=kmelia560&SourceFile=1201537314113"
        + ".gif&MimeType=image/gif&Directory=Attachment/1225Images/\" />"
        + "</a><a target=\"_blank\" href=\"http://www.postgresql.org/\"> <img width=\"90\" "
        + "height=\"48\" border=\"0\" alt=\"Logo PostgreSQL\" src=\"/webSilverpeas/attached_file/"
        + "PostgreSQL.jpg?ComponentId=kmelia560&SourceFile=1201537347371.jpg&MimeType="
        + "image/gif&Directory=Attachment/1225Images/\" /></a><a target=\"_blank\" "
        + "href=\"http://lucene.apache.org/\"> <img width=\"110\" height=\"17\" border=\"0\" "
        + "alt=\"Logo Lucene\" src=\"/webSilverpeas/attached_file/lucene_green_300.gif?ComponentId="
        + "kmelia560&SourceFile=1201537365091.gif&MimeType=image/gif&Directory=Attachment/1225Images/\" />"
        + "</a><a href=\"http://www.fckeditor.net/\"> <img width=\"100\" height=\"23\" border=\"0\" "
        + "alt=\"Logo FCK Editor\" src=\"/webSilverpeas/attached_file/FCK.jpg?ComponentId=kmelia560&"
        + "SourceFile=1201538274570.jpg&MimeType=image/pjpeg&Directory=Attachment/1225Images/\" />"
        + "</a><br /><br />Silverpeas peut utiliser des composants propri&eacute;taires (Weblogic, "
        + "Websphere, Microsoft SQL Server, Oracle...).</p><p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/webSilverpeas/attached_file/BrowserOs.jpg?ComponentId=kmelia560&SourceFile"
        + "=1221139304307.jpg&MimeType=image/pjpeg&Directory=Attachment/1225Images/\" /></p>";
    String result = instance.parseHtmlContent(htmlContent);
    assertEquals(expResult, result);
  }

  /**
   * Test of convertToWebUrl method, of class KmeliaTagUtil.
   */
  @org.junit.Test
  public void testConvertToWebUrl() {
    SiteTagUtil.setServerContext("/webSilverpeas");
    SiteTagUtil.setFileServerName("/attached_file");
    String content = "<p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/silverpeas/FileServer/BrowserOs.jpg?ComponentId=kmelia560&amp;SourceFile"
        + "=1221139304307.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" /></p>";

    String servletMapping = "/FileServer/";
    String webContext = "/webSilverpeas/attached_file/";
    String expResult = "<p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/webSilverpeas/attached_file/BrowserOs.jpg?ComponentId=kmelia560&SourceFile"
        + "=1221139304307.jpg&MimeType=image/pjpeg&Directory=Attachment/1225Images/\" /></p>";
    String result = instance.convertToWebUrl(content, servletMapping, webContext);
    assertEquals(expResult, result);
    content = "<img width=\"60\" height=\"51\" border=\"0\" alt=\"\" "
        + "src=\"/silverpeas/FileServer/OpenSource2.gif?ComponentId=kmelia560&amp;SourceFile="
        + "1166795078206.gif&amp;MimeType=image/gif&amp;Directory=Attachment/1225Images\" />"
        + "</p><p>Silverpeas est aussi distribu&eacute;e dans une version commerciale dot&eacute;e "
        + "de modules additionnels et qui b&eacute;n&eacute;ficie du support de l'&eacute;diteur."
        + "<br /><br /><a target=\"_blank\" href=\"http://www.jboss.com/\"><img width=\"150\" "
        + "height=\"47\" border=\"0\" alt=\"Logo Jboss\" "
        + "src=\"/silverpeas/FileServer/Jboss.gif?ComponentId=kmelia560&amp;SourceFile=1201537314113"
        + ".gif&amp;MimeType=image/gif&amp;Directory=Attachment/1225Images/\" />"
        + "</a><a target=\"_blank\" href=\"http://www.postgresql.org/\"> <img width=\"90\" "
        + "height=\"48\" border=\"0\" alt=\"Logo PostgreSQL\" src=\"/silverpeas/FileServer/"
        + "PostgreSQL.jpg?ComponentId=kmelia560&amp;SourceFile=1201537347371.jpg&amp;MimeType="
        + "image/gif&amp;Directory=Attachment/1225Images/\" /></a><a target=\"_blank\" "
        + "href=\"http://lucene.apache.org/\"> <img width=\"110\" height=\"17\" border=\"0\" "
        + "alt=\"Logo Lucene\" src=\"/silverpeas/FileServer/lucene_green_300.gif?ComponentId="
        + "kmelia560&amp;SourceFile=1201537365091.gif&amp;MimeType=image/gif&amp;Directory=Attachment/1225Images/\" />"
        + "</a><a href=\"http://www.fckeditor.net/\"> <img width=\"100\" height=\"23\" border=\"0\" "
        + "alt=\"Logo FCK Editor\" src=\"/silverpeas/FileServer/FCK.jpg?ComponentId=kmelia560&amp;"
        + "SourceFile=1201538274570.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" />"
        + "</a><br /><br />Silverpeas peut utiliser des composants propri&eacute;taires (Weblogic, "
        + "Websphere, Microsoft SQL Server, Oracle...).</p><p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/silverpeas/FileServer/BrowserOs.jpg?ComponentId=kmelia560&amp;SourceFile"
        + "=1221139304307.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" /></p>";
    expResult = "<img width=\"60\" height=\"51\" border=\"0\" alt=\"\" "
        + "src=\"/webSilverpeas/attached_file/OpenSource2.gif?ComponentId=kmelia560&SourceFile="
        + "1166795078206.gif&MimeType=image/gif&Directory=Attachment/1225Images\" />"
        + "</p><p>Silverpeas est aussi distribu&eacute;e dans une version commerciale dot&eacute;e "
        + "de modules additionnels et qui b&eacute;n&eacute;ficie du support de l'&eacute;diteur."
        + "<br /><br /><a target=\"_blank\" href=\"http://www.jboss.com/\"><img width=\"150\" "
        + "height=\"47\" border=\"0\" alt=\"Logo Jboss\" "
        + "src=\"/webSilverpeas/attached_file/Jboss.gif?ComponentId=kmelia560&SourceFile=1201537314113"
        + ".gif&MimeType=image/gif&Directory=Attachment/1225Images/\" />"
        + "</a><a target=\"_blank\" href=\"http://www.postgresql.org/\"> <img width=\"90\" "
        + "height=\"48\" border=\"0\" alt=\"Logo PostgreSQL\" src=\"/webSilverpeas/attached_file/"
        + "PostgreSQL.jpg?ComponentId=kmelia560&SourceFile=1201537347371.jpg&MimeType="
        + "image/gif&Directory=Attachment/1225Images/\" /></a><a target=\"_blank\" "
        + "href=\"http://lucene.apache.org/\"> <img width=\"110\" height=\"17\" border=\"0\" "
        + "alt=\"Logo Lucene\" src=\"/webSilverpeas/attached_file/lucene_green_300.gif?ComponentId="
        + "kmelia560&SourceFile=1201537365091.gif&MimeType=image/gif&Directory=Attachment/1225Images/\" />"
        + "</a><a href=\"http://www.fckeditor.net/\"> <img width=\"100\" height=\"23\" border=\"0\" "
        + "alt=\"Logo FCK Editor\" src=\"/webSilverpeas/attached_file/FCK.jpg?ComponentId=kmelia560&"
        + "SourceFile=1201538274570.jpg&MimeType=image/pjpeg&Directory=Attachment/1225Images/\" />"
        + "</a><br /><br />Silverpeas peut utiliser des composants propri&eacute;taires (Weblogic, "
        + "Websphere, Microsoft SQL Server, Oracle...).</p><p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/webSilverpeas/attached_file/BrowserOs.jpg?ComponentId=kmelia560&amp;SourceFile"
        + "=1221139304307.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" /></p>";
    result = instance.convertToWebUrl(content, servletMapping, webContext);
    assertEquals(expResult, result);
  }

  @org.junit.Test
  public void testConvertRestToWebUrl() {
    SiteTagUtil.setServerContext("/webSilverpeas");
    SiteTagUtil.setFileServerName("/attached_file");
    String content = "<p><img width=\"1720\" height=\"1860\" src=\"/silverpeas/attached_file/compon"
        + "entId/kmelia560/attachmentId/15311/lang/fr/name/AlpesJUG_tranparent.gif\" alt=\"\" /></p>";
    String servletMapping = "/attached_file/";
    String webContext = "/webSilverpeas/attached_file/";
    String expResult = "<p><img width=\"1720\" height=\"1860\" src=\"/webSilverpeas/attached_file/compon"
        + "entId/kmelia560/attachmentId/15311/lang/fr/name/AlpesJUG_tranparent.gif\" alt=\"\" /></p>";
    String result = instance.convertRestToWebUrl(content, servletMapping, webContext);
    assertEquals(expResult, result);
    content = "<p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/silverpeas/FileServer/BrowserOs.jpg?ComponentId=kmelia560&amp;SourceFile"
        + "=1221139304307.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" /></p>";
    expResult = "<p align=\"left\">Silverpeas est "
        + "compatible avec :</p><p style=\"text-align: left;\"><img width=\"232\" height=\"83\" "
        + "alt=\"\" src=\"/silverpeas/FileServer/BrowserOs.jpg?ComponentId=kmelia560&amp;SourceFile"
        + "=1221139304307.jpg&amp;MimeType=image/pjpeg&amp;Directory=Attachment/1225Images/\" /></p>";
    result = instance.convertRestToWebUrl(content, servletMapping, webContext);
    assertEquals(expResult, result);
  }
}
