import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.altirnao.aodocs.custom.Document;
import com.altirnao.aodocs.custom.DocumentService;
import com.altirnao.aodocs.custom.ReadableDocument;
import com.altirnao.aodocs.custom.action.UpdateAction;

public void update(ReadableDocument document) throws Exception {

	DocumentService documentService = getDocumentService();

	//Relate Note to parent grant, if Note is being added to a class other than Grant

	//---------------------------------------
	//Does Note have a parent Grant?
	//---------------------------------------
	List grantIds = document.getFromRelatedDocumentIds("Grant to Note");


	//---------------------------------------------------
	//No parent grant, determine from other relationships
	//---------------------------------------------------

	if(grantIds.size() == 0){

	   //---------------------------------------------------
	   //Does Note have parent Task
	   //---------------------------------------------------
	   if(grantIds.size() == 0){
		  List taskIds = document.getFromRelatedDocumentIds("Task to Note");
		  List tasks = documentService.loadReadableDocuments(taskIds);
		  if(!tasks.isEmpty()){
			 ReadableDocument task= (ReadableDocument)tasks.get(0);
			 grantIds = task.getFromRelatedDocumentIds("Grant to Task");
		  }
	   }

	   //----------------------------------------------------
	   //If not, does Note have parent Report?
	   //----------------------------------------------------
	   if(grantIds.size() == 0){
		  List reportIds = document.getFromRelatedDocumentIds("Report to Note");
		  List reports = documentService.loadReadableDocuments(reportIds);
		  if(!reports.isEmpty()){
			 ReadableDocument report = (ReadableDocument)reports.get(0);
			 grantIds = report.getFromRelatedDocumentIds("Grant to Report");
		  }
	   }

	   //----------------------------------------------------
	   //If not, does Note have parent File?
	   //----------------------------------------------------
	   if(grantIds.size() == 0){
		  List fileIds = document.getFromRelatedDocumentIds("File to Note");
		  List files = documentService.loadReadableDocuments(fileIds);
		  if(!files.isEmpty()){
			 ReadableDocument file = (ReadableDocument)files.get(0);
			 grantIds = file.getFromRelatedDocumentIds("Grant to File");
		  }
	   }

	   //---------------------------------------
	   //Assign Grant
	   //---------------------------------------
	   List grants = documentService.loadReadableDocuments(grantIds);
	   if (!grants.isEmpty()) {
		  ReadableDocument grant = (ReadableDocument)grants.get(0);
		  Document doc = documentService.lockDocument(document);
		  doc.addFromRelatedDocument("Grant to Note", grant);
	   }
	}
	
}