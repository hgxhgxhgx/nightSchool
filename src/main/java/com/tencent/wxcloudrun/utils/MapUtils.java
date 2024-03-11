package com.tencent.wxcloudrun.utils;

import org.apache.commons.lang3.StringUtils;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Arrays;

@Service
public class MapUtils {





    //point格式 123,234  或  (123,234)
    public static   String calDistance(String point1, String point2){
        String point1Format = StringUtils.strip(point1, "()");
        String point2Format = StringUtils.strip(point2,"()");
        GeodeticCurve geodeticCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere,
                new GlobalCoordinates(pointStrToDouble(point1Format)[0],
                        pointStrToDouble(point1Format)[1]),
                new GlobalCoordinates(pointStrToDouble(point2Format)[0],
                        pointStrToDouble(point2Format)[1]));
        return String.valueOf(geodeticCurve.getEllipsoidalDistance());

    }

    private static double[] pointStrToDouble(String point){
        String[] split = point.split(",", 2);
        return Arrays.stream(split).mapToDouble(Double::parseDouble).toArray();

    }
}
