/**
 * Copyright 2005-2013 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.ksb.messaging.remotedservices;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * JAX-RS annotated interface for a baseball card collection service which might track the 
 * cards in a collection.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Path("/")
public interface BaseballCardCollectionService {
    
    @GET
    public List<BaseballCard> getAll();

    /**
     * gets a card by it's (arbitrary) identifier
     */
    @GET
    @Path("/BaseballCard/id/{id}")
    public BaseballCard get(@PathParam("id") Integer id);
    
    /**
     * gets all the cards in the collection with the given player name
     */
    @GET
    @Path("/BaseballCard/playerName/{playerName}")
    public List<BaseballCard> get(@PathParam("playerName") String playerName);

    /**
     * Add a card to the collection.  This is a non-idempotent method 
     * (because you can more one of the same card), so we'll use @POST
     * @return the (arbitrary) numerical identifier assigned to this card by the service
     */
    @POST
    @Path("/BaseballCard")
    public Integer add(BaseballCard card);

    
    /**
     * update the card for the given identifier.  This will replace the card that was previously
     * associated with that identifier.
     */
    @PUT
    @Path("/BaseballCard/id/{id}")
    @Consumes("application/xml")
    public void update(@PathParam("id") Integer id, BaseballCard card);

    /**
     * delete the card with the given identifier.
     */
    @DELETE
    @Path("/BaseballCard/id/{id}")
    public void delete(@PathParam("id") Integer id);
    
    /**
     * This method lacks JAX-RS annotations
     */
    public void unannotatedMethod();
    
}
