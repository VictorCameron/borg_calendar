/*
This file is part of BORG.
 
    BORG is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
 
    BORG is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
    You should have received a copy of the GNU General Public License
    along with BORG; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 
Copyright 2003 by ==Quiet==
 */
package net.sf.borg.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import net.sf.borg.common.util.Errmsg;
import net.sf.borg.common.util.Resource;
import net.sf.borg.common.util.Version;
import net.sf.borg.model.AppointmentModel;
import net.sf.borg.model.TaskModel;




// weekView handles the printing of a single week
class DayView extends View
{
    static
    {
        Version.addVersion("$Id$");
    }
    
    private DayPanel dayPanel;
    
 
    static private void printPrintable( Printable p ) throws Exception
    {
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
	    aset.add(new Copies(1));
        aset.add(MediaSizeName.NA_LETTER);
        aset.add(OrientationRequested.LANDSCAPE);	

        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(p);
        //printJob.pageDialog(aset);
        if (printJob.printDialog(aset))
                printJob.print(aset);
        
    }
    static void printDay(int month, int year, int date) throws Exception
    {
        
        // use the Java print service
        // this relies on dayPanel.print to fill in a Graphic object and respond to the Printable API
        DayPanel cp = new DayPanel(month,year,date);
        printPrintable(cp);
    }
    
    private void printDay() throws Exception
    {
        printPrintable(dayPanel);
    }
    
    private void printAction()
    {
        try
        {
            printDay();
        }
        catch( Exception e )
        {
            Errmsg.errmsg(e);
        }
    }
    
    public DayView(int month, int year, int date)
    {
        super();
        addModel(AppointmentModel.getReference());
        addModel(TaskModel.getReference());
        dayPanel = new DayPanel(month,year,date);
        
        dayPanel.setBackground(Color.WHITE);
        PageFormat pf = new PageFormat();
        pf.setOrientation( PageFormat.LANDSCAPE );
        Double w = new Double( WeekPanel.prev_scale * pf.getWidth());
        Double h = new Double( WeekPanel.prev_scale * pf.getHeight());
        dayPanel.setPreferredSize( new Dimension(w.intValue(), h.intValue()  ));
        
        JScrollPane sp = new JScrollPane(dayPanel);
        sp.setPreferredSize(new Dimension(w.intValue(), h.intValue()));
        
        // for the preview, create a JFrame with the preview panel and print menubar
        JMenuBar menubar = new JMenuBar();
        JMenu pmenu = new JMenu();
        pmenu.setText(Resource.getResourceString("Action"));
        JMenuItem mitem = new JMenuItem();
        mitem.setText(Resource.getResourceString("Print"));
        mitem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                printAction();
            }
        });
        pmenu.add(mitem);
        JMenuItem quititem = new JMenuItem();
        quititem.setText(Resource.getResourceString("Dismiss"));
        quititem.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent evt)
            {
                try{destroy();}catch(Exception e){}
            }
        });
        pmenu.add(quititem);
        menubar.add(pmenu);
        menubar.setBorder(new BevelBorder(BevelBorder.RAISED));
        
        setJMenuBar(menubar);
        
        getContentPane().add(sp, BorderLayout.CENTER);
        setTitle(Resource.getResourceString("Day_View"));
        setDefaultCloseOperation( DISPOSE_ON_CLOSE );
        pack();
        setVisible(true);
    }
    
    
    public void destroy()
    {
        this.dispose();
    }
    
    public void refresh()
    {
    }
    
}


