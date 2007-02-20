Platemaker Documentation

The platemaker can be used to generate printable pages, in Adobe Acrobat format (.pdf), from queries you make to an EFG.

New users will find it takes a few tries to generate a good print. We have set some defaults that are meant for 8.5" x 11" paper, but keep in mind that author data is very variable!

Some tips:

1) NEW USERS: STICK WITH QUERIES THAT RETURN ***16*** OR FEWER RESULTS. Generating plates for large datasets typically takes a long time (5+ minutes) and produces large pdf files.

2A) IMAGES TOO SMALL? TRY REDUCING NUMBER OF ROWS/COLUMNS. If the platemaker generates pages and the images are deemed to be too small, the easiest way to resolve this is to reduce the number of rows and columns per page. In the current version of the software, text is allotted a certain amount of space regardless of whether it is used. This may result in some whitespace authors might wish they could remove in order to generate a very compact plate. This issue may be addressed in a future release.

2B) IMAGES STILL TOO SMALL? TRY REDUCING CAPTION FONT SIZE.

3) STANDARDIZED IMAGES AND TEXT WILL LOOK BEST. Authors that have standardized their images and text so that they all have the same proportions will find it much easier to generate a print they are happy with. Currently, the amount of space allotted for each taxon (species) is set to be equal, and the platemaker aligns the images and text in each row. If there is one image in the dataset that is twice as long as the others it may cause all the others to print smaller than the author would prefer. Also, a set of mixed images that are both portrait and landscape oriented will generate a lot of white space.

4) THERE IS NO TEXT WRAPPING OF CAPTIONS. Currently, captions are limited to **a single row of text**. We recommend that you do not use descriptive text fields to caption your images. If your caption is cut off, reduce the font size.

5) USE EITHER TOP OR BOTTOM CAPTIONS, NOT BOTH. Currently the software does not do a very good job dealing with the case where the author puts captions both above and below the image. The result is typically that the images get rendered very small. Authors that cannot resolve this may get better results by choosing to place captions either above *OR* below the image, but not both.

6) ERROR: OPENING OF PDF FILE FAILS. If you are using the Firefox web browser and you view Adobe Acrobat files with the Adobe Firefox plugin, viewing large pdf files may fail with an error message. If this problem occurs please download a standalone pdf viewer such as Foxit, or set your web browser to avoid the plugin with these instructions (for Firefox 2.x):
Tools --> Options --> select Content tab --> click Manage button --> Find "PDF" in the left column, click on it and then click Change Action --> select "Open them with the default application"

