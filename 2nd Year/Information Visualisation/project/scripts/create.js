var zodiacSigns = [
  "Aries",
  "Taurus",
  "Gemini",
  "Cancer",
  "Leo",
  "Virgo",
  "Libra",
  "Scorpio",
  "Sagittarius",
  "Capricorn",
  "Aquarius",
  "Pisces",
];

// Emoji character
  const marriageEmoji = [
    " ",
    "📖",
    "👗",
    "🌾",
    "🌼",
    "🪵",
    "🍭",
    "🧶",
    "🧱",
    "🏺",
    "🥫",
    "🔪",
    "👘",
    "🧺",
    "🐘",
    "🔮",
    "🫖",
    "🫖",
    "🫖",
    "🫖",
    "🫖",
    "🥈",
    "🥈",
    "🥈",
    "🥈",
    "🥈",
    "🫧",
    "🫧",
    "🫧",
    "🫧",
    "🫧",
    "🫧",
    "🪸",
    "🪸",
    "🪸",
    "🪸",
    "🪸",
    "💚",
    "💚",
    "💚",
    "💚",
    "💚",
  ];

  const marriageMeaning = [
    " ",
    "Paper",
    "Cotton",
    "Wheat",
    "Flower",
    "Wood",
    "Sugar",
    "Wool",
    "Clay",
    "Ceramic",
    "Tin",
    "Steel",
    "Silk",
    "Linen",
    "Ivory",
    "Crystal",
    "Porcelain",
    "Porcelain",
    "Porcelain",
    "Porcelain",
    "Porcelain",
    "Silver",
    "Silver",
    "Silver",
    "Silver",
    "Silver",
    "Pearls",
    "Pearls",
    "Pearls",
    "Pearls",
    "Pearls",
    "Coral",
    "Coral",
    "Coral",
    "Coral",
    "Coral",
    "Emerald",
    "Emerald",
    "Emerald",
    "Emerald",
    "Emerald",
  ];


const marriageEmojiLegend = [
  "1: 📖 Paper",
  "2: 👗 Cotton",
  "3: 🌾 Wheat",
  "4: 🌼 Flower",
  "5: 🪵 Wood",
  "6: 🍭 Sugar",
  "7: 🧶 Wool",
  "8: 🧱 Clay",
  "9: 🏺 Ceramic",
  "10: 🥫 Tin",
  "11: 🔪 Steel",
  "12: 👘 Silk",
  "13: 🧺 Linen",
  "14: 🐘 Ivory",
  "15: 🔮 Crystal",
  "16-19: 🫖 Porcelain",
  "20-24: 🥈 Silver",
  "25-29: 🫧 Pearls",
  "30-34: 🪸 Coral",
  "35-40: 💚 Emerald",
];

function noDataToShow(data,chartContainer) {
  if (data.length === 0) {
  // Remove any existing message
  d3.select(chartContainer).select(".no-data-message").remove();
  d3.select("#chordDiagram").select("svg").remove();
  // Display a message when there's no data
  d3.select(chartContainer)
    .append("div")
    .attr("class", "no-data-message")
    .text("No data to show");

    return true; 
  }
  d3.select(chartContainer).select(".no-data-message").remove();
  return false;
}

