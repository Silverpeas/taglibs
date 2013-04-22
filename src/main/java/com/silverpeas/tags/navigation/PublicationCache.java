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

package com.silverpeas.tags.navigation;

import java.util.concurrent.ConcurrentHashMap;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.config.Configurateur;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

public class PublicationCache {

  private static PublicationCache instance;
  private KmeliaTagUtil themetracker = null;

  // Cache treeview
  private ConcurrentHashMap<String, PublicationDetail> publicationCache =
      new ConcurrentHashMap<String, PublicationDetail>();
  private long publicationCacheAge = System.currentTimeMillis();

  private PublicationCache(KmeliaTagUtil themetracker) {
    super();
    this.themetracker = themetracker;
  }

  public static PublicationCache getInstance(KmeliaTagUtil themetracker) {
    if (instance == null) {
      instance = new PublicationCache(themetracker);
    }
    return instance;
  }

  public PublicationDetail getPublication(String pubId) throws Exception {
    manageCache();

    PublicationDetail pub = publicationCache.get(pubId);
    if (pub == null) {
      synchronized (this) {
        pub = themetracker.getPublicationDetail(pubId);
        publicationCache.putIfAbsent(pubId, pub);
      }
    }

    return pub;
  }

  private void manageCache() {
    String cacheActivate = Configurateur.getConfigValue("publicationsCache");
    String refreshDelay = Configurateur.getConfigValue("publicationsCache.refreshDelay");
    if (refreshDelay == null)
      refreshDelay = "3600";
    if (cacheActivate == null)
      cacheActivate = "true";
    if ((System.currentTimeMillis() - publicationCacheAge >= Long.valueOf(refreshDelay) * 1000) ||
        cacheActivate.equals("false")) {
      publicationCache.clear();
      publicationCacheAge = System.currentTimeMillis();
    }
  }
}
