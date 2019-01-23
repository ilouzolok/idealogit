
/* Global variable that holds the id of the 
 * div element in which the robot is located
 */
var robotCellId;

/**
 * Creates a dim x dim grid of cells (div elements)
 * and sets the initial position of the robot to be the 
 * first cell (0,0), when the page is loaded
 */
function createGrid(dim) {
	var gridCells = "";
	var cellId;
	var cellText;
	for (row = 0; row < dim ; row ++){
		for (col = 0; col < dim; col ++){
			cellId = row + "-" + col;
			cellText = "("+ col + "," + row + ")";
			if(row == 0 && col == 0){
				//Place the robot at the initial grid position, when no script is provided
				//Note that the Unicode representation of a robot face is &#129302;
				gridCells += "<div id="+"'"+cellId+"' class = 'cell'>&#129302;</div>";
				robotCellId = "#" + cellId;
			}else{
				gridCells += "<div id="+"'"+cellId+"' class = 'cell'>"+cellText+"</div>";
			}
		}
	}
	$(".grid-container").html(gridCells);
}

/**
 * Asynchronously sends the robot script for 
 * server-side processing, and partially updates
 * the page upon success with a new position of the robot
 * on the grid.
 */
function postRobotScript(){
	//Capture the script entered in the text area, on the front-end
	var script = $("#script").val();
	if(script != ""){
		$.ajax({
			url: "/idealo/robot/run-script",
			type:"POST",
			contentType: "text/plain",
			data: script,
			success: function(response){
				updateRobotPosition(response);
			}
		});
	}
	else{
		alert("Script area is empty. Please enter a script to submit");
	}
}

/**
 * Parses the server response to determine the id
 * of the cell (div element) on the grid that corresponds to the 
 * new robot position, as well as its direction.
 */
function updateRobotPosition(response){
	/* Split the response to obtain both the id of the div and the robot direction
	 * The server encodes that information in the following format: "row-col:direction"
	 */
	var responseData = response.split(":");
	var id = "#"+responseData[0];
	if(id != robotCellId){
		/* Get rid of the # sign in the robot cell id, and split the resulting substring to 
		 * obtain both the row and col of the cell and use these to write the its text
		 */
		var cellId = robotCellId.substring(1); 
		var position = cellId.split("-");
		var cellText = "("+position[0]+","+position[1]+")";
		/* Since the robot has a new position on the grid, replace the robot Unicode character 
		 * in its previous cell, with a regular cell text of the format (row,col)
		 */
		$(robotCellId).html(cellText);
		
		/* Place the robot Unicode character along with its direction, 
		 * in the new cell as determined by the server
		 */
		robotCellId = id;
		$(robotCellId).html("&#129302; " + responseData[1]);
	}
}