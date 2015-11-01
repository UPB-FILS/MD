function(doc, req){	
    //req.method will determine the action
    //PUT    = Update existing doc
    //POST   = Create new doc if doc._id is not present or
	//         Update an existing documente if doc._id is present
    //DELETE = Delete existing doc, CouchDB way ;-)
	
	//The main part of the response (JSON)
	/*
		{
			action: 'error' | 'created' | 'updated' | 'deleted',
			doc: new_doc
		}
	*/
    
    if(req.method == "DELETE"){
		//Delete document keeping history
		//The document may be 'undeleted'
    	doc['_deleted']=true;
      	return [doc, JSON.stringify({"action":"deleted"})];
    }

    if(req.method == "PUT"){
      //update document
    	var payload = JSON.parse(req.body);
        doc['done'] = payload["done"];
	  	doc['value'] = payload["value"];    
    	return [doc,JSON.stringify({"action":"updated","doc":doc})];
    }

    if(req.method == "POST"){
    	var payload = JSON.parse(req.body);
        if(doc === null){
          //Create new document
          newdoc = {};
          newdoc['_id'] = req['uuid'];
		  newdoc['id'] = payload["id"];
          newdoc['done'] = payload["done"];
		  newdoc['value'] = payload["value"];
		  return [newdoc, JSON.stringify({"action":"created", "sid":req.id, "tid":req['uuid'], "doc":newdoc})];
        }else{
          //Update existing document
          doc['done'] = payload["done"];
		  doc['value'] = payload["value"];
          return [doc, JSON.stringify({"action":"updated", "sid":req.id, "tid":req['uuid'], "doc":doc})];
		}
    }

 	//unknown request - send error with request payload
    return [null, JSON.stringify({"action":"error", "req":req})];
}