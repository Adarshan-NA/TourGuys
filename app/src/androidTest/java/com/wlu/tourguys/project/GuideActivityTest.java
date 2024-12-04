package com.wlu.tourguys.project;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class GuideActivityTest {

    @Rule
    public ActivityScenarioRule<GuideActivity> activityScenarioRule =
            new ActivityScenarioRule<>(GuideActivity.class);

    @Test
    public void testValidateInput_validInput() {
        // 使用 ActivityScenario.launch() 启动 GuideActivity
        ActivityScenario.launch(GuideActivity.class).onActivity(activity -> {
            // 模拟有效的输入
            String location = "Toronto";
            String date = "2024-12-01";
            // 调用被测试的方法
            boolean result = activity.validateInput(location, date);
            assertTrue(result);
        });
    }
    @Test
    public void testValidateInput_invalidLocation() {
        // 使用 ActivityScenario.launch() 启动 GuideActivity
        ActivityScenario.launch(GuideActivity.class).onActivity(activity -> {
            // 模拟无效的地点输入
            String location = "";  // 空字符串作为无效地点
            String date = "2024-12-01";  // 合法日期

            // 调用被测试的方法
            boolean result = activity.validateInput(location, date);

            // 验证返回值是否为 false（无效地点）
            assertFalse(result);
        });
    }
    @Test
    public void testValidateInput_invalidDate() {
        // 使用 ActivityScenario.launch() 启动 GuideActivity
        ActivityScenario.launch(GuideActivity.class).onActivity(activity -> {
            // 模拟有效地点和无效日期输入
            String location = "Toronto";  // 合法地点
            String date = "invalid-date";  // 无效日期（非法格式）

            // 调用被测试的方法
            boolean result = activity.validateInput(location, date);

            // 验证返回值是否为 false（无效日期）
            assertFalse(result);
        });
    }
    @Test
    public void testValidateInput_validLocation_invalidDateFormat() {
        // 使用 ActivityScenario.launch() 启动 GuideActivity
        ActivityScenario.launch(GuideActivity.class).onActivity(activity -> {
            // 模拟有效地点和无效日期格式
            String location = "Toronto";  // 合法地点
            String date = "2024/12/01";  // 无效日期格式（假设 "2024-12-01" 是有效日期格式）

            // 调用被测试的方法
            boolean result = activity.validateInput(location, date);

            // 验证返回值是否为 false（无效日期格式）
            assertFalse(result);
        });
    }
}
