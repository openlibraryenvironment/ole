package org.kuali.ole.sip2.common;


import org.kuali.ole.common.OLESIP2Util;

/**
 * This class represents the patron status information that
 * is received as a part of the SIP2PatronStatusResponse response message.
 *
 * @author Gayathri A
 */
public class PatronStatus {

    private boolean chargePrivilegesDenied;
    private boolean renewalPrivilegesDenied;
    private boolean recallPrivilegesDenied;
    private boolean holdPrivilegesDenied;
    private boolean cardReportedLost;
    private boolean tooManyItemsCharged;
    private boolean tooManyItemsOverdue;
    private boolean tooManyRenewals;
    private boolean tooManyClaimsOfItemsReturned;
    private boolean tooManyItemsLost;
    private boolean excessiveOutstandingFines;
    private boolean excessiveOutstandingFees;
    private boolean recallOverdue;
    private boolean tooManyItemsBilled;

    /**
     * Constructs and initializes a new PatronStatus object with
     * all the variables set to false.
     */
    public PatronStatus() {
        this.chargePrivilegesDenied = false;
        this.renewalPrivilegesDenied = false;
        this.recallPrivilegesDenied = false;
        this.holdPrivilegesDenied = false;
        this.cardReportedLost = false;
        this.tooManyItemsCharged = false;
        this.tooManyItemsOverdue = false;
        this.tooManyRenewals = false;
        this.tooManyClaimsOfItemsReturned = false;
        this.tooManyItemsLost = false;
        this.excessiveOutstandingFines = false;
        this.excessiveOutstandingFees = false;
        this.recallOverdue = false;
        this.tooManyItemsBilled = false;
    }

    /**
     * Returns true if and only if the patron's charge privileges are
     * denied.
     *
     * @return true if charge privileges are denied, otherwise false
     */
    public boolean isChargePrivilegesDenied() {
        return chargePrivilegesDenied;
    }

    /**
     * Sets the value that tells if the patron's charge privileges are denied.
     *
     * @param chargePrivilegesDenied true or false
     */
    public void setChargePrivilegesDenied(boolean chargePrivilegesDenied) {
        this.chargePrivilegesDenied = chargePrivilegesDenied;
    }

    /**
     * Returns true if and only if the patron's renewal privileges are
     * denied.
     *
     * @return true if renewal privileges are denied, otherwise false
     */
    public boolean isRenewalPrivilegesDenied() {
        return renewalPrivilegesDenied;
    }

    /**
     * Sets the value that tells if the patron's renewal privileges are
     * denied.
     *
     * @param renewalPrivilegesDenied true or false
     */
    public void setRenewalPrivilegesDenied(boolean renewalPrivilegesDenied) {
        this.renewalPrivilegesDenied = renewalPrivilegesDenied;
    }

    /**
     * Returns true if and only if the patron's recall privileges are
     * denied.
     *
     * @return true if recall privileges are denied, otherwise false
     */
    public boolean isRecallPrivilegesDenied() {
        return recallPrivilegesDenied;
    }

    /**
     * Sets the value that tells if the patron's recall privileges are
     * denied.
     *
     * @param recallPrivilegesDenied true or false
     */
    public void setRecallPrivilegesDenied(boolean recallPrivilegesDenied) {
        this.recallPrivilegesDenied = recallPrivilegesDenied;
    }

    /**
     * Returns true if and only if the patron's hold privileges are
     * denied.
     *
     * @return true if hold privileges are denied, otherwise false
     */
    public boolean isHoldPrivilegesDenied() {
        return holdPrivilegesDenied;
    }

    /**
     * Sets the value that tells if the patron's hold privileges are
     * denied.
     *
     * @param holdPrivilegesDenied true or false
     */
    public void setHoldPrivilegesDenied(boolean holdPrivilegesDenied) {
        this.holdPrivilegesDenied = holdPrivilegesDenied;
    }

    /**
     * Returns true if and only if the patron's card is reported lost.
     *
     * @return true if card is reported lost, otherwise false
     */
    public boolean isCardReportedLost() {
        return cardReportedLost;
    }

    /**
     * Sets the value that tells if the patron's card is reported lost.
     *
     * @param cardReportedLost true or false
     */
    public void setCardReportedLost(boolean cardReportedLost) {
        this.cardReportedLost = cardReportedLost;
    }

    /**
     * Returns true if and only if the patron has too many items charged.
     *
     * @return true if patron has too many items charged, otherwise false
     */
    public boolean isTooManyItemsCharged() {
        return tooManyItemsCharged;
    }

    /**
     * Sets the value that tells if the patron has too many items charged.
     *
     * @param tooManyItemsCharged true or false
     */
    public void setTooManyItemsCharged(boolean tooManyItemsCharged) {
        this.tooManyItemsCharged = tooManyItemsCharged;
    }

    /**
     * Returns true if and only if the patron has too many overdue items.
     *
     * @return true if patron has too many overdue items, otherwise false
     */
    public boolean isTooManyItemsOverdue() {
        return tooManyItemsOverdue;
    }

    /**
     * Sets the value that tells if the patron has too many overdue items.
     *
     * @param tooManyItemsOverdue true or false
     */
    public void setTooManyItemsOverdue(boolean tooManyItemsOverdue) {
        this.tooManyItemsOverdue = tooManyItemsOverdue;
    }

