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
package org.kuali.rice.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kuali.rice.core.framework.resourceloader.SpringResourceLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.util.ArrayList;
import java.util.Collection;

/** 
 * Wraps a collection of bean factories delegating to the inner bean factories.
 * 
 * The first bean factory that returns a non-null/true result is the value that is returned/
 */
public final class CompositeBeanFactory implements BeanFactory {

	private static final Log LOG = LogFactory.getLog(CompositeBeanFactory.class);
	
	private final Collection<BeanFactory> factories;
	
	public static BeanFactory createBeanFactory(Collection<? extends SpringResourceLoader> rls) {
		if (rls == null || rls.isEmpty()) {
			throw new IllegalArgumentException("rls is null or empty");
		}
		
		final Collection<BeanFactory> bfs = new ArrayList<BeanFactory>();
		for (SpringResourceLoader rl : rls) {
			bfs.add(rl.getContext());
		}
		return new CompositeBeanFactory(bfs);
	}
	
	public CompositeBeanFactory(Collection<? extends BeanFactory> factories) {
		if (factories == null || factories.isEmpty()) {
			throw new IllegalArgumentException("factories is null or empty");
		}
		
		this.factories = new ArrayList<BeanFactory>(factories);
	}

	@Override
	public Object getBean(String name) throws BeansException {
		for (BeanFactory f : factories) {
			try {
				Object o = f.getBean(name);
				if (o != null) {
					return o;
				}	
			} catch (BeansException e) {
				LOG.debug("bean exception", e);
			}
		}
		return null;
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		for (BeanFactory f : factories) {
			try {
				T t = f.getBean(name, requiredType);
				if (t != null) {
					return t;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		for (BeanFactory f : factories) {
			try {
				T t = f.getBean(requiredType);
				if (t != null) {
					return t;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return null;
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		for (BeanFactory f : factories) {
			try {
				Object o = f.getBean(name, args);
				if (o != null) {
					return o;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return null;
	}

	@Override
	public boolean containsBean(String name) {
		for (BeanFactory f : factories) {
			try {
				boolean b = f.containsBean(name);
				if (b) {
					return b;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return false;
	}

	@Override
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		for (BeanFactory f : factories) {
			try {
				boolean b = f.isSingleton(name);
				if (b) {
					return b;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return false;
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		for (BeanFactory f : factories) {
			try {
				boolean b = f.isPrototype(name);
				if (b) {
					return b;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return false;
	}

	@Override
	public boolean isTypeMatch(String name, Class targetType) throws NoSuchBeanDefinitionException {
		for (BeanFactory f : factories) {
			try {
				boolean b = f.isTypeMatch(name, targetType);
				if (b) {
					return b;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return false;
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		for (BeanFactory f : factories) {
			try {
				Class<?> c = f.getType(name);
				if (c != null) {
					return c;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return null;
	}

	@Override
	public String[] getAliases(String name) {
		for (BeanFactory f : factories) {
			try {
				String[] s = f.getAliases(name);
				if (s != null) {
					return s;
				}	
			} catch (BeansException e) {
				LOG.info("bean exception", e);
			}
		}
		return null;
	}
}