function createChordDiagram(data) {

  if (noDataToShow(data, "#chordDiagram")) {
    return; // Return early if there's no data
  }

  // create input data: a square matrix with 0s and all zodiac signs
  var matrix_comp = Array.from({ length: zodiacSigns.length }, () =>
    Array(zodiacSigns.length).fill(0)
  );
  var matrix_names = Array.from({ length: zodiacSigns.length }, () =>
    Array(zodiacSigns.length).fill("")
  );
  var matrix_count = Array.from({ length: zodiacSigns.length }, () =>
    Array(zodiacSigns.length).fill(0)
  );

  function handleMouseOver(event, item) {
    // Select all chord paths
    d3.select(this).style("fill", "pink");

    d3.selectAll(".group-links")
      .filter(function () {
        return (
          +d3.select(this).attr("source-index") === item.index ||
          +d3.select(this).attr("target-index") === item.index
        );
      })
      .style("opacity", "1");
  }

  function handleMouseLeave(event, item) {
    const opacityScale = d3
      .scaleLinear()
      .domain([0, d3.max(matrix_count.flat())])
      .range([0.3, 1]);

    // Select all chord paths
    d3.select(this).style("fill", "grey");

    d3.selectAll(".group-links")
      .filter(function () {
        return (
          +d3.select(this).attr("source-index") === item.index ||
          +d3.select(this).attr("target-index") === item.index
        );
      })
      .style("opacity", function () {
        const sourceIndex = +d3.select(this).attr("source-index");
        const targetIndex = +d3.select(this).attr("target-index");
        const divorceCount = matrix_count[sourceIndex][targetIndex];

        return opacityScale(divorceCount);
      });
  }
  // create the svg area
  var svg = d3
    .select("#chordDiagram")
    .append("svg")
    .attr("width", width + margin.left + margin.right + 200)
    .attr("height", height + 250)
    .append("g")
    .attr("transform", `translate(250, 180)`);

  data.forEach(function (d) {
    var i = zodiacSigns.indexOf(d.Zodiac_woman);
    var j = zodiacSigns.indexOf(d.Zodiac_man);

    if (i !== -1 && j !== -1) {
      matrix_comp[i][j] = d.Compatibility_rate;
      matrix_comp[j][i] = d.Compatibility_rate;
      matrix_names[i][j] = d.Zodiac_woman + d.Zodiac_man;
      matrix_names[j][i] = d.Zodiac_man + d.Zodiac_woman;

      // Increment counts in both positions
      matrix_count[i][j] += 1;
      matrix_count[j][i] += 1;
    }
  });

  // create color scale
  const colorScale = d3
    .scaleSequential(d3.interpolatePurples)
    .domain([
      d3.min(data, (d) => d.Compatibility_rate),
      d3.max(data, (d) => d.Compatibility_rate),
    ]);

  var originalOpacity;

  // Tooltip Interaction
  var currentTooltipData;
  const showTooltip = function (event, d) {
    const tooltipX = event.pageX + 10; // Add an offset if needed
    const tooltipY = event.pageY - 10; // Subtract an offset if needed

    const zodiac_names = matrix_names[d.source.index][d.target.index];
    const zodiac_names_parsing = zodiac_names.match(/[A-Z][a-z]+/g); // Split by uppercase letters
    const compatibility = matrix_comp[d.source.index][d.target.index];
    const divorce_count = matrix_count[d.source.index][d.target.index];

    originalOpacity = d3.select(this).style("opacity");
    currentTooltipData = d;

    // Fade out all links
    d3.selectAll(".group-links").style("opacity", 0.2);

    // Highlight the specific link
    d3.select(this).style("opacity", 1);

    // Highlight the corresponding group in pink
    d3.selectAll(".group-" + d.source.index).style("fill", "#b5b7f5");
    d3.selectAll(".group-" + d.target.index).style("fill", "#b5b7f5");

    tooltip
      .style("visibility", "visible")
      .style("left", tooltipX + "px")
      .style("top", tooltipY + "px")
      .html(
        zodiac_names_parsing[0] +
          " and " +
          zodiac_names_parsing[1] +
          "<br>Compatibility Rate: " +
          compatibility +
          "<br>Divorce Count: " +
          divorce_count
      );
  };

  var hideTooltip = function (d) {
    // Access the stored data and perform operations
    if (currentTooltipData) {
      d3.selectAll(".group-" + currentTooltipData.source.index).style("fill", "#eef1fb");
      d3.selectAll(".group-" + currentTooltipData.target.index).style("fill", "#eef1fb");}

    // Reset opacity of the specific link
    d3.select(this).style("opacity", originalOpacity);

    // Reset opacity of all links
    d3.selectAll(".group-links").style("opacity", 1);

    tooltip.style("visibility", "hidden");
};

  // give this matrix to d3.chord(): it will calculates all the info we need to draw arc and ribbon
  var res = d3
    .chord()
    .padAngle(0.05) // padding between entities (black arc)
    .sortSubgroups(d3.descending)(matrix_count);

  // Adjust the inner and outer radii of the groups
  const innerRadius = 130
  const outerRadius = 120

  // add the groups on the inner part of the circle
  svg
    .append("g")
    .selectAll("path")
    .data(res.groups)
    .enter()
    .append("path")
    .attr("class", function (d, i) {
      return "group-" + i; // Add a class based on the index
    })
    .style("fill", "#eef1fb")
    .style("stroke", "black")
    .attr("d", function (d) {
      const arc = d3
        .arc()
        .innerRadius(innerRadius)
        .outerRadius(outerRadius)
        .startAngle(d.startAngle - 0.05)
        .endAngle(d.endAngle + 0.05);

      return arc(d);
    })
    .style("opacity", 0) // Set initial opacity to 0
    .transition() // Apply transition effect
    .duration(1000) // Set duration of animation (in milliseconds)
    .style("opacity", 1); // Set final opacity to 1

  // Add the links between groups
  svg
    .append("g")
    .selectAll("g")
    .data(res)
    .enter()
    .append("g")
    .append("path")
    .attr("class", "group-links") // Assign a class to the links
    .attr("source-index", function (d) {
      return d.source.index;
    })
    .attr("target-index", function (d) {
      return d.target.index;
    })
    .filter(function (d) {
      const divorceCount = matrix_count[d.source.index][d.target.index];
      return divorceCount > 0; // Only include links with divorce count > 0
    })
    .attr("d", function(d) {
      const sourceIndex = d.source.index;
      const targetIndex = d.target.index;
      const divorceCount = matrix_count[sourceIndex][targetIndex];

      const normalizationFactor = 150;
    
      // Calculate the angle range for source and target
      const sourceAngleRange = d.source.endAngle - d.source.startAngle;
      const targetAngleRange = d.target.endAngle - d.target.startAngle;
    
      // Calculate the proportion of divorceCount compared to the maximum divorce count
      const maxDivorceCount = d3.max(matrix_count, row => d3.max(row));
      const proportion = divorceCount / maxDivorceCount;
    
      const ribbonGenerator = d3.ribbon().radius(150);
    
      // Normalize the angle ranges
      const normalizedSourceAngleRange = sourceAngleRange / normalizationFactor;
      const normalizedTargetAngleRange = targetAngleRange / normalizationFactor;
    
      // Adjust start and end angles with proportion and normalized ranges
      d.source.startAngle += proportion * normalizedSourceAngleRange;
      d.target.endAngle += proportion * normalizedTargetAngleRange;
      d.source.endAngle += proportion * normalizedSourceAngleRange;
      d.target.startAngle += proportion * normalizedTargetAngleRange;
    
      return ribbonGenerator(d);
    }) 
    .attr("d", d3.ribbon().radius(120))
    .style("fill", function (d) {
      const compatibilityRate = matrix_comp[d.source.index][d.target.index];
      return colorScale(compatibilityRate);
    })
    .style("stroke", "black")
    .style("opacity", 0) // Set initial opacity to 0
    .transition() // Apply transition effect
    .duration(1000) // Set duration of animation (in milliseconds)
    .style("opacity", 1); // Set final opacity to 1

    // Add mouseover and mouseleave events after the transition
    svg.selectAll(".group-links")
      .on("mouseover", showTooltip)
      .on("mouseleave", hideTooltip);

  // Add labels for zodiac signs
  const zodiacLabels = svg
    .selectAll(".zodiac-label")
    .data(res.groups)
    .enter()
    .append("g")
    .attr("class", "zodiac-label")
    .attr("transform", function (d) {
      const arc = d3
        .arc()
        .innerRadius(innerRadius + 50)
        .outerRadius(outerRadius + 50)
        .startAngle(d.startAngle - 0.05)
        .endAngle(d.endAngle + 0.05);

      var labelPoint = arc.centroid(d); // Get the centroid of the arc

      // Adjust label position for labels on the left side
      if (d.startAngle > Math.PI) {
        labelPoint[0] -= 40; 
      } else {
        labelPoint[0] -= 25;
      }

      if(d.index === 11 || d.index === 0) {
        //Aries and Pisces were giving problems 
        labelPoint[1] -=10;    
      } 
      else
        labelPoint[1] -=25; 

      return `translate(${labelPoint})`;
      });

  zodiacLabels
    .append("g")
    .attr("class", "zodiac-group")
    .attr("transform", "translate(0, 0)")
    .each(function (d) {
      const zodiacName = zodiacSigns[d.index];
      const sourceIndex = d.index;
  
      const button = d3
        .select(this)
        .append("foreignObject")
        .attr("width", 60)
        .attr("height", 50)
        .append("xhtml:button")
        .on("click", (event, d) => {
          // Reset all filters to false
          for (const zodiac of zodiacSigns) {
            if (zodiac !== zodiacName) filtersState[zodiac] = false;
          }
          // Set the clicked zodiac as selected
          filtersState[zodiacName] = !filtersState[zodiacName];

          // Update visualization
          update(originalData);
        })
        .attr("class", "zodiac-button")
        .on("mouseover", function (event) {
          // Highlight the corresponding group and adjust links
          d3.selectAll(".group-" + sourceIndex).style("fill", "#b5b7f5");

          // Fade out other links
          svg
            .selectAll(".group-links")
            .filter(function () {
              return (
                +d3.select(this).attr("source-index") !== sourceIndex &&
                +d3.select(this).attr("target-index") !== sourceIndex
              );
            })
            .style("opacity", 0.3);
        })
        .on("mouseout", function (event) {
          // Reset the fill and opacity when mouse leaves the button
          d3.selectAll(".group-" + sourceIndex).style("fill", "#eef1fb");
          svg.selectAll(".group-links").style("opacity", 1);
        });
  
      button.append("xhtml:div").style("font-size", "10px").text(zodiacName);
  
      button
        .append("xhtml:img")
        .attr("src", `img/${zodiacName.toLowerCase()}.svg`)
        .attr("width", 15)
        .attr("height", 15);
    });
  
  var tooltip = d3
    .select("#chordDiagram")
    .append("div")
    .attr("class", "tooltip")
    .style("position", "absolute")
    .style("visibility", "hidden")
    .style("background-color", "white")
    .style("border", "solid")
    .style("border-width", "2px")
    .style("border-radius", "5px")
    .style("padding", "5px")
    .style("min-width", "240px"); 

  // Gradient stuff
  const defs = svg.append("defs");
  const gradient = defs
    .append("linearGradient")
    .attr("id", "colorScaleGradient")
    .attr("x1", "0%")
    .attr("y1", "0%")
    .attr("x2", "0%")
    .attr("y2", "100%");

  gradient
    .append("stop")
    .attr("offset", "0%")
    .attr("stop-color", d3.interpolatePurples(1));

  gradient
    .append("stop")
    .attr("offset", "100%")
    .attr("stop-color", d3.interpolatePurples(0));

  const legend = svg.append("g").attr("transform", `translate(275, -100)`); // Adjusted transform values
  const legendHeight = 200; // Define a specific value for the legend height
  const legendWidth = 20;

  legend
    .append("rect")
    .attr("width", legendWidth)
    .attr("height", legendHeight)
    .style("fill", "url(#colorScaleGradient)");

  // Add a text label
  legend
    .append("text")
    .attr("x", legendWidth + 15 / 2)
    .attr("y", legendHeight + 20) // Adjust the vertical position as needed
    .attr("text-anchor", "middle")
    .style("font-size", "12px")
    .text("Compatibility"); 

  // Add ticks to the gradient legend
  const gradientScale = d3
    .scaleLinear()
    .domain([0, 1])
    .range([legendHeight, 0]);

  const gradientAxis = d3
    .axisRight(gradientScale)
    .ticks(5) 
    .tickSize(5); 

  legend
    .append("g")
    .attr("transform", `translate(${legendWidth}, 0)`)
    .call(gradientAxis);
}


