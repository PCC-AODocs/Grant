import com.altirnao.aodocs.custom.*;
import java.util.List;

public void update(ReadableDocument document) throws Exception {
	
DocumentService documentService = getDocumentService();
Document task= documentService.lockDocument(document);

//---------------------------------------
//TEST 1 - does Task have a parent Grant?
//---------------------------------------
List grantIds = task.getFromRelatedDocumentIds("Grant to Task");

//----------------------------------------------------
//TEST 2 - does Task have parent Report?
//----------------------------------------------------

List reportIds = task.getFromRelatedDocumentIds("Report to Task");
List reports = documentService.loadReadableDocuments(reportIds);
if(!reports.isEmpty()){
   ReadableDocument report = (ReadableDocument)reports.get(0);
   task.setField("Report Title", report.getTitle());
   if(grantIds.size() == 0){
     grantIds = report.getFromRelatedDocumentIds("Grant to Report");
   }
}


//---------------------------------------
//Assign values from Grant
//---------------------------------------
List grants = documentService.loadReadableDocuments(grantIds);
if (!grants.isEmpty()) {

   ReadableDocument grant = (ReadableDocument) grants.get(0);

   task.addFromRelatedDocument("Grant to Task", grant);
   task.setField("Project Title", grant.getTitle());
   task.setField("Agency", grant.getField("Agency"));
   task.setHierarchicalCategory("Agency/Competition", grant.getHierarchicalCategory("Agency/Competition"));
   task.setField("Competition", grant.getField("Competition"));
   task.setField("PCC Director / Lead", grant.getField("PCC Director / Lead"));
   task.setField("Grant Id", grant.getField("Grant Id"));
}

}