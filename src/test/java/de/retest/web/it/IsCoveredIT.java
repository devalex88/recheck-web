package de.retest.web.it;

import static de.retest.web.testutils.PageFactory.page;
import static de.retest.web.testutils.PageFactory.Page.COVERED_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.web.RecheckSeleniumAdapter;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

public class IsCoveredIT {
	RemoteWebDriver driver;
	RecheckAdapter recheckAdapter;

	@BeforeEach
	void before() {
		driver = WebDriverFactory.driver( Driver.CHROME );
		recheckAdapter = new RecheckSeleniumAdapter();
	}

	@Test
	void fully_covered_element_should_not_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement fullyOverlappedElement = driver.findElement( By.id( "fully-overlapped-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( fullyOverlappedElement );
		rootElement.stream() //
				.findFirst() //
				.ifPresent( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "true" ) );
		assertThatCode( () -> fullyOverlappedElement.click() ).isInstanceOf( ElementClickInterceptedException.class );
	}

	@Test
	void partially_covered_element_should_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement partiallyOverlappedElementOnRight =
				driver.findElement( By.id( "partially-overlapped-element-right" ) );
		final WebElement partiallyOverlappedElementOnLeft =
				driver.findElement( By.id( "partially-overlapped-element-left" ) );
		final Set<RootElement> rootElementOne = recheckAdapter.convert( partiallyOverlappedElementOnRight );
		rootElementOne.stream().findFirst()
				.ifPresent( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "false" ) );
		final Set<RootElement> rootElementTwo = recheckAdapter.convert( partiallyOverlappedElementOnLeft );
		rootElementTwo.stream().findFirst()
				.ifPresent( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "false" ) );
		partiallyOverlappedElementOnRight.click();
		partiallyOverlappedElementOnLeft.click();
	}

	@Test
	void covered_by_child_element_should_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement coveredByChildElement = driver.findElement( By.id( "covered-by-child-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( coveredByChildElement );
		rootElement.stream().findFirst()
				.ifPresent( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "false" ) );
		coveredByChildElement.click();
	}

	@Test
	void non_overlapped_element_should_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement nonOverlappingElement = driver.findElement( By.id( "non-overlapping-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( nonOverlappingElement );
		rootElement.stream().findFirst()
				.ifPresent( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "false" ) );
		nonOverlappingElement.click();
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}
}