// Function to create chart
function createBarChart(data, update=false) {
  let ageGapGroupSelected = null;

  if (noDataToShow(data, "#barChart")) {
    return; // Return early if there's no data
  }
  var tooltip = d3
    .select("#barChart")
    .append("div")
    .attr("class", "tooltip")
    .style("opacity", 0);

  // Use the provided tooltip interaction functions
  const showTooltip = function (event, d) {
    const tooltipX = event.pageX + 10; // Add an offset if needed
    const tooltipY = event.pageY - 10; // Subtract an offset if needed

    // Fade out other bars
    d3.selectAll(".bar").style("opacity", 0.3);

    // Make hovered bar full opacity
    d3.select(this).style("opacity", 1);

    tooltip
      .style("opacity", 1)
      .html(
        `Age Gap Group: ${d.age_gap_group}
        <br>Average Duration: ${d.average_marriage_duration}
        <br>Marriage Duration Meaning: ${
          marriageMeaning[Math.floor(d.average_marriage_duration)]
        }`
      )
      .style("left", tooltipX + "px")
      .style("top", tooltipY + "px")
      .style("visibility", "visible")
      .style("position", "absolute")
      .style("background-color", "white")
      .style("border", "solid")
      .style("border-width", "2px")
      .style("border-radius", "5px")
      .style("padding", "5px");
  };

  // A function that change this tooltip when the leaves a point: just need to set opacity to 0 again
  const hideTooltip = function (d) {
    tooltip.style("visibility", "hidden");

    // Restore full opacity on all bars
    d3.selectAll(".bar").style("opacity", 1);
  };

  function onClick(event, d) {
    if (ageGapGroupSelected == d.age_gap_group) {
      filtersState.ageGapMin = 0;
      filtersState.ageGapMax = 30;

      d3.selectAll(".bar").style("opacity", 1);

      d3.select(this).style("fill", "#b5b7f5");

      updateByBarChart(data);
    } else {
      const parts = d.age_gap_group.split("-");

      // Lookup min/max for age group
      var min = parts[0];
      var max = parts[1];

      // Set filters
      filtersState.ageGapMin = parseInt(min);
      filtersState.ageGapMax = parseInt(max);

      // Update visualization
      updateByBarChart(data);

      // Fade out other bars
      d3.selectAll(".bar").style("fill", "#b5b7f5");
      d3.selectAll(".bar").style("opacity", 0.3);

      // Make hovered bar full opacity
      d3.select(this).style("fill", "#686cd7");

      ageGapGroupSelected = d.age_gap_group;
    }
  }

  // Preprocess the data to create age gap groups and calculate average marriage duration
  const ageGapGroups = {};
  const divorcePerAgeGap = {};

  data.forEach((d) => {
    const ageGap = d.Age_Gap; // Use the actual Age_Gap value
    let ageGapGroup;

    if (ageGap >= 0 && ageGap <= 5) {
      ageGapGroup = "0-5";
    } else if (ageGap > 5 && ageGap <= 10) {
      ageGapGroup = "5-10";
    } else if (ageGap > 10 && ageGap <= 15) {
      ageGapGroup = "10-15";
    } else if (ageGap > 15 && ageGap <= 20) {
      ageGapGroup = "15-20";
    } else if (ageGap > 20 && ageGap <= 25) {
      ageGapGroup = "20-25";
    } else if (ageGap > 25 && ageGap <= 30) {
      ageGapGroup = "25-30";
    } else {
      ageGapGroup = "+30";
    }

    if (!ageGapGroups[ageGapGroup]) {
      ageGapGroups[ageGapGroup] = [];
    }

    ageGapGroups[ageGapGroup].push(d.Marriage_duration);

    if (!divorcePerAgeGap[ageGapGroup]) {
      divorcePerAgeGap[ageGapGroup] = 0;
    }

    divorcePerAgeGap[ageGapGroup] += 1;
  });

  const averages = [];
  var maxAverages = 0;

  for (const ageGapGroup in ageGapGroups) {
    if (ageGapGroups.hasOwnProperty(ageGapGroup)) {
      const marriageDurations = ageGapGroups[ageGapGroup];
      const averageMarriageDuration = d3.mean(marriageDurations).toFixed(1);
      averages.push({
        age_gap_group: ageGapGroup,
        average_marriage_duration: averageMarriageDuration,
      });

      if (averageMarriageDuration > maxAverages) {
        maxAverages = 1 + averageMarriageDuration;
      }
    }
  }

  // Sort the averages data by age gap group
  averages.sort((a, b) => {
    // Define the natural order of age gap groups
    const order = ["0-5", "5-10", "10-15", "15-20", "20-25", "25-30"];
    return order.indexOf(a.age_gap_group) - order.indexOf(b.age_gap_group);
  });

  const svg = d3
    .select("#barChart")
    .append("svg")
    .attr("width", width + 300)
    .attr("height", height + margin.top + margin.bottom + 10)
    .append("g")
    .attr("transform", `translate(${width / 4}, ${height / 4})`);

  // Create x and y scales
  const xScale = d3
    .scaleBand()
    .domain(averages.map((d) => d.age_gap_group))
    .range([0, width])
    .padding(0.1);

  const yScale = d3
    .scaleLinear()
    .domain([
      0,
      parseInt(d3.max(averages, (d) => parseInt(d.average_marriage_duration))) +
        1,
    ])
    .range([height, 0]);

  const maxAgeGroupDivorces = d3.max(Object.values(divorcePerAgeGap));
  // Scale bar widths
  const widthScale = d3
    .scaleLinear()
    .domain([1, maxAgeGroupDivorces])
    .range([10, 30]);

  // Append and style the bars using the averages data and scales
  svg
    .selectAll(".bar")
    .data(averages, (d) => d.age_gap_group) // Bind averages data
    .enter()
    .append("rect")
    .attr("class", "bar data")
    .attr("x", (d) => xScale(d.age_gap_group))
    .attr("y", height) // Set initial height to 0
    .attr("width", xScale.bandwidth)
    .attr("fill", "#b5b7f5")
    .on("mouseover", showTooltip)
    .on("mouseleave", hideTooltip)
    .on("click", onClick)
    .attr("height", 0) // Set initial height to 0
    .transition() // Add transition
    .duration(1000) // Set the duration of the transition (in milliseconds)
    .delay((d, i) => i * 100) // Add a delay based on the index
    .attr("y", (d) => yScale(d.average_marriage_duration))
    .attr("height", (d) => height - yScale(d.average_marriage_duration));

  // Append x and y axes to the chart
  svg
    .append("g")
    .attr("class", "x-axis")
    .attr("transform", `translate(0,${height})`)
    .call(d3.axisBottom(xScale));

  d3.select("#my_rect").transition().duration(2000).attr("width", "400");

  svg
    .append("g")
    .attr("class", "y-axis")
    .call(d3.axisLeft(yScale).tickSizeOuter(0));

  // Append icon images
  svg
    .selectAll(".bar data")
    .data(averages)
    .enter()
    .append("text")
    .text((d) => marriageEmoji[Math.floor(parseInt(d.average_marriage_duration))])
    .attr("x", (d) => xScale(d.age_gap_group))
    .attr("y", (d) => yScale(d.average_marriage_duration) - 8)
    .style("font-size", 30);

  // Append x and y axis labels
  svg
    .append("text")
    .attr("class", "x-axis-label")
    .attr("x", width / 2)
    .attr("y", height + margin.top + 20)
    .style("text-anchor", "middle")
    .text("Age gap group")
    .style("font-size", 12);

  svg
    .append("text")
    .attr("class", "y-axis-label")
    .attr("x", -height / 2)
    .attr("y", -margin.left + 30)
    .style("text-anchor", "middle")
    .attr("transform", "rotate(-90)")
    .text("Average Marriage Duration")
    .style("font-size", 12);

  // Legend
  const legendWidth = 100;
  const legendHeight = height;

  const legend = svg
    .append("g")
    .attr(
      "transform",
      `translate(${width - 30},${height - legendHeight - 10})`
    );

  const cols = 2;
  const colWidth = legendWidth / cols;
  const colPadding = 40;

  // Legend title
  legend
    .append("text")
    .attr("x", legendWidth)
    .attr("y", -15)
    .text("Marriage Duration Meaning")
    .style("font-size", 10)
    .style("text-anchor", "middle");

  legend
    .selectAll(".legend-text")
    .data(marriageEmojiLegend)
    .enter()
    .append("text")
    .attr("class", "legend-text")
    .attr("x", (d, i) => {
      if (i< 10){
        return colWidth;
      } else {
        return 2 * colWidth + colPadding;
      }
    })
    .attr("y", (d, i) => {
      const row = Math.floor(i % 10);
      return row * 20;
    })
    .text((d) => d)
    .style("font-size", 12);
}

