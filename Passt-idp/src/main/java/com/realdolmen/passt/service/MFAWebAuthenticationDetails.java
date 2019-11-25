package com.realdolmen.passt.service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author Angelo Carly
 * Class that stores the filled in MFA code to be verified
 */
public class MFAWebAuthenticationDetails extends WebAuthenticationDetails
{

    private String verificationCode;

    public MFAWebAuthenticationDetails(HttpServletRequest request)
    {
        super(request);
        verificationCode = request.getParameter("code");
    }

    public String getVerificationCode()
    {
        return verificationCode;
    }
}
