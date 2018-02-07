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
package org.kuali.rice.krms.impl.repository.mock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;

public class TermRepositoryServiceMockImpl implements TermRepositoryService {
    // cache variable 
    // The LinkedHashMap is just so the values come back in a predictable order

    private Map<String, TermDefinition> termMap = new LinkedHashMap<String, TermDefinition>();
    private Map<String, TermSpecificationDefinition> termSpecificationMap = new LinkedHashMap<String, TermSpecificationDefinition>();
    private Map<String, TermResolverDefinition> termResolverMap = new LinkedHashMap<String, TermResolverDefinition>();

    public void clear() {
        this.termMap.clear();
        this.termSpecificationMap.clear();
        this.termResolverMap.clear();
    }

    @Override
    public List<TermResolverDefinition> findTermResolversByNamespace(String namespace)
            throws RiceIllegalArgumentException {
        List<TermResolverDefinition> list = new ArrayList<TermResolverDefinition>();
        for (TermResolverDefinition info : this.termResolverMap.values()) {
            if (info.getNamespace().equals(namespace)) {
                list.add(info);
            }
        }
        return list;
    }

    @Override
    public TermDefinition getTerm(String termId)
            throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.termMap.containsKey(termId)) {
            throw new RiceIllegalArgumentException(termId);
        }
        return this.termMap.get(termId);
    }

    @Override
    public TermSpecificationDefinition getTermSpecificationById(String id) throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.termSpecificationMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.termSpecificationMap.get(id);
    }

    @Override
    public TermSpecificationDefinition createTermSpecification(TermSpecificationDefinition termSpec)
            throws RiceIllegalArgumentException {
        try {
            TermSpecificationDefinition orig = this.getTermSpecificationById(termSpec.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(termSpec.getId() + "." + termSpec.getName());
            }
        } catch (RiceIllegalArgumentException ex) {
            // same as getting null
        }
        TermSpecificationDefinition.Builder copy = TermSpecificationDefinition.Builder.create(termSpec);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        TermSpecificationDefinition termSpecDefinition = copy.build();
        termSpecificationMap.put(termSpecDefinition.getId(), termSpecDefinition);
        return termSpecDefinition;
    }

    @Override
    public TermDefinition createTerm(TermDefinition termDef) throws RiceIllegalArgumentException {
        try {
            TermDefinition orig = this.getTerm(termDef.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(termDef.getId());
            }
        } catch (RiceIllegalArgumentException ex) {
            // same as getting null
        }
        TermDefinition.Builder copy = TermDefinition.Builder.create(termDef);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        TermDefinition termDefinition = copy.build();
        termMap.put(termDefinition.getId(), termDefinition);
        return termDefinition;
    }

    @Override
    public TermResolverDefinition getTermResolverById(String id) throws RiceIllegalArgumentException {
        // GET_BY_ID
        if (!this.termResolverMap.containsKey(id)) {
            throw new RiceIllegalArgumentException(id);
        }
        return this.termResolverMap.get(id);
    }

    @Override
    public List<TermResolverDefinition> findTermResolversByOutputId(String id, String namespace)
            throws RiceIllegalArgumentException {
        List<TermResolverDefinition> list = new ArrayList<TermResolverDefinition>();
        for (TermResolverDefinition info : this.termResolverMap.values()) {
            if (info.getNamespace().equals(namespace)) {
                if (info.getOutput().getId().equals(id)) {
                    list.add(info);
                }
            }
        }
        return list;
    }

    @Override
    public TermResolverDefinition createTermResolver(TermResolverDefinition termResolver) throws RiceIllegalArgumentException {
        try {
            TermResolverDefinition orig = this.getTermResolverById(termResolver.getId());
            if (orig != null) {
                throw new RiceIllegalArgumentException(termResolver.getId() + "." + termResolver.getName());
            }
        } catch (RiceIllegalArgumentException ex) {
            // same as getting null
        }
        TermResolverDefinition.Builder copy = TermResolverDefinition.Builder.create(termResolver);
        if (copy.getId() == null) {
            copy.setId(UUID.randomUUID().toString());
        }
        TermResolverDefinition termResolverDefinition = copy.build();
        termResolverMap.put(termResolverDefinition.getId(), termResolverDefinition);
        return termResolverDefinition;
    }

    @Override
    public void updateTermSpecification(TermSpecificationDefinition termSpec) throws RiceIllegalArgumentException {
        TermSpecificationDefinition existing = this.getTermSpecificationById(termSpec.getId());
        if (existing == null) {
            throw new RiceIllegalArgumentException(termSpec.getId() + " does not exist");
        }
        this.termSpecificationMap.put(termSpec.getId(), termSpec);

    }

    @Override
    public void deleteTermSpecification(String id) throws RiceIllegalArgumentException {
        if (this.termSpecificationMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id + " does not exist");
        }
    }

    @Override
    public void updateTerm(TermDefinition termDef) throws RiceIllegalArgumentException {
        TermDefinition existing = this.getTerm(termDef.getId());
        if (existing == null) {
            throw new RiceIllegalArgumentException(termDef.getId() + " does not exist");
        }
        this.termMap.put(termDef.getId(), termDef);
    }

    @Override
    public void deleteTerm(String id) throws RiceIllegalArgumentException {
        if (this.termMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id + " does not exist");
        }
    }

    @Override
    public void updateTermResolver(TermResolverDefinition termResolver) throws RiceIllegalArgumentException {
        TermResolverDefinition existing = this.getTermResolverById(termResolver.getId());
        if (existing == null) {
            throw new RiceIllegalArgumentException(termResolver.getId() + " does not exist");
        }
        this.termResolverMap.put(termResolver.getId(), termResolver);
    }

    @Override
    public void deleteTermResolver(String id) throws RiceIllegalArgumentException {
        if (this.termResolverMap.remove(id) == null) {
            throw new RiceIllegalArgumentException(id + " does not exist");
        }
    }

    @Override
    public TermResolverDefinition getTermResolverByNameAndNamespace(String name, String namespace) 
            throws RiceIllegalArgumentException {
      if (name == null || name.trim().isEmpty()) {
          throw new RiceIllegalArgumentException ("name is " + name);
      }
      if (namespace == null || namespace.trim().isEmpty()) {
          throw new RiceIllegalArgumentException ("namespace is " + namespace);
      }
      for (TermResolverDefinition info : this.termResolverMap.values()) {
          if (namespace.equals(info.getNamespace())) {
              if (name.equals(name)) {
                  return info;
              }
          }
      }
      return null;
    }

    @Override
    public TermSpecificationDefinition getTermSpecificationByNameAndNamespace(String name, String namespace) 
            throws RiceIllegalArgumentException {
      if (name == null || name.trim().isEmpty()) {
          throw new RiceIllegalArgumentException ("name is " + name);
      }
      if (namespace == null || namespace.trim().isEmpty()) {
          throw new RiceIllegalArgumentException ("namespace is " + namespace);
      }
      for (TermSpecificationDefinition info : this.termSpecificationMap.values()) {
          if (namespace.equals(info.getNamespace())) {
              if (name.equals(name)) {
                  return info;
              }
          }
      }
      return null;
    }

    @Override
    public List<TermSpecificationDefinition> findAllTermSpecificationsByContextId(String contextId) {
	throw new UnsupportedOperationException(getClass().getName() + " does not support this operation");
    }
}