function createYearSlider(data) {
  const chart = document.getElementById("yearSlider");

  const chartLayout = {
    width: chart.clientWidth,
    height: chart.clientHeight,
    margin: {
      left: 20,
      right: 20,
      top: 30,
      bottom: 12,
    },
  };

  var slider = d3
    .sliderBottom()
    .min(d3.min(data, (d) => +d.Year_divorce))
    .max(d3.max(data, (d) => +d.Year_divorce))
    .step(1)
    .width(chartLayout.width - margin.left -margin.top
      )
    .height(chartLayout.height)
    .displayValue(true)
    .displayFormat(d3.format(".0f"))
    .tickFormat((x) => +x)
    .handle(d3.symbol().type(d3.symbolCircle).size(200)())
    .ticks(16)
    .tickPadding(0)
    .default([
      d3.min(data, (d) => +d.Year_divorce),
      d3.max(data, (d) => +d.Year_divorce),
    ])
    .fill("#3f007d")
    .on("onchange", (val) => {
      filtersState.yearMin = +val[0];
      filtersState.yearMax = +val[1];
      updateCleveland(data); 
      update(data);
    });

  d3.select("div#yearSlider")
    .append("svg")
    .attr(
      "width",
      chartLayout.width 
    )
    .attr("height", 75)
    .append("g")
    .attr(
      "transform",
      `translate(${chartLayout.margin.left}, 
              ${chartLayout.margin.top})`
    )
    .call(slider);
}

