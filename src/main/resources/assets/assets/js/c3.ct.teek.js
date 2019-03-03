/**
 * Configuration Functions. These are overwritten for each different classifier trainer
*/

function renderConfigurations(){
$("#list").empty();
$("#list").append("<h2>Available configurations:</h2>");
$.getJSON("../configurations",function(json){	
	if(json.configurations==null){
		$("#list").append("<h3>There are currently no configurations in this microservice. You can add one</h3>");
	}else{
		for (var i=0; i< json.configurations.length; i++){
			$("#list").append("<li><a href=\"configuration.html?confId="+json.configurations[i].id+"\">/configurations/"+json.configurations[i].id+"</a></li>");
		}
	}
});
}

function renderConfiguration(confId){
$("#list").empty();
$("#list").append("<h2>Available configuration:</h2>");
$.getJSON("../configurations/"+confId,function(json){
	if(json==null){
		$("#list").append("<h3>The configuration with id "+confId+" does not exist. You can create it</h3>");
	}else{
		$("#list").append("<h3>Id: "+json.id+"</h3><ul>");
		$("#list").append("<li>Folds: "+json.folds+"</li>");
		$("#list").append("<li>Include implicits: "+json.includeImplicits+"</li>");
		$("#list").append("<li>Assignment threshold: "+json.assignmentThreshold+"</li>");
		$("#list").append("<li>Selection policy: "+json.selectionPolicy+"</li>");
		$("#list").append("<li>Word embedding Id: "+json.wordEmbeddingId+"</li>");
		$("#list").append("<li>Algorithm: "+json.algorithm+"</li>");
		$("#list").append("<li>Distance Measure: "+json.distanceMeasure+"</li>");
		$("#list").append("<li>APD (assignments per document): "+json.apd+"</li>");
		$("#list").append("</ul>");
	}
});
}

function createConfiguration(form){
	var json = "{ \"configurations\":[{\"id\": "+form[0].value+
									", \"folds\": "+form[1].value+
									", \"includeImplicits\": "+form[2].value+
									", \"assignmentThreshold\": "+form[3].value+
									", \"selectionPolicy\": \""+form[4].value+"\""+
									", \"wordEmbeddingId\": "+form[5].value+
									", \"algorithm\": \""+form[6].value+"\""+
									", \"distanceMeasure\": \""+form[7].value+"\""+
									", \"apd\": "+form[8].value+
				" }]}";
	console.log(json);
	
	var url="../configurations";
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function updateConfiguration(form){
	var json = "{ \"id\": "+form[0].value+
				", \"folds\": "+form[1].value+
				", \"includeImplicits\": "+form[2].value+
				", \"assignmentThreshold\": "+form[3].value+
				", \"selectionPolicy\": \""+form[4].value+"\""+
				", \"wordEmbeddingId\": "+form[5].value+
				", \"algorithm\": \""+form[6].value+"\""+
				", \"distanceMeasure\": \""+form[7].value+"\""+
				", \"apd\": "+form[8].value+
				" }";
	console.log(json);
	
	var url="../configurations/"+form[0].value;
	
	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'PUT',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 console.log('succes: '+data);
		}
	 });
}

