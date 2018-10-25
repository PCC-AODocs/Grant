import java.util.List;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public void executeAsync(ReadableDocument doc) throws Exception {

	String grantId = (String)doc.getField("Grant Id");
	ReadableDocument grant = getDocumentService().loadReadableDocument(grantId);



	/*
	List reportIds = grant.getToRelatedDocumentIds("Grant to Report");
	List reports = getDocumentService().loadReadableDocuments(reportIds);
	if(!reports.isEmpty()){
	   Document report = getDocumentService().lockDocument((ReadableDocument)reports.get(0));
	   report.setHierarchicalCategory("Agency", grant.getHierarchicalCategory("Agency"));
	   getDocumentService().store(report);
	}
	*/

	//int i = 0;

	List fileIds = grant.getToRelatedDocumentIds("Grant to File");
	List files = getDocumentService().loadReadableDocuments(fileIds);

	for (ReadableDocument f: files){
	   
	   //debug("Updating file " + f.getTitle());
	   if(((Long)doc.getField("Repeat")).intValue() == 0){
		debug("Bulk Load Files Repeat = 0");
		break;
	   }
	   /*if( i > 5){
		  debug("i > 5");
		  break;
	   }*/

	   Document file = getDocumentService().lockDocument(f);
	   file.setHierarchicalCategory("Agency", grant.getHierarchicalCategory("Agency"));
	   getDocumentService().store(file);
	   //i = i + 1;
	}

	debug(grant.getTitle() + " completed Update Agency");

}