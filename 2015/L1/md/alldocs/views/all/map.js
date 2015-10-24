function(doc) { 
	emit(doc._rev, doc);
}