function renderConfigurationUpdate(confId){
	$.getJSON("../configurations/"+confId,function(json){
		if(json==null){
			//nothing but empty fields
		}else{
			$("#id").val(json.id);
			$("#folds").val(json.folds);
			$("#includeImplicits").empty();
			if(json.includeImplicits==true){
				$("#includeImplicits").append("<option value=\"true\" selected>true</selected>");
				$("#includeImplicits").append("<option value=\"false\">false</selected>");
			}else{
				$("#includeImplicits").append("<option value=\"true\">true</selected>");
				$("#includeImplicits").append("<option value=\"false\" selected>false</selected>");
			}
			$("#assignmentThreshold").val(json.assignmentThreshold);
			$("#selectionPolicy").empty();
			if(json.selectionPolicy=="MicroaverageF1"){
				$("#selectionPolicy").append("<option value=\"MicroaverageF1\" selected>Microaverage F1</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MicroaverageF1\">Microaverage F1</option>");
			}
			if(json.selectionPolicy=="MicroaveragePrecision"){
				$("#selectionPolicy").append("<option value=\"MicroaveragePrecision\" selected>Microaverage Precision</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MicroaveragePrecision\">Microaverage Precision</option>");
			}
			if(json.selectionPolicy=="MicroaverageRecall"){
				$("#selectionPolicy").append("<option value=\"MicroaverageRecall\" selected>Microaverage Recall</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MicroaverageRecall\">Microaverage Recall</option>");
			}
			if(json.selectionPolicy=="MacroaverageF1"){
				$("#selectionPolicy").append("<option value=\"MacroaverageF1\" selected>Macroaverage F1</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MacroaverageF1\">Macroaverage F1</option>");
			}
			if(json.selectionPolicy=="MacroaveragePrecision"){
				$("#selectionPolicy").append("<option value=\"MacroaveragePrecision\" selected>Macroaverage Precision</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MacroaveragePrecision\">Macroaverage Precision</option>");
			}
			if(json.selectionPolicy=="MacroaverageRecall"){
				$("#selectionPolicy").append("<option value=\"MacroaverageRecall\" selected>Macroaverage Recall</option>");
			}else{
				$("#selectionPolicy").append("<option value=\"MacroaverageRecall\">Macroaverage Recall</option>");
			}
			//new in Word Embedding SVM:
			renderWordEmbeddingSelection(json.wordEmbeddingId);
			$("#algorithm").empty
			if(json.algorithm=="ntfc"){
				$("#algorithm").append("<option value=\"ntfc\" selected>NTFC</option>");
				$("#algorithm").append("<option value=\"dai\">Dai</option>");
			}else{
				$("#algorithm").append("<option value=\"ntfc\">NTFC</option>");
				$("#algorithm").append("<option value=\"dai\" selected>Dai</option>");
			}
			
			$("#distanceMeasure").empty();
			if(json.algorithm=="WMD"){
				$("#distanceMeasure").append("<option value=\"WMD\" selected>WMD</option>");
				$("#distanceMeasure").append("<option value=\"BAM\">BAM</option>");
			}else{
				$("#distanceMeasure").append("<option value=\"WMD\">WMD</option>");
				$("#distanceMeasure").append("<option value=\"BAM\" selected>BAM</option>");
			}
			
			$("#apd").val(json.apd);
			
			
		}
	});
}

function uploadConfigurationJSON(json){
	var url="../configurations";	
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'POST',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function deleteAllConfigurations(){
	var url="../configurations";	
	var json="";
	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
		 	console.log('succes: '+data);
		}
	 });
}

function deleteConfiguration(confId){
var url="../configurations/"+confId;
var json="";

	$.ajax({
		url: url,
		headers: {
		    'Accept': 'application/json',
	        'Content-Type':'application/json'
	    },
	    method: 'DELETE',
	    dataType: 'json',
	    data: json,
	    success: function(data){
			 	console.log('succes: '+data);
		}
	 });
}

function renderConfigurationSelectionForm(){
	$("#configuration").empty();
	var configurationJSON;
	$.getJSON("../configurations",function(json){
		configurationJSON = json;
		console.log(configurationJSON);
	}).done(function(){
		
		var appendString = "";			
		for(var j=0; j< configurationJSON.configurations.length; j++){				
			appendString = appendString +"<option value=\""+configurationJSON.configurations[j].id+"\"";
			appendString+=">";
			appendString = appendString +configurationJSON.configurations[j].id+" (";
			appendString = appendString +configurationJSON.configurations[j].folds+" folds, includeImplicits: ";
			appendString = appendString +configurationJSON.configurations[j].includeImplicits+", ";
			appendString = appendString +configurationJSON.configurations[j].assignmentThreshold+" assignmentThreshold, ";
			appendString = appendString +configurationJSON.configurations[j].selectionPolicy+" selectionPolicy ";
			appendString = appendString +")</option>";				
		}
		$("#configuration").append(appendString);
		
	});
}

function renderModel(modId){
	$("#list").empty();
	$("#list").append("<h2>Available model:</h2>");
	$.getJSON("../models/"+modId,function(json){	
		$("#list").append("Id: "+json.id+"<br/>");
		$("#list").append("Configuration Id: "+json.configurationId+"<br/>");
		$("#list").append("Progress:"+json.progress+"<br/>");
		$("#list").append("TrainingLog:<br/> "+json.trainingLog+"<br/>");
		$("#list").append("WordEmbedding:<br/> "+json.wordEmbedding+"<br/>");
		$("#list").append("<h4>Configuration:</h4>");
		$("#list").append("Id:"+json.configuration.id+"<br/>");
		$("#list").append("Folds:"+json.configuration.folds+"<br/>");
		$("#list").append("IncludeImplicits:"+json.configuration.includeImplicits+"<br/>");
		$("#list").append("SelectionPolicy:"+json.configuration.selectionPolicy+"<br/>");
		$("#list").append("Algorithm:"+json.configuration.algorithm+"<br/>");
		$("#list").append("DistanceMeasure:"+json.configuration.distanceMeasure+"<br/>");
		$("#list").append("WordEmbeddingId:"+json.configuration.wordEmbeddingId+"<br/>");
		$("#list").append("APD (Assignments per Document):"+json.configuration.apd+"<br/>");
		
	}).fail(function(){
		$("#list").empty();
		$("#list").append("<h3>There are currently no such model in this microservice. You can add one by starting a training process</h3>");
	});
}

