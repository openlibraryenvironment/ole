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
package org.kuali.rice.core.web.cache;

import org.kuali.rice.core.api.cache.CacheManagerRegistry;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.core.impl.services.CoreImplServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping(value = "/core/admin/cache")
public class CacheAdminController extends UifControllerBase {

    private CacheManagerRegistry registry;

    public synchronized CacheManagerRegistry getRegistry() {
        if (registry == null) {
            registry = CoreImplServiceLocator.getCacheManagerRegistry();
        }
        return registry;
    }

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected CacheAdminForm createInitialForm(HttpServletRequest request) {
        return new CacheAdminForm();
    }

    @Override
	@RequestMapping(params = "methodToCall=start")
	public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {

        final Tree<String, String> cacheTree = new Tree<String,String>();

        final Node<String,String> root = new Node<String,String>("Root", "Root");
        final List<CacheManager> cms = new ArrayList<CacheManager>(getRegistry().getCacheManagers());
        Collections.sort(cms, new ByName());

        for (final CacheManager cm : cms) {
            final String name = getRegistry().getCacheManagerName(cm);
            final Node<String, String> cmNode = new Node<String, String>(name, name);
            final List<String> names = new ArrayList<String>(cm.getCacheNames());
            Collections.sort(names, String.CASE_INSENSITIVE_ORDER);

            for (final String cn : names) {
                String cacheSize = getCacheSize(name, cn);
                final Node<String, String> cNode = new Node<String, String>(cn, cn + (cacheSize != null ? " - " + cacheSize : ""));
                //no way to get a keySet from the cache w/o calling the nativeCache
                //method which is a bad idea b/c it will tie the rice codebase to
                //a caching implementation
                cmNode.addChild(cNode);
            }

            root.addChild(cmNode);
        }

        cacheTree.setRootElement(root);
        ((CacheAdminForm) form).setCacheTree(cacheTree);

        return super.start(form, result, request, response);
    }

	@RequestMapping(params = "methodToCall=flush", method = RequestMethod.POST)
	public ModelAndView flush(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) {
        Person user = GlobalVariables.getUserSession().getPerson();
        boolean isAuthorized = KimApiServiceLocator.getPermissionService().isAuthorized(
						user.getPrincipalId(),
						KRADConstants.KUALI_RICE_SYSTEM_NAMESPACE,
						KRADConstants.USE_CACHE_ADMINISTRATION_SCREEN,
						Collections.singletonMap(KimConstants.AttributeConstants.PRINCIPAL_ID, user.getPrincipalId()));
        if(isAuthorized){

            //FIXME: Could optimize this such that specific cache flushes don't execute if a complete CacheManager
            //flush was requested
            for (String name : ((CacheAdminForm) form).getFlush()) {
                //path == cacheManager index, cache index
                final List<Integer> path = path(removePrefix(name));
                final Tree<String, String> tree = ((CacheAdminForm) form).getCacheTree();
                final Integer cmIdx = path.get(0);
                final Node<String, String> cmNode = tree.getRootElement().getChildren().get(cmIdx);
                final String cmName = cmNode.getData();
                final CacheManager cm = getRegistry().getCacheManager(cmName);

                if (path.size() == 1) {
                    flushAllCaches(cm);
                    GlobalVariables.getMessageMap().putInfoForSectionId("mainGroup_div","flush.all.cachemanager", cmName);
                } else {
                    final Integer cIdx = path.get(1);
                    final Node<String, String> cNode = cmNode.getChildren().get(cIdx);
                    final String cName = cNode.getData();
                    flushSpecificCache(cm, cName);
                    GlobalVariables.getMessageMap().putInfoForSectionId("mainGroup_div",
                            "flush.single.cachemanager", cName, cmName);
                }
            }
        }else{
           GlobalVariables.getMessageMap().putError("flush","error.authorization.general",user.getPrincipalName(),"flush","cachemanager");
        }
        return super.start(form, result, request, response);
    }

    private static void flushSpecificCache(CacheManager cm, String cache) {
        for (String s : cm.getCacheNames()) {
            if (cache.equals(s)) {
                 cm.getCache(s).clear();
                 return;
            }
        }
    }

    private static void flushAllCaches(CacheManager cm) {
        for (String s : cm.getCacheNames()) {
            cm.getCache(s).clear();
        }
    }

    // given: 35_node_2_parent_node_0_parent_root will remove "35_"
    private static String removePrefix(String s) {
        final StringBuilder sbn = new StringBuilder(s);
        sbn.delete(0, sbn.indexOf("_") + 1);
        return sbn.toString();
    }

    // given: node_2_parent_node_0_parent_root will return {0, 2}
    private static List<Integer> path(String s) {
        final String[] path = s.split("_parent_");
        final List<Integer> pathIdx = new ArrayList<Integer>();
        //ignore length - 2 to ignore root
        for (int i = path.length - 2; i >= 0; i--) {
            pathIdx.add(Integer.valueOf(path[i].substring(5)));
        }
        return Collections.unmodifiableList(pathIdx);
    }

    /**
     * If the cache manager implementation is ehcache, it will return the current size of the cache. Did this in such
     * a way that it will fallback gracefully just in case the cache implementation being used is not ehcache.
     */
    private String getCacheSize(String cacheManagerName, String cacheName) {
        Object nativeCache = getRegistry().getCacheManager(cacheManagerName).getCache(cacheName).getNativeCache();
        try {
            Class<?> ehcache = Class.forName("net.sf.ehcache.Cache");
            if (ehcache.isInstance(nativeCache)) {
                Object intSize = ehcache.getDeclaredMethod("getSize").invoke(nativeCache);
                if (intSize != null) {
                    return intSize.toString();
                }
            }
        } catch (ClassNotFoundException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        } catch (InvocationTargetException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        }
        return null;
    }

    private final class ByName implements Comparator<CacheManager> {

        @Override
        public int compare(CacheManager o1, CacheManager o2) {
            return String.CASE_INSENSITIVE_ORDER.compare(getRegistry().getCacheManagerName(o1),
                    getRegistry().getCacheManagerName(o2));
        }
    }

}
