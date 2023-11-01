function handleMouseOver(event, item) {
  // Select all chord paths 
  d3.select(this)
    .style("fill", "pink"); 

  d3.selectAll(".group-links")
    .filter(function() {
      return +d3.select(this).attr("source-index") === item.index || +d3.select(this).attr("target-index") === item.index;
    })
    .style("opacity", "1");
}

function handleMouseLeave(event, item) {
  const opacityScale = d3.scaleLinear()
    .domain([0, d3.max(matrix_count.flat())])  
    .range([0.3, 1]);

  // Select all chord paths
  d3.select(this)
    .style("fill", "grey"); 

  d3.selectAll(".group-links")
    .filter(function() {
      return +d3.select(this).attr("source-index") === item.index || +d3.select(this).attr("target-index") === item.index;
    })
    .style("opacity", function() {
      const sourceIndex = +d3.select(this).attr("source-index");
      const targetIndex = +d3.select(this).attr("target-index");
      const divorceCount = matrix_count[sourceIndex][targetIndex];

      return opacityScale(divorceCount);
    });
}