function createAgeSliderWoman(data) {
  var slider = d3
    .sliderHorizontal()
    .min(d3.min(data, (d) => +d.Age_partner_woman))
    .max(d3.max(data, (d) => +d.Age_partner_woman))
    .step(1)
    .width(80)
    .displayValue(true)
    .displayFormat(d3.format(".0f"))
    .handle(d3.symbol().type(d3.symbolCircle).size(200)())
    .tickFormat((x) => +x)
    .default([
      d3.min(data, (d) => +d.Age_partner_woman),
      d3.max(data, (d) => +d.Age_partner_woman),
    ])
    .ticks(5)
    .fill("#e784cd")
    .on("onchange", (val) => {
      filtersState.ageWomanMin = +val[0];
      filtersState.ageWomanMax = +val[1];
      update(data);
    });

  d3.select("#ageSliderWoman")
    .append("svg")
    .attr("width", 200)
    .attr("height", 100)
    .append("g")
    .attr("transform", "translate(30,30)")
    .call(slider);
}

function createAgeSliderMan(data) {
  var slider = d3
    .sliderHorizontal()
    .min(d3.min(data, (d) => +d.Age_partner_man))
    .max(d3.max(data, (d) => +d.Age_partner_man))
    .step(1)
    .width(80)
    .default([
      d3.min(data, (d) => +d.Age_partner_man),
      d3.max(data, (d) => +d.Age_partner_man),
    ])
    .displayValue(true)
    .displayFormat(d3.format(".0f"))
    .handle(d3.symbol().type(d3.symbolCircle).size(200)())
    .tickFormat((x) => +x)
    .ticks(5)
    .fill("#4647f2")
    .on("onchange", (val) => {
      filtersState.ageManMin = +val[0];
      filtersState.ageManMax = +val[1];
      update(data);
    });

  d3.select("#ageSliderMan")
    .append("svg")
    .attr("width", 200)
    .attr("height", 100)
    .append("g")
    .attr("transform", "translate(30,30)")
    .call(slider);
}

function createIncomeSliderWoman(data) {
  var slider = d3
    .sliderHorizontal()
    .min(d3.min(data, (d) => +d.Income_woman))
    .max(d3.max(data, (d) => +d.Income_woman))
    .width(80)
    .default([
      d3.min(data, (d) => +d.Income_woman),
      d3.max(data, (d) => +d.Income_woman),
    ])
    .displayValue(true)
    .displayFormat(d3.format(".0f"))
    .handle(d3.symbol().type(d3.symbolCircle).size(200)())
    .tickFormat((x) => +x)
    .ticks(2)
    .fill("#e784cd")
    .on("onchange", (val) => {
      filtersState.incomeWomanMin = +val[0];
      filtersState.incomeWomanMax = +val[1];
      update(data);
    });

  d3.select("#incomeSliderWoman")
    .append("svg")
    .attr("width", 200)
    .attr("height", 100)
    .append("g")
    .attr("transform", "translate(30,30)")
    .call(slider);
}

function createIncomeSliderMan(data) {
  var slider = d3
    .sliderHorizontal()
    .min(d3.min(data, (d) => +d.Income_man))
    .max(d3.max(data, (d) => +d.Income_man))
    .width(80)
    .default([
      d3.min(data, (d) => +d.Income_man),
      d3.max(data, (d) => +d.Income_man),
    ])
    .displayValue(true)
    .displayFormat(d3.format(".0f"))
    .handle(d3.symbol().type(d3.symbolCircle).size(200)())
    .tickFormat((x) => +x)
    .ticks(2)
    .fill("#4647f2")
    .on("onchange", (val) => {
      filtersState.incomeManMin = +val[0];
      filtersState.incomeManMax = +val[1];
      update(data);
    });

  d3.select("#incomeSliderMan")
    .append("svg")
    .attr("width", 200)
    .attr("height", 100)
    .append("g")
    .attr("transform", "translate(30,30)")
    .call(slider);
}

