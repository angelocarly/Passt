package com.realdolmen.passt.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.realdolmen.passt.domain.User;
import com.realdolmen.passt.dto.UserDto;
import com.realdolmen.passt.exception.EmailExistsException;
import com.realdolmen.passt.exception.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import com.realdolmen.passt.service.UserService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import javax.validation.Valid;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Angelo Carly
 *
 * Controller for user registration
 */
@Controller
@SpringBootApplication

public class RegistrationController
{

    Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private UserService userService;

    //Register a user
    @PostMapping("/register")
    public String register(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, Errors errors, Model model)
    {
        //Validate user
        if (!result.hasErrors())
        {
            try
            {
                userService.createUser(convertToEntity(userDto));
            } catch (UsernameExistsException e)
            {
                result.rejectValue("username", "usernameInUse");
            } catch (EmailExistsException e)
            {
                result.rejectValue("email", "emailInUse");
            }
        }
        if (result.hasErrors())
        {
            model.addAttribute("user", userDto);
            return "register";
        }

        //Succesful registration
        
        //If the user has 2FA enabled, redirect to the 2FA page
        User user = userService.getUser(userDto.getUsername());
        if (userDto.isUsing2FA())
        {
            try
            {
                model.addAttribute("qrcodebase64", generateQRCode(user.getSecret2FA(), user.getId(), "passt"));
            } catch (WriterException | IOException ex)
            {
                java.util.logging.Logger.getLogger(RegistrationController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "qrcode";
        }

        //Otherwise go directly to the homepage
        return "redirect:/client";
    }

    //Register form
    @GetMapping("/register")
    public ModelAndView index(Model model)
    {
        model.addAttribute("user", new UserDto());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    private User convertToEntity(UserDto dto)
    {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setUsername(dto.getUsername());
        user.setUsing2FA(dto.isUsing2FA());
        return user;
    }

    private String generateQRCode(String secret, UUID user, String issuer) throws WriterException, IOException
    {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(String.format("otpauth://totp/%s:%s?secret=%s&issuer=%s", issuer, user.toString(), secret, issuer), BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        byte[] encodeBase64 = Base64.encodeBase64(pngData);
        String base64Encoded = new String(encodeBase64, "UTF-8");
        return base64Encoded;
    }

}
