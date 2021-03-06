/*
 * Copyright (C) 2001-2016 Food and Agriculture Organization of the
 * United Nations (FAO-UN), United Nations World Food Programme (WFP)
 * and United Nations Environment Programme (UNEP)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *
 * Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 * Rome - Italy. email: geonetwork@osgeo.org
 */

package org.fao.geonet.solr;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.solr.client.solrj.SolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by francois on 18/01/16.
 */
@RequestMapping(value = {
        "search"
    })
@Api(value = "search",
    tags = "search",
    description = "Search operations")
@Controller
public class SolrApi {

  @Autowired
  SolrJProxy solrProxy;

  @Autowired
  SolrConfig solrConfig;


  /**
   * Delete document.
   */
  @RequestMapping(value = "/update",
      method = RequestMethod.DELETE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @ApiOperation(value = "Delete documents",
      nickname = "delete")
  public void delete(@RequestParam(required = true)
                     String collection,
                     String query,
                     HttpServletRequest request) throws Exception {


    // TODO: Check if user can delete documents first
    // throw new SecurityException(String.format(
    // "Current user can't remove document with filter '%s'.", filter));

    SolrClient client = solrProxy.getServer();
    client.deleteByQuery(collection, query);
    client.commit(collection);
  }

  /**
   * Not used.
     */
  @ResponseBody
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({
      Exception.class
      })
  public Object unauthorizedHandler(final Exception exception) {
    exception.printStackTrace();
    return new LinkedHashMap<String, String>() {{
        put("code", "index-is-down");
        put("message", exception.getClass().getSimpleName());
        put("description", exception.getMessage());
        }
    };
  }
}
