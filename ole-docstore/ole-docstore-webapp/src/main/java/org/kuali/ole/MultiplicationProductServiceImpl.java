/**
 * Copyright 2005-2012 The Kuali Foundation
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
package org.kuali.ole;

/**
 * Kuali Rice ArcheType Help
 * <p/>
 * This is an example of a service implementation.
 * <p/>
 * An implementation of the ProductService that uses multiplication.
 */
public class MultiplicationProductServiceImpl implements ProductService {
    @Override
    public Integer product(Integer left, Integer right) {
        if (left == null) {
            throw new IllegalArgumentException("left is null");
        }

        if (right == null) {
            throw new IllegalArgumentException("right is null");
        }

        return left * right;
    }
}