function createCalendarHeatmap(data) {
  // Define variables for your margin, width, and height
  var margin = { top: 30, right: 30, bottom: 30, left: 30 };
  var width = 400;
  var height = 600;

  // Use the provided tooltip interaction functions
  const showTooltip = function (event, d) {
    const tooltipX = event.pageX + 10; // Add an offset if needed
    const tooltipY = event.pageY - 10; // Subtract an offset if needed

    // Highlight the specific square
    d3.select(this).style("opacity", 1);

    tooltip
      .style("opacity", 1)
      .html(`Divorce Count: ${d.Count}`)
      .style("left", tooltipX + "px")
      .style("top", tooltipY + "px")
      .style("visibility", "visible")
      .style("position", "absolute")
      .style("background-color", "white")
      .style("border", "solid")
      .style("border-width", "2px")
      .style("border-radius", "5px")
      .style("padding", "5px");
  };

  // A function to hide the tooltip
  var hideTooltip = function (d) {
    tooltip.style("visibility", "hidden");
  };

  // Extract the day component from the date
  data.forEach(function (d) {
    const parts = d.Divorce_date.split("/");
    d.Day = parseInt(parts[0], 10);
  });

  // Transform the data into Month/Day/Count format
  const transformedData = [];

  data.forEach((d) => {
    const month = d.Month_divorce;
    const day = d.Day;
    const season = d.Season;

    // See if entry already exists
    let entry = transformedData.find(
      (e) => e.Season === season && e.Month === month && e.Day === day
    );

    if (!entry) {
      // Add new entry
      entry = {
        Season: season,
        Month: month,
        Day: day,
        Count: 0,
      };
      transformedData.push(entry);
    }

    // Increment count
    entry.Count++;
  });

  // Sort the transformed data
  transformedData.sort((a, b) => {
    if (a.Month < b.Month) return -1;
    if (a.Month > b.Month) return 1;

    if (a.Day < b.Day) return -1;
    if (a.Day > b.Day) return 1;

    return 0;
  });

  const countMax = d3.max(transformedData, (d) => d.Count);

  // Create arrays for days and months
  var days = Array.from({ length: 31 }, (_, i) => i + 1);
  var months = Array.from({ length: 12 }, (_, i) => i + 1);

  // Append the SVG element to your HTML
  var svg = d3
    .select("#calendarHeatmap")
    .append("svg")
    .attr("width", width + margin.left + margin.right)
    .attr("height", height + margin.top + margin.bottom)
    .append("g")
    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

  // Build Y scales and axis
  var y = d3.scaleBand().domain(days).range([0, height]).padding(0.1);
  svg
    .append("g")
    .style("font-size", 15)
    .call(d3.axisLeft(y).tickSize(0))
    .select(".domain")
    .remove();

  // Build X scales and axis
  var x = d3.scaleBand().domain(months).range([0, width]).padding(0.05);
  svg
    .append("g")
    .style("font-size", 15)
    .call(d3.axisTop(x).tickSize(0))
    .select(".domain")
    .remove();

  // Build color scale
  var myColor = d3
    .scaleSequential()
    .interpolator(d3.interpolatePurples)
    .domain([0, countMax]);

  // Create a tooltip
  var tooltip = d3
    .select("body")
    .append("div")
    .style("opacity", 0)
    .attr("class", "tooltip")
    .style("background-color", "white")
    .style("border", "solid")
    .style("border-width", "2px")
    .style("border-radius", "5px")
    .style("padding", "5px");

  // Add the squares
  svg
    .selectAll()
    .data(transformedData)
    .enter()
    .append("rect")
    .attr("x", function (d) {
      return x(d.Month);
    })
    .attr("y", function (d) {
      return y(d.Day);
    })
    .style("fill", function (d) {
      return myColor(d.Count);
    })
    .style("stroke-width", 4)
    .style("opacity", 1)
    .attr("width", x.bandwidth() - x.paddingInner())
    .attr("height", y.bandwidth() - y.paddingInner())
    .on("mouseover", showTooltip)
    .on("mouseleave", hideTooltip);
}

  function createSunburst(data, update = false) {
    // Use the provided tooltip interaction functions
    const showTooltip = function (event, d) {
      const tooltipX = event.pageX + 10; // Add an offset if needed
      const tooltipY = event.pageY - 10; // Subtract an offset if needed

      // Fade out other bars
      d3.selectAll(".path").style("opacity", 0.3);
      // Highlight the specific square
      d3.select(this).style("opacity", 1);

      tooltip
        .style("opacity", 1)
        .html(`Divorce Count: ${d.value}`)
        .style("left", tooltipX + "px")
        .style("top", tooltipY + "px")
        .style("visibility", "visible")
        .style("position", "absolute")
        .style("background-color", "white")
        .style("border", "solid")
        .style("border-width", "2px")
        .style("border-radius", "5px")
        .style("padding", "5px");
    };

    // A function to hide the tooltip
    var hideTooltip = function (d) {
      tooltip.style("visibility", "hidden");
    };

    if (noDataToShow(data, "#calendarHeatmap")) {
      return; // Return early if there's no data
    }
    // Color scale uses Season, Month, Day
    // Extract the day component from the date
    const radius = 40;

    const monthNames = {
      1: "Jan",
      2: "Feb",
      3: "Mar",
      4: "Apr",
      5: "May",
      6: "Jun",
      7: "Jul",
      8: "Aug",
      9: "Sep",
      10: "Oct",
      11: "Nov",
      12: "Dec",
    };

    // Objects to hold aggregate values
    const seasonCounts = {};
    const monthCounts = {};

    data.forEach(function (d) {
      const parts = d.Divorce_date.split("/");
      d.Day = parseInt(parts[0], 10);
    });

    const aggregated = data.reduce((acc, d) => {
      const season = acc[d.Season] || {};
      const month = season[d.Month_divorce] || {};
      const day = month[d.Day] || 0;

      // Season counts
      seasonCounts[d.Season] = seasonCounts[d.Season] || 0;
      seasonCounts[d.Season]++;

      // Month counts
      monthCounts[d.Month_divorce] = monthCounts[d.Month_divorce] || 0;
      monthCounts[d.Month_divorce]++;

      season[d.Month_divorce] = month;
      month[d.Day] = day + 1;

      acc[d.Season] = season;

      return acc;
    }, {});

    const minSeasonCount = d3.min(Object.values(seasonCounts));
    const maxSeasonCount = d3.max(Object.values(seasonCounts));

    const minMonthCount = d3.min(Object.values(monthCounts));
    const maxMonthCount = d3.max(Object.values(monthCounts));

    // Create scales
    const seasonScale = d3
      .scaleLinear()
      .domain([minSeasonCount, maxSeasonCount])
      .range([0, 1]);

    const monthScale = d3
      .scaleLinear()
      .domain([minMonthCount, maxMonthCount])
      .range([0, 1]);

    // Normalize counts
    const normalizedSeasonCounts = {};
    for (let season in seasonCounts) {
      normalizedSeasonCounts[season] = seasonScale(seasonCounts[season]);
    }

    const normalizedMonthCounts = {};
    for (let month in monthCounts) {
      normalizedMonthCounts[month] = monthScale(monthCounts[month]);
    }

    // Use normalized counts for color scales
    const seasonColor = d3
      .scaleSequential()
      .domain([0, 1])
      .interpolator(d3.interpolatePurples);

    const monthColor = d3
      .scaleSequential()
      .domain([0, 1])
      .interpolator(d3.interpolatePurples);

    const dayColor = d3
      .scaleSequential()
      .interpolator(d3.interpolatePurples)
      .domain([0, 15]);

    function toHierarchy(data) {
      const root = { name: "root", children: [] };

      for (let season in data) {
        const seasonNode = { name: season, children: [] };

        for (let month in data[season]) {
          const monthNode = { name: month, children: [] };

          for (let day in data[season][month]) {
            monthNode.children.push({
              name: day,
              value: data[season][month][day],
            });
          }

          seasonNode.children.push(monthNode);
        }

        root.children.push(seasonNode);
      }

      return root;
    }

    const hierarchy1 = toHierarchy(aggregated);

    const hierarchy2 = d3
      .hierarchy(hierarchy1)
      .sum((d) => d.value)
      .sort((a, b) => a.data.name - b.data.name);

    // SORT SEASONS
    // Desired order
    const seasonOrder = ["Winter", "Spring", "Summer", "Autumn"];

    // Sort function
    function sortSeasons(a, b) {
      return seasonOrder.indexOf(a.name) - seasonOrder.indexOf(b.name);
    }

    // Build hierarchy
    hierarchy2.sort(sortSeasons);

    // SORT WINTER MONTHS
    // Desired month order for Winter
    const winterMonthOrder = ["12", "1", "2", "3"];

    function sortWinterMonths(a, b) {
      return (
        winterMonthOrder.indexOf(a.data.name) -
        winterMonthOrder.indexOf(b.data.name)
      );
    }

    hierarchy2.children[0].children.sort(sortWinterMonths);

    const partition = d3.partition().size([2 * Math.PI, hierarchy2.height + 1]);

    let root = partition(hierarchy2);

    // Sort function
    function sortSeasons(a, b) {
      return (
        seasonOrder.indexOf(a.data.name) - seasonOrder.indexOf(b.data.name)
      );
    }

    root.each((d) => (d.current = d));

    // Compute the layout.

    // Create the arc generator.
    const arc = d3
      .arc()
      .startAngle((d) => d.x0)
      .endAngle((d) => d.x1)
      .padAngle((d) => Math.min((d.x1 - d.x0) / 2, 0.005))
      .padRadius(radius * 1.5)
      .innerRadius((d) => d.y0 * radius)
      .outerRadius((d) => Math.max(d.y0 * radius, d.y1 * radius - 1));

    // Create the SVG container.
    const svg = d3
      .select("#sunBurst")
      .append("svg")
      .attr("viewBox", [-width / 2 + 30, -height, width +60, width -50])
      .style("font", "10px sans-serif")
      .style("transform", "translate(-20%, 10)");

    // Append the arcs.
    const path = svg
      .append("g")
      .selectAll("path")
      .data(root.descendants().slice(1))
      .join("path")
      .attr("fill", (d) => {
        if (d.depth == 1) {
          return seasonColor(normalizedSeasonCounts[d.data.name]);
        } else if (d.depth == 2) {
          return monthColor(normalizedMonthCounts[d.data.name]);
        } else {
          return dayColor(d.value);
        }
      })
      .attr("fill-opacity", (d) =>
        arcVisible(d.current) ? (d.children ? 0.6 : 0.4) : 0
      )
      .attr("pointer-events", (d) => (arcVisible(d.current) ? "auto" : "none"))

      .attr("d", (d) => arc(d.current))
      .on("mouseover", showTooltip)
      .on("mouseleave", hideTooltip);

    // Make them clickable if they have children.
    path
      .filter((d) => d.children)
      .style("cursor", "pointer")
      .on("click", clicked);

    const format = d3.format(",d");
    path.append("title").text(
      (d) =>
        `${d
          .ancestors()
          .map((d) => d.data.name)
          .reverse()
          .join("/")}\n${format(d.value)}`
    );

    const label = svg
      .append("g")
      .attr("pointer-events", "none")
      .attr("text-anchor", "middle")
      .style("user-select", "none")
      .selectAll("text")
      .data(root.descendants().slice(1))
      .join("text")
      .attr("dy", "0.35em")
      .attr("fill-opacity", (d) => +labelVisible(d.current))
      .attr("transform", (d) => labelTransform(d.current))
      .text((d) => {
        if (d.depth == 2) {
          return monthNames[d.data.name];
        } else {
          return d.data.name;
        }
      });

    const parent = svg
      .append("circle")
      .datum(root)
      .attr("r", radius)
      .attr("fill", "none")
      .attr("pointer-events", "all")
      .on("click", clicked);

    
    // Create a tooltip
    var tooltip = d3
      .select("#sunBurst")
      .append("div")
      .style("opacity", 0)
      .attr("class", "tooltip");
  
    
    // Gradient stuff
    const defs = svg.append("defs");
    const gradient = defs
      .append("linearGradient")
      .attr("id", "colorScaleGradient")
      .attr("x1", "100%")
      .attr("y1", "0%")
      .attr("x2", "0%")
      .attr("y2", "100%");

    gradient
      .append("stop")
      .attr("offset", "0%")
      .attr("stop-color", d3.interpolatePurples(1));

    gradient
      .append("stop")
      .attr("offset", "100%")
      .attr("stop-color", d3.interpolatePurples(0));

    const legend = svg.append("g").attr("transform", `translate(150, -75)`); 
    const legendHeight = 160;
    const legendWidth = 20;

    legend
      .append("rect")
      .attr("width", legendWidth)
      .attr("height", legendHeight)
      .style("fill", "url(#colorScaleGradient)");

    // Create legends
    legend.append("text").attr("x", 0).attr("y", -10).text("Most common");

    // Add a text label
    legend
      .append("text")
      .attr("x", legendWidth + 27 / 2)
      .attr("y", legendHeight + 20)
      .attr("text-anchor", "middle")
      .text("Less common"); // Text to indicate what the scale represents

    // Add ticks to the gradient legend
    const gradientScale = d3
      .scaleLinear()
      .domain([0, 1]) // Domain for the gradient scale
      .range([legendHeight, 0]); // Range for the gradient scale

    const gradientAxis = d3
      .axisRight(gradientScale)
      .ticks(5) // Number of ticks
      .tickSize(5); // Size of the ticks

    // Handle zoom on click.
    function clicked(event, p) {
      parent.datum(p.parent || root);

      root.each(
        (d) =>
          (d.target = {
            x0:
              Math.max(0, Math.min(1, (d.x0 - p.x0) / (p.x1 - p.x0))) *
              2 *
              Math.PI,
            x1:
              Math.max(0, Math.min(1, (d.x1 - p.x0) / (p.x1 - p.x0))) *
              2 *
              Math.PI,
            y0: Math.max(0, d.y0 - p.depth),
            y1: Math.max(0, d.y1 - p.depth),
          })
      );

      const t = svg.transition().duration(750);

      // Transition the data on all arcs, even the ones that aren’t visible,
      // so that if this transition is interrupted, entering arcs will start
      // the next transition from the desired position.
      path
        .transition(t)
        .tween("data", (d) => {
          const i = d3.interpolate(d.current, d.target);
          return (t) => (d.current = i(t));
        })
        .filter(function (d) {
          return +this.getAttribute("fill-opacity") || arcVisible(d.target);
        })
        .attr("fill-opacity", (d) =>
          arcVisible(d.target) ? (d.children ? 0.6 : 0.4) : 0
        )
        .attr("pointer-events", (d) => (arcVisible(d.target) ? "auto" : "none"))

        .attrTween("d", (d) => () => arc(d.current));

      label
        .filter(function (d) {
          return +this.getAttribute("fill-opacity") || labelVisible(d.target);
        })
        .transition(t)
        .attr("fill-opacity", (d) => +labelVisible(d.target))
        .attrTween("transform", (d) => () => labelTransform(d.current));

      changed(event, p);
    }

    function changed(event, d) {
      if (d.depth == 1) {
        filtersState.month = "";
        // Season click
        if (filtersState.season !== d.data.name) {
          filtersState.season = d.data.name;
          updateBySunburst(data);
        }
      } else if (d.depth == 2 || d.depth == 3) {
        // Season click
        if (filtersState.month !== d.data.name) {
          filtersState.month = d.data.name;
          updateBySunburst(data);
        }
      } else {
        filtersState.season = "";
        filtersState.month = "";
        updateBySunburst(data);
      }
    }

    function arcVisible(d) {
      return d.y1 <= 3 && d.y0 >= 1 && d.x1 > d.x0;
    }

    function labelVisible(d) {
      return d.y1 <= 3 && d.y0 >= 1 && (d.y1 - d.y0) * (d.x1 - d.x0) > 0.03;
    }

    function labelTransform(d) {
      const x = (((d.x0 + d.x1) / 2) * 180) / Math.PI;
      const y = ((d.y0 + d.y1) / 2) * radius;
      return `rotate(${x - 90}) translate(${y},0) rotate(${x < 180 ? 0 : 180})`;
    }
  }

