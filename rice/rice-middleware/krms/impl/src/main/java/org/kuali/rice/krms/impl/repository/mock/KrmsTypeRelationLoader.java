/**
 * Copyright 2005-2014 The Kuali Foundation
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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krms.impl.repository.mock;

import java.util.List;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;

/**
 *
 * @author nwright
 */
public class KrmsTypeRelationLoader {

    private KrmsTypeRepositoryService krmsTypeRepositoryService = null;

    public KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return krmsTypeRepositoryService;
    }

    public void setKrmsTypeRepositoryService(KrmsTypeRepositoryService krmsTypeRepositoryService) {
        this.krmsTypeRepositoryService = krmsTypeRepositoryService;
    }

    public void loadTypeRelation(String id, String fromTypeId, String toTypeId, String relType, Integer sequenceNumber) {
        RelationshipType relationshipType = RelationshipType.USAGE_ALLOWED;
        if (!relType.equals("A")) {
            relationshipType = RelationshipType.UNKNOWN;
        }
        TypeTypeRelation.Builder bldr = TypeTypeRelation.Builder.create(fromTypeId, relationshipType, sequenceNumber, toTypeId);
        bldr.setId(id);
        bldr.setActive(true);
        TypeTypeRelation existing = this.findExisting(bldr);
        if (existing == null) {
            this.getKrmsTypeRepositoryService().createTypeTypeRelation(bldr.build());
        } else {
            // consider comparing and only update if different!
            bldr.setVersionNumber(existing.getVersionNumber());
            this.getKrmsTypeRepositoryService().updateTypeTypeRelation(bldr.build());
        }
    }

    private TypeTypeRelation findExisting(TypeTypeRelation.Builder bldr) {
        List<TypeTypeRelation> list = this.getKrmsTypeRepositoryService().findTypeTypeRelationsByFromType(bldr.getFromTypeId());
        for (TypeTypeRelation rel : list) {
            if (bldr.getToTypeId().equals(rel.getToTypeId())) {
                return rel;
            }
        }
        return null;
    }

    public void load() {
        // agendas for contexts
        loadTypeRelation("10000", "10000", "10002", "A", 1);
        loadTypeRelation("10001", "10000", "10003", "A", 6);
        loadTypeRelation("10010", "10001", "10014", "A", 1);
        loadTypeRelation("10011", "10001", "10015", "A", 2);
        loadTypeRelation("10009", "10001", "10013", "A", 3);
// rules for agendas
        loadTypeRelation("10006", "10002", "10010", "A", 2);
        loadTypeRelation("10003", "10002", "10006", "A", 3);
        loadTypeRelation("10005", "10002", "10008", "A", 4);
        loadTypeRelation("10002", "10002", "10005", "A", 5);
        loadTypeRelation("10008", "10003", "10012", "A", 7);
        loadTypeRelation("10007", "10003", "10011", "A", 8);
        loadTypeRelation("10012", "10004", "10016", "A", 2);
// propositions for rules
        loadTypeRelation("10030", "10005", "10021", "A", 1);
        loadTypeRelation("10057", "10005", "10032", "A", 1);
        loadTypeRelation("10026", "10005", "10020", "A", 2);
        loadTypeRelation("10033", "10005", "10022", "A", 3);
        loadTypeRelation("10067", "10005", "10037", "A", 5);
        loadTypeRelation("10055", "10006", "10031", "A", 0);
        loadTypeRelation("10054", "10006", "10030", "A", 1);
        loadTypeRelation("10036", "10006", "10024", "A", 2);
        loadTypeRelation("10035", "10006", "10023", "A", 3);
        loadTypeRelation("10014", "10008", "10017", "A", 1);
        loadTypeRelation("10059", "10009", "10033", "A", 2);
        loadTypeRelation("10065", "10009", "10036", "A", 3);
        loadTypeRelation("10056", "10010", "10031", "A", 0);
        loadTypeRelation("10015", "10010", "10017", "A", 1);
        loadTypeRelation("10114", "10010", "10040", "A", 1);
        loadTypeRelation("10116", "10010", "10052", "A", 1);
        loadTypeRelation("10113", "10010", "10054", "A", 1);
        loadTypeRelation("10115", "10010", "10055", "A", 1);
        loadTypeRelation("10019", "10010", "10018", "A", 2);
        loadTypeRelation("10023", "10010", "10019", "A", 3);
        loadTypeRelation("10038", "10010", "10025", "A", 6);
        loadTypeRelation("10045", "10010", "10027", "A", 7);
        loadTypeRelation("10049", "10010", "10028", "A", 8);
        loadTypeRelation("10042", "10010", "10026", "A", 9);
        loadTypeRelation("10060", "10010", "10033", "A", 11);
        loadTypeRelation("10066", "10010", "10036", "A", 12);
        loadTypeRelation("10062", "10010", "10034", "A", 13);
        loadTypeRelation("10064", "10010", "10035", "A", 14);
        loadTypeRelation("10070", "10010", "10038", "A", 15);
        loadTypeRelation("10108", "10010", "10074", "A", 15);
        loadTypeRelation("10111", "10010", "10075", "A", 15);
        loadTypeRelation("10053", "10011", "10029", "A", 1);
        loadTypeRelation("10058", "10012", "10032", "A", 1);
        loadTypeRelation("10027", "10012", "10020", "A", 2);
        loadTypeRelation("10039", "10013", "10025", "A", 4);
        loadTypeRelation("10072", "10013", "10039", "A", 5);
        loadTypeRelation("10024", "10013", "10019", "A", 10);
        loadTypeRelation("10020", "10013", "10018", "A", 11);
        loadTypeRelation("10031", "10013", "10021", "A", 12);
        loadTypeRelation("10046", "10013", "10027", "A", 13);
        loadTypeRelation("10050", "10013", "10028", "A", 14);
        loadTypeRelation("10016", "10013", "10017", "A", 16);
        loadTypeRelation("10073", "10013", "10040", "A", 18);
        loadTypeRelation("10040", "10014", "10025", "A", 5);
        loadTypeRelation("10071", "10014", "10038", "A", 6);
        loadTypeRelation("10109", "10014", "10074", "A", 6);
        loadTypeRelation("10112", "10014", "10075", "A", 6);
        loadTypeRelation("10025", "10014", "10019", "A", 9);
        loadTypeRelation("10028", "10014", "10020", "A", 10);
        loadTypeRelation("10021", "10014", "10018", "A", 11);
        loadTypeRelation("10032", "10014", "10021", "A", 12);
        loadTypeRelation("10034", "10014", "10022", "A", 13);
        loadTypeRelation("10068", "10014", "10037", "A", 15);
        loadTypeRelation("10047", "10014", "10027", "A", 16);
        loadTypeRelation("10051", "10014", "10028", "A", 17);
        loadTypeRelation("10043", "10014", "10026", "A", 18);
        loadTypeRelation("10017", "10014", "10017", "A", 19);
        loadTypeRelation("10052", "10015", "10028", "A", 5);
        loadTypeRelation("10074", "10015", "10040", "A", 6);
        loadTypeRelation("10075", "10015", "10041", "A", 7);
        loadTypeRelation("10076", "10016", "10042", "A", 2);
        loadTypeRelation("10077", "10016", "10043", "A", 2);
        loadTypeRelation("10078", "10016", "10044", "A", 2);
        loadTypeRelation("10079", "10016", "10045", "A", 2);
        loadTypeRelation("10080", "10016", "10046", "A", 2);
        loadTypeRelation("10081", "10016", "10047", "A", 2);
        loadTypeRelation("10084", "10016", "10050", "A", 2);
        loadTypeRelation("10085", "10016", "10051", "A", 2);
        loadTypeRelation("10086", "10016", "10052", "A", 2);
        loadTypeRelation("10087", "10016", "10053", "A", 2);
        loadTypeRelation("10088", "10016", "10054", "A", 2);
        loadTypeRelation("10089", "10016", "10055", "A", 2);
        loadTypeRelation("10090", "10016", "10056", "A", 2);
        loadTypeRelation("10091", "10016", "10057", "A", 2);
        loadTypeRelation("10092", "10016", "10058", "A", 2);
        loadTypeRelation("10093", "10016", "10059", "A", 2);
        loadTypeRelation("10094", "10016", "10060", "A", 2);
        loadTypeRelation("10095", "10016", "10061", "A", 2);
        loadTypeRelation("10096", "10016", "10062", "A", 2);
        loadTypeRelation("10098", "10016", "10064", "A", 2);
        loadTypeRelation("10099", "10016", "10065", "A", 2);
        loadTypeRelation("10100", "10016", "10066", "A", 2);
        loadTypeRelation("10101", "10016", "10067", "A", 2);
        loadTypeRelation("10102", "10016", "10068", "A", 2);
        loadTypeRelation("10103", "10016", "10069", "A", 2);
        loadTypeRelation("10105", "10016", "10071", "A", 2);
        loadTypeRelation("10106", "10016", "10072", "A", 2);
// parameters for propositions
        loadTypeRelation("10019-A-10100", "10019", "10100", "A", 1);
        loadTypeRelation("10019-A-10102", "10019", "10102", "A", 2);
        loadTypeRelation("10019-A-10101", "10019", "10101", "A", 3);
        // TERM PARAMETER
        loadTypeRelation("10100-A-10104", "10100", "10104", "A", 1);
    }
}
