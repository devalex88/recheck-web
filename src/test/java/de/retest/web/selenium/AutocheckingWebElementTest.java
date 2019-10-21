package de.retest.web.selenium;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;

class AutocheckingWebElementTest {

	@Test
	void check_is_skiped_when_skipCheck_is_called() throws Exception {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );
		final AutocheckingWebElement wrapper = new AutocheckingWebElement( delegate, driver );

		wrapper.skipCheck().clear();
		verify( delegate, times( 1 ) ).clear();
		verify( driver, never() ).check( Mockito.any(), Mockito.any() );

		wrapper.skipCheck().submit();
		verify( delegate, times( 1 ) ).submit();
		verify( driver, never() ).check( Mockito.any(), Mockito.any() );

		wrapper.skipCheck().sendKeys();
		verify( delegate, times( 1 ) ).sendKeys();
		verify( driver, never() ).check( Mockito.any(), Mockito.any() );

		wrapper.skipCheck().click();
		verify( delegate, times( 1 ) ).click();
		verify( driver, never() ).check( Mockito.any(), Mockito.any() );

	}

	@Test
	void changing_methods_should_check_and_delegate_calls() {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );
		final AutocheckingWebElement wrapper = new AutocheckingWebElement( delegate, driver );

		wrapper.clear();
		verify( delegate, times( 1 ) ).clear();
		verify( driver, times( 1 ) ).check( Mockito.any(), Mockito.any() );

		wrapper.submit();
		verify( delegate, times( 1 ) ).submit();
		verify( driver, times( 2 ) ).check( Mockito.any(), Mockito.any() );

		wrapper.sendKeys();
		verify( delegate, times( 1 ) ).sendKeys();
		verify( driver, times( 3 ) ).check( Mockito.any(), Mockito.any() );

		wrapper.click();
		verify( delegate, times( 1 ) ).click();
		verify( driver, times( 4 ) ).check( Mockito.any(), Mockito.any() );
	}

	@Test
	void other_methods_should_delegate_calls() {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingWebElement wrapper =
				new AutocheckingWebElement( delegate, mock( AutocheckingRecheckDriver.class ) );

		wrapper.getAttribute( "test" );
		verify( delegate, times( 1 ) ).getAttribute( "test" );

		wrapper.getCssValue( "css" );
		verify( delegate, times( 1 ) ).getCssValue( "css" );

		wrapper.getLocation();
		verify( delegate, times( 1 ) ).getLocation();

		wrapper.getRect();
		verify( delegate, times( 1 ) ).getRect();

		wrapper.getSize();
		verify( delegate, times( 1 ) ).getSize();

		wrapper.getTagName();
		verify( delegate, times( 1 ) ).getTagName();

		wrapper.getText();
		verify( delegate, times( 1 ) ).getText();

		wrapper.isDisplayed();
		verify( delegate, times( 1 ) ).isDisplayed();

		wrapper.isEnabled();
		verify( delegate, times( 1 ) ).isEnabled();

		wrapper.isSelected();
		verify( delegate, times( 1 ) ).isSelected();
	}
}
