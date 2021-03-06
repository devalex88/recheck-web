package de.retest.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class PathsToWebDataMappingTest {

	@Test
	void should_have_shortest_xpath_as_root() {
		final Map<String, Map<String, Object>> mapping = new HashMap<>();
		mapping.put( "//html[1]", new HashMap<>() );
		mapping.put( "//html[1]/body[1]", new HashMap<>() );
		mapping.put( "//html[1]/body[1]/div[1]", new HashMap<>() );

		final PathsToWebDataMapping cut1 = new PathsToWebDataMapping( mapping );
		assertThat( cut1.getRootPath() ).isEqualTo( "//html[1]" );

		final PathsToWebDataMapping cut2 = new PathsToWebDataMapping( "//html[1]/body[1]/div[1]/iframe[1]", mapping );
		assertThat( cut2.getRootPath() ).isEqualTo( "//html[1]/body[1]/div[1]/iframe[1]/html[1]" );
	}

}
