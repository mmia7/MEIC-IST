// Declare a variable to hold the loaded JSON data.
var globalData;
var originalData;

var show; 

// Define margins for the visualizations.
const margin = { top: 20, right: 20, bottom: 50, left: 80 };

// Calculate the width and height of the visualizations based on the margins.
const width = 400 - margin.left - margin.right;
const height = 200 - margin.top - margin.bottom;

const defaultFiltersState = {
  // years
  yearMax: 0,
  yearMin: 0,

  ageWomanMin: 0,
  ageWomanMax: 0,

  ageManMin: 0,
  ageManMax: 0,

  incomeWomanMin: 0,
  incomeWomanMax: 0,

  incomeManMin: 0,
  incomeManMax: 0,

  ageGapMin: 0,
  ageGapMax: 0,

  season: "",
  month: "",

  Aries: false,
  Taurus: false,
  Gemini: false,
  Cancer: false,
  Leo: false,
  Virgo: false,
  Libra: false,
  Scorpio: false,
  Sagittarius: false,
  Capricorn: false,
  Aquarius: false,
  Pisces: false,

  manNoEducation: true,
  manElementary: true,
  manMiddle: true,
  manHigh: true,
  manCollege: true,
  manOther: true,

  womanNoEducation: true,
  womanElementary: true,
  womanMiddle: true,
  womanHigh: true,
  womanCollege: true,
  womanOther: true,
};

let filtersState = {};


function setupValues(data) {
  filtersState = { ...defaultFiltersState };

  filtersState["yearMin"] = d3.min(data, (d) => +d.Year_divorce);
  filtersState["yearMax"] = d3.max(data, (d) => +d.Year_divorce);

  filtersState["ageWomanMin"] = d3.min(data, (d) => +d.Age_partner_woman);
  filtersState["ageWomanMax"] = d3.max(data, (d) => +d.Age_partner_woman);
  filtersState["ageManMin"] = d3.min(data, (d) => +d.Age_partner_man);
  filtersState["ageManMax"] = d3.max(data, (d) => +d.Age_partner_man);

  filtersState["incomeWomanMin"] = d3.min(data, (d) => +d.Income_woman);
  filtersState["incomeWomanMax"] = d3.max(data, (d) => +d.Income_woman);
  filtersState["incomeManMin"] = d3.min(data, (d) => +d.Income_man);
  filtersState["incomeManMax"] = d3.max(data, (d) => +d.Income_man);

  filtersState["ageGapMin"] = d3.min(data, (d) => +d.Age_Gap);
  filtersState["ageGapMax"] = d3.max(data, (d) => +d.Age_Gap);
}

// This function initiates the dashboard and loads the CSV data.
function startDashboard() {
  // Load the CSV data using D3.js.
  d3.dsv(";", "data/divorces_cleaned.csv")
    .then((data) => {
      data.forEach((d) => {
        d.Age_Gap = parseInt(d.Age_Gap); // Convert Age_Gap to int
        d.Age_partner_man = parseInt(d.Age_partner_man);
        d.Age_partner_woman = parseInt(d.Age_partner_woman);
        d.Compatibility_rate = parseFloat(d.Compatibility_rate);
        d.Marriage_duration = parseInt(d.Marriage_duration);
        d.Zodiac_man = d.Zodiac_man.trim(); // Trim extra spaces from Zodiac columns
        d.Zodiac_woman = d.Zodiac_woman.trim();
        d.Income_woman = parseInt(d.Monthly_income_partner_woman_peso);
        d.Income_man = parseInt(d.Monthly_income_partner_man_peso);
        d.Level_of_education_partner_man =
        d.Level_of_education_partner_man.trim();
        d.Level_of_education_partner_woman =
          d.Level_of_education_partner_woman.trim();
        d.Divorce_Date = d.Divorce_Date; // Parse Divorce_Date to Date object
        d.Month_divorce = parseInt(d.Month_divorce);
        d.Year_divorce = parseInt(d.Year_divorce);
        d.Season = d.Season.trim();
      });
      // Once the data is loaded successfully, store it in the globalData variable.
      globalData = data;
      originalData = data;

      setupValues(data);
      // Create different visualizations using the loaded data.

  

      createChordDiagram(data);
      createBarChart(data);
      createSunburst(data);
      createYearSlider(data);
      createAgeSliderWoman(data);
      createAgeSliderMan(data);
      createIncomeSliderWoman(data);
      createIncomeSliderMan(data);
      createClevelandDotPlot(data);
    })
    .catch((error) => {
      // If there's an error while loading the CSV data, log the error.
      console.error("Error loading the CSV file:", error);
    });
}
