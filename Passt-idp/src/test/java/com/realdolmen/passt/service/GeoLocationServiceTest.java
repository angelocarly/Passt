/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.realdolmen.passt.service;

import com.maxmind.geoip2.exception.GeoIp2Exception;
import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author Angelo Carly
 * Test on GeoLocationService which returns a city based on IP.
 */
public class GeoLocationServiceTest
{

    private GeoLocationService geolocationService = new GeoLocationService();

    @Test(expected = GeoIp2Exception.class)
    public void testFetchCity_GivenEmptyString_ThrowsGeoIp2Exception() throws IOException, GeoIp2Exception
    {
        geolocationService.fetchCity("");
    }

    @Test(expected = GeoIp2Exception.class)
    public void testFetchCity_GivenLocalhost_ThrowsGeoIp2Exception() throws IOException, GeoIp2Exception
    {
        geolocationService.fetchCity("localhost");
    }
    
    @Test(expected = GeoIp2Exception.class)
    public void testFetchCity_GivenLoopback_ThrowsGeoIp2Exception() throws IOException, GeoIp2Exception
    {
        geolocationService.fetchCity("127.0.0.1");
    }
}
