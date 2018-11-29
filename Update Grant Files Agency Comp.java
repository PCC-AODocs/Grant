import java.util.List;
import java.util.Arrays;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;


public void executeAsync(ReadableDocument doc) throws Exception {


List results = doc.getToRelatedDocumentIds("Grant to File");
debug("size grant " + doc.getTitle() + "=" + results.size());

for(int i = 0; i<results.size(); i++){


Document file = getDocumentService().loadDocument((String)results.get(i));


//--------------------------------------------
// Handle Agency/Competition entry
//--------------------------------------------

List grantIds = file.getFromRelatedDocumentIds("Grant to File");
List grants = getDocumentService().loadReadableDocuments(grantIds);

if(grants.size() > 0){
ReadableDocument grant = (ReadableDocument)grants.get(0);

String agency = (String)grant.getHierarchicalCategory("Agency").get(0);
String competition = (String)grant.getHierarchicalCategory("Competition").get(0);
String title = (String)grant.getTitle();
Boolean createAgency = true;
Boolean createCompetition = true;
Boolean createTitle = true;

List<String> items = getCategoryService().listValues("Agency/Competition");


if(items.contains(agency)){
  createAgency = false;
  List<string> competitions = getCategoryService().listValues("Agency/Competition", Arrays.asList(agency));
  if(competitions.contains(competition)){
     createCompetition = false;
     List<string> titles = getCategoryService().listValues("Agency/Competition", Arrays.asList(agency, competition));
     createTitle = !titles.contains(title);
  }
}

if(createAgency){
   getCategoryService().createCategoryValue("Agency/Competition", agency, "");
}
if(createCompetition){
   getCategoryService().createCategoryValue("Agency/Competition", Arrays.asList(agency), competition, "");
}
if(createTitle){
   getCategoryService().createCategoryValue("Agency/Competition", Arrays.asList(agency, competition), title, "");
}


file.setHierarchicalCategory("Agency/Competition", Arrays.asList(agency, competition, title));
getDocumentService().store(file);

}

}


debug("grant " + doc.getTitle() + " updated.");

}