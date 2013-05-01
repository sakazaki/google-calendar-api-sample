package jp.bizreach.sample.googlecalendarapi.action;

import java.io.IOException;
import java.util.List;

import jp.bizreach.sample.googlecalendarapi.component.GoogleApiComponent;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.services.calendar.model.CalendarListEntry;
import com.opensymphony.xwork2.ActionSupport;

@Namespace("")
public class SampleCalendarAction extends ActionSupport {

	@Autowired
	private GoogleApiComponent apiComponent;
	
	private List<CalendarListEntry> entryList;

	@Action(value = "", results = { @Result(location = "sample_calendar.vm") })
	public String index() {

		try {
			entryList = apiComponent.getCalendar();
		} catch (IOException e) {
			return ERROR;
		}

		return SUCCESS;
	}

	/**
	 * entryListを取得します。
	 * @return entryList
	 */
	public List<CalendarListEntry> getEntryList() {
	    return entryList;
	}

}
