import java.util.List;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public List executeWithFeedback(Locale locale, ReadableDocument doc) {
	try {
	
DocumentSearchRequest docSearch = new DocumentSearchRequest("Grant");
List<readableDocuments> results = getDocumentService().findDocuments(docSearch);

for(int i = 0; i<results.size(); i++){
  Document grant = getDocumentService().lockDocument((ReadableDocument)results.get(i));
  //grant.setField("Competition",grant.getField("CompetitionOld"));
  getDocumentService().store(grant);
  debug("Grant " + doc.getField("Grant Id") + " updated");
}


return getFeedbackHelper().successMessage("Completed");


 
 
 
Compile	
 
 
 
 
 
 
Save	
 
 
 
 
 
 
Cancel	
 
 
 
You have unsaved changes
Custom script editor
Custom scripts	
 > Bulk Update Grants
Script name:

Bulk Update Grants
Type
Viewer Action
Security mode:

Run with current user's privileges

Async execution:

Parameters
Name	
Default value	
<Add new value>
 
Method:

_execute

Imports

import java.util.List;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

 
User triggered custom code.
public List executeWithFeedback(Locale locale, ReadableDocument doc) {
	try {

DocumentSearchRequest docSearch = new DocumentSearchRequest("Grant");
List<readableDocuments> results = getDocumentService().findDocuments(docSearch);

for(int i = 0; i<results.size(); i++){
  Document grant = getDocumentService().lockDocument((ReadableDocument)results.get(i));
  //grant.setField("Competition",grant.getField("CompetitionOld"));
  getDocumentService().store(grant);
  debug("Grant " + doc.getField("Grant Id") + " updated");
}


return getFeedbackHelper().successMessage("Completed");

 
	} catch (Exception e) {
		handleError(e, doc);
		return getFeedbackHelper().errorMessage(e.getClass() +" "+ e.getMessage());
	}
}