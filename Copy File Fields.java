import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.altirnao.aodocs.custom.Document;
import com.altirnao.aodocs.custom.DocumentService;
import com.altirnao.aodocs.custom.ReadableDocument;
import com.altirnao.aodocs.custom.action.UpdateAction;

public void update(ReadableDocument document) throws Exception {

DocumentService documentService = getDocumentService();
Document file = documentService.lockDocument(document);

//--------------------------------------
// Get Title from Attachment
//---------------------------------------
com.google.common.collect.ImmutableSet<Attachment> attachments = file.getAttachments();
if(attachments.size() == 0){
  throw new Exception("Cannot create a File without an attachment.  Please attach a file before saving.");

//Attachment exists, continue
}else{

Attachment a = (Attachment)attachments.iterator().next();
file.setTitle(a.getTitle());


//---------------------------------------
//TEST 1 - does File have a parent Grant?
//---------------------------------------
List grantIds = file.getFromRelatedDocumentIds("Grant to File");
ReadableDocument report = null;

//----------------------------------------------------
//TEST 2 - does File have parent Report?
//----------------------------------------------------
List reportIds = file.getFromRelatedDocumentIds("Report to File");
List reports = documentService.loadReadableDocuments(reportIds);
if(!reports.isEmpty()){
   report = (ReadableDocument)reports.get(0);

   //IF NO PARENT GRANT, get from Report
   if(grantIds.size() == 0){
     grantIds = report.getFromRelatedDocumentIds("Grant to Report");
   }
}

//---------------------------------------------------
//TEST 3 - does File have parent Task
//---------------------------------------------------
List taskIds = file.getFromRelatedDocumentIds("Task to File");
List tasks = documentService.loadReadableDocuments(taskIds);
if(!tasks.isEmpty()){
   ReadableDocument task= (ReadableDocument)tasks.get(0);
   
  // get parent report
   if(report == null){
      reportIds = task.getFromRelatedDocumentIds("Report to Task");
      reports = documentService.loadReadableDocuments(reportIds);
      if(!reports.isEmpty()){
         report = (ReadableDocument)reports.get(0);
      }
    }

   //IF NO PARENT GRANT, get from Report
   if(grantIds.size() == 0){
     grantIds = task.getFromRelatedDocumentIds("Grant to Task");
   }
}


//---------------------------------------
//Assign values from Grant
//---------------------------------------
List grants = documentService.loadReadableDocuments(grantIds);
if (!grants.isEmpty()) {
   ReadableDocument grant = (ReadableDocument)grants.get(0);
   file.addFromRelatedDocument("Grant to File", grant);
   if(report != null){
     file.addFromRelatedDocument("Report to File", report);
   }
   file.setField("Project Title", grant.getTitle());
   file.setHierarchicalCategory("Agency", grant.getHierarchicalCategory("Agency"));
   file.setField("Competition", grant.getField("Competition"));
   file.setField("Grant Id", grant.getField("Grant Id"));


   //------------------------------------------------
   // Populate Agency/Competition category if needed
   //------------------------------------------------
   String agency = (String)grant.getHierarchicalCategory("Agency").get(0);
   String competition = (String)grant.getHierarchicalCategory("Competition").get(0);
   String title = (String)grant.getTitle();

   Boolean createAgency = true;
   Boolean createCompetition = true;
   Boolean createTitle = true;

   List<String> items = getCategoryService().listValues("Agency/Competition");

   //check each level of hierarchy to see if need to add new value
   //1. check for agency
   if(items.contains(agency)){
     createAgency = false;
     //2.check for competition
     List<string> competitions = getCategoryService().listValues("Agency/Competition", Arrays.asList(agency));
     if(competitions.contains(competition)){
        createCompetition = false;
        //3.check for Project Title
        List<string> titles = getCategoryService().listValues("Agency/Competition", Arrays.asList(agency, competition));
        createTitle = !titles.contains(title);
     }
   }

   //Create where needed
   if(createAgency){
      getCategoryService().createCategoryValue("Agency/Competition", agency, "");
   }
   if(createCompetition){
      getCategoryService().createCategoryValue("Agency/Competition", Arrays.asList(agency), competition, "");
   }
   if(createTitle){
      getCategoryService().createCategoryValue("Agency/Competition", Arrays.asList(agency, competition), title, "");
   }


   //---------------------------
   //Set Agency/Competition value
   //----------------------------
   file.setHierarchicalCategory("Agency/Competition", Arrays.asList(agency, competition, title));

}  //end if grant not null
 


} // end attachment exists

}