///////////////////////////////////////////////////////////////////////////////////
  

let worldData; // Define a global variable
d3.dsv(",", "data/data_world.csv")
.then((data) => {
  worldData = data; // Assign the data to the global variable

  // Convert 'Year' and 'Divorce Rate' to numeric values
  worldData.forEach((row) => {
    row.Year = parseInt(row.Year);
    row["Divorce Rate"] = parseFloat(row["Divorce Rate"]);
  });
})
.catch((error) => {
  console.error("Error loading world data:", error);
});


function createClevelandDotPlot(data) {

  d3.select("#clevelandDotPlot").select("svg").remove();
  
  if (noDataToShow(data, "#clevelandDotPlot")) {
    return; // Return early if there's no data
  }
  var svg = d3
      .select("#clevelandDotPlot")
      .append("svg")
      .attr("width", width + margin.left + margin.right + 100)  
      .attr("height", height + margin.top + margin.bottom + 500) 
      .append("g")
      .attr("transform", `translate(100,0)`);

  // Define scales
  const countries = worldData.map(d => d.Country);

  const xScale = d3.scaleLinear()
      .domain([0, d3.max(worldData, d => d["Divorce Rate"])])
      .range([0, width]);

  const yScale = d3.scaleBand()
      .domain(countries)
      .range([0, height*5])
      .padding(0.1);


  const filteredData = worldData.filter(d => d.Year === filtersState.yearMin || d.Year === filtersState.yearMax);
  
  const legendData = [
    { year: filtersState.yearMin, color: 'pink' },
    { year: filtersState.yearMax, color: '#3f007d' }
  ];

  // Define the tooltip element
  const tooltip = d3.select('#clevelandDotPlot')
    .append('div')
    .attr('class', 'tooltip')
    .style('opacity', 0);
  
   // Add x-axis
  svg.append('g')
        .attr('class', 'axis')
        .attr('transform', `translate(0, 650)`)
        .call(d3.axisBottom(xScale));
    
  // Add x-axis grid lines
  svg.append("g")
      .attr("class", "grid")
      .call(d3.axisBottom(xScale).tickSize(-height).tickFormat(""))
      .selectAll(".tick line")
      .attr("y2", width + margin.left + margin.right +249);

  // Add y-axis
  const yAxis = svg.append('g')
        .attr('class', 'axis')
        .call(d3.axisLeft(yScale));

  // Add y-axis grid lines
  svg.append("g")
        .attr("class", "grid")
        .call(d3.axisLeft(yScale).tickSize(-width).tickFormat(""));

  // Draw dots
  svg.selectAll('.dot')
  .data(filteredData)
  .enter().append('circle')
  .attr('class', 'dot')
  .attr('cx', d => xScale(d["Divorce Rate"]))
  .attr('cy', d => yScale(d.Country) + yScale.bandwidth() / 2)  
  .attr('r', 5)
  .style('fill', d => d.Year === filtersState.yearMin ? 'pink' : '#3f007d') 
  .style('opacity', 0) 
  .on('mouseover', function (event, d) {
    const divorceRate = d["Divorce Rate"];
    tooltip.transition().duration(200).style('opacity', 0.9);
    tooltip.html(`Divorce Rate: ${divorceRate}`)
      .style('left', (event.pageX) + 'px')
      .style('top', (event.pageY - 28) + 'px');
  })
  .on('mouseout', function (d) {
    tooltip.transition().duration(500).style('opacity', 0);
  });

  // Transition to fade in the new dots
    svg.selectAll('.dot')
      .transition()
      .duration(500)
      .style('opacity', 1);

  // Add y-axis label
  yAxis.append('text')
      .attr('class', 'axis-label')
      .attr('transform', 'rotate(-90)')
      .attr('x', -250)
      .attr('y', -100) 
      .style('text-anchor', 'middle')
      .text('Country');

  // Highlight the label for "xalapa"
  yAxis.selectAll('.tick')
      .filter(function(d) { return d === 'Xalapa'; }) // Assuming 'xalapa' is in the countries array
      .select('text')
      .style('font-weight', 'bold') // Change the style to your preference
      .style('fill', '#686cd7');

  // Add x-axis label
  svg.append('text')
      .attr('class', 'axis-label')
      .attr('x', width / 2)
      .attr('y', height + margin.top + 550)
      .style('text-anchor', 'middle')
      .text('Divorce Rate');




  const legend = svg.append('g')
      .attr('class', 'legend')
      .attr('transform', `translate(${width+20}, 200)`);
    
  const legendItem = legend.selectAll('.legend-item')
      .data(legendData)
      .enter().append('g')
      .attr('class', 'legend-item')
      .attr('transform', (d, i) => `translate(0, ${i * 20})`);
    
  legendItem.append('circle')
      .attr('r', 6)
      .attr('cx', 0)
      .attr('cy', 0)
      .style('fill', d => d.color);
    
  legendItem.append('text')
      .attr('x', 20)
      .attr('y', 5)
      .text(d => d.year);

}