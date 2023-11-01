function ageFilter(d) {
  // Returns true if the data point is within the age range
  return (
    +d.Age_partner_woman >= filtersState.ageWomanMin &&
    +d.Age_partner_woman <= filtersState.ageWomanMax &&
    +d.Age_partner_man >= filtersState.ageManMin &&
    +d.Age_partner_man <= filtersState.ageManMax
  );
}

function yearFilter(d) {
  return (
    +d.Year_divorce >= filtersState.yearMin &&
    +d.Year_divorce <= filtersState.yearMax
  );
}

function incomeFilter(d) {
  return (
    +d.Income_woman >= filtersState.incomeWomanMin &&
    +d.Income_woman <= filtersState.incomeWomanMax &&
    +d.Income_man >= filtersState.incomeManMin &&
    +d.Income_man <= filtersState.incomeManMax
  );
}

function ageGapFilter(d) {
  return (
    +d.Age_Gap > filtersState.ageGapMin && +d.Age_Gap < filtersState.ageGapMax
  );
}

function seasonFilter(d) {
  if (filtersState.season === '') {
    return true;
  } else {
    return filtersState.season === d.Season;
  }
}

function monthFilter(d) {
  
  if (filtersState.month === '') {
    return true;
  } else {
    return filtersState.month == d.Month_divorce;
  }
}


function auxZodiacFilter(d, zodiac) {
  return d.Zodiac_man === zodiac || d.Zodiac_woman === zodiac;
}

function zodiacFilter(d) {
  const allZodiacsFalse = zodiacSigns.every(zodiac => !filtersState[zodiac]);
  
  if (allZodiacsFalse) {
    return true;
  }

  return (
    (filtersState.Aries && auxZodiacFilter(d, "Aries")) ||
    (filtersState.Taurus && auxZodiacFilter(d, "Taurus")) ||
    (filtersState.Gemini && auxZodiacFilter(d, "Gemini")) ||
    (filtersState.Cancer && auxZodiacFilter(d, "Cancer")) ||
    (filtersState.Leo && auxZodiacFilter(d, "Leo")) ||
    (filtersState.Virgo && auxZodiacFilter(d, "Virgo")) ||
    (filtersState.Libra && auxZodiacFilter(d, "Libra")) ||
    (filtersState.Scorpio && auxZodiacFilter(d, "Scorpio")) ||
    (filtersState.Sagittarius && auxZodiacFilter(d, "Sagittarius")) ||
    (filtersState.Capricorn && auxZodiacFilter(d, "Capricorn")) ||
    (filtersState.Aquarius && auxZodiacFilter(d, "Aquarius")) ||
    (filtersState.Pisces && auxZodiacFilter(d, "Pisces"))
  );
}

function manEducationFilter(d) {
  return (
    (filtersState.manNoEducation || d.Level_of_education_partner_man !== "WITHOUT EDUCATION") && 
    (filtersState.manElementary || d.Level_of_education_partner_man !== "ELEMENTARY SCHOOL") &&
    (filtersState.manMiddle ||d.Level_of_education_partner_man !== "MIDDLE SCHOOL") &&
    (filtersState.manHigh || d.Level_of_education_partner_man !== "HIGH SCHOOL") &&
    (filtersState.manCollege || d.Level_of_education_partner_man !== "COLLEGE") &&
    (filtersState.manOther || d.Level_of_education_partner_man !== "OTHER")
  );
}

function womanEducationFilter(d) {
  return (
    (filtersState.womanNoEducation ||
      d.Level_of_education_partner_woman !== "WITHOUT EDUCATION") &&
    (filtersState.womanElementary ||
      d.Level_of_education_partner_woman !== "ELEMENTARY SCHOOL") &&
    (filtersState.womanMiddle ||
      d.Level_of_education_partner_woman !== "MIDDLE SCHOOL") &&
    (filtersState.womanHigh ||
      d.Level_of_education_partner_woman !== "HIGH SCHOOL") &&
    (filtersState.womanCollege ||
      d.Level_of_education_partner_woman !== "COLLEGE") &&
    (filtersState.womanOther || d.Level_of_education_partner_woman !== "OTHER")
  );
}


function update(data) {
  
  const dataFiltered = data.filter(
    (d) =>
      ageFilter(d) &&
      yearFilter(d) &&
      incomeFilter(d) &&
      zodiacFilter(d) &&
      manEducationFilter(d) &&
      womanEducationFilter(d)
  );

  // Select the existing chart containers and remove their contents
  d3.select("#chordDiagram").select("svg").remove();
  d3.select("#barChart").select("svg").remove();
  d3.select("#sunBurst").select("svg").remove();


  // Create and update the charts with new data
  createChordDiagram(dataFiltered);
  createBarChart(dataFiltered, true);
  createSunburst(dataFiltered, true);
}


function updateByBarChart(data){
  const dataFiltered = data.filter(
    (d) =>
      ageGapFilter(d) 
  );

  // Select the existing chart containers and remove their contents
  d3.select("#chordDiagram").select("svg").remove();
  d3.select("#sunBurst").select("svg").remove();

  // Create and update the charts with new data
  createChordDiagram(dataFiltered);
  createSunburst(dataFiltered, true);
}

function updateBySunburst(data){
  const dataFiltered = data.filter(
    (d) =>
      seasonFilter(d) &&
      monthFilter(d)
  );

  // Select the existing chart containers and remove their contents
  d3.select("#chordDiagram").select("svg").remove();
  d3.select("#barChart").select("svg").remove();

  // Create and update the charts with new data
  createChordDiagram(dataFiltered);
  createBarChart(dataFiltered, true);
}



function updateCleveland(data){
  const dataFiltered = data.filter(
    (d) =>
    yearFilter(d)
  );

  // Select the existing chart containers and remove their contents
  const svg = d3.select("#clevelandDotPlot").select("svg");

  // Fade out old dots
  svg.selectAll('.dot')
    .transition()
    .duration(500)
    .style('opacity', 0)
    .remove();

  // Remove old axes
  svg.select('.x-axis').remove();
  svg.select('.y-axis').remove();

  // Wait for the transition to complete before creating new dots
  setTimeout(() => {
    // Create and update the charts with new data
    createClevelandDotPlot(dataFiltered);
  }, 500);
}