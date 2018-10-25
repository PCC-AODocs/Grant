import java.util.List;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public List executeWithFeedback(Locale locale, ReadableDocument doc) {
	try {
	
		ReadableDocument wrongGrant = getDocumentService().loadReadableDocument("R5x9LP79cpwlE1fNyl");

		List fileIds = wrongGrant .getToRelatedDocumentIds("Grant to File");
		List files = getDocumentService().loadDocuments(fileIds);

		ReadableDocument rightGrant = getDocumentService().loadReadableDocument("R5x9awD3XPm6XH7XQE");

		for(int i = 0; i < files.size(); i++){
		  //Document file = getDocumentService().lockDocument(files.get(i));
		  Document file = (Document)files.get(i);
		  file.removeFromRelatedDocument("Grant to File", wrongGrant);
		  file.addFromRelatedDocument("Grant to File", rightGrant);
		  getDocumentService().store(file);
		}

		return getFeedbackHelper().successMessage("Reassigned");

} catch (Exception e) {
		handleError(e, doc);
		return getFeedbackHelper().errorMessage(e.getClass() +" "+ e.getMessage());
	}
}