#!/bin/bash -xe
#
# Copyright 2005-2014 The Kuali Foundation
#
# Licensed under the Educational Community License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.opensource.org/licenses/ecl2.php
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Update authorized keys from SVN for servers
# Script must be called from Jenkins as it sets $WORKSPACE

KEYFILE="$WORKSPACE/src/main/resources/authorized_keys"
PRIVATEKEY=/var/lib/jenkins/.ssh/kr-key.pem

# check return code and exit if not zero
check_ret_code() {
  ret_code=$?
  if [ $ret_code -ne 0 ]; then
    printf "\n\nReturn code is $ret_code.  Exiting.....\n\n";  
    exit $ret_code
  fi
}

hosts=( 
ubuntu@ci.rice.kuali.org
ec2-user@env1.rice.kuali.org
ec2-user@env2.rice.kuali.org
ec2-user@env3.rice.kuali.org
ec2-user@env4.rice.kuali.org
ec2-user@env5.rice.kuali.org
ec2-user@demo.rice.kuali.org
ec2-user@env7.rice.kuali.org
ec2-user@env8.rice.kuali.org
ec2-user@env9.rice.kuali.org
ec2-user@env10.rice.kuali.org
ec2-user@env11.rice.kuali.org
)

# Make sure key file exists and it is not zero byte
if [[ ! -s $KEYFILE ]]; then
   echo "Unable to checkout $KEYFILE or it is zero byte"
   exit 1
fi

# Update servers with the key
for host in ${hosts[@]}; do
   echo Updating $host
   ssh -i $PRIVATEKEY $host "chmod 600 ~/.ssh/authorized_keys"
   check_ret_code
   scp -i $PRIVATEKEY $KEYFILE $host:.ssh/.
   check_ret_code
done

echo Updated authorized_keys successfully.
