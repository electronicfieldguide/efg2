/**
 * $Id$
 * $Name:  $
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */
/**
 * A temporary object used in some of the stack operations Should be extended to
 * implement equals and hashcode if it is used as part of a Collection.
 */
package project.efg.client.interfaces.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import project.efg.client.impl.gui.AutofitTableColumns;
import project.efg.client.impl.gui.EFGTableModel;
import project.efg.client.impl.gui.TableSorter;
import project.efg.client.impl.gui.TableSorterObject;
import project.efg.client.interfaces.nogui.EFGDatasourceObjectInterface;
import project.efg.client.utils.gui.DataObjectTransferHandler;
import project.efg.client.utils.gui.HelpEFG2ItemListener;
import project.efg.client.utils.gui.TableDNDRecognizer;
import project.efg.util.interfaces.EFGImportConstants;
import project.efg.util.utils.DBObject;

/**
 * @author kasiedu
 *
 */
public abstract class TableSorterMainInterface  extends JDialog{
	/**
	 * @author kasiedu
	 *
	 */

	static final long serialVersionUID = 1;

	static Logger log = null;
	protected static Hashtable display2LegalMap;
	protected static Hashtable legal2DisplayMap;
	protected static Hashtable columnHeadersToolTips;
	
	static {
		try {

			log = Logger.getLogger(TableSorterMainInterface.class);
			columnHeadersToolTips = new Hashtable();
			columnHeadersToolTips.put(EFGImportConstants.NAME_DISPLAY,"");
			columnHeadersToolTips.put(EFGImportConstants.MEDIARESOURCE_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.mediaresource.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.LEGALNAME_DISPLAY,"");
			columnHeadersToolTips.put(EFGImportConstants.SEARCHABLE_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.searchable.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.ISLISTS_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.list.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.NUMERICRANGE_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.numericrange.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.NUMERIC_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.numericvalue.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.ONTAXONPAGE_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.taxonpage.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.CATEGORICAL_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.categorical.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.NARRATIVE_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.narrative.tooltip"));
			columnHeadersToolTips.put(EFGImportConstants.ORDER_DISPLAY,
					EFGImportConstants.EFGProperties.getProperty("TableSorterMain.order.tooltip"));
			
			display2LegalMap = new Hashtable();
			display2LegalMap.put(EFGImportConstants.NAME_DISPLAY, EFGImportConstants.NAME);
			display2LegalMap.put(EFGImportConstants.MEDIARESOURCE_DISPLAY,EFGImportConstants.MEDIARESOURCE);
			display2LegalMap.put(EFGImportConstants.LEGALNAME_DISPLAY, EFGImportConstants.LEGALNAME);
			display2LegalMap.put(EFGImportConstants.SEARCHABLE_DISPLAY, EFGImportConstants.SEARCHABLE);
			display2LegalMap.put(EFGImportConstants.ISLISTS_DISPLAY,EFGImportConstants.ISLISTS);
			display2LegalMap.put(EFGImportConstants.NUMERICRANGE_DISPLAY,EFGImportConstants.NUMERICRANGE);
			display2LegalMap.put(EFGImportConstants.NUMERIC_DISPLAY,EFGImportConstants.NUMERIC);
			display2LegalMap.put(EFGImportConstants.ONTAXONPAGE_DISPLAY,EFGImportConstants.ONTAXONPAGE);
			display2LegalMap.put(EFGImportConstants.CATEGORICAL_DISPLAY,EFGImportConstants.CATEGORICAL);
			display2LegalMap.put(EFGImportConstants.NARRATIVE_DISPLAY,EFGImportConstants.NARRATIVE);
			display2LegalMap.put(EFGImportConstants.ORDER_DISPLAY,EFGImportConstants.ORDER);
			
			legal2DisplayMap = new Hashtable();
			legal2DisplayMap.put(EFGImportConstants.NAME, EFGImportConstants.NAME_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.MEDIARESOURCE,EFGImportConstants.MEDIARESOURCE_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.LEGALNAME, EFGImportConstants.LEGALNAME_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.SEARCHABLE, EFGImportConstants.SEARCHABLE_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.ISLISTS,EFGImportConstants.ISLISTS_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.NUMERICRANGE,EFGImportConstants.NUMERICRANGE_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.NUMERIC,EFGImportConstants.NUMERIC_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.ONTAXONPAGE,EFGImportConstants.ONTAXONPAGE_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.CATEGORICAL,EFGImportConstants.CATEGORICAL_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.NARRATIVE,EFGImportConstants.NARRATIVE_DISPLAY);
			legal2DisplayMap.put(EFGImportConstants.ORDER,EFGImportConstants.ORDER_DISPLAY);

		} catch (Exception ee) {
		}
	}
	
	private TableSorter sorter;
	JCheckBox sortingCheck;
	
	private EFGDatasourceObjectInterface ds;
	private DBObject dbObject;
	private TableSorterObject sorterObject;
	
	private int tableWidth = 0;
	private int tableHeight = 0;
	
	//private JSplitPane spane;
	public static DataFlavor listFlavor = new DataFlavor(
			DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.List",
			"List");

	public static DataObjectTransferHandler dndHandler = new DataObjectTransferHandler();

	protected static TableDNDRecognizer dndRecognizer=new TableDNDRecognizer();

	public static boolean isDragged;
	
	public static boolean isChanged = false;

	public TableSorterMainInterface(DBObject dbObject, 
			EFGDatasourceObjectInterface ds, 
			JFrame frame) {
		super(frame, ds.getDisplayName(), true);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		ToolTipManager.sharedInstance().setDismissDelay(60000);
		setLocationRelativeTo(frame);
		
		this.ds = ds;
		this.dbObject = dbObject;
		this.sorterObject = 
			this.createData(this.ds);//get the column names
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		if(this.sorterObject != null){
			init();
		} else {
			log.error("An error has occured");
			this.close();
		}
	}
	

	/* (non-Javadoc)
	 * @see project.efg.Imports.efgImpl.TabelSorterMainInterface#getDBObject()
	 */
	public DBObject getDBObject(){
		return this.dbObject;
	}
	public EFGDatasourceObjectInterface getDataSourceObject(){
		return this.ds;
	}
	/**
	 * 
	 * @param sorter
	 * @param ds
	 */
	public abstract void updateMetadataTable(TableSorter sorter,
			EFGDatasourceObjectInterface ds);
	/**
	 * Template method to be implemented by sub classes
	 * @param ds
	 * @return
	 */
	public abstract TableSorterObject
	createData(EFGDatasourceObjectInterface ds);
	/* *
	 * @see project.efg.Imports.efgImpl.TabelSorterMainInterface#close()
	 */
	public void close() {
		if(this.sorter.isChanged() || isChanged ){
			int res = JOptionPane.showOptionDialog(this,
					"Data has not been updated,do you want to save before quitting?\n",
					"Changes not saved!!",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, new String[] {
				"Yes",  "No"}, "No");
			switch (res) {
			case JOptionPane.YES_OPTION: // Yes
				this.updateMetadataTable(this.sorter,this.ds);
				break;
			default: // No 
				break;
			}
		}
		this.sorter.setIsChanged(false);
		isChanged = false;
		this.dispose();
		
	}
	public String[] getColumnNames(){
		return this.sorterObject.getColumnNames();
	}

	

	private JPanel addPanel(){
	
		JPanel panel = new JPanel(new BorderLayout());
		EFGTableModel mt = 
			new EFGTableModel(this.sorterObject);
		this.sorter = new TableSorter(mt);
		mt.setSorter(this.sorter);
		JTable table = new JTable(this.sorter){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void changeSelection(int rowIndex, int columnIndex,
					boolean toggle, boolean extend) {
				if (!isDragged) {
					super
							.changeSelection(rowIndex, columnIndex, toggle,
									extend);
				}
			}
			  //Implement table cell tool tips.
           /* public String getToolTipText(MouseEvent e) {
                
                java.awt.Point p = e.getPoint();
               // int rowIndex = rowAtPoint(p);
                int colIndex = columnAtPoint(p);
                int realColumnIndex = convertColumnIndexToModel(colIndex);
                
               String colName = this.getColumnName(realColumnIndex);
               return (String)columnHeadersToolTips.get(colName);
            }*/
			 //Implement table header tool tips.
		    protected JTableHeader createDefaultTableHeader() {
		        return new JTableHeader(columnModel) {
		            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					public String getToolTipText(MouseEvent e) {
		               
		                java.awt.Point p = e.getPoint();
		                int index = columnModel.getColumnIndexAtX(p.x);
		                int realIndex = 
		                        columnModel.getColumn(index).getModelIndex();
		                String colName =table.getColumnName(realIndex);
		                return (String)columnHeadersToolTips.get(colName);
		                
		            }
		        };
		    }
		};
		
		sorter.setTableHeader(table.getTableHeader());
 
		 table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
			
			table.setTransferHandler(dndHandler);
			table.setDropTarget(new DropTarget(table, DataObjectTransferHandler
					.getDropHandler()));
			table.addMouseListener(dndRecognizer);
			table.addMouseMotionListener(dndRecognizer);
	        table.setDragEnabled(true);
	        table.getTableHeader().setReorderingAllowed(false);
	    
		// Set up column sizes.
	
		this.initColumnSizes(table, mt);
		this.setRenderers(table);
				
		// Set up tool tips for column headers.
		table.getTableHeader().setToolTipText(
						"Click to sort");
		Dimension tableD = AutofitTableColumns.autoResizeTable(table,true);
		
		
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setSize(tableD);
		// Add the scroll pane to this panel.
		panel.add(scrollPane, BorderLayout.CENTER);
		JPanel pan = new JPanel();
		JButton updateBtn = new JButton(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.updatebtn"));
		updateBtn.addActionListener(new UpdateListener(this,this.sorter,this.ds));
		updateBtn.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.updatebtn.tooltip"));
		
		JButton doneBtn = new JButton("Done");
		doneBtn.addActionListener(new DoneListener(this));
		doneBtn.setToolTipText("Click to close");

		pan.add(updateBtn);
		pan.add(doneBtn);
		panel.add(pan, BorderLayout.SOUTH);
		this.tableWidth = tableD.width + 35;
		this.tableHeight = tableD.height + 50;
		panel.setSize(new Dimension(this.tableWidth,this.tableHeight));
		
	
		return panel;
	} 
	private void addMenus(){
		JMenu fileMenu = new JMenu("File");
		JMenu helpMenu = new JMenu("Help");
		
		
		String sortOn =EFGImportConstants.SORTING_ON;// "Sorting ON";
		JRadioButtonMenuItem onBtn = new JRadioButtonMenuItem(sortOn);
		   onBtn.setMnemonic(KeyEvent.VK_O);
		   onBtn.setActionCommand(sortOn);
		   onBtn.setSelected(false);
		   RadioButtonListener rbl = new RadioButtonListener(this.sorter);
		   String sortOff =EFGImportConstants.SORTING_OFF;// "Sorting OFF";
		 
		   JRadioButtonMenuItem offBtn = new JRadioButtonMenuItem( sortOff);
		    offBtn.setMnemonic(KeyEvent.VK_F);
		    offBtn.setSelected(true);
		    offBtn.setActionCommand(sortOff);
		    offBtn.addActionListener(rbl);
		    onBtn.addActionListener(rbl);
		    ButtonGroup btnGroup = new ButtonGroup();
		   btnGroup.add(onBtn);
		   btnGroup.add(offBtn);
		    
		this.sortingCheck = new JCheckBox(EFGImportConstants.SORTING_OFF);
		sortingCheck.setForeground(Color.BLUE);
		sortingCheck.addItemListener(new CheckBoxListener(this.sorter));
		
		
		
		
		
		
		JMenuItem updateMenu = new JMenuItem(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.updatebtn"));
		updateMenu.addActionListener(new UpdateListener(this,this.sorter,this.ds));
		updateMenu.setToolTipText(EFGImportConstants.EFGProperties
				.getProperty("TableSorterMain.updatebtn.tooltip"));
		
		JMenuItem closeMenu = new JMenuItem("Close");
		JMenuItem helpItem = new JMenuItem("Help Contents");
		helpItem.addActionListener(new HelpEFG2ItemListener(EFGImportConstants.KEY_METADATA_HELP));
		helpMenu.add(helpItem);
		
		closeMenu.addActionListener(new DoneListener(this));
		JMenu checkMenu = new JMenu("Check Data For Errors");
		//sub menus
		
		String propItem = 
			EFGImportConstants.EFGProperties.getProperty(
					"SynopticKeyTreeMain.checkimagesmenuitem","Check Images"
					);
		JMenuItem checkMediaMenu = 
			new JMenuItem(propItem);
		 propItem = 
				EFGImportConstants.EFGProperties.getProperty(
						"SynopticKeyTreeMain.checkimagesmenuitem.tooltip",
						"verify image files"
						);
		checkMediaMenu.setToolTipText(propItem);
		//checkMediaMenu.addActionListener(new CheckListener(this.getDBObject(),this.ds));
		JMenuItem checkCategoricalMenu = new JMenuItem("Check Categorical Characters");
		checkCategoricalMenu.setVisible(false);
		checkCategoricalMenu.setToolTipText("Not yet avaliable");
		
		JMenuItem checkListMenu = new JMenuItem("Check Lists");
		checkListMenu.setVisible(false);
		JMenuItem checkNumericMenu = new JMenuItem("Check Numeric Data");
		checkNumericMenu.setVisible(false);
		//add Menu Items
		checkMenu.add(checkMediaMenu);
		checkMenu.add(checkNumericMenu);
		checkMenu.add(checkListMenu);
		checkMenu.add(checkCategoricalMenu);
		
		fileMenu.add(updateMenu);
		fileMenu.addSeparator();
		
		fileMenu.add(onBtn);
		fileMenu.add(offBtn);
		
		fileMenu.addSeparator();
		fileMenu.add(closeMenu);
		JMenuBar mBar = new JMenuBar();
		mBar.add(fileMenu);
		mBar.add(helpMenu);
	
		this.setJMenuBar(mBar);
		
	}
	private void init(){
		this.getContentPane().setLayout(new BorderLayout());
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		
		
		
		JPanel panel = addPanel();
		this.addMenus();
		setSize(panel.getWidth(),600);
		getContentPane().add(panel, BorderLayout.CENTER);
		JLabel label = new JLabel(
				EFGImportConstants.EFGProperties.getProperty("TableSorterMain.title"),
				SwingConstants.CENTER);
		getContentPane().add(label, BorderLayout.NORTH);
		
		
	}
	/*
	 * This method picks good column sizes. If all column heads are wider than
	 * the column's cells' contents, then you can just use
	 * column.sizeWidthToFit().
	 */
	private void initColumnSizes(JTable table, EFGTableModel model) {
		TableColumn column = null;
		for (int i = 0; i < table.getColumnCount(); i++) {
			column = table.getColumnModel().getColumn(i);
			column.sizeWidthToFit();
		}
	}


	/**
	 * Set the renderers for some of the columns
	 */
	private void setRenderers(JTable table) {

		for (int i = 0; i < this.sorterObject.getColumnNames().length; i++) {
			String colName = this.sorterObject.getColumnNames()[i];

			if ((colName.equalsIgnoreCase(EFGImportConstants.NAME_DISPLAY))
					|| (colName
							.equalsIgnoreCase(EFGImportConstants.ONTAXONPAGE_DISPLAY))
					|| (colName.equalsIgnoreCase(EFGImportConstants.SEARCHABLE_DISPLAY))
					|| (colName.equalsIgnoreCase(EFGImportConstants.ISLISTS_DISPLAY))) {
			} else if (colName.equalsIgnoreCase(EFGImportConstants.ORDER_DISPLAY)) {
				JTextField textField = new JTextField(i + 1 + "");
				
				table.getColumn(colName).setCellEditor(
						new DefaultCellEditor(textField));
				// Set up tool tips for the sport cells.
				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer
						.setToolTipText("The value you select determines the order in which the fields appear on the search page");
				table.getColumn(colName).setCellRenderer(renderer);
				//table.getColumn(colName).setCellRenderer(new MultiLineHeaderRenderer());
			} else {
				table.getColumn(colName).setCellRenderer(
						new project.efg.client.impl.gui.RadioButtonRenderer());
				table.getColumn(colName).setCellEditor(
						new project.efg.client.impl.gui.RadioButtonEditor(new JCheckBox()));
			}
		}
	}
	class UpdateListener implements ActionListener {
		private TableSorterMainInterface sorterMain;
		private TableSorter sorter;
		private EFGDatasourceObjectInterface ds;

		public UpdateListener(TableSorterMainInterface sorterMain,
				TableSorter sorter,
				EFGDatasourceObjectInterface ds) {
			this.sorterMain = sorterMain;
			this.sorter = sorter;
			this.ds = ds;
		}

		public void actionPerformed(ActionEvent evt) {
			this.sorterMain.updateMetadataTable(sorter,ds);
			this.sorterMain.sorter.setIsChanged(false);
			isChanged = false;
		}
	}
	class RadioButtonListener implements ActionListener {
		private TableSorter sorter;
		/**
		 * 
		 */
		public RadioButtonListener(TableSorter sorter) {
			this.sorter = sorter;
		
		}
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand(); 
			if(command != null){
				if(EFGImportConstants.SORTING_OFF.equalsIgnoreCase(command.trim())){
					//sortingCheck.setText("Sorting OFF");
			    	//sortingCheck.setForeground(Color.BLUE);
			    	this.sorter.cancelSorting();
			    	this.sorter.setSortingState(false);
				}
				else if(EFGImportConstants.SORTING_ON.equalsIgnoreCase(command.trim())){
					//sortingCheck.setText("Sorting ON");
			    //	sortingCheck.setForeground(Color.GREEN);
			    	this.sorter.setSortingState(true);
			    	this.sorter.synchronizeModelWithView();
			    	  this.sorter.setSortingStatus(this.sorter.getLastSortingColumn(), 
			    			  TableSorter.ASCENDING);
				}
				
				
			}
			
		}

	}

	class CheckBoxListener implements ItemListener  {
	
		private TableSorter sorter;
		public CheckBoxListener(
				TableSorter sorter
				) {
			this.sorter = sorter;
		}
		/* (non-Javadoc)
		 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
		 */
		public void itemStateChanged(ItemEvent e) {
		    if (e.getStateChange() == ItemEvent.DESELECTED) {
		    	sortingCheck.setText("Sorting OFF");
		    	sortingCheck.setForeground(Color.BLUE);
		    	this.sorter.cancelSorting();
		    	this.sorter.setSortingState(false);
		    	
		    }
		    else  if (e.getStateChange() == ItemEvent.SELECTED) {
		    	sortingCheck.setText("Sorting ON");
		    	sortingCheck.setForeground(Color.GREEN);
		    	this.sorter.setSortingState(true);
		    	this.sorter.synchronizeModelWithView();
		    	  this.sorter.setSortingStatus(this.sorter.getLastSortingColumn(), 
		    			  TableSorter.ASCENDING);
		    }
		  
		}
	}
	
	
	class DoneListener implements ActionListener {
		private TableSorterMainInterface sorterMain;

		public DoneListener(TableSorterMainInterface sorterMain) {
			this.sorterMain = sorterMain;
		}

		public void actionPerformed(ActionEvent evt) {
			
			this.sorterMain.close();
		}
	}
}