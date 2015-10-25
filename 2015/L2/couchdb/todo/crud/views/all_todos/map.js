function(doc) {
  if (typeof doc.id !== 'undefined') {
    emit([doc.id], 
		{
			"_id": doc._id,
			"_rev": doc._rev,
			"id":doc.id, 
			"value":doc.value, 
			"done":doc.done
		}
	);
  } 
}