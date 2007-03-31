$Id$
This is used to build installers on Linux and Mac os.
To build a linux installer:
Execute build_dist.xml with the 'makeImportLinux' target.
For macs:
Execute build_dist.xml with the 'makeImportMac' target.

In either case a Zip File will be made in the IzPackInstaller 
directory and its name will include the words linux or mac
depending on the target you selected.

It is recommended that you make these installers
on the OS that corresponds to the installer
you intend to make.

For Macs we have added some dmg packages
Check out the module efg2macpackages
and make sure it is at the same level with efg2 folder.


This folder also contains a readme file for mac users.
The main readme file for mac/linux users can be found in 
IzPackInstaller/ReadMe folder 
All of the scripts can be found in the IzPackInstaller
directory.