/**
 * $Id$
 * $Name$
 * 
 * Copyright (c) 2003  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu, Kimmy Lin
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
package project.efg.Imports.efgImpl;

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceContext;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.File;

public class MyDragSourceListener implements DragSourceListener {
	private FileTree tree;

	private FileTreeNode selNode;

	public MyDragSourceListener(FileTree tree) {
		super();
		 this.tree = tree;
		this.selNode = (FileTreeNode) this.tree
				.getLastSelectedPathComponent();
	}

	public void dragDropEnd(DragSourceDropEvent dragSourceDropEvent) {
		if (dragSourceDropEvent.getDropSuccess()) {
			int dropAction = dragSourceDropEvent.getDropAction();
			if (dropAction == DnDConstants.ACTION_MOVE) {
				File f = new File(this.selNode.getFullName());
				tree.removeSelectedNode(selNode, f);
			}
		}
	}

	public void dragEnter(DragSourceDragEvent dragSourceDragEvent) {
		DragSourceContext context = dragSourceDragEvent
				.getDragSourceContext();
		int dropAction = dragSourceDragEvent.getDropAction();
		if ((dropAction & DnDConstants.ACTION_COPY) != 0) {
			context.setCursor(DragSource.DefaultCopyDrop);
		} else if ((dropAction & DnDConstants.ACTION_MOVE) != 0) {
			context.setCursor(DragSource.DefaultMoveDrop);
		} else {
			context.setCursor(DragSource.DefaultCopyNoDrop);
		}
	}

	public void dragExit(DragSourceEvent dragSourceEvent) {
	}

	public void dragOver(DragSourceDragEvent dragSourceDragEvent) {
	}

	public void dropActionChanged(DragSourceDragEvent dragSourceDragEvent) {
	}
}