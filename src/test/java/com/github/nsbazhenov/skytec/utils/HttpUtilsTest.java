package com.github.nsbazhenov.skytec.utils;

import com.github.nsbazhenov.skytec.utils.HttpUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class HttpUtilsTest {

    @Test
    public void parseOneParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("id=123e4567-e89b-12d3-a456-426614174000");

        Assert.assertEquals("123e4567-e89b-12d3-a456-426614174000", parameters.get("id"));
    }

    @Test
    public void parseTwoParametersTest2() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("id=123e4567-e89b-12d3-a456-426614174000&param=sad");

        Assert.assertEquals("123e4567-e89b-12d3-a456-426614174000", parameters.get("id"));
        Assert.assertEquals("sad", parameters.get("param"));
    }

    @Test
    public void parseOneIncorrectParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("?id=123e4567-e89b-12d3-a456-426614174000&param=sad");

        Assert.assertNull(parameters.get("id"));
        Assert.assertEquals("sad", parameters.get("param"));
    }

    @Test
    public void parseTwIncorrectParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("?id=123e4567-e89b-12d3-a456-426614174000&?param=sad");

        Assert.assertNull(parameters.get("id"));
        Assert.assertNull(parameters.get("param"));
    }


    @Test
    public void parseParameterNullTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap(null);

        Assert.assertNull(parameters);
    }

    @Test
    public void parseParameterEmptyStringTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("");

        Assert.assertEquals("", parameters.get(""));
    }

    @Test
    public void parseParameterWithoutValueTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("param");

        Assert.assertEquals("", parameters.get("param"));
    }

    @Test
    public void parseEmptyParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("id=");

        Assert.assertEquals("", parameters.get("id"));
    }

    @Test
    public void parseOneAmpersandParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("&");

        Assert.assertNull( parameters.get("id"));
    }

    @Test
    public void parseTwoAmpersandParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("&&");

        Assert.assertNull( parameters.get("id"));
    }

//    @Test
//    public void parseOneEqualsParameterTest() {
//        Map<String, String> parameters = HttpUtils.queryParamsToMap("=");
//
//        Assert.assertNull(parameters.get("id"));
//    }
//
//    @Test
//    public void parseTwoEqualsParameterTest() {
//        Map<String, String> parameters = HttpUtils.queryParamsToMap("==");
//
//        Assert.assertNull( parameters.get("id"));
//    }

    @Test
    public void parseOneCorrectAndOneIncorrectParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("id==123e4567-e89b-12d3-a456-426614174000&param=sad");

        Assert.assertEquals("", parameters.get("id"));
        Assert.assertEquals("sad", parameters.get("param"));
    }

    @Test
    public void parseTwoIncorrectParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("id==123e4567-e89b-12d3-a456-426614174000&&param=sad");

        Assert.assertEquals("", parameters.get("id"));
        Assert.assertEquals("sad", parameters.get("param"));
    }

    @Test
    public void parseIncorrectFormatParameterTest() {
        Map<String, String> parameters = HttpUtils.queryParamsToMap("id=123e4567-e89b-12d3-a456-426614174000&&param=sad");

        Assert.assertEquals("123e4567-e89b-12d3-a456-426614174000", parameters.get("id"));
        Assert.assertEquals("sad", parameters.get("param"));
    }
}
