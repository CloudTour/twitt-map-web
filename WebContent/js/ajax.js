var isRealtime = false;
var timer;
var interval = 5000;
var realtimeBegin;
var begin;
var end;
var timestamp = "0";

var heatmap;
var map;
var loc;

function init() {
	log("init...");
	debugger;

	if (isRealtime) {
		startTimer();
	}

	log("init done.");
	log("");
}

function initMap() {
	log("init map...")

	map = new google.maps.Map(document.getElementById('map-canvas'), {
		center : new google.maps.LatLng(0, 0),
		zoom : 2,
	});

	log("init map done.")
	log("");
}

function submit() {
	log("submit...");
	if (loc == null || loc != $("#locinput").val()) {
		loc = $("#locinput").val();
		log("location: " + loc);

		updateCenter();
	} else if (!isRealtime) {
		updateOneTime();
	}
	log("");
}

function getSns() {
	if (isRealtime)
		return;
	if (heatmap != null) 
		heatmap.setMap(null);

	log("get " + $("#sentiselect").val() + " sentiment map...");
	updateHeatmap($("#sentiselect").val());
	log("");
}

function updateCenter() {
	if (loc == "") {
		log("update center: 0, 0");
		map.setZoom(2);
		map.setCenter(new google.maps.LatLng(0, 0));
		if (!isRealtime)
			updateOneTime();
		return;
	}

	log("update center...");
	var geocoder = new google.maps.Geocoder();
	geocoder.geocode({
		'address' : loc
	}, function(results, status) {
		log("get location from google.");
		if (status == google.maps.GeocoderStatus.OK) {
			map.setZoom(5);
			map.setCenter(results[0].geometry.location);

			if (!isRealtime)
				updateOneTime();
		} else {
			alert("Could not find location: " + location);
		}
	});
}

function updateOneTime() {
	log("update map one time. ");
	begin = $("#beginPicker").val();
	if ($("#endPicker").val() == "")
		end = currentDatetime();
	else
		end = $("#endPicker").val();
	log("begin time: " + begin);
	log("end time: " + end);

	if (heatmap != null)
		heatmap.setMap(null);

	var parameters = toUTC(begin) + ";" + toUTC(end);
	updateHeatmap(parameters);
	log("");
}

function updateRealTime() {
	log("update map real time.");
	if (begin == null || realtimeBegin != $("#beginPicker").val())
		begin = realtimeBegin = $("#beginPicker").val();
	else
		begin = end;
	end = currentDatetime();
	log("begin time: " + begin);
	log("end time: " + end);

	var parameters = toUTC(begin) + ";" + toUTC(end);
	updateHeatmap(parameters);
	log("");
}

function updateHeatmap(parameters) {
	debugger;
	log(parameters);
	var mapdata = getMapDataFromServer(parameters);
	var places = mapdata.split(";");
	var heatmapdata = [];
	for (var i = 0; i < places.length; ++i) {
		if (places[i] == "")
			continue;
		var latlng = places[i].split(",");
		heatmapdata[i] = new google.maps.LatLng(latlng[0], latlng[1]);
	}

	log("get " + heatmapdata.length + " locations.");
	if (heatmapdata.length == 0)
		return;
	

	log("update heat map.");
	heatmap = new google.maps.visualization.HeatmapLayer({
		data : heatmapdata
	});
	heatmap.setMap(map);
}

function currentDatetime() {
	return toDatetimeString(new Date());
}

function toDatetimeString(date) {
	str = date.getFullYear() + "-" + (date.getMonth() + 1) + "-"
			+ date.getDate();
	str += " " + date.getHours() + ":" + (date.getMinutes()) + ":"
			+ date.getSeconds();
	return str;
}

function toUTC(datetime) {
	var datetimeSet = datetime.split(" ");
	var dateSet = datetimeSet[0].split("-");
	var timeSet = datetimeSet[1].split(":");

	var d = new Date(
			Number(dateSet[0]), 	// year
			Number(dateSet[1]) - 1, // month
			Number(dateSet[2]),		// date
			Number(timeSet[0]), 	// hour
			Number(timeSet[1]),		// minute
			Number(timeSet[2])		// second
	);

	var utc = d.getTime() + (d.getTimezoneOffset() * 60000);
	var date = new Date(utc - 60 * 5);
	return toDatetimeString(date);
}

function startTimer() {
	log("start timer..");
	timer = setInterval(function() {
		updateRealTime();
	}, interval);
}

function stopTimer() {
	log("stop timers.");
	clearInterval(timer);
}

function switchRealtime() {
	$("#endPicker").val("");
	if (heatmap != null)
		heatmap.setMap(null);
	begin = end = null;

	if ($("#realtime").is(":checked")) {
		$("#endPicker").attr("disabled", "disabled");
		$("#sentiselect").attr("disabled", "disabled");
		startTimer();
		isRealtime = true;
	} else {
		$("#sentiselect").removeAttr("disabled");
		$("#endPicker").removeAttr("disabled");
		stopTimer();
		isRealtime = false;
	}
}


/*
 * creates a new XMLHttpRequest object which is the backbone of AJAX,
 * or returns false if the browser doesn't support it
 */
function getXMLHttpRequest() {
	var xmlHttpReq = false;
	// to create XMLHttpRequest object in non-Microsoft browsers
	if (window.XMLHttpRequest) {
		xmlHttpReq = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		try {
			// to create XMLHttpRequest object in later versions
			// of Internet Explorer
			xmlHttpReq = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (exp1) {
			try {
				// to create XMLHttpRequest object in older versions
				// of Internet Explorer
				xmlHttpReq = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (exp2) {
				xmlHttpReq = false;
			}
		}
	}
	return xmlHttpReq;
}

/*
 * AJAX call starts with this function
 */
function getMapDataFromServer(parameters) {
	var xmlHttpRequest = getXMLHttpRequest();
	xmlHttpRequest.open("POST", "MapLoader", false);
	xmlHttpRequest.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded");

	xmlHttpRequest.send(parameters);

	return xmlHttpRequest.responseText;
}

function log(msg) {
	txt = $("#status").val();
	txt += msg + "\n";
	$("#status").val(txt);
	$("#status").scrollTop($("#status")[0].scrollHeight);
}
