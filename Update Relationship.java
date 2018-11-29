import java.util.List;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public List executeWithFeedback(Locale locale, ReadableDocument doc) {
	try {
	
DocumentSearchRequest docSearch = new DocumentSearchRequest("Role History");
List<readableDocuments> results = getDocumentService().findDocuments(docSearch);

//int i = 0;
for(int i = 0; i<results.size(); i++){

  Document person = getDocumentService().lockDocument((ReadableDocument)results.get(i));
  List grantIds = person.getFromRelatedDocumentIds("Grant to Person Roles");
  if(grantIds.size() > 0){
     ReadableDocument grant = getDocumentService().loadDocument((String)grantIds.get(0));
     if(grant.getField("Begin Funding") != null){
       //debug(person.getField("Begin Date").toString());
       if(person.getField("Begin Date") == null || 
           (person.getField("Begin Date").toString().startsWith("Mon Nov 05") && person.getField("Begin Date").toString().endsWith("2018"))){
          person.setField("Begin Date", grant.getField("Begin Funding"));
          getDocumentService().store(person);
       }
     }
  }
}   
   
return getFeedbackHelper().successMessage("Completed");

} catch (Exception e) {
		handleError(e, doc);
		return getFeedbackHelper().errorMessage(e.getClass() +" "+ e.getMessage());
	}
}