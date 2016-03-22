package com.jx.retrofitlearn.converter.string;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by jiang.xu on 2016/3/1.
 */
final class StringResponseBodyConverter implements Converter<ResponseBody, String> {


    @Override
    public String convert(ResponseBody value) throws IOException {
        try {
            String s = value.string();
            return s;
        } finally {
            value.close();
        }
    }
}