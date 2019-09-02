/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2015 e-Evolution Consultants. All Rights Reserved.      *
 * Copyright (C) 2003-2015 Victor Pérez Juárez 								  *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Contributor(s): Victor Pérez Juárez  (victor.perez@e-evolution.com)		  *
 * Sponsors: e-Evolution Consultants (http://www.e-evolution.com/)            *
 *****************************************************************************/

package org.eevolution.form;

import org.compiere.process.IPrintDocument;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.adempiere.webui.window.SimplePDFViewer;
import org.compiere.model.I_AD_Table;
import org.compiere.model.MQuery;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.PrintInfo;
import org.compiere.print.MPrintFormat;
import org.compiere.print.ReportEngine;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import java.io.FileInputStream;

/**
 * Created by eEvolution author Victor Perez <victor.perez@e-evolution.com> on 25/01/15.
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 * Improve definition
 */
public class WPrintDocument implements IPrintDocument {

    public void print(PO document, int printFormatId, int windowNo, boolean askPrint, boolean batchPrintMode) {
        boolean retValue = true;
        String tableName = MTable.get(Env.getCtx(), document.get_Table_ID()).get_Translation(I_AD_Table.COLUMNNAME_Name);
        if (FDialog.ask(windowNo, SessionManager.getAppDesktop().getComponent(), "Print", tableName)) {
            do {
                try {
                    String keyColumnName = document.get_KeyColumns()[0];
                    MPrintFormat format = MPrintFormat.get(
                            Env.getCtx(),
                            printFormatId,
                            false);
                    MQuery query = new MQuery(document.get_TableName());
                    query.addRestriction(keyColumnName, MQuery.EQUAL, document.get_ValueAsInt(keyColumnName));

                    //	Engine
                    PrintInfo info = new PrintInfo(
                            document.get_TableName(),
                            document.get_Table_ID(),
                            document.get_ValueAsInt(keyColumnName));
                    ReportEngine reportEngine = new ReportEngine(Env.getCtx(), format, query, info);
                    if (reportEngine != null) {
                        SimplePDFViewer win = new SimplePDFViewer(format.getName(), new FileInputStream(reportEngine.getPDF()));
                        SessionManager.getAppDesktop().showWindow(win, "center");
                    }

                } catch (Exception e) {
                	
                } finally {
                    retValue = FDialog.ask(windowNo, SessionManager.getAppDesktop().getComponent(), Msg.getMsg(Env.getCtx(), "PrintoutOK?"));
                }

            } while (!retValue);
        }
    }
}
