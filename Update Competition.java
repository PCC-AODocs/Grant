import java.util.List;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public List executeWithFeedback(Locale locale, ReadableDocument doc) {
	try {
		DocumentSearchRequest docSearch = new DocumentSearchRequest("Grant");
		List<readableDocuments> results = getDocumentService().findDocuments(docSearch);
		//int i = 0;
		for(int i = 0; i<results.size(); i++){
		  Document grant = getDocumentService().lockDocument((ReadableDocument)results.get(i));
		  grant.setField("Competition",grant.getField("CompetitionOld"));
		  getDocumentService().store(grant);

		}

		return getFeedbackHelper().successMessage("Completed");
		} catch (Exception e) {
			handleError(e, doc);
			return getFeedbackHelper().errorMessage(e.getClass() +" "+ e.getMessage());
		}
}