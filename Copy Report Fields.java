import com.altirnao.aodocs.custom.*;
import java.util.List;

public void update(ReadableDocument document) throws Exception {

	DocumentService documentService = getDocumentService();
	Document report = documentService.lockDocument(document);

	List grantIds = report.getFromRelatedDocumentIds("Grant to Report");
	List grants = documentService.loadReadableDocuments(grantIds);
	if (!grants.isEmpty()) {
	   ReadableDocument grant = (ReadableDocument) grants.get(0);
	   report.setField("Project Title", grant.getTitle());
	   report.setHierarchicalCategory("Agency", grant.getHierarchicalCategory("Agency"));
	   report.setField("Competition", grant.getField("Competition"));
	   report.setField("Grant Id", grant.getField("Grant Id"));
	}
}