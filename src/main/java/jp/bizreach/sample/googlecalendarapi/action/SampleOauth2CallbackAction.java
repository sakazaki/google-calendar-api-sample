package jp.bizreach.sample.googlecalendarapi.action;

import javax.servlet.http.HttpServletRequest;

import jp.bizreach.sample.googlecalendarapi.component.GoogleApiComponent;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("")
public class SampleOauth2CallbackAction extends ActionSupport {

	@Autowired
	private GoogleApiComponent apiComponent;

	@Autowired
	private HttpServletRequest request;

	@Action(value = "oauth2callback", results = { @Result(location = "sample_oauth2.vm") })
	public String index() {
		apiComponent.receiveOauth2Callback(request);
		return SUCCESS;
	}
}