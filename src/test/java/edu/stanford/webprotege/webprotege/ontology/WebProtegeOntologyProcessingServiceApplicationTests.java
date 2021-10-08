package edu.stanford.webprotege.webprotege.ontology;

import org.junit.jupiter.api.Test;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

@SpringBootTest
class WebProtegeOntologyProcessingServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	OWLDataFactory dataFactory() {
		return new OWLDataFactoryImpl();
	}

}
