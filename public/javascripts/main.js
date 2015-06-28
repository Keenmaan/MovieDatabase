function loadPieChart(){

    var lightColors = ["LightBlue", "LightCoral", "LightCyan", "LightGoldenRodYellow", "LightGray", "LightGreen", "LightPink",
        "Khaki", "Magenta", "Orange", "Orchid", "LightSalmon", "LightSeaGreen", "LightSkyBlue", "LightSlateGray", "LightSteelBlue", "LightYellow",
        "SlateBlue", "Violet", "Turquoise"];
    var darkColors = ["DarkBlue", "Coral", "DarkCyan", "DarkGoldenRod", "DarkGray", "DarkGreen", "Pink", "DarkKhaki", "DarkMagenta",
        "DarkOrange", "DarkOrchid", "DarkSalmon", "DarkSeaGreen", "SkyBlue", "DarkSlateGray", "SteelBlue", "Yellow", "DarkKhaki", "DarkMagenta",
        "DarkSlateBlue", "DarkViolet", "DarkTurquoise"];

    var genres = document.getElementsByClassName("genres");
    var counts = document.getElementsByClassName("counts");

    var idx = 0;
    for(var i = 0; i < genres.length; ++i) {
        idx = genres[i].innerHTML.indexOf("</span>") + 7;
        genres[i].innerHTML = genres[i].innerHTML.substring(idx, genres[i].innerHTML.length);
    }

    var data = [];

    for(var i = 0; i < genres.length; ++i) {

        var tmp = {};
        tmp.value = counts[i].innerHTML;
        tmp.color = darkColors[i];
        tmp.highlight = lightColors[i];
        tmp.label = genres[i].innerHTML;
        data.push(tmp);
    }

    var ctx = document.getElementById("chart-area").getContext("2d");
    var myChart = new Chart(ctx).Pie(data, {
        legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\">&nbsp&nbsp&nbsp</span>&nbsp<%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>"
    });
    document.getElementById("pie-chart-legend").innerHTML = (myChart.generateLegend());
};