function renderActiveModel(){
	$("#list").empty();
	$("#list").append("<h2>Active model:</h2>");
	$.getJSON("../model",function(json){	
		$("#list").append("Id: "+json.id+"<br/>");
		$("#list").append("Configuration Id: "+json.configurationId+"<br/>");
		$("#list").append("Progress:"+json.progress+"<br/>");
		$("#list").append("TrainingLog:<br/> "+json.trainingLog+"<br/>");
		$("#list").append("WordEmbedding:<br/> "+json.wordEmbedding+"<br/>");
		$("#list").append("<h4>Configuration:</h4>");
		$("#list").append("Id:"+json.configuration.id+"<br/>");
		$("#list").append("Folds:"+json.configuration.folds+"<br/>");
		$("#list").append("IncludeImplicits:"+json.configuration.includeImplicits+"<br/>");
		$("#list").append("SelectionPolicy:"+json.configuration.selectionPolicy+"<br/>");
		$("#list").append("Algorithm:"+json.configuration.algorithm+"<br/>");
		$("#list").append("DistanceMeasure:"+json.configuration.distanceMeasure+"<br/>");
		$("#list").append("WordEmbeddingId:"+json.configuration.wordEmbeddingId+"<br/>");
		$("#list").append("APD (Assignments per Document):"+json.configuration.apd+"<br/>");
	}).fail(function(){
		$("#list").empty();
		$("#list").append("<h3>There is no active model in this microservice. You can assign one.</h3>");
	});
}

function renderWordEmbeddingSelection(wordEmbeddingId){
	$.getJSON("../wordembeddings",function(json){	
		
		$("#wordEmbeddingId").empty();
		for(var i=0; i<json.wordEmbeddings.length; i++){
			if(json.wordEmbeddings[i].id==wordEmbeddingId){
				$("#wordEmbeddingId").append("<option value=\""+json.wordEmbeddings[i].id+"\" selected>"+json.wordEmbeddings[i].id+": "+json.wordEmbeddings[i].metadata.modelName+"</option>");
			}else{
				$("#wordEmbeddingId").append("<option value=\""+json.wordEmbeddings[i].id+"\">"+json.wordEmbeddings[i].id+": "+json.wordEmbeddings[i].metadata.modelName+"</option>");
			}
			
		}
		
	});
}

function renderWordEmbeddings(){
$.getJSON("../wordembeddings",function(json){	
		
		$("#list").empty();
		if(json.wordEmbeddings==null){
			$("#list").append("<h3>There is no wordembedding in this microservice. You can upload one.</h3>");
		}else{
			$("#list").append("<ul>");
			for(var i=0; i<json.wordEmbeddings.length; i++){
				$("#list").append("<li><a href=\"wordembedding.html?weId="+json.wordEmbeddings[i].id+"\">"+json.wordEmbeddings[i].id+": "+json.wordEmbeddings[i].metadata.modelName+"</a></li>");	
			}
			$("#list").append("</ul>");
		}	
		
	}).fail(function(){
		$("#list").empty();
		$("#list").append("<h3>There is no wordembedding in this microservice. You can upload one.</h3>");
	});
}

function renderWordEmbedding(weId){
	$.getJSON("../wordembeddings/"+weId,function(json){	
			$("#list").empty();
			$("#list").append("<ul>");
			$("#list").append("<li>Id: "+json.id+"</li>");
			$("#list").append("<li>Name: "+json.metadata.modelName+"</li>");
			$("#list").append("<li>Creation date: "+json.metadata.creationDate+"</li>");
			$("#list").append("<li>Technical information: "+json.metadata.technicalInformation+"</li>");
			$("#list").append("<li>Structural metadata: "+json.metadata.structuralMetadata+"</li>");
			$("#list").append("<li>Natural language: "+json.metadata.naturalLanguage+"</li>");
			$("#list").append("<li>Model subject: "+json.metadata.modelSubject+"</li>");
			$("#list").append("<li>Creator: "+json.metadata.creator+"</li>");
			$("#list").append("<li>Algorithm: "+json.algorithm+"</li>");
			$("#list").append("<li>Dimensions: "+json.dimensions+"</li>");
			$("#list").append("<li>Terms: "+json.terms+"</li>");
			$("#list").append("<li>Local file path: "+json.localFilePath+"</li>");
			$("#list").append("</ul>");			
		}).fail(function(){
			$("#list").empty();
			$("#list").append("<h3>This word embedding doesn't seem to be in this microservice. You can upload it.</h3>");
		});
}

