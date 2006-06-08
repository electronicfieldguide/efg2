/********************************

	Scrolling/Expanding List
	Version 1.0
	last revision: 04.26.2004
	steve@slayeroffice.com

	http://slayeroffice.com/code/scroll_menu/

	Please notify me of any improvments you make on this
	code so that I can update the version hosted on
	slayeroffice

	please leave this notice intact.

********************************/

window.onload=init;

var d=document;
var expander; 			// object reference for the plus/minus div
var lContainer; 			// object reference for the UL

var currentTop=0;			// the current Y position of the UL
var zInterval = null;		// animation thread interval
var direction;			// direction we're scrolling the UL. 0==up, 1==down
var startTop=0;			// the starting top of the UL

var scrollRate=6;			// initial rate of scrolling
var scrollTick=0;			// keeps track of long we've scrolled so we can slow it down accordingly

var listExpand=true;		// boolean to tell us if we're expanding or contracting the list
var listHeight=60;			// the current height of the UL
var isExpanded=false;		// boolean to denote if the list is expanded or not. initiliazed to true as it will be set to false as soon as the expand control is clicked.

var MAX_SCROLL_TICK=4;		// the maximum value of scrollTick before it's reset
var LI_PADDING=2;		// the LI's padding value. used to compensate for overall dimensions
var LI_HEIGHT=10;		// the height of the LI
var MAX_LIST_HEIGHT=300;		// maximum height of the list when fully expanded
var MIN_LIST_HEIGHT=100;		// contracted height of the list
var REBOUND = -4;		// the value of scrollRate when we stop scrolling
var FAST_EXPAND=5;		// the initial rate of list expansion
var SLOW_EXPAND=1;		// the end rate of expansion
var SPEED_TRANSITION=20;	// when this value + the MAX or MIN list height is reached, we set scrollRate to its slower rate

function init() {
	if(!d.getElementById)return; // bail out if this is an older browser

	up=d.getElementById("upArrow");
	down=d.getElementById("downArrow");

	// apply onclick behaviors to the up arrow, down arrow and expansion control elements
	down.onclick=function(){scrollObjects(0);}
	up.onclick=function(){scrollObjects(1);}
	expander=d.getElementById("changeSize");
	expander.onclick=function(){if(!isExpanded)isExpanded=true;changeListSize(); }

	lContainer = d.getElementById("listContainer");

	d.getElementById("nContainer").style.height=MIN_LIST_HEIGHT+"px";
}

function scrollObjects(dir) {
	if(zInterval)return; // already scrolling.
	if(isExpanded)return; // list is expanded. no need to scroll.
	if((!dir && currentTop<=-240) || (dir && currentTop==0))return; // dont scroll up if we're at the top or down if at the bottom of the list
	direction=dir;
	zInterval=setInterval("animate()",20);
}

function animate() {
	// increment or decrement currentTop based on direction
	if(!direction) {
		currentTop-=scrollRate;
	} else {
		currentTop+=scrollRate;
	}
	scrollTick++;	
	if(scrollTick>=MAX_SCROLL_TICK) {
		scrollRate--; // slow the scroll rate down for a little style
		scrollTick=0;
	}

	lContainer.style.top=currentTop+"px";
	if(scrollRate<=REBOUND) {
		// scroll is finished. clear the interval and reset vars for the next scroll
		clearInterval(zInterval);
		zInterval=null;
		startTop=currentTop;
		scrollTick=0;
		scrollRate=6;
	}
}

function changeListSize() {
	listExpand=listExpand?false:true;
	clearInterval(zInterval);
	zInterval=setInterval("expandList()",20);
}

function expandList() {
	//if(zInterval)return; // already expanding or contracting.
	if(!listExpand) {
		if(listHeight<MAX_LIST_HEIGHT-SPEED_TRANSITION) {
			listHeight+=FAST_EXPAND;
		} else {
			listHeight+=SLOW_EXPAND;
		}
		if(currentTop<0) {
			currentTop+=3;
			lContainer.style.top=currentTop+"px";
		}
		if(listHeight<MAX_LIST_HEIGHT)document.getElementById("nContainer").style.height=listHeight+"px";

		if(listHeight>=MAX_LIST_HEIGHT && currentTop>=0) {
			clearInterval(zInterval);
			zInterval=null;
			expander.style.backgroundImage="url(minus.gif)";
			currentTop=0;
			isExpanded=true;
		}
	} else {
		if(listHeight>MIN_LIST_HEIGHT+SPEED_TRANSITION) {
			listHeight-=FAST_EXPAND;
		} else {
			listHeight-=SLOW_EXPAND;
		}
		document.getElementById("nContainer").style.height=listHeight+"px";
		if(listHeight<=MIN_LIST_HEIGHT) {
			clearInterval(zInterval);
			zInterval=null;
			expander.style.backgroundImage="url(plus.gif)";
			isExpanded=false;
		}
	}
}