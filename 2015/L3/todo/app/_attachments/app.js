//Create new task
function add_task(){
	var id = $$("list").add({
		done:-1, 
		value:"ToDo"
	}, 0);
	$$("list").resize();
	$$("list").edit(id);
}

//Check a task. Mark it as being DONE
function mark_task(id, e){ 
	var item_id = $$('list').locate(e);
	$$("list").getItem(item_id).done = 1;
	$$("list").updateItem(item_id);
	list_filter();
}

//filtering grid
function list_filter(){
	var val = $$('selector').getValue() || -1;

	$$('list').filter(function(obj){
		return obj.done == val;
	});
	$$('list').resize();
}

//Extend list so that it is editable
webix.protoUI({
	name:"editactivelist"
}, webix.ActiveContent, webix.EditAbility, webix.ui.list);


//Proxy for CouchDB
webix.proxy.proxyCouchDB = {
    $proxy:true,

    load:function(view, callback){
        //GET JSON Array from database/design_document/_list/[list_name]/[view_name]  
        webix.ajax(this.source, callback, view);
    },
    save:function(view, update, dp, callback){

        //your saving pattern
        if(update.operation == "update"){
			webix.ajax().header({
			    "Content-type":"application/json"
			}).post(dp.config.url.source+ "\/" + update.data["_id"], 
				JSON.stringify(update.data), 
				[function(text, data, xhr){
			    //response
			    //console.log(text);
				//console.log(data.json());
				//console.log(xhr);
				var msg = data.json()
				if('action' in msg){
					var item = view.getItem(update.data["id"]);
					item._rev = xhr.getResponseHeader('X-Couch-Update-NewRev'); //setting _rev property and value for it
					view.updateItem(update.data["id"],item);
					view.refresh();							
				}
				},callback]
			);
		}

		if(update.operation == "insert"){
			webix.ajax().header({
			    "Content-type":"application/json"
			}).post(dp.config.url.source, 
				JSON.stringify(update.data), 
				[function(text, data, xhr){
			    //response
			    //console.log(text);
				//console.log(data.json());
				//console.log(xhr);
				var msg = data.json()
				if('action' in msg){
					var item = view.getItem(update.data["id"]);
					item._id = xhr.getResponseHeader('X-Couch-Id');
					item._rev = xhr.getResponseHeader('X-Couch-Update-NewRev'); //setting _rev property and value for it
					view.updateItem(update.data["id"],item);
					view.refresh();
				}
				}, callback]
			);
		}
	}
};

//Main toolbar
var toolbar = {
	view:"toolbar",
	cols:[
		{view:"button", id:"createTask", value:"And new task", width: 150,
			//Create a new task
			click:add_task 
		},
		{}, 
		{view:"segmented", id:"selector", width: 200, options: [
				{ id:-1, value:"Active"},
				{ id:1, value:"Finished"}
			],
			on:{
				//Show Active or Finished taks 
				onChange : list_filter 
			}
		}
	]
};

//Main task view
//A list with custom item display
var list = {
	view:"editactivelist", 
	id:"list", 
	borderless:true, 
	autoheight:true,
	drag:"order",
	editable:true, 
	editor:"text",	
	activeContent:{
        wipButton:{
            id:"wipButtonId",
            view:"button",
			type:"icon",
			icon:"check-circle",
			width:32,
			click: mark_task
        },		
        doneButton:{
            id:"doneButtonId",
            view:"button",
			type:"icon",
			icon:"circle-o",
            width:32
        }
	},
	template: function (obj, common) {
		var active = (obj.done == -1);
		if (active)
			return "<div style='float:left;'>" + common.wipButton(obj, common) + "</div><div style='float:left;'>" + obj.value + "</div>";
		else 
			return "<div style='float:left;'>" + common.doneButton(obj, common) + "</div><div style='float:left;'>" + obj.value + "</div>";	
	},	
	on:{
		//disallow edit for finished tasks
		onBeforeEditStart:function(id){
			if (this.getItem(id).done == 1)
				return false;
		},
		//delete empty task
		onAfterEditStop:function(state, editor){
			if (state.value == "")
				this.remove(editor.id);
		},
		//save data after reordering
		onAfterDropOrder:function(id){
			webix.dp(this).save(id);
		},
		//filter just after data loading
		onAfterLoad:list_filter
	},

	url: "proxyCouchDB->../crud/_list/todo_list/all_todos",
	save:"proxyCouchDB->../crud/_update/todo_update"
};

app = {};
//Main layout of the application
app.ui = {
	id: "mainLayout",
	rows: [
		toolbar,
		list	
	]
};