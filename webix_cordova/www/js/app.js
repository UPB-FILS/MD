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

//Synchronization between local PouchDB and remote CouchDB
function syncDB(){
	var sync = myPouch.sync(remoteCouch, {
	  retry: true
	}).on('change', function (info) {
	  // handle change
		console.log(info);
	}).on('paused', function () {
	  // replication paused (e.g. user went offline)
		console.log("paused");
	}).on('active', function () {
	  // replicate resumed (e.g. user went back online)
		console.log("active");
	}).on('denied', function (info) {
	  // a document failed to replicate, e.g. due to permissions
		console.log(info);
	}).on('complete', function (info) {
	  // handle complete
		console.log(info);
	}).on('error', function (err) {
	  // handle error
		console.log(err);
		//cancel replication (test if it works?!?)
		sync.cancel();
	});	
	
}

//PouchDB setup

var myPouch = new PouchDB('todo');
//Use your own database - this is a test database
var remoteCouch = "https://dragosstoica.cloudant.com/todo";//"http://dragosstoica.iriscouch.com/todo";

//Proxy for PouchDB
webix.proxy.proxyPouchDB = {
    $proxy:true,

    load:function(view, callback){
        //Build JSON Array from database
		myPouch.allDocs({
		  include_docs: true
		}).then(function (result) {
		  // handle result
			console.log(result);
			var todo_data = [];
			result.rows.forEach(function(element, index, array){
				todo_data.push(element.doc);
			});
			view.parse(todo_data, 'json');
		}).catch(function (err) {
			//something really bad happened 
		  console.log(err);
		});
    },
    save:function(view, update, dp, callback){

        //your saving pattern
        if(update.operation == "update"){
			//already having an _id
			myPouch.put(update.data).then(function (response) {
			  // handle response
				var item = view.getItem(update.data["id"]);
				item._rev = response.rev;
				view.updateItem(update.data["id"],item);
				view.refresh();	
				webix.dp(view).reset();
			}).catch(function (err) {
			  console.log(err);
			});
		}

		if(update.operation == "insert"){
			myPouch.post(update.data).then(function (response) {
			  // handle response
				var item = view.getItem(update.data["id"]);
				item._id = response.id;
				item._rev = response.rev;
				view.updateItem(update.data["id"],item);
				view.refresh();
				webix.dp(view).reset();
			}).catch(function (err) {
			  console.log(err);
			});
		}
	}
};

//Main toolbar
var toolbar = {
	view:"toolbar",
	cols:[
		{view:"button", id:"createTask", type:"icon", icon:"plus-square-o",
			//Create a new task
			click:add_task 
		},
		{view:"button", id:"syncDB", type:"icon", icon:"refresh", click: syncDB},
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

	url: "proxyPouchDB->todo",
	save:"proxyPouchDB->todo"
};

myapp = {};
//Main layout of the application
myapp.ui = {
	id: "mainLayout",
	rows: [
		toolbar,
		list	
	]
};