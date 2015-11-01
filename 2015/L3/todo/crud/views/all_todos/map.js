function(doc) {
  if (typeof doc.id !== 'undefined') {
    emit(doc.id, doc);
  } 
}