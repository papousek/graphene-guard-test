package pl.itcrowd.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.guard.RequestGuardException;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pl.itcrowd.richfaces.PanelBean;
import pl.itcrowd.richfaces.PollBean;

import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
public class GuardTest {

    private static final String WEBAPP_SRC = "src/main/webapp";

    @Drone
    WebDriver browser;

    @ArquillianResource
    URL deploymentUrl;

    @FindBy(id = "f:btn1")
    private WebElement btn1;

    @FindBy(id = "f:btn2")
    private WebElement btn2;

    @FindBy(id = "f:inputA")
    private WebElement inputA;

    @FindBy(id = "f:inputB")
    private WebElement inputB;

    @FindBy(id = "f:serverDate")
    private WebElement serverDate;

    @Deployment(testable = false)
    public static WebArchive createDeployment()
    {

        File[] libs = Maven.resolver()
            .loadPomFromFile("pom.xml")
            .resolve("org.jboss.spec.javax.el:jboss-el-api_2.2_spec:1.0.2.Final", "org.richfaces.core:richfaces-core-api:4.3.0.Final",
                "org.richfaces.core:richfaces-core-impl:4.3.0.Final", "org.richfaces.ui:richfaces-components-api:4.3.0.Final",
                "org.richfaces.ui:richfaces-components-ui:4.3.0.Final", "org.jboss.spec.javax.faces:jboss-jsf-api_2.1_spec:2.0.1.Final",
                "com.sun.faces:jsf-impl:2.1.7-jbossorg-2")
            .withTransitivity()
            .asFile();


        return ShrinkWrap.create(WebArchive.class, "guard-test.war")
            .addClasses(PanelBean.class, PollBean.class)
            .addAsLibraries(libs)
            .merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory(WEBAPP_SRC).as(GenericArchive.class), "/",
                Filters.includeAll());
    }

    /**
     * This test shows that if user forgets to use guard, another guards fail
     */
    @Test
    public void firstTest()
    {
        browser.get(deploymentUrl + "noPoll.jsf");

        guardAjax(btn1).click();
        btn2.click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
    }

    @Test
    public void onChange()
    {
        browser.get(deploymentUrl + "noPoll.jsf");

        try {
            guardAjax(inputA).sendKeys("Any");
        } catch (RequestGuardException e) {
            fail("Unexpected RequestGuardException");
        }

        inputB.sendKeys("Anything");
        guardAjax(serverDate).click(); //blur
    }

    @Test
    public void onChangeWithReload()
    {
        browser.get(deploymentUrl + "noPoll.jsf");

        try {
            guardAjax(inputA).sendKeys("Any");
        } catch (RequestGuardException e) {
            fail("Unexpected RequestGuardException");
        }

        browser.get(deploymentUrl + "noPoll.jsf");

        inputB.sendKeys("Anything");
        guardAjax(serverDate).click(); //blur
    }

    /**
     * This test shows that if are Xhr requests in the background (using a4j:poll)
     * guards sometime fail.
     * <p/>
     * I also joined to project screen shot with strange exception.
     */
    @Test
    public void secondTest()
    {
        browser.get(deploymentUrl + "withPoll.jsf");

        guardAjax(btn1).click();
        guardAjax(btn2).click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
        guardAjax(btn1).click();
        guardAjax(btn2).click();
    }

    @Test
    public void slowRequest()
    {
        browser.get(deploymentUrl + "buttons.jsf");

        guardAjax(btn1).click();
    }

    @Test
    public void a4jQueueRequestDelay()
    {
        browser.get(deploymentUrl + "buttons.jsf");

        guardAjax(btn2).click();
    }
}