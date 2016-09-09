import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.AndroidDriver;
import static org.junit.Assert.assertTrue;
import org.openqa.selenium.WebElement;
import org.junit.Before;
import org.junit.After;
import java.util.List;
import org.junit.Test;
import java.io.File;
import java.net.URL;


/**
 * Created by Alex on 9/9/16.
 */

public class AudioTest {

    private AndroidDriver driver;

    @Before
    public void setup() throws Exception {
        File appDir = new File("src/test/resources/apk");
        File app = new File(appDir, "vk.apk");
        DesiredCapabilities caps = new DesiredCapabilities();
        //my test device hardcoded. Run "adb devices" and put your device name there
        caps.setCapability("deviceName", "02157df2add15b04");
        caps.setCapability("platformName", "Android");
        caps.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    //Test is running on English locale
    @Test
    public void VKAudioTest() throws InterruptedException {

        //using creds of test account to log in
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/auth_login\")").sendKeys("+79218780717");
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/auth_pass\")").sendKeys("1234123412");
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/auth_login_btn\")").click();
        Thread.sleep(5000);

        //going into Menu > Music
        WebElement MenuButton = (WebElement) driver.findElementsByAndroidUIAutomator("new UiSelector().className(\"android.widget.ImageButton\")").get(0);
        MenuButton.click();
        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Music\")").click();
        Thread.sleep(2000);

        //My music should be opened by default
        WebElement DefaultAlbumName = driver.findElementByAndroidUIAutomator("new UiSelector().text(\"My music\")");
        assertTrue(DefaultAlbumName.isDisplayed());

        //default songs from test account should be on screen
        List MoscowsOnScreenNotPlaying = driver.findElementsByAndroidUIAutomator("new UiSelector().text(\"Moscow\")");
        assertTrue(MoscowsOnScreenNotPlaying.size() == 1);

        //let's play a song
        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Moscow\")").click();
        Thread.sleep(2000);

        //now song name should be twice on screen
        List MoscowsOnScreenPlaying = driver.findElementsByAndroidUIAutomator("new UiSelector().text(\"Moscow\")");
        assertTrue(MoscowsOnScreenPlaying.size() == 2);

        //let's hit Pause and open fullscreen audio player by hitting cover pic
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/audio_panel_play\")").click();
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/audio_panel_cover\")").click();

        //checking major UI elements. Play button should be in "Play" state since we paused a song
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_play\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_progress\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_prev\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_next\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_playlist\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_broadcast\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_shuffle\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_repeat\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_menu\")").isDisplayed());
        assertTrue(driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/aplayer_play\")").isDisplayed());

        //let's give a song enough time to be fully downloaded and go back from player
        Thread.sleep(25000);
        driver.pressKeyCode(AndroidKeyCode.BACK);

        //now we'll check dropdown items. Let's hit Recommended and choose it
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/text1\")").click();
        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Recommendations\")").click();
        Thread.sleep(5000);

        //5 seconds should be enough to show at least 4 recommended songs on screen
        List RecommendedSongs = driver.findElementsByAndroidUIAutomator("new UiSelector().resourceId(\"com.vkontakte.android:id/audio_title\")");
        assertTrue(RecommendedSongs.size() > 3);

        //now let's go to Saved from dropdown
        driver.findElementByAndroidUIAutomator("new UiSelector().resourceId(\"android:id/text1\")").click();
        driver.findElementByAndroidUIAutomator("new UiSelector().text(\"Saved\")").click();
        Thread.sleep(2000);

        //we expect Moscow song to be among Saved items. We gave 30+ seconds to be downloaded when song was in player, this should be enough
        List MoscowsOnSavedScreen = driver.findElementsByAndroidUIAutomator("new UiSelector().text(\"Moscow\")");
        assertTrue(MoscowsOnSavedScreen.size() == 2);

        driver.quit();
    }
}