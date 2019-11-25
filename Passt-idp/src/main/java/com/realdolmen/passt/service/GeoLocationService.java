package com.realdolmen.passt.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import org.springframework.stereotype.Service;

/**
 *
 * @author Angelo Carly
 */
@Service
public class GeoLocationService
{

    public String fetchCity(String ip) throws IOException, GeoIp2Exception
    {
        File database = new File("src/main/resources/geolocation/GeoLite2-City.mmdb");
        DatabaseReader reader = new DatabaseReader.Builder(database).build();
        
        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = reader.city(ipAddress);
        
        StringBuilder cityResponse = new StringBuilder();
        cityResponse.append(response.getCountry().getIsoCode());
        cityResponse.append(" ");
        cityResponse.append(response.getCity().getName());
        
        return cityResponse.toString();
    }
    
}