function deleteWordEmbedding(weId){
	var url="../wordembeddings/"+weId;
	var json="";

		$.ajax({
			url: url,
			headers: {
			    'Accept': 'application/json',
		        'Content-Type':'application/json'
		    },
		    method: 'DELETE',
		    dataType: 'json',
		    data: json,
		    success: function(data){
				 	console.log('succes: '+data);
			}
		 });
 }

function findTaxRootId(categories, roots){
	var rootId=categories[0].id;
	var maxImplicit=0;
	for(var i=0;i<categories.length;i++){
		var implicits=0;
		if(implicits>maxImplicit&&!roots.includes(categories[i].id)){
			maxImplicit=implicits;
			rootId = categories[i].id;
		}
		
	}
	return rootId;
}

function readTaxonomy(){
	console.log("Reading the taxonomy");
	var categoryJSON;
	var relationshipJSON;
	
	$.getJSON("../categories",function(json){
		categoryJSON = json;
	}).done(function(){
		$.getJSON("../relationships",function(json){
				relationshipJSON = json;
			}).done(function(){
					var categoryDisplay = new Array();
					var categoryDescendants = new Array();
					var categoryEqualities = new Array();
					var categoryAssignments = new Array();
					
					var categoryDisplayCounter = 0;
					if(relationshipJSON.relationships==null){
						for(var i=0;i<categoryJSON.categories.length;i++){
							categoryDisplay[categoryDisplayCounter]=categoryJSON.categories[i];
							categoryAssignments[categoryDisplayCounter]=getCategoryAssignmentArray(targetfunctionJSON.assignments,categoryJSON.categories[i].id);
							categoryDisplayCounter++;
						}
					}else{
						var roots = new Array();
									
						//var rootId=findRootId(categoryJSON.categories, targetfunctionJSON.assignments, roots);
						var rootId=categoryJSON.categories[0].id;
						//implement BFS to find everything connected to the root. Repeat for all remaining nodes.
						var visited = new Array();
						var queue = new Array();
						
						var unvisited = getIds(categoryJSON.categories);
						//visited.push(rootId);
						queue.push(rootId);
						roots.push(rootId);
						while(queue.length>0){
							var catId=queue.pop();
							visited.push(catId);
							unvisited=deleteFromArray(unvisited, catId);
							console.log("Processing category "+catId);
							console.log(visited);	
							console.log(unvisited)
							for(var j=0;j<categoryJSON.categories.length;j++){
								if(categoryJSON.categories[j].id==catId){
									categoryDisplay[categoryDisplayCounter]=categoryJSON.categories[j];
								}
							}
							categoryDescendants[categoryDisplayCounter]=new Array();
							categoryEqualities[categoryDisplayCounter]=new Array();
							for (var i=0;i<relationshipJSON.relationships.length;i++){
								var rel=relationshipJSON.relationships[i];
								if(rel.type=="Sub"&&rel.fromId==catId&&!visited.includes(rel.toId)){
									queue.push(rel.toId)
									for(var j=0;j<categoryJSON.categories.length;j++){
										var cat=categoryJSON.categories[j];
										if(cat.id==rel.toId){
											categoryDescendants[categoryDisplayCounter].push(cat);
										}
									}							
								}else if(rel.type=="Equality"&&rel.fromId==catId){
									categoryEqualities[categoryDisplayCounter].push(rel.toId);
								}else if(rel.type=="Equality"&&rel.toId==catId){
									categoryEqualities[categoryDisplayCounter].push(rel.fromId);
								}
							}
							categoryDisplayCounter++;
							if(queue.length==0&&unvisited.length>0){
								//check for alternative roots
								rootId=findRootId(buildArrayFromIds(categoryJSON.categories,unvisited), targetfunctionJSON.assignments, roots);
								console.log("Found new root: "+rootId);
								console.log("Has descendants: "+getDescendantAmount(relationshipJSON.relationships,rootId));
								if(!roots.includes(rootId)&&getDescendantAmount(relationshipJSON.relationships,rootId)>0){
									console.log("pushing rootId: "+rootId);
									queue.push(rootId);
								}else{
									console.log("unvisited: "+unvisited);
									for(var i=0; i<unvisited.length; i++){
										categoryDisplay[categoryDisplayCounter]=getObjectById(categoryJSON.categories, unvisited[i]);
										categoryDisplayCounter++;
									}
								}										
							}
						}
						
						
					}
					
					console.log(categoryDisplay);
					console.log(categoryDescendants);
					console.log(categoryEqualities);
					renderTaxonomy(categoryDisplay, categoryDescendants, categoryEqualities);
			
		});
		
	});
}

