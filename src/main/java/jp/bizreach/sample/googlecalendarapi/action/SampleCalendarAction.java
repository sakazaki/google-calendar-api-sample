package jp.bizreach.sample.googlecalendarapi.action;

import java.io.IOException;

import jp.bizreach.sample.googlecalendarapi.component.GoogleApiComponent;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

@Namespace("")
public class SampleCalendarAction extends ActionSupport {

	@Autowired
	private GoogleApiComponent apiComponent;

	@Action(value = "", results = { @Result(location = "sample_calendar.vm") })
	public String index() {

		try {
			apiComponent.getCalendar();
		} catch (IOException e) {
			return ERROR;
		}

		return SUCCESS;
	}

}
