/**
 * $Id$
 * $Name:  $
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
package project.efg.server.utils;

import com.missiondata.fileupload.OutputStreamListener;


public class FileUploadListener implements OutputStreamListener
{
  private FileUploadStats fileUploadStats = new FileUploadStats();

  public FileUploadListener(long totalSize)
  {
    fileUploadStats.setTotalSize(totalSize);
  }

  public void start()
  {
    fileUploadStats.setCurrentStatus("start");
  }

  public void bytesRead(int byteCount)
  {
    fileUploadStats.incrementBytesRead(byteCount);
    fileUploadStats.setCurrentStatus("reading");
  }

  public void error(String s)
  {
    fileUploadStats.setCurrentStatus("error");
  }

  public void done()
  {
    fileUploadStats.setBytesRead(fileUploadStats.getTotalSize());
    fileUploadStats.setCurrentStatus("done");
  }

  public FileUploadStats getFileUploadStats()
  {
    return fileUploadStats;
  }

  public static class FileUploadStats
  {
    private long totalSize = 0;
    private long bytesRead = 0;
    private long startTime = System.currentTimeMillis();
    private String currentStatus = "none";

    public long getTotalSize()
    {
      return totalSize;
    }

    public void setTotalSize(long totalSize)
    {
      this.totalSize = totalSize;
    }

    public long getBytesRead()
    {
      return bytesRead;
    }

    public long getElapsedTimeInSeconds()
    {
      return (System.currentTimeMillis() - startTime) / 1000;
    }

    public String getCurrentStatus()
    {
      return currentStatus;
    }

    public void setCurrentStatus(String currentStatus)
    {
      this.currentStatus = currentStatus;
    }

    public void setBytesRead(long bytesRead)
    {
      this.bytesRead = bytesRead;
    }

    public void incrementBytesRead(int byteCount)
    {
      this.bytesRead += byteCount;
    }
  }
}
