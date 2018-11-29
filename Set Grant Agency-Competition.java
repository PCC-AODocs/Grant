import java.util.List;
import java.util.Arrays;
import java.util.Locale;
import com.altirnao.aodocs.common.script.*;
import com.altirnao.aodocs.custom.*;

public void update(ReadableDocument document) throws Exception {

String agency = (String)document.getHierarchicalCategory("Agency").get(0);
String competition = (String)document.getHierarchicalCategory("Competition").get(0);
String title = (String)document.getTitle();
Boolean createAgency = true;
Boolean createCompetition = true;
Boolean createTitle = true;

List<String> items = getCategoryService().listValues("Agency/Competition");
List<String> agencyOnly = getCategoryService().listValues("Agency");

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

if(!agencyOnly.contains(agency)){
 getCategoryService().createCategoryValue("Agency", agency, "");
}

Document grant = getDocumentService().lockDocument(document);
grant.setHierarchicalCategory("Agency/Competition", Arrays.asList(agency, competition, title));

}