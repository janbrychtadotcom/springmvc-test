package com.test.mvc4test.portlet.controller;

import com.liferay.portletmvc4spring.context.PortletApplicationContextUtils;
import com.test.mvc4test.portlet.dto.User;

import com.liferay.portletmvc4spring.bind.annotation.ActionMapping;
import com.liferay.portletmvc4spring.bind.annotation.RenderMapping;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Locale;

import javax.portlet.*;

import com.test.share.SharedAppBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import test.builder.service.TestLocalServiceUtil;

/**
 * @author honza
 */
@Controller
@RequestMapping("VIEW")
public class UserController {

	@ModelAttribute("user")
	public User getUserModelAttribute() {
		return new User();
	}

	@RenderMapping
	public String prepareView(RenderRequest renderRequest) {


		PortletSession portletSession = renderRequest.getPortletSession();
		PortletContext portletContext = portletSession.getPortletContext();
		ApplicationContext applicationContext = PortletApplicationContextUtils.getRequiredWebApplicationContext(portletContext);
		SharedAppBean sharedAppBean = applicationContext.getBean("sharedAppBean", SharedAppBean.class);

		renderRequest.setAttribute("sharedBeanRandomInt", sharedAppBean.getId());
		renderRequest.setAttribute("testMethodValue", TestLocalServiceUtil.testMethod());


		return "mvc4test/user";
	}

	@RenderMapping(params = "javax.portlet.action=success")
	public String showGreeting(ModelMap modelMap) {

		DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy G");

		Calendar todayCalendar = Calendar.getInstance();

		modelMap.put("todaysDate", dateFormat.format(todayCalendar.getTime()));

		return "greeting";
	}

	@ActionMapping
	public void submitApplicant(
		@ModelAttribute("user") User user, BindingResult bindingResult,
		ModelMap modelMap, Locale locale, ActionResponse actionResponse,
		SessionStatus sessionStatus) {

		_localValidatorFactoryBean.validate(user, bindingResult);

		if (!bindingResult.hasErrors()) {
			if (_logger.isDebugEnabled()) {
				_logger.debug("firstName=" + user.getFirstName());
				_logger.debug("lastName=" + user.getLastName());
			}

			MutableRenderParameters mutableRenderParameters =
				actionResponse.getRenderParameters();

			mutableRenderParameters.setValue("javax.portlet.action", "success");

			sessionStatus.setComplete();
		}
		else {
			bindingResult.addError(
				new ObjectError(
					"user",
					_messageSource.getMessage(
						"please-correct-the-following-errors", null, locale)));
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		UserController.class);

	@Autowired
	private LocalValidatorFactoryBean _localValidatorFactoryBean;

	@Autowired
	private MessageSource _messageSource;

}