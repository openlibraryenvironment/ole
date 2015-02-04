/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.ole.select.document.service.impl;

import org.kuali.ole.module.purap.PurapConstants;
import org.kuali.ole.module.purap.businessobject.PurApAccountingLine;
import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.ole.module.purap.document.PurchasingAccountsPayableDocument;
import org.kuali.ole.module.purap.document.service.PurapService;
import org.kuali.ole.module.purap.service.impl.PurapAccountingServiceImpl;
import org.kuali.ole.module.purap.util.PurApItemUtils;
import org.kuali.ole.module.purap.util.PurApObjectUtils;
import org.kuali.ole.select.businessobject.OlePaymentRequestItem;
import org.kuali.ole.select.businessobject.OlePurchaseOrderItem;
import org.kuali.ole.select.document.service.OlePurapAccountingService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.businessobject.AccountingLineBase;
import org.kuali.ole.sys.businessobject.SourceAccountingLine;
import org.kuali.ole.sys.service.NonTransactional;
import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.ole.select.businessobject.OleInvoiceItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Contains a number of helper methods to deal with accounts on Purchasing Accounts Payable Documents
 */

@NonTransactional
public class OlePurapAccountingServiceImpl extends PurapAccountingServiceImpl implements OlePurapAccountingService {
    private static org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePurapAccountingServiceImpl.class);

    private PurapService purapService;


    @Override
    public void setPurapService(PurapService purapService) {
        this.purapService = purapService;
    }

    /**
     * calculates values for a list of accounting lines based on an amount
     *
     * @param sourceAccountingLines
     * @param totalAmount
     */
    @Override
    public <T extends PurApAccountingLine> void updateAccountAmountsWithTotal(List<T> sourceAccountingLines, KualiDecimal totalAmount) {
        if ((totalAmount != null) && KualiDecimal.ZERO.compareTo(totalAmount) != 0) {

            KualiDecimal accountTotal = KualiDecimal.ZERO;
            T lastAccount = null;
            BigDecimal accountTotalPercent = BigDecimal.ZERO;


            for (T account : sourceAccountingLines) {
                if (ObjectUtils.isNotNull(account.getAccountLinePercent())) {
                    BigDecimal pct = new BigDecimal(account.getAccountLinePercent().toString()).divide(new BigDecimal(100));
                    account.setAmount(new KualiDecimal(pct.multiply(new BigDecimal(totalAmount.toString())).setScale(KualiDecimal.SCALE, KualiDecimal.ROUND_BEHAVIOR)));
                } else if (ObjectUtils.isNotNull(account.getAmount()) && ObjectUtils.isNull(account.getAccountLinePercent())) {
                    KualiDecimal dollar = account.getAmount().multiply(new KualiDecimal(100));
                    BigDecimal dollarToPercent = dollar.bigDecimalValue().divide((totalAmount.bigDecimalValue()), 0, RoundingMode.FLOOR);
                    account.setAccountLinePercent(dollarToPercent);
                } else if (ObjectUtils.isNotNull(account.getAmount()) && ObjectUtils.isNotNull(account.getAccountLinePercent())) {
                    account.setAmount(account.getAmount());
                    account.setAccountLinePercent(account.getAccountLinePercent());
                } else {
                    account.setAmount(KualiDecimal.ZERO);
                }
                accountTotal = accountTotal.add(account.getAmount());
                lastAccount = account;
            }

            if (lastAccount != null) {
                KualiDecimal difference = new KualiDecimal(0);
                difference = totalAmount.subtract(accountTotal);
                if (ObjectUtils.isNotNull(lastAccount.getAmount())) {
                    if ((difference.abs()).isLessEqual(new KualiDecimal(1).multiply(new KualiDecimal(sourceAccountingLines.size()).divide(new KualiDecimal(2))))) {

                        lastAccount.setAmount(lastAccount.getAmount().add(difference));
                    } else {
                        lastAccount.setAmount(lastAccount.getAmount());
                    }
                }
                BigDecimal percentDifference = new BigDecimal(100).subtract(accountTotalPercent).setScale(BIG_DECIMAL_SCALE,BigDecimal.ROUND_CEILING);
                if (ObjectUtils.isNotNull(lastAccount.getAccountLinePercent())) {

                    KualiDecimal differencePercent = (((new KualiDecimal(accountTotalPercent)).subtract(new KualiDecimal(100))).abs());
                    if ((differencePercent.abs()).isLessEqual(new KualiDecimal(1).multiply((new KualiDecimal(sourceAccountingLines.size()).divide(new KualiDecimal(2)))))) {
                        lastAccount.setAccountLinePercent(lastAccount.getAccountLinePercent().add(percentDifference));
                    } else {
                        lastAccount.setAccountLinePercent(lastAccount.getAccountLinePercent());
                    }
                }
            }
        } else {
            // zero out if extended price is zero
            for (T account : sourceAccountingLines) {
                account.setAmount(KualiDecimal.ZERO);
            }
        }
    }

    /**
     * @see org.kuali.ole.module.purap.service.PurapAccountingService#generateAccountDistributionForProration(java.util.List,
     *      org.kuali.rice.kns.util.KualiDecimal, java.lang.Integer)
     */
    @Override
    public List<PurApAccountingLine> generateAccountDistributionForProration(List<SourceAccountingLine> accounts, KualiDecimal totalAmount, Integer percentScale, Class clazz) {
    /* String methodName = "generateAccountDistributionForProration()";
     if (LOG.isDebugEnabled()) {
         LOG.debug(methodName + " started");
     }
     List<PurApAccountingLine> newAccounts = new ArrayList();

     if (totalAmount.isZero()) {
         throwRuntimeException(methodName, "Purchasing/Accounts Payable account distribution for proration does not allow zero dollar total.");
     }

     BigDecimal percentTotal = BigDecimal.ZERO;
     BigDecimal totalAmountBigDecimal = totalAmount.bigDecimalValue();
     for (SourceAccountingLine accountingLine : accounts) {
         if (LOG.isDebugEnabled()) {
             LOG.debug(methodName + " " + accountingLine.getAccountNumber() + " " + accountingLine.getAmount() + "/" + totalAmountBigDecimal);
         }
         //  BigDecimal pct = accountingLine.getAmount().bigDecimalValue().divide(totalAmountBigDecimal, percentScale, BIG_DECIMAL_ROUNDING_MODE);
         BigDecimal pct = BigDecimal.ZERO;
         if (accountingLine.getSourceAcountLinePercent() != null)
             pct = accountingLine.getSourceAcountLinePercent();
         else
             pct = accountingLine.getAmount().bigDecimalValue().divide(totalAmountBigDecimal, percentScale, BIG_DECIMAL_ROUNDING_MODE);
         pct = pct.stripTrailingZeros().multiply(ONE_HUNDRED);

         if (LOG.isDebugEnabled()) {
             LOG.debug(methodName + " pct = " + pct + "  (trailing zeros removed)");
         }

         BigDecimal lowestPossible = this.getLowestPossibleRoundUpNumber();
         if (lowestPossible.compareTo(pct) <= 0) {
             PurApAccountingLine newAccountingLine;
             newAccountingLine = null;

             try {
                 newAccountingLine = (PurApAccountingLine) clazz.newInstance();
             } catch (InstantiationException e) {
                 e.printStackTrace();
             } catch (IllegalAccessException e) {
                 e.printStackTrace();
             }

             PurApObjectUtils.populateFromBaseClass(AccountingLineBase.class, accountingLine, newAccountingLine);
             newAccountingLine.setAccountLinePercent(pct);
             if (LOG.isDebugEnabled()) {
                 LOG.debug(methodName + " adding " + newAccountingLine.getAccountLinePercent());
             }
             newAccounts.add(newAccountingLine);
             percentTotal = percentTotal.add(newAccountingLine.getAccountLinePercent());
             if (LOG.isDebugEnabled()) {
                 LOG.debug(methodName + " total = " + percentTotal);
             }
         }
     }

     if ((percentTotal.compareTo(BigDecimal.ZERO)) == 0) {
        *//*
            * This means there are so many accounts or so strange a distribution that we can't round properly... not sure of viable
            * solution
            *//*
            throwRuntimeException(methodName, "Can't round properly due to number of accounts");
        }

        // Now deal with rounding
        if ((ONE_HUNDRED.compareTo(percentTotal)) < 0) {
           *//*
            * The total percent is greater than one hundred Here we find the account that occurs latest in our list with a percent
            * that is higher than the difference and we subtract off the difference
            *//*
            BigDecimal difference = percentTotal.subtract(ONE_HUNDRED);
            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " Rounding up by " + difference);
            }

            boolean foundAccountToUse = false;
            int currentNbr = newAccounts.size() - 1;
            while (currentNbr >= 0) {
                PurApAccountingLine potentialSlushAccount = (PurApAccountingLine) newAccounts.get(currentNbr);

                if ((difference.compareTo(potentialSlushAccount.getAccountLinePercent())) < 0) {
                    // the difference amount is less than the current accounts percent... use this account
                    // the 'potentialSlushAccount' technically is now the true 'Slush Account'
                    potentialSlushAccount.setAccountLinePercent(potentialSlushAccount.getAccountLinePercent().subtract(difference).movePointLeft(2).stripTrailingZeros().movePointRight(2));
                    foundAccountToUse = true;
                    break;
                }
                currentNbr--;
            }

            if (!foundAccountToUse) {
               *//*
                * We could not find any account in our list where the percent of that account was greater than that of the
                * difference... doing so on just any account could result in a negative percent value
                *//*
                throwRuntimeException(methodName, "Can't round properly due to math calculation error");
            }

        } else if ((ONE_HUNDRED.compareTo(percentTotal)) > 0) {
           *//*
            * The total percent is less than one hundred Here we find the last account in our list and add the remaining required
            * percent to its already calculated percent
            *//*
            BigDecimal difference = ONE_HUNDRED.subtract(percentTotal);
            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " Rounding down by " + difference);
            }
            PurApAccountingLine slushAccount = (PurApAccountingLine) newAccounts.get(newAccounts.size() - 1);
            slushAccount.setAccountLinePercent(slushAccount.getAccountLinePercent().add(difference).movePointLeft(2).stripTrailingZeros().movePointRight(2));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " ended");
        }
        return newAccounts;*/
        return super.generateAccountDistributionForProration(accounts, totalAmount, percentScale, clazz);
    }

    /**
     * Generates an account summary, that is it creates a list of source accounts by rounding up the purap accounts off of the purap
     * items.
     *
     * @param items                    the items to determ
     * @param itemTypeCodes            the item types to determine whether to look at an item in combination with itemTypeCodesAreIncluded
     * @param itemTypeCodesAreIncluded value to tell whether the itemTypeCodes parameter lists inclusion or exclusion variables
     * @param useZeroTotals            whether to include items with a zero dollar total
     * @param useAlternateAmount       an alternate amount used in certain cases for GL entry
     * @return a list of source accounts
     */
    @Override
    protected List<SourceAccountingLine> generateAccountSummary(List<PurApItem> items, Set<String> itemTypeCodes, Boolean itemTypeCodesAreIncluded, Boolean useZeroTotals, Boolean useAlternateAmount, Boolean useTaxIncluded, Boolean taxableOnly) {

        items = PurApItemUtils.getAboveTheLineOnly(items);
        List<PurApItem> itemsToProcess = getProcessablePurapItems(items, itemTypeCodes, itemTypeCodesAreIncluded, useZeroTotals);
        Map<PurApAccountingLine, KualiDecimal> accountMap = new HashMap<PurApAccountingLine, KualiDecimal>();
        Map<PurApAccountingLine, KualiDecimal> qtyMap = new HashMap<PurApAccountingLine, KualiDecimal>();

        for (PurApItem currentItem : itemsToProcess) {
            if (PurApItemUtils.checkItemActive(currentItem)) {
                List<PurApAccountingLine> sourceAccountingLines = currentItem.getSourceAccountingLines();

                // skip if item is not taxable and taxable only flag has been set
                if (taxableOnly) {
                    PurchasingAccountsPayableDocument document = currentItem.getPurapDocument();
                    if (!purapService.isTaxableForSummary(document.isUseTaxIndicator(), purapService.getDeliveryState(document), currentItem)) {
                        continue;
                    }
                }

                if (!useTaxIncluded) {
                    // if no use tax set the source accounting lines to a clone so we can update
                    // them to be based on the non tax amount
                    PurApItem cloneItem = (PurApItem) ObjectUtils.deepCopy(currentItem);
                    sourceAccountingLines = cloneItem.getSourceAccountingLines();
                    updateAccountAmountsWithTotal(sourceAccountingLines, currentItem.getTotalRemitAmount());
                }

                for (PurApAccountingLine account : sourceAccountingLines) {

                    // skip account if not taxable and taxable only flag is set
                    if (taxableOnly) {
                        PurchasingAccountsPayableDocument document = currentItem.getPurapDocument();
                        // check if account is not taxable, if not skip this account
                        if (!purapService.isAccountingLineTaxable(account, purapService.isDeliveryStateTaxable(purapService.getDeliveryState(document)))) {
                            continue;
                        }
                    }

                    // getting the total to set on the account
                    KualiDecimal total = KualiDecimal.ZERO;
                    KualiDecimal qty = KualiDecimal.ZERO;
                    if (accountMap.containsKey(account)) {
                        total = accountMap.get(account);
                    }
                    if (qtyMap.containsKey(account)) {
                        qty = qtyMap.get(account);
                    }

                    if (useAlternateAmount) {
                        total = total.add(account.getAlternateAmountForGLEntryCreation());
                    } else {
                        if (ObjectUtils.isNotNull(account.getAmount())) {
                            total = total.add(account.getAmount());
                        }
                        if (currentItem.getItemQuantity() != null && account.getAccountLinePercent() != null) {
                            qty = qty.add(currentItem.getItemQuantity().divide(new KualiDecimal(ONE_HUNDRED)).multiply(new KualiDecimal(account.getAccountLinePercent())));
                        }
                    }

                    accountMap.put(account, total);
                    qtyMap.put(account, qty);
                }
            }
        }

        // convert list of PurApAccountingLine objects to SourceAccountingLineObjects
        Iterator<PurApAccountingLine> iterator = accountMap.keySet().iterator();
        List<SourceAccountingLine> sourceAccounts = new ArrayList<SourceAccountingLine>();
        for (Iterator<PurApAccountingLine> iter = iterator; iter.hasNext(); ) {
            PurApAccountingLine accountToConvert = iter.next();
            if (accountToConvert.isEmpty()) {
                String errorMessage = "Found an 'empty' account in summary generation " + accountToConvert.toString();
                LOG.error("generateAccountSummary() " + errorMessage);
                throw new RuntimeException(errorMessage);
            }
            KualiDecimal sourceLineTotal = accountMap.get(accountToConvert);
            SourceAccountingLine sourceLine = accountToConvert.generateSourceAccountingLine();
            sourceLine.setAmount(sourceLineTotal);
            sourceLine.setSourceAccountQty(qtyMap.get(accountToConvert));
            sourceAccounts.add(sourceLine);
        }

        // sort the sourceAccounts list first by account number, then by object code, ignoring chart code
        Collections.sort(sourceAccounts, new Comparator<SourceAccountingLine>() {
            @Override
            public int compare(SourceAccountingLine sal1, SourceAccountingLine sal2) {
                int compare = 0;
                if (sal1 != null && sal2 != null) {
                    if (sal1.getAccountNumber() != null && sal2.getAccountNumber() != null) {
                        compare = sal1.getAccountNumber().compareTo(sal2.getAccountNumber());
                        if (compare == 0) {
                            if (sal1.getFinancialObjectCode() != null && sal2.getFinancialObjectCode() != null) {
                                compare = sal1.getFinancialObjectCode().compareTo(sal2.getFinancialObjectCode());
                            }
                        }
                    }
                }
                return compare;
            }
        });

        return sourceAccounts;
    }

    /**
     * Generates an account summary, that is it creates a list of source accounts by rounding up the purap accounts off of the purap
     * items.
     *
     * @param items                    the items to determ
     * @param itemTypeCodes            the item types to determine whether to look at an item in combination with itemTypeCodesAreIncluded
     * @param itemTypeCodesAreIncluded value to tell whether the itemTypeCodes parameter lists inclusion or exclusion variables
     * @param useZeroTotals            whether to include items with a zero dollar total
     * @param useAlternateAmount       an alternate amount used in certain cases for GL entry
     * @return a list of source accounts
     */
    protected List<SourceAccountingLine> generateAccountSummaryForManual(List<PurApItem> items, Set<String> itemTypeCodes, Boolean itemTypeCodesAreIncluded, Boolean useZeroTotals, Boolean useAlternateAmount, Boolean useTaxIncluded, Boolean taxableOnly) {
        items = PurApItemUtils.getAboveTheLineOnly(items);
        List<PurApItem> itemsToProcess = getProcessablePurapItems(items, itemTypeCodes, itemTypeCodesAreIncluded, useZeroTotals);
        Map<PurApAccountingLine, KualiDecimal> accountMap = new HashMap<PurApAccountingLine, KualiDecimal>();

        for (PurApItem currentItem : itemsToProcess) {
            if (PurApItemUtils.checkItemActive(currentItem)) {
                List<PurApAccountingLine> sourceAccountingLines = currentItem.getSourceAccountingLines();

                // skip if item is not taxable and taxable only flag has been set
                if (taxableOnly) {
                    PurchasingAccountsPayableDocument document = currentItem.getPurapDocument();
                    if (!purapService.isTaxableForSummary(document.isUseTaxIndicator(), purapService.getDeliveryState(document), currentItem)) {
                        continue;
                    }
                }

                if (!useTaxIncluded) {
                    // if no use tax set the source accounting lines to a clone so we can update
                    // them to be based on the non tax amount
                    PurApItem cloneItem = (PurApItem) ObjectUtils.deepCopy(currentItem);
                    sourceAccountingLines = cloneItem.getSourceAccountingLines();
                    updateAccountAmountsWithTotal(sourceAccountingLines, currentItem.getTotalRemitAmount());
                }

                for (PurApAccountingLine account : sourceAccountingLines) {

                    // skip account if not taxable and taxable only flag is set
                    if (taxableOnly) {
                        PurchasingAccountsPayableDocument document = currentItem.getPurapDocument();
                        // check if account is not taxable, if not skip this account
                        if (!purapService.isAccountingLineTaxable(account, purapService.isDeliveryStateTaxable(purapService.getDeliveryState(document)))) {
                            continue;
                        }
                    }

                    // getting the total to set on the account
                    KualiDecimal total = KualiDecimal.ZERO;
                    if (accountMap.containsKey(account)) {
                        total = accountMap.get(account);
                    }

                    if (useAlternateAmount) {
                        total = total.add(account.getAlternateAmountForGLEntryCreation());
                    } else {
                        if (ObjectUtils.isNotNull(account.getAmount())) {
                            total = total.add(account.getAmount());
                        }
                    }

                    accountMap.put(account, total);
                }
            }
        }

        // convert list of PurApAccountingLine objects to SourceAccountingLineObjects
        Iterator<PurApAccountingLine> iterator = accountMap.keySet().iterator();
        List<SourceAccountingLine> sourceAccounts = new ArrayList<SourceAccountingLine>();
        for (Iterator<PurApAccountingLine> iter = iterator; iter.hasNext(); ) {
            PurApAccountingLine accountToConvert = iter.next();
            if (accountToConvert.isEmpty()) {
                String errorMessage = "Found an 'empty' account in summary generation " + accountToConvert.toString();
                LOG.error("generateAccountSummary() " + errorMessage);
                throw new RuntimeException(errorMessage);
            }
            SourceAccountingLine sourceLine = accountToConvert.generateSourceAccountingLine();
            sourceAccounts.add(sourceLine);
        }

        // sort the sourceAccounts list first by account number, then by object code, ignoring chart code
        Collections.sort(sourceAccounts, new Comparator<SourceAccountingLine>() {
            @Override
            public int compare(SourceAccountingLine sal1, SourceAccountingLine sal2) {
                int compare = 0;
                if (sal1 != null && sal2 != null) {
                    if (sal1.getAccountNumber() != null && sal2.getAccountNumber() != null) {
                        compare = sal1.getAccountNumber().compareTo(sal2.getAccountNumber());
                        if (compare == 0) {
                            if (sal1.getFinancialObjectCode() != null && sal2.getFinancialObjectCode() != null) {
                                compare = sal1.getFinancialObjectCode().compareTo(sal2.getFinancialObjectCode());
                            }
                        }
                    }
                }
                return compare;
            }
        });

        return sourceAccounts;
    }

    @Override
    public List<PurApAccountingLine> generateAccountDistributionForProrationByQty(List<SourceAccountingLine> accounts, KualiDecimal totatQty, Integer percentScale, Class clazz) {
        String methodName = "generateAccountDistributionForProrationByQty()";
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " started");
        }
        List<PurApAccountingLine> newAccounts = new ArrayList();

        if (totatQty.isZero()) {
            throwRuntimeException(methodName, "Purchasing/Accounts Payable account distribution for proration does not allow zero dollar total.");
        }

        BigDecimal percentTotal = BigDecimal.ZERO;
        BigDecimal totalQtyBigDecimal = totatQty.bigDecimalValue();
        for (SourceAccountingLine accountingLine : accounts) {
            KualiDecimal qty = KualiDecimal.ZERO;
            if (ObjectUtils.isNotNull(accountingLine.getSourceAccountQty())) {
                qty = accountingLine.getSourceAccountQty();
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " " + accountingLine.getAccountNumber() + " " + qty + "/" + totalQtyBigDecimal);
            }
            BigDecimal pct = qty.bigDecimalValue().divide(totalQtyBigDecimal, percentScale, BIG_DECIMAL_ROUNDING_MODE);
            pct = pct.stripTrailingZeros().multiply(ONE_HUNDRED);

            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " pct = " + pct + "  (trailing zeros removed)");
            }

            BigDecimal lowestPossible = this.getLowestPossibleRoundUpNumber();
            if (lowestPossible.compareTo(pct) <= 0) {
                PurApAccountingLine newAccountingLine;
                newAccountingLine = null;

                try {
                    newAccountingLine = (PurApAccountingLine) clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

                PurApObjectUtils.populateFromBaseClass(AccountingLineBase.class, accountingLine, newAccountingLine);
                newAccountingLine.setAccountLinePercent(pct);
                if (LOG.isDebugEnabled()) {
                    LOG.debug(methodName + " adding " + newAccountingLine.getAccountLinePercent());
                }
                newAccounts.add(newAccountingLine);
                percentTotal = percentTotal.add(newAccountingLine.getAccountLinePercent());
                if (LOG.isDebugEnabled()) {
                    LOG.debug(methodName + " total = " + percentTotal);
                }
            }
        }

        if ((percentTotal.compareTo(BigDecimal.ZERO)) == 0) {
            /*
             * This means there are so many accounts or so strange a distribution that we can't round properly... not sure of viable
             * solution
             */
            throwRuntimeException(methodName, "Can't round properly due to number of accounts");
        }

        // Now deal with rounding
        if ((ONE_HUNDRED.compareTo(percentTotal)) < 0) {
            /*
             * The total percent is greater than one hundred Here we find the account that occurs latest in our list with a percent
             * that is higher than the difference and we subtract off the difference
             */
            BigDecimal difference = percentTotal.subtract(ONE_HUNDRED);
            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " Rounding up by " + difference);
            }

            boolean foundAccountToUse = false;
            int currentNbr = newAccounts.size() - 1;
            while (currentNbr >= 0) {
                PurApAccountingLine potentialSlushAccount = newAccounts.get(currentNbr);

                BigDecimal linePercent = BigDecimal.ZERO;
                if (ObjectUtils.isNotNull(potentialSlushAccount.getAccountLinePercent())) {
                    linePercent = potentialSlushAccount.getAccountLinePercent();
                }

                if ((difference.compareTo(linePercent)) < 0) {
                    // the difference amount is less than the current accounts percent... use this account
                    // the 'potentialSlushAccount' technically is now the true 'Slush Account'
                    potentialSlushAccount.setAccountLinePercent(linePercent.subtract(difference).movePointLeft(2).stripTrailingZeros().movePointRight(2));
                    foundAccountToUse = true;
                    break;
                }
                currentNbr--;
            }

            if (!foundAccountToUse) {
                /*
                 * We could not find any account in our list where the percent of that account was greater than that of the
                 * difference... doing so on just any account could result in a negative percent value
                 */
                throwRuntimeException(methodName, "Can't round properly due to math calculation error");
            }

        } else if ((ONE_HUNDRED.compareTo(percentTotal)) > 0) {
            /*
             * The total percent is less than one hundred Here we find the last account in our list and add the remaining required
             * percent to its already calculated percent
             */
            BigDecimal difference = ONE_HUNDRED.subtract(percentTotal);
            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " Rounding down by " + difference);
            }
            PurApAccountingLine slushAccount = newAccounts.get(newAccounts.size() - 1);

            BigDecimal slushLinePercent = BigDecimal.ZERO;
            if (ObjectUtils.isNotNull(slushAccount.getAccountLinePercent())) {
                slushLinePercent = slushAccount.getAccountLinePercent();
            }

            slushAccount.setAccountLinePercent(slushLinePercent.add(difference).movePointLeft(2).stripTrailingZeros().movePointRight(2));
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " ended");
        }
        return newAccounts;
    }

    @Override
    public List<SourceAccountingLine> generateSummaryForManual(List<PurApItem> items) {
        String methodName = "generateSummary()";
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " started");
        }
        List<SourceAccountingLine> returnList = generateAccountSummaryForManual(items, null, ITEM_TYPES_EXCLUDED_VALUE, ZERO_TOTALS_RETURNED_VALUE, ALTERNATE_AMOUNT_NOT_USED, USE_TAX_INCLUDED, false);
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " ended");
        }
        return returnList;
    }

    @Override
    public List<PurApAccountingLine> generateAccountDistributionForProrationByManual(List<SourceAccountingLine> accounts, Class clazz) {
        String methodName = "generateAccountDistributionForProrationByManual()";
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " started");
        }
        List<PurApAccountingLine> newAccounts = new ArrayList();
        BigDecimal percent = new BigDecimal(0);
        if (accounts.size() > 0) {
            percent = new BigDecimal(100).divide(new BigDecimal(accounts.size()), 2, RoundingMode.HALF_UP);
        }
        for (SourceAccountingLine accountingLine : accounts) {

            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " " + accountingLine.getAccountNumber());
            }
            PurApAccountingLine newAccountingLine;
            newAccountingLine = null;

            try {
                newAccountingLine = (PurApAccountingLine) clazz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            PurApObjectUtils.populateFromBaseClass(AccountingLineBase.class, accountingLine, newAccountingLine);
            newAccountingLine.setAccountLinePercent(percent);
            newAccountingLine.setAmount(new KualiDecimal(0));
            if (LOG.isDebugEnabled()) {
                LOG.debug(methodName + " adding " + newAccountingLine.getAccountLinePercent());
            }
            newAccounts.add(newAccountingLine);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(methodName + " ended");
        }
        return newAccounts;
    }


}
