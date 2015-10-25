function(doc, req){	
    //req.method will determine the action
    //PUT = Update existing doc
    //POST = Create new doc
    //DELETE = Delete existing doc
    //response may be
    // data : {
    // }
   

    if(req.method == "DELETE"){
    	//doc['_deleted']=true;
      //keep instances and single events
    	if(doc['event_pid'] == "" || doc['event_pid'] != "0") doc['rec_type'] = "none";
    	//all documents having event_pid == _id must have rec_trype to "none"
      //delete all recurrent events
    	if(doc['event_pid'] == "0") doc['_deleted']=true; //doc['rec_type'] = "none";
    	return [doc, JSON.stringify({"action":"deleted"})];
    }

    if(req.method == "PUT"){
      //Add attachments
    	var payload = req.form;
    	doc['start_date'] = payload["start_date"];
   		doc['end_date'] = payload["end_date"];
   		doc['text'] = payload["text"];
   		doc['event_pid'] = payload["event_pid"];
   		doc['event_length'] = payload["event_length"];
   		doc['rec_pattern'] = payload["rec_pattern"];
   		doc['rec_type'] = payload["rec_type"];
   		doc['channel'] = payload["channel"];
   		doc['client'] = payload["client"];
   		doc['outlet'] = payload["outlet"];
   		doc['username'] = payload["username"];
   		doc['sr_report'] = JSON.parse(payload["sr_report"]);
      if(typeof payload["attachments"] !== 'undefined') {
        var file_attachments = JSON.parse(payload["attachments"]);
        //keep all attachments
        if(typeof file_attachments["keep_all"] === 'undefined') {
          if(typeof doc._attachments === 'undefined') doc._attachments = {};
          //some files may be added, and old ones are unchanged
          for(var file in file_attachments){
            if(typeof doc._attachments[file] === 'undefined')
              doc._attachments[file] = file_attachments[file];
          }
        }
         
      }
      doc['color'] = (typeof payload["color"] === 'undefined')?"#1796b0":payload["color"];
    	return [doc,JSON.stringify({"action":"updated","doc":doc})];
    }

    if(req.method == "POST"){
    	var payload = req.form;
        if(doc === null){
          //Create new event
          doc = {};
          doc['_id'] = req['uuid'];
          doc['doctype'] = "event";
          doc['id'] = doc["_id"];
          doc['start_date'] = payload["start_date"];
       		doc['end_date'] = payload["end_date"];
       		doc['text'] = payload["text"];
       		doc['event_pid'] = payload["event_pid"];
       		if(payload["event_pid"] == "" && payload.rec_type.length > 0 && payload.rec_pattern.length > 0){
       			doc['event_pid'] = "0";
       		}
       		doc['event_length'] = payload["event_length"];
       		doc['rec_pattern'] = payload["rec_pattern"];
       		doc['rec_type'] = payload["rec_type"];
       		doc['channel'] = payload["channel"];
       		doc['client'] = payload["client"];
       		doc['outlet'] = payload["outlet"];
       		doc['username'] = payload["username"];
       		doc['sr_report'] = JSON.parse(payload['sr_report']);
          doc['color'] = (typeof payload["color"] === 'undefined')?"#1796b0":payload["color"]; 
        }else{
          //Update existing event
          doc['start_date'] = payload["start_date"];
       		doc['end_date'] = payload["end_date"];
       		doc['text'] = payload["text"];
       		doc['event_pid'] = payload["event_pid"];
       		doc['event_length'] = payload["event_length"];
       		doc['rec_pattern'] = payload["rec_pattern"];
       		doc['rec_type'] = payload["rec_type"];
       		doc['channel'] = payload["channel"];
       		doc['client'] = payload["client"];
       		doc['outlet'] = payload["outlet"];
       		doc['username'] = payload["username"];
       		doc['sr_report'] = JSON.parse(payload["sr_report"]); 
          doc['color'] = (typeof payload["color"] === 'undefined')?"#1796b0":payload["color"];
        }
    	if(payload["rec_type"] == "none") return [doc, JSON.stringify({"action":"deleted"})];
    	return [doc, JSON.stringify({"action":"inserted", "sid":req.id, "tid":req['uuid'], "doc":doc})];
    }

 	//unknown request - send error with request payload
    return [null, JSON.stringify({"action":"error", "req":req})];
}