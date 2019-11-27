package com.kubara.michal.inzynierka.adminpanel.security;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private MessageSource messages;
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		 
			setDefaultFailureUrl("/showLoginPage?error");
		 
			super.onAuthenticationFailure(request, response, exception);
		
	        Locale locale = request.getLocale();
	 
	        String errorMessage = "Błąd logowania";
	        
	        System.out.println(exception.getMessage());
	        
	        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
	            errorMessage = messages.getMessage("auth.message.disabled", null, locale);
	            //String resendMsg = messages.getMessage("auth.message.resendActivationLink", null, locale);
	            //request.getSession().setAttribute("RESEND_ACTIVATION_LINK", resendMsg);
	        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
	            errorMessage = messages.getMessage("auth.message.expired", null, locale);
	        } else if (exception.getMessage().equalsIgnoreCase("Bad credentials")) {
	        	errorMessage = messages.getMessage("message.badCredentials", null, locale);
	        } else if ( exception.getMessage().equalsIgnoreCase("User credentials have expired")) {
	        	errorMessage = messages.getMessage("auth.message.notVerified", null, locale);
	        }
	 
	        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);

	}

}