    /**
     * Returns true if and only if the patron has too many renewals.
     *
     * @return true if patron has too many renewals, otherwise false
     */
    public boolean isTooManyRenewals() {
        return tooManyRenewals;
    }

    /**
     * Sets the value that tells if the patron has too many renewals.
     *
     * @param tooManyRenewals true or false
     */
    public void setTooManyRenewals(boolean tooManyRenewals) {
        this.tooManyRenewals = tooManyRenewals;
    }

    /**
     * Returns true if and only if the patron has too many claims
     * of items returned.
     *
     * @return true if patron has too many claims of items returned,
     * otherwise false
     */
    public boolean isTooManyClaimsOfItemsReturned() {
        return tooManyClaimsOfItemsReturned;
    }

    /**
     * Sets the value that tells if the patron has too many claims
     * of items returned.
     *
     * @param tooManyClaimsOfItemsReturned true or false
     */
    public void setTooManyClaimsOfItemsReturned(boolean tooManyClaimsOfItemsReturned) {
        this.tooManyClaimsOfItemsReturned = tooManyClaimsOfItemsReturned;
    }

    /**
     * Returns true if and only if the patron has too many lost items.
     *
     * @return true if patron has too many lost items, otherwise false
     */
    public boolean isTooManyItemsLost() {
        return tooManyItemsLost;
    }

    /**
     * Sets the value that tells if the patron has too many lost items.
     *
     * @param tooManyItemsLost true or false
     */
    public void setTooManyItemsLost(boolean tooManyItemsLost) {
        this.tooManyItemsLost = tooManyItemsLost;
    }

    /**
     * Returns true if and only if the patron has excessive outstanding fines.
     *
     * @return true if patron has excessive outstanding fines, otherwise false
     */
    public boolean isExcessiveOutstandingFines() {
        return excessiveOutstandingFines;
    }

    /**
     * Sets the value that tells if the patron has excessive outstanding fines.
     *
     * @param excessiveOutstandingFines true or false
     */
    public void setExcessiveOutstandingFines(boolean excessiveOutstandingFines) {
        this.excessiveOutstandingFines = excessiveOutstandingFines;
    }

    /**
     * Returns true if and only if the patron has excessive outstanding fees.
     *
     * @return true if patron has excessive outstanding fees, otherwise false
     */
    public boolean isExcessiveOutstandingFees() {
        return excessiveOutstandingFees;
    }

    /**
     * Sets the value that tells if the patron has excessive outstanding fees.
     *
     * @param excessiveOutstandingFees true or false
     */
    public void setExcessiveOutstandingFees(boolean excessiveOutstandingFees) {
        this.excessiveOutstandingFees = excessiveOutstandingFees;
    }

    /**
     * Returns true if and only if the patron has overdue recall.
     *
     * @return true if patron has overdue recall, otherwise false
     */
    public boolean isRecallOverdue() {
        return recallOverdue;
    }

    /**
     * Sets the value that tells if the patron has overdue recall.
     *
     * @param recallOverdue true or false
     */
    public void setRecallOverdue(boolean recallOverdue) {
        this.recallOverdue = recallOverdue;
    }

    /**
     * Returns true if and only if the patron has too many billed items.
     *
     * @return true if patron has too many billed items, otherwise false
     */
    public boolean isTooManyItemsBilled() {
        return tooManyItemsBilled;
    }

    /**
     * Sets the value that tells if the patron has too many billed items.
     *
     * @param tooManyItemsBilled true or false
     */
    public void setTooManyItemsBilled(boolean tooManyItemsBilled) {
        this.tooManyItemsBilled = tooManyItemsBilled;
    }

    /**
     * Returns a String presentation of this PatronStatus object.
     *
     * @return string presentation of this object
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(OLESIP2Util.bool2CharEmpty(chargePrivilegesDenied));
        builder.append(OLESIP2Util.bool2CharEmpty(renewalPrivilegesDenied));
        builder.append(OLESIP2Util.bool2CharEmpty(recallPrivilegesDenied));
        builder.append(OLESIP2Util.bool2CharEmpty(holdPrivilegesDenied));
        builder.append(OLESIP2Util.bool2CharEmpty(cardReportedLost));
        builder.append(OLESIP2Util.bool2CharEmpty(tooManyItemsCharged));
        builder.append(OLESIP2Util.bool2CharEmpty(tooManyItemsOverdue));
        builder.append(OLESIP2Util.bool2CharEmpty(tooManyRenewals));
        builder.append(OLESIP2Util.bool2CharEmpty(tooManyClaimsOfItemsReturned));
        builder.append(OLESIP2Util.bool2CharEmpty(tooManyItemsLost));
        builder.append(OLESIP2Util.bool2CharEmpty(excessiveOutstandingFines));
        builder.append(OLESIP2Util.bool2CharEmpty(excessiveOutstandingFees));
        builder.append(OLESIP2Util.bool2CharEmpty(recallOverdue));
        builder.append(OLESIP2Util.bool2CharEmpty(tooManyItemsBilled));
        return builder.toString();
    }
}
