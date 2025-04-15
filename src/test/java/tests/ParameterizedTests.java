package tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class ParameterizedTests extends TestBase {


    @BeforeEach
    @DisplayName("Авторизация")
    void successfulLoginTest() {
        open("https://vkusvill.ru/");
        $x("//div[contains(@class, 'UniversMainIcBtn__Text') and normalize-space(text())='Войти']").click();
        $x("//input[@name='USER_PHONE']").setValue("9022772808");
        $x("//button[normalize-space()='Продолжить']").click();
        $x("//input[@name='SMS']").setValue("772808");
        $x("//div[@class='UniversMainIcBtn__Text btn_text _desktop-md' and normalize-space(text())='Кабинет']").click();
        $x("//div[@class='VV_PersonalSB20User']")
                .shouldHave(text("O914095"));
    }


    @ValueSource(strings = {
            "Холодец", "Водка"
    })
    @ParameterizedTest(name = "Тест на поиск товаров по поисковому запросу {0}")
    void searchProducts(String searchQuery) {
        $x("//input[@name='q']").setValue(searchQuery).pressEnter();
        $x("//div[@class='ProductCards__item ProductCards__item--col-lg-1-3']").shouldBe(visible);
    }

    @ValueSource(strings = {
            "Вино", "Хлеб", "Аминь"
    })
    @ParameterizedTest(name = "Тест на добавление товара в корзину из поисковой выдачи {0}")
    void addedProductToCartViaSearch(String searchQuery) {
        $x("//input[@name='q']").setValue(searchQuery).pressEnter();
        sleep(5000);
        $x("//div[@class='ProductCard__cartButton']").click();
        $x("//a[@title='Корзина']").click();
        executeJavaScript(
                "arguments[0].click();",
                $x("//a[contains(@class, 'js-delivery__basket--clear')]")
        );
        $x("//a[@id='js-lk-modal-confirm-callback']").click();
        sleep(5000);
        $x("//input[@name='q']").setValue(searchQuery).pressEnter();
        $x("//div[@class='ProductCard__cartButton']").click();
        $x("//a[@title='Корзина']").click();
        //Проверка на добавление товара
        $x("//span[@class='js-delivery__basket--totals_q']")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("1 товар"));
    }
}
