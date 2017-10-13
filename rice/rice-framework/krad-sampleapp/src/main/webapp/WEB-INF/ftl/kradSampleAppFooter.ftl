<#--

    Copyright 2005-2014 The Kuali Foundation

    Licensed under the Educational Community License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.opensource.org/licenses/ecl2.php

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<#macro demo_kradappfooter group>

<div class="demo-thirdTier clearfix">
    <div class="demo-headerFooterContainer">
        <div>
            <div class="demo-kradDescription">
                <h2>About KRAD</h2>

                <div class="demo-blurb">Kuali Rapid Application Development (KRAD) is a framework that eases the
                    development of enterprise web applications by providing reusable solutions and tooling that enables
                    developers to build in a rapid and agile fashion. KRAD is a complete framework for web developers
                    that provides infrastructure in all the major areas of an application (client, business, and data),
                    and integrates with other modules of the Rice middleware project. In future releases, KNS will be
                    absorbed into and replaced by KRAD.
                </div>
            </div>
            <div class="demo-contactInfo">
                <h2>Collaborate</h2>
                <div>
                    <ul>
                        <li>
                            <a target="_self"
                               href="https://groups.google.com/a/kuali.org/forum/?fromgroups#!forum/rice.usergroup.krad"
                               id="KradSampleAppHome-UserGroupLink">KRAD User Group</a>
                        </li>
                        <li>
                            <a target="_self" href="https://groups.google.com/a/kuali.org/groups/dir"
                               id="KradSampleAppHome-KualiGroupsLink">Kuali Discussion Groups</a>
                        </li>
                        <li>
                            <a target="_self" href="https://twitter.com/kuali" id="KradSampleAppHome-TweetLink">Follow
                                our tweets</a>
                        </li>
                        <li>
                            <a target="_self" href="http://jira.kuali.org/" id="KradSampleAppHome-BugLink">Report a
                                bug</a>
                        </li>
                        <li>
                            <a target="_self" href="http://www.kuali.org/join/how" id="KradSampleAppHome-CollabLink">Join Kuali</a>
                        </li>
                    </ul>
                </div>
            </div>
            <a class="twitter-timeline" width="240" data-tweet-limit="2" data-chrome="nofooter noscrollbar transparent noheader" data-theme="dark" href="https://twitter.com/kuali" data-widget-id="359754571758837761">Tweets by @kuali</a>
            <script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");</script>

        </div>
    </div>
</div>
<div class="demo-appFooter">
    <div class="demo-headerFooterContainer">
        <div>
            <div style="position: relative;">
                <div class="demo-foundationInfo">&#169; 2013 <a href="http://www.kuali.org" target="_blank">Kuali
                    Foundation</a></div>
                <div class="demo-poweredBy">
                    Powered by <br/> <span style="font-size: 1.2em; font-weight: bold;">KRAD</span> <br/> ${ConfigProperties.version}
                </div>
            </div>
        </div>
    </div>

</div>

</#macro>
