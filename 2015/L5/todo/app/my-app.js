// Initialize your app
var myApp = new Framework7({
    init: false //Disable App's automatica initialization
});

// Export selectors engine
var $$ = Dom7;

// Add view
var mainView = myApp.addView('.view-main', {
    // Because we use fixed-through navbar we can enable dynamic navbar
    dynamicNavbar: true,
    domCache: true //enable inline pages
});

var ACTIVE = true;

/*
 * PouchDB utilities
 */

var myPouchDB = new PouchDB("todo");

// TODO - implement server synchronization. Add sync button
function syncWithServer(){
    
};

// ~ END PouchDB ~

//Add callback function that will be executed when Page with data-page="about" attribute will be initialized
myApp.onPageInit('index', function (page) {
    // console.log('index page initialized');
    // build task list from PouchDB
    myPouchDB.allDocs({
      include_docs: true,
      attachments: false
    }).then(function (result) {
      // handle result[].doc
        var listItems = result.rows.reduce(function(previousValue, currentValue){
            previousValue.push(currentValue.doc);
            return previousValue;
        },[]);
        myList.deleteAllItems();
        myList.appendItems(listItems);
        myList.update();
        filterTasks();
        $$('.done-mark').on('click', registerDoneButton);
    }).catch(function (err) {
      console.log(err);
        myApp.alert('Error fetching documents from PouchDB.', 'ERROR!');
    });
});

var myList = myApp.virtualList('.list-block.virtual-list', {
    // Array with items data
    items: [
    ],
    // Custom render function to render item's HTML
    renderItem: function (index, item) {
        var result = '<li class="swipeout">' +
                    '<div class="swipeout-content">' +
                        '<label class="label-checkbox item-content">' +
                        '<input type="checkbox" name="my-checkbox" value="' + item.title +'" ' ;
        result += item.done?"checked='checked' disabled":"disabled";
        result += ' />' +
                    '<div class="item-media">'+
                    '<i class="icon icon-form-checkbox"></i>'+
                    '</div>'+
                    '<div class="item-inner">' +
                      '<div class="item-title">' + item.title + '</div>' +
                    '</div>' +
                    '</label>' +
                '</div>';
        result += item.done?"</li>":
                    '<div class="swipeout-actions-left">' +
                        '<a href="#" class="done-mark" _id="' + item._id + '">DONE</a>' +
                    '</div>' +
               '</li>';
        return result ;
    }
});

// Mark task DONE
function registerDoneButton(){
    //console.log("DONE clicked!");
    var selectedID = this.attributes.getNamedItem('_id').value;
    var itemIndex = -1;
    var selectedItem = myList.items.filter(function(obj, idx){
        if(obj._id == selectedID) {
            itemIndex = idx;
            return true;
        }
    });
    selectedItem[0].done = true;
    myPouchDB.put(selectedItem[0]).then(function (response) {
      // handle response
        selectedItem[0]._rev = response.rev;
        myList.replaceItem(itemIndex, selectedItem[0]);
        filterTasks();
        //myList.update();
    }).catch(function (err) {
      console.log(err);
       myApp.alert('Error saving document to PouchDB.', 'ERROR!'); 
    });
}

//Show active/done tasks
function filterTasks(){
    myList.resetFilter();
    if (ACTIVE){
        var indexes =  myList.items.reduce(function(previousValue, currentValue,index){
            if (! currentValue.done){
                return previousValue.concat(index);
            }else {
                return previousValue;
            }
        },[]);
         myList.filterItems(indexes);
    }else{
        var indexes =  myList.items.reduce(function(previousValue, currentValue,index){
            if (currentValue.done) {
                return previousValue.concat(index);
            }else{
                return previousValue;
            }
        },[]);
         myList.filterItems(indexes);
    }
}

// Add new task to the list
$$('.createToDO').on('click', function () {
    console.log("click on new task!");
    console.log(JSON.stringify(myApp.formToJSON('#newItem')));
    var newDoc = myApp.formToJSON('#newItem');
    newDoc.done = false;
    
    
    myPouchDB.post(newDoc).then(function (response) {
      // handle response
        /*
        {
          "ok" : true,
          "id" : "8A2C3761-FFD5-4770-9B8C-38C33CED300A",
          "rev" : "1-d3a8e0e5aa7c8fff0c376dac2d8a4007"
        }
        */
        // Append Item
        // Load about page:
        mainView.router.load({pageName: 'index'});
        newDoc._id = response.id;
        newDoc._rev = response.rev;
        myList.prependItem(newDoc);
        myList.update();
        filterTasks();
        $$('.done-mark').on('click', registerDoneButton);
    }).catch(function (err) {
      console.log(err);
         myApp.alert('Error saving new document in PouchDB.', 'ERROR!');
    });
    
});

$$('.navbar-inner .right .link').on('click', function () {
    console.log("click on active/done task!")
    if (this.innerHTML == "DONE") {
        this.innerHTML = "Active";
        ACTIVE = false;
        filterTasks();
    }else{
        this.innerHTML = "DONE";
        ACTIVE = true;
        filterTasks();
    }
});

myApp.init();