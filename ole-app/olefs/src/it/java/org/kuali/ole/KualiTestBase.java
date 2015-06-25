package org.kuali.ole;

import org.kuali.ole.fixture.UserNameFixture;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.util.ErrorMessage;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 10/2/13
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class KualiTestBase extends OLETestCaseBase implements KualiTestConstants  {
    protected void changeCurrentUser(UserNameFixture sessionUser) throws Exception {
        Person p = sessionUser.getPerson();
        GlobalVariables.setUserSession(new UserSession(p.getPrincipalName()));
    }

    protected void changeCurrentUser(Person p) throws Exception {
        GlobalVariables.setUserSession(new UserSession(p.getPrincipalName()));
    }

    /**
     * This method is used during debugging to dump the contents of the error map, including the key names. It is not used by the
     * application in normal circumstances at all.
     */
    protected String dumpMessageMapErrors() {
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            return "";
        }

        StringBuilder message = new StringBuilder();
        for ( String key : GlobalVariables.getMessageMap().getErrorMessages().keySet() ) {
            List<ErrorMessage> errorList = GlobalVariables.getMessageMap().getErrorMessages().get(key);

            for ( ErrorMessage em : errorList ) {
                message.append(key).append(" = ").append( em.getErrorKey() );
                if (em.getMessageParameters() != null) {
                    message.append( " : " );
                    String delim = "";
                    for ( String parm : em.getMessageParameters() ) {
                        message.append(delim).append("'").append(parm).append("'");
                        if ("".equals(delim)) {
                            delim = ", ";
                        }
                    }
                }
            }
            message.append( '\n' );
        }
        return message.toString();
    }

}
