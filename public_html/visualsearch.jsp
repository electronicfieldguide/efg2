<% 

// $Id$ 

%>

<%@ 
  page import = "java.io.*,
		java.util.*,
  		project.efg.util.*"
%>

<%
	// URL args: searchSTR
 	String search  = (String)request.getAttribute("searchStr");
	String orsearch= search;
	String genus  = (String)request.getAttribute("Genus");
	String species  = (String)request.getAttribute("Species");
	String sex  = (String)request.getAttribute("Sex");
	String link = "";
	genus=genus.trim();
	species=species.trim();

	if ( sex!=null ) sex=sex.trim();
	if  ( search == null || search.equals("") ) {
		search = "Genus=" + genus;
		if ( species != null )
			search += "&Species=" + species;
	}
	if ( sex != null && orsearch == null ) search +="&Sex=" + sex;
//	if ( genus.equals("Family") ) {
	//       link = "search?Rank=Family&TaxonName=" + species;
       // }
//	else {

	//CreateLink cl = new CreateLink(genus,species,sex);
	//HashSet hs = cl.getLink();
	//Iterator it = hs.iterator();
        //if ( cl.size()==1 )
	//	link = (String)it.next();
	//else
		
		link = "/search?" + search;
 	
  //      }

%> 

<html>
<head>
<title></title>

<script language="javascript">
function forwardNewWindow(){

window.open('/efg<%=link %>','','toolbar=yes,status=yes,scrollbars=yes,location=yes,menubar=yes,directories=yes,width=650,height=650')

}

window.onLoad = forwardNewWindow; 


</script>

</head>
<body onLoad="forwardNewWindow()">

</body>
</html>

<!-- $Log$
<!-- Revision 1.1  2006/01/25 21:03:48  kasiedu
<!-- Initial revision
<!--
<!-- Revision 1.2  2005/04/25 04:04:52  ram
<!-- correct some /servlet/ stuff for tomcat 5
<!--
<!-- Revision 1.1.1.1  2003/10/17 17:03:09  kimmylin
<!-- no message
<!--
<!-- Revision 1.1.1.1  2003/07/30 17:04:03  kimmylin
<!-- no message
<!--
<!-- Revision 1.1.1.1  2003/07/15 19:12:59  kimmylin
<!-- front and backend separation
<!--
<!-- Revision 1.3  2002/06/08 18:52:01  hdong1
<!-- correct the query
<!-- correct the vitualkey query for plant

-->