function renderTaxonomy(categoryDisplay, categoryDescendants, categoryEqualities){
	$("#list").empty();
	$("#list").append("<h2>Available assignments:</h2>");
	if(categoryDisplay.length==0){
		$("#list").append("<h3>There is currently no taxonomy in this microservice. You can add one</h3>");
	}else{
		var visited = new Array();
		for(var i=0;i<categoryDisplay.length;i++){
			
			var catResult = generateTaxonomyCategory(categoryDisplay,categoryDescendants,categoryEqualities,  i, visited, 0);
			var appendString = catResult.appendString;
			visited = catResult.visited;
			console.log("rendering category");
			console.log(visited);
			
			$("#list").append(appendString);
			$("#cat"+categoryDisplay[i].id+"desc").hide();
			$("#cat"+categoryDisplay[i].id+"hideCats").hide();
		}
	}
	
}

function generateTaxonomyCategory(categories, descendants, equalities, i, visited, depth){
	var appendString="";
	if(!visited.includes(categories[i])){
		visited.push(categories[i]);
		appendString = "<div id=\"cat"+categories[i].id+"\" style=\"padding-left: 10px\">";
		appendString += "<table>";
		appendString += "<tr><td>";	
		appendString += "<b>Category "+categories[i].label;
		appendString += "</b> </td></tr>";
		if(typeof equalities[i]!="undefined" && equalities[i].length>0){
				appendString += " equal to ";
			for(var j=0;j<equalities[i].length;j++){
				var eq = getObjectById(categories, equalities[i][j]);
				appendString += "<a href=\"#cat"+eq.id+"\" onclick=\"showEq("+eq.id+")\">"
				appendString += eq.label+"</a>";
				if(j<equalities[i].length-1){
					appendString += ", ";
				}
					
			}
		}		
		appendString += "</b></td></tr>";
		/*
		appendString += "<tr><td><label>"
		appendString += assignments[i].length+" assigned documents </label></td><td>";
		appendString += "<button onclick=\"hideDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideDocs\">Hide</button>";
		appendString += "<button onclick=\"showDocs("+categories[i].id+")\" id=\"cat"+categories[i].id+"showDocs\">Show</button>";
		appendString += "<td"
		if(typeof descendants[i]!="undefined" && descendants[i].length>0){
			appendString += "><label>"+descendants[i].length+" sub-categories</label></td><td>";
			appendString += "<button onclick=\"hideDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideCats\">Hide</button>";
			appendString += "<button onclick=\"showDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"showCats\">Show</button>";
		}else{
			appendString += "colspan=2>"
		}
		appendString += "</td></tr>";
		*/
		
		appendString += "<tr><td><b>"
		appendString += categories[i].label+"</b> <a href=\"suggestion.html?catId="+categories[i].id+"\" class=\"button next\">Suggest Sub-Categories</a>";
		appendString += "</td></tr>"
		
		if(typeof descendants[i]!="undefined"&&descendants[i].length>0){
			appendString += "<tr><td>"
			appendString += "<b>"+categories[i].label+": "+descendants[i].length+" sub-categories</b> ";
			appendString += "<button onclick=\"hideDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"hideCats\">Hide</button>";
			appendString += "<button onclick=\"showDesc("+categories[i].id+")\" id=\"cat"+categories[i].id+"showCats\">Show</button>";
			appendString += "<br/>";
				
			appendString += "<div id=\"cat"+categories[i].id+"desc\">";		
			for(var j=0; j<descendants[i].length; j++){
				console.log("category "+i+" "+categories[i].label+" descendant "+j+" is category: ")
				console.log(categories.indexOf(descendants[i][j]));
				var descIndex = categories.indexOf(descendants[i][j]); 
				var catResult=generateTaxonomyCategory(categories, descendants, equalities, descIndex,visited, depth+1);
				appendString += catResult.appendString;
				visited = catResult.visited;
			}
			appendString += "</div>";
			appendString += "</td></tr>"
		}
		appendString += "</div>";
		appendString += "</table>";
	}
	var output = {appendString: appendString, visited: visited};
	return output;
	
	
}