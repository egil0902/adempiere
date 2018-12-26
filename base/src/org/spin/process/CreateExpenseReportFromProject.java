/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/

package org.spin.process;

import java.math.BigDecimal;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MTimeExpense;
import org.compiere.model.MTimeExpenseLine;
import org.compiere.model.X_S_TimeExpense;

/** Generated Process for (Create Expense Report from Drop Ship Orders)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.1
 */
public class CreateExpenseReportFromProject extends CreateExpenseReportFromProjectAbstract {
	/**	Expense Report	*/
	private MTimeExpense expenseReport = null;
	/**	Result	*/
	private StringBuffer result = new StringBuffer();
	/**	Counter	*/
	private int created = 0;
	
	@Override
	protected String doIt() throws Exception {
		//	Loop for keys
		for(Integer key : getSelectionKeys()) {
			int orderLineId = getSelectionAsInt(key, "SOL_C_OrderLine_ID");
			int linkOrderLineId = getSelectionAsInt(key, "SOL_Link_OrderLine_ID");
			MOrderLine orderLine = new MOrderLine(getCtx(), orderLineId, get_TrxName());
			MOrder order = orderLine.getParent();
			//	Create new
			if(expenseReport == null) {
				processDocument();
				createExpenseReport(order);
			}
			//	Set Quantity
			BigDecimal qty = getSelectionAsBigDecimal(key, "SOL_QtyToDeliver");
			MTimeExpenseLine expenseLine = new MTimeExpenseLine(getCtx(), 0, get_TrxName());
			expenseLine.setS_TimeExpense_ID(expenseReport.getS_TimeExpense_ID());
			expenseLine.setDateExpense(getDateReport());
			expenseLine.setIsTimeReport(true);
			expenseLine.setIsInvoiced(false);
			//	Validate product
			expenseLine.setM_Product_ID(orderLine.getM_Product_ID());
			expenseLine.setQty(qty);
			if(order.get_ValueAsInt("S_Contract_ID") > 0) {
				expenseLine.set_ValueOfColumn("S_ContractLine_ID", order.get_ValueAsInt("S_Contract_ID"));
			}
			if(order.getC_Project_ID() > 0) {
				expenseLine.setC_Project_ID(order.getC_Project_ID());
			}
			expenseLine.setC_OrderLine_ID(orderLineId);
			if(linkOrderLineId > 0) {
				MOrderLine linkOrderLine = new MOrderLine(getCtx(), linkOrderLineId, get_TrxName());
				MOrder linkOrder = linkOrderLine.getParent();
				if(linkOrder.getDocStatus().equals(MOrder.ACTION_Complete)) {
					expenseLine.set_ValueOfColumn("Link_OrderLine_ID", linkOrderLineId);
				}
			}
			expenseLine.saveEx();
		}
		//	Last
		processDocument();
		
		return getDocumentResult();
	}
	
	/**
	 * Process Expense
	 */
	private void processDocument() {
		if(expenseReport == null) {
			return;
		}
		//	Process Selection
		if(!expenseReport.processIt(getDocAction())) {
			throw new AdempiereException("@Error@ " + expenseReport.getProcessMsg());
		}
		//	
		expenseReport.saveEx();
		addDocumentResult(expenseReport.getDocumentNo());
		addLog(expenseReport.getS_TimeExpense_ID(), null, null, expenseReport.toString());
	}
	
	/**
	 * Create Expense Report
	 * @param order
	 */
	private void createExpenseReport(MOrder order) {
		expenseReport = new MTimeExpense(getCtx(), 0, get_TrxName());
		expenseReport.setDocStatus(X_S_TimeExpense.DOCSTATUS_Drafted);
		expenseReport.setDocAction(X_S_TimeExpense.DOCACTION_Complete);
		expenseReport.setC_BPartner_ID(order.getC_BPartner_ID());
		expenseReport.setDateReport(getDateReport());
		expenseReport.setM_Warehouse_ID(order.getM_Warehouse_ID());
		expenseReport.setM_PriceList_ID(order.getM_PriceList_ID());
		if(order.get_ValueAsInt("S_Contract_ID") > 0) {
			expenseReport.set_ValueOfColumn("S_Contract_ID", order.get_ValueAsInt("S_Contract_ID"));
		}
		expenseReport.saveEx();
	}
	
	/**
	 * Add document to result
	 * @param documentNo
	 */
	private void addDocumentResult(String documentNo) {
		created++;
		//	Add message
		if(result.length() > 0) {
			result.append(", ");
		}
		result.append(documentNo);
	}
	
	/**
	 * Get Document Result
	 * @return
	 */
	private String getDocumentResult() {
		//	Add message
		String returnMessage = "@Created@: " + created;
		if(result.length() > 0) {
			returnMessage = "@Created@: " + created + " [" + result + "]";
		}
		return returnMessage;
	}
}