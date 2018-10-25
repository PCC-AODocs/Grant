import com.altirnao.aodocs.custom.*;
import java.util.List;

public void update(ReadableDocument document) throws Exception {

	//-----------------------------------------
	// Single doc instance to store Id value
	//-----------------------------------------
	String idFactoryDocId = "R5wljIO5Zy9BR7VaMx";

	//---------------------------------------
	// Assign Grant Id on create
	// Update Grant Id Factory
	//--------------------------------------
	if(document.getField("Grant Id") == null){ 
	  
	  Document idFactory  = getDocumentService().loadDocument(idFactoryDocId);
	  long nextId = ((Long) idFactory.getField("Next Id")).longValue();
	  idFactory.setField("Next Id", Long.valueOf(nextId + 1));
	  getDocumentService().store(idFactory);

	  Document grantDoc = getDocumentService().lockDocument(document);
	  grantDoc.setField("Grant Id", nextId);

	}
}