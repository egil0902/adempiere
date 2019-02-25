/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2016 e-Evolution Consultants. All Rights Reserved.      *
 * Copyright (C) 2003-2016 Victor Pèrez Juárez 								  *
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
 * Contributor(s): Victor Pérez Juúrez  (victor.perez@e-evolution.com)		  *
 * Sponsors: e-Evolution Consultants (http://www.e-evolution.com/)            *
 *****************************************************************************/

package org.eevolution.form;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Level;

import org.adempiere.webui.component.ConfirmPanel;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.ListModelTable;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Textbox;
import org.adempiere.webui.component.WListbox;
import org.adempiere.webui.editor.WLocatorEditor;
import org.adempiere.webui.editor.WNumberEditor;
import org.adempiere.webui.editor.WStringEditor;
import org.adempiere.exceptions.ValueChangeEvent;
import org.adempiere.exceptions.ValueChangeListener;
import org.adempiere.webui.panel.ADForm;
import org.adempiere.webui.panel.CustomForm;
import org.adempiere.webui.panel.IFormController;
import org.adempiere.webui.session.SessionManager;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MAttributeSetInstance;
import org.compiere.model.MLocatorLookup;
import org.compiere.model.MMovement;
import org.compiere.model.MProduct;
import org.compiere.process.ProcessInfo;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.service.ScanBar;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Borderlayout;
import org.zkoss.zul.Center;
import org.zkoss.zul.North;
import org.zkoss.zul.South;
import org.zkoss.zul.Separator;

/**
 * Implementation Scan Bar UI
 * @author victor.perez@e-evoluton.com, www.e-evolution.com 
 */
public class WScanBarUI extends ScanBar implements IFormController, EventListener ,ValueChangeListener
{
    /**
	 *
	 */
	private static final long serialVersionUID = 4210542409436277344L;


    /** Logger.          */
    private static CLogger log = CLogger.getCLogger(WScanBarUI.class);

	protected CustomForm form = new CustomForm();
    // new panel
    /** Grid for components*/
    private Grid newGrid = GridFactory.newGridLayout();
	
    protected Label locatorLabel = new Label();
	protected WLocatorEditor locatorField = new WLocatorEditor();
	
	 protected Label locatorToLabel = new Label();
	protected WLocatorEditor locatorToField = new WLocatorEditor();
	
	protected Label productLabel = new Label();
    /** Field for specifying SKU Product */
	protected Textbox productField = new Textbox();

	private Label qtyCountLabel = new Label();
	private WNumberEditor qtyCountField = new WNumberEditor();
	
	protected Label productValueLabel = new Label();
    /** Field for specifying SKU Product */
	protected Textbox productValueField = new Textbox();
    
	protected Label UOMLabel = new Label();
    /** Field for specifying SKU Product */
	protected Textbox UOMField = new Textbox();
    
	protected Label upcLabel = new Label();
	protected WStringEditor upcField = new WStringEditor();
	
	protected Label lotLabel = new Label();
	protected WStringEditor lotField = new WStringEditor();
	
	protected Label serNoLabel = new Label();
	protected WStringEditor serNoField = new WStringEditor();
	 

    // account panel
    /** Grid for components for scan products **/
	protected Panel productPanel = new Panel();
    /** Table to hold data of accounts. */
    protected WListbox productTable = new WListbox();

    /** confirmation panel. */
    private ConfirmPanel confirmPanel = new ConfirmPanel();
    /** Confirmation Grid. */
    private Grid gridConfirm = GridFactory.newGridLayout();

    /**
     * Default constructor.
     */
    public WScanBarUI()
    {
        super();
        initForm();
    }


    /**
     * Initialises the panel.
     */
    protected void initForm()
    {
        log.info("");
        try
        {
            staticInitialise();
            dynamicInitialise();
            zkInit();
        }
        catch(Exception e)
        {
            log.log(Level.SEVERE, "", e);
        }

        return;
    }


    /**
     * Initialises the static components of the form.
     */
    private void staticInitialise()
    {
        createProductPanel();
        productPanel();
        createConfirmPanel();
        return;
    }

    private void zkInit()
	{
		Borderlayout contentPane = new Borderlayout();
		form.appendChild(contentPane);

		North north = new North();
		contentPane.appendChild(north);
		north.appendChild(newGrid);

		Center center = new Center();
        contentPane.appendChild(center);
		center.appendChild(productPanel);

		South south = new South();
		contentPane.appendChild(south);
		Panel southPanel = new Panel();
		south.appendChild(southPanel);
		southPanel.appendChild(new Separator());
		southPanel.appendChild(gridConfirm);
	}

    /**
     * Creates the account panel.
     *
     * The account panel contains:
     * <li>a table detailing all accounts
     * <li>a button for creating charges for selected accounts
     */
    private void productPanel()
    {
    	Borderlayout borderlayout = new Borderlayout();
    	borderlayout.setStyle("position: absolute");
    	borderlayout.setWidth("100%");
    	borderlayout.setHeight("100%");
    	productPanel.appendChild(borderlayout);

		North north = new North();
		north.setBorder("none");
		borderlayout.appendChild(north);
        Label label = new Label(Msg.translate(Env.getCtx(), "M_Product_ID"));
        label.setStyle("font-weight: bold;");
		north.appendChild(label);

		Center center = new Center();
		center.setBorder("none");
		center.setFlex(true);
		center.setAutoscroll(true);
		borderlayout.appendChild(center);
		center.appendChild(productTable);

		South south = new South();
		south.setBorder("none");
		borderlayout.appendChild(south);
		Panel southPanel = new Panel();
		southPanel.setAlign("right");
		south.appendChild(southPanel);
        return;
    }

    /**
     * Creates the New Charge panel.
     *
     * The New Charge panel is used to specify the name and key of an account
     * and whether or not the account is a charge account.
     */
    private void createProductPanel()
    {
    //  load Locator
    	locatorLabel.setValue(Msg.translate(Env.getCtx(), "M_Locator_ID"));
    	locatorLabel.setMandatory(true);
    	
    	MLocatorLookup locator = new MLocatorLookup(Env.getCtx(), form.getWindowNo());
    	locator.setOnly_Warehouse_ID(Env.getContextAsInt(Env.getCtx(), "#M_Warehouse_ID"));
    	locatorField = new WLocatorEditor ("M_Locator_ID", true, false, true, locator, form.getWindowNo());
    	locatorField.setMandatory(true);
    	locatorField.addValueChangeListener(this);

		locatorToLabel.setValue(Msg.translate(Env.getCtx(), "M_LocatorTo_ID"));
		locatorToLabel.setVisible(false);
    	MLocatorLookup locatorTo = new MLocatorLookup(Env.getCtx(), form.getWindowNo());
    	locatorToField = new WLocatorEditor ("M_LocatorTo_ID", true, false, true, locatorTo, form.getWindowNo());
    	//locatorField.setMandatory(true);
    	locatorToField.addValueChangeListener(this);
    	locatorToField.setVisible(false);

    	upcLabel.setText(Msg.getElement(Env.getCtx(), "Value", false));
    	upcLabel.setMandatory(true);
        upcField = new WStringEditor ("UPC", false, false, true, 10, 30, null, null);
		upcField.getComponent().addEventListener(Events.ON_CHANGE, this);
		
		qtyCountLabel.setText(Msg.getElement(Env.getCtx(), "QtyCount", false));
		qtyCountField.getComponent().addEventListener(Events.ON_CHANGE, this);
		
		lotLabel.setText(Msg.getElement(Env.getCtx(), "Lot", false));
        lotField = new WStringEditor ("Lot", false, false, true, 10, 30, null, null);
		lotField.getComponent().addEventListener(Events.ON_CHANGE, this);
		
		serNoLabel.setText(Msg.getElement(Env.getCtx(), "SerNo", false));
        serNoField = new WStringEditor ("SerNo", false, false, true, 10, 30, null, null);
		serNoField.getComponent().addEventListener(Events.ON_CHANGE, this);
		
        productValueLabel.setValue(Msg.translate(Env.getCtx(), "Value"));
        productLabel.setValue(Msg.translate(Env.getCtx(), "Name"));
        UOMLabel.setValue(Msg.translate(Env.getCtx(), "C_UOM_ID"));

    	Rows rows = new Rows();
    	newGrid.appendChild(rows);

        Row row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        
        row = new Row();
        rows.appendChild(row);
        row.appendChild(locatorLabel);
        row.appendChild(locatorField.getComponent());
        
        row = new Row();
        rows.appendChild(row);
        row.appendChild(locatorToLabel);
        row.appendChild(locatorToField.getComponent());
    	
        
        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        
    	row = new Row();
        rows.appendChild(row);
        row.appendChild(upcLabel);
        row.appendChild(upcField.getComponent());
        row.appendChild(qtyCountLabel);
        row.appendChild(qtyCountField.getComponent());
        
        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        
        row = new Row();
        rows.appendChild(row);
        row.appendChild(serNoLabel);
        row.appendChild(serNoField.getComponent());
        
        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        
        row = new Row();
        rows.appendChild(row);
        row.appendChild(lotLabel);
        row.appendChild(lotField.getComponent());

        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        
        //Product Info
        productValueField.setReadonly(true);
        row = new Row();
        rows.appendChild(row);
        row.appendChild(productValueLabel);
        row.appendChild(productValueField);
        
        productField.setReadonly(true);
        row.appendChild(productLabel);
        row.appendChild(productField);  
        
        UOMField.setReadonly(true);
        row.appendChild(UOMLabel);
        row.appendChild(UOMField);  
        
        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());

        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        
        row = new Row();
        rows.appendChild(row);
        row.setSpans("3");
        row.appendChild(new Separator());
        upcField.setVisible(false);
        qtyCountField.setVisible(false);
        lotField.setVisible(false);
        serNoField.setVisible(false);
        return;
    }


    /**
     *  Initialises the dynamic components of the form.
     *  <li>Gets defaults for primary AcctSchema
     *  <li>Creates Table with Accounts
     */
    private void dynamicInitialise()
    {
        ListModelTable model = new ListModelTable();
        productTable.setData(model, getColumnNames());
		setColumnClass(productTable);
        return;
    }   //  dynInit
    
	@Override
	public void valueChange(ValueChangeEvent e) {
		String name = e.getPropertyName();
		Object value = e.getNewValue();
		if(name.equals("M_Locator_ID"))
		{
	        if(locatorField.getDisplay() == null)
        	{
                FDialog.error(form.getWindowNo(), form, Msg.parseTranslation(Env.getCtx(), "@M_Locator_ID@ @NotFound@ :"), locatorField.getDisplay());
                locatorField.getComponent().setFocus(true);
                return;
        	}
	        
	       
			getData();
        	cleanFields();
        	setLocator();
        }
		
		if(getTable().getTableName().equals(MMovement.Table_Name) && locatorField.getDisplay()  != null)
        {
        	locatorToLabel.setVisible(true);
        	locatorToField.setVisible(true);
        	locatorToField.getComponent().setFocus(true);
        	
        	 if(locatorToField.getDisplay() == null || locatorToField.getDisplay().length() == 0)
         	{
                 locatorToField.getComponent().setFocus(true);
                 return; 
         	}
         	setLocatorTo();
        } 	 
	        
		upcField.setVisible(true);
	}

    /**
     *  Event Listener.
     *
     *  @param event event that has been fired.
     */
    public void onEvent(Event event)
    {
        log.info(event.getName());
        
        //
        if (event.getTarget().getId().equals(ConfirmPanel.A_OK))
        {	
        	createLine();
            close();
            return;
        }

        if (event.getTarget().equals(lotField.getComponent()))
        {	
			addScanLot();
			return;
        }	
        
        if (event.getTarget().equals(serNoField.getComponent()))
        {	
			addScanSetNo();
			return;
        }	

        addScanProduct();
        return;
    }

    private void setLocator() {
    	if (locatorField.getValue() !=null)
			locatorId = (Integer)locatorField.getValue();
		else
			locatorId = 0;
	}
    
    private void setLocatorTo() {
    	if (locatorToField.getValue() !=null)
			locatorToId = (Integer)locatorToField.getValue();
		else
			locatorToId = 0;
	}


	private void addScanProduct() {
    	
    	String upc = upcField.getDisplay();
    	if(upc == null)
    		return ;
    	
    	MProduct product = getMProduct(upc);
    	
    	if(product == null)
    	{
            FDialog.error(form.getWindowNo(), form, Msg.parseTranslation(Env.getCtx(), "@M_Product_ID@ @NotFound@"), upc);
            upcField.setValue("");
            return;
    	}
    	
    	setProduct(product);
    	
    	productValueField.setValue(product.getValue());
    	productField.setValue(product.getName()); 
    	UOMField.setValue(product.getC_UOM().getName());

    	
    	if(isLot())
    	{	
    		lotField.setVisible(true);
    	}	
    	
    	if(isSerNo())
    	{
    		serNoField.setVisible(true);
    	}
    	else
    	{
    		qtyCountField.setVisible(true);
    	}
    	
    	addProductLine();
	}
    
    private void addScanLot()
    {
    	if(!isLot())
    		return;
    	
    	String lotNo = lotField.getDisplay().trim().toUpperCase();
    	
    	if( lotNo == null ||  lotNo.length() <= 0)
    	{
            FDialog.error(form.getWindowNo(), form, Msg.parseTranslation(Env.getCtx(),"@LotNo@ @NotFound@"), lotNo);
            return;
    	}
    	setLotNo(lotNo);
    	serNoField.getComponent().setFocus(true);
    	addProductLine();
    }
    
    private void addScanSetNo()
    {
    	if(!isSerNo())
    		return;
    	
    	String serNo = serNoField.getDisplay().trim().toUpperCase();
    	
    	if( serNo == null ||  serNo.length() <= 0)
    	{
            FDialog.error(form.getWindowNo(), form, Msg.parseTranslation(Env.getCtx(),"@SerNo@ @NotFound@"), serNo);
            return;
    	}
    	
    	boolean isSOTrx = Env.isSOTrx(Env.getCtx());
    	if(isSOTrx)
    	{
			MAttributeSetInstance asi = getAttributeSetInstance(getProduct(),
					getLotNo(), serNo, getM_Locater_ID(), null);
			if (asi == null)
				if (!FDialog.ask(form.getWindowNo(), form, Msg
						.parseTranslation(Env.getCtx(),
								"@SerNo@ @NotFound@ @M_Locator_ID@ "+locatorField.getDisplay()+" @AddLine@ ?"), serNoField
						.getDisplay()))
				{
					cleanFields();
					return;
				}	
    	}
    	
    	setSerNo(serNo);
    	addProductLine();
    }


	private void addProductLine() {
		
		if (getProduct() == null)
			return;
		
		BigDecimal qtyCount = (BigDecimal) qtyCountField.getValue();
		BigDecimal qty = null;
		boolean reset = false;
		if(( qtyCount == null && !isSerNo()))
		{	
			qtyCountField.getComponent().setFocus(true);
			return;
		}	
		
		if(isLot() && getLotNo() == null)
		{	
			lotField.getComponent().setFocus(true);
			return;
		}	
		
		if(isSerNo() && getSerNo() == null)
		{	
			serNoField.getComponent().setFocus(true);
			return;
		}	
		
		if(qtyCount != null)
		{	
			qty = qtyCount;
			reset = true;
		}	
		else
			qty = Env.ONE;

		Integer referenceId = 0;
		//Check Validation if source have elements
		if(getSource() != null && source.size() >0)
		{
			ArrayList<Object> values = checkProduct(product, qty, reset);
			if(values == null)
			{	
				FDialog.error(form.getWindowNo(), form, Msg.parseTranslation(Env.getCtx(), "@NotValid@"), getProduct().getName() 
						+ " " + Msg.parseTranslation(Env.getCtx(), "@SerNo@") + ":" + getSerNo());
				cleanFields();
				return;
			}
			referenceId = (Integer) values.get(0);
		}
		
		//AddLine in grid
		LinkedHashMap<String, Vector> dataSource = getData();
		
		String key = getProduct().getValue();		
		if (isLot())
			 key = key + getLotNo();
		if (isSerNo())
			 key = key +  getSerNo();
		
		if(dataSource.containsKey(key) && !isSerNo())
		{
			Vector<Object> line = dataSource.get(key);
			if(qtyCount != null && qtyCount.signum() > 0)
				line.set(4, qtyCount);
			else
				line.set(4, BigDecimal.ONE);
			
			dataSource.put(key, line);
			setData(dataSource);
			cleanFields();
			return;
		}
		else if(dataSource.containsKey(key) 
		|| getProduct().getValue().equals(getLotNo()) 
		|| getProduct().getValue().equals(getSerNo()))
		{	
			FDialog.error(form.getWindowNo(), form, Msg.parseTranslation(Env.getCtx(), "@NotValid@"), getProduct().getName() 
					+ " " + Msg.parseTranslation(Env.getCtx(), "@SerNo@") + ":" + getSerNo());
			cleanFields();
			return;
		}	

		Vector<Object> line = new Vector<Object>(6);
		line.add(getProduct().getValue());
		line.add(getProduct().getName());
		line.add(getLotNo());
		line.add(getSerNo());
		if(qtyCount != null && qtyCount.signum() > 0)
			line.add(qtyCount);
		else
			line.add(BigDecimal.ONE);
		line.add(0);
		line.add(referenceId);
		dataSource.put(key, line);
		setData(data);		
		cleanFields();
	}
	
	private void cleanFields()
	{
		productTable.clear();
		ListModelTable model = new ListModelTable(getDataModel());
		productTable.setData(model, getColumnNames());
		upcField.setValue("");
		qtyCountField.setValue(null);
		qtyCountField.setVisible(false);
		lotField.setValue("");
		lotField.setVisible(false);
		serNoField.setValue("");
		serNoField.setVisible(false);
		
		upcField.getComponent().setFocus(true);		
	}

    /**
     *  Create Confirmation Panel with OK Button.
     */
    public void createConfirmPanel()
    {
        Rows rows = new Rows();
        Row row = new Row();
        confirmPanel.addActionListener(this);
        row.appendChild(confirmPanel);
        rows.appendChild(row);
        gridConfirm.appendChild(rows);

        return;
    }   //  ConfirmPanel


    public void close()
    {
		form.dispose();
    }

	public ADForm getForm() {
		return form;
	}

	public ProcessInfo getProcessInfo()
	{
		return getForm().getProcessInfo();
	}

}


