package com.bigv.app.yocc.controller;

import android.content.Context;
import android.util.Log;

import com.bigv.app.yocc.pojo.AddressBookPojo;
import com.bigv.app.yocc.pojo.AgentMasterPojo;
import com.bigv.app.yocc.pojo.AgentReplacerPojo;
import com.bigv.app.yocc.pojo.CallBlockPojo;
import com.bigv.app.yocc.pojo.CallDetailsPojo;
import com.bigv.app.yocc.pojo.CallDetailsSummeryPojo;
import com.bigv.app.yocc.pojo.CallPriorityPojo;
import com.bigv.app.yocc.pojo.CallTranscriptionPojo;
import com.bigv.app.yocc.pojo.CallifyPojo;
import com.bigv.app.yocc.pojo.ChangePasswordPojo;
import com.bigv.app.yocc.pojo.GroupAddressBookPojo;
import com.bigv.app.yocc.pojo.HomeScreenPojo;
import com.bigv.app.yocc.pojo.HourWiseCallPojo;
import com.bigv.app.yocc.pojo.LanguagePojo;
import com.bigv.app.yocc.pojo.LiveCallPojo;
import com.bigv.app.yocc.pojo.LoginPojo;
import com.bigv.app.yocc.pojo.MenuMasterPojo;
import com.bigv.app.yocc.pojo.MonthWiseCallPojo;
import com.bigv.app.yocc.pojo.PasswordCheckPojo;
import com.bigv.app.yocc.pojo.RemarkPojo;
import com.bigv.app.yocc.pojo.RemarkResponsePojo;
import com.bigv.app.yocc.pojo.ReportCallListAndSummeryPojo;
import com.bigv.app.yocc.pojo.ResultPojo;
import com.bigv.app.yocc.pojo.RoutingPatternPojo;
import com.bigv.app.yocc.pojo.RoutingTypePojo;
import com.bigv.app.yocc.pojo.WeekWiseCallDurationPojo;
import com.bigv.app.yocc.pojo.WeekWiseCallPojo;
import com.bigv.app.yocc.utils.AUtils;
import com.bigv.app.yocc.webservices.ActionWebservices;
import com.bigv.app.yocc.webservices.AddressBookWebservices;
import com.bigv.app.yocc.webservices.CallDetailWebservice;
import com.bigv.app.yocc.webservices.HomeScreenWebservices;
import com.bigv.app.yocc.webservices.LoginWebservices;
import com.bigv.app.yocc.webservices.RemarkApi;
import com.bigv.app.yocc.webservices.ReportWebservice;
import com.bigv.app.yocc.webservices.SettingsWebservices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import quickutils.core.QuickUtils;
import retrofit2.Call;

/**
 * Created by mithun on 11/9/17.
 */

public class SyncServer {

    private static final String TAG = "SyncServer";
    private final Gson gson;
    private Context context;

    public SyncServer(Context context) {
        this.context = context;
        gson = new Gson();
    }

    public LoginPojo loginUser(String username, String password) {

        LoginPojo loginPojo = null;
        try {

            LoginWebservices service = AUtils.createService(LoginWebservices.class, AUtils.SERVER_URL);

            loginPojo = service.loginUser(username, password).execute().body();
            if (!AUtils.isNull(loginPojo)) {
                QuickUtils.prefs.save(AUtils.KEY, loginPojo.getKey());
            }
        } catch (Exception e) {
            loginPojo = null;
        }
        return loginPojo;
    }


    public HomeScreenPojo getHomeScreenItems() {

        HomeScreenPojo homeScreenPojo = null;
        try {

            HomeScreenWebservices service = AUtils.createService(HomeScreenWebservices.class, AUtils.SERVER_URL);

            homeScreenPojo = service.getHomeScreenData(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1"),
                    QuickUtils.prefs.getString(AUtils.USERNAME, ""),
                    QuickUtils.prefs.getString(AUtils.PASSWORD, "")).execute().body();

            if (!AUtils.isNull(homeScreenPojo)) {

                QuickUtils.prefs.save(AUtils.HOME_SCREEN_POJO, gson.toJson(homeScreenPojo));
            }
        } catch (Exception e) {
            homeScreenPojo = null;
        }
        return homeScreenPojo;
    }

    public boolean getCallDetailsList(String fdate, String tdate) {

        List<CallDetailsPojo> callDetailsPojoList = null;
        try {

            CallDetailWebservice service = AUtils.createService(CallDetailWebservice.class, AUtils.SERVER_URL);
            callDetailsPojoList = service.getCallDetailsList(
                    QuickUtils.prefs.getString(AUtils.KEY, ""), fdate, tdate,
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1")).execute().body();

            if (!AUtils.isNull(callDetailsPojoList) && !callDetailsPojoList.isEmpty()) {

                Type type = new TypeToken<List<CallDetailsPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.CALL_DETAILS_POJO_LIST,
                        gson.toJson(callDetailsPojoList, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.CALL_DETAILS_POJO_LIST, null);
                return false;
            }
        } catch (Exception e) {

            callDetailsPojoList = null;
        }
        return false;
    }

    public boolean getCallDetailsSummery(String fdate, String tdate) {

        CallDetailsSummeryPojo callDetailsSummeryPojo = null;
        try {

            CallDetailWebservice service = AUtils.createService(CallDetailWebservice.class, AUtils.SERVER_URL);
            callDetailsSummeryPojo = service.getCallDetailsSummery(
                    QuickUtils.prefs.getString(AUtils.KEY, ""), fdate, tdate,
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1")).execute().body();

            if (!AUtils.isNull(callDetailsSummeryPojo)) {

                QuickUtils.prefs.save(AUtils.CALL_DETAILS_SUMMERY_POJO,
                        gson.toJson(callDetailsSummeryPojo, CallDetailsSummeryPojo.class));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.CALL_DETAILS_SUMMERY_POJO, null);
                return false;
            }
        } catch (Exception e) {

            callDetailsSummeryPojo = null;
        }

        return false;
    }

    public boolean getWeekWiseCallDuration() {

        List<WeekWiseCallDurationPojo> wiseCallDurationPojoList = null;
        try {

            HomeScreenWebservices service = AUtils.createService(HomeScreenWebservices.class, AUtils.SERVER_URL);
            wiseCallDurationPojoList = service.getWeekWiseCallDuration(
                    QuickUtils.prefs.getString(AUtils.KEY, ""),
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1")).execute().body();

            if (!AUtils.isNull(wiseCallDurationPojoList) && !wiseCallDurationPojoList.isEmpty()) {

                Type type = new TypeToken<List<WeekWiseCallDurationPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.WEEK_WISE_CALL_DURATION_LIST,
                        gson.toJson(wiseCallDurationPojoList, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.WEEK_WISE_CALL_DURATION_LIST, null);
                return false;
            }

        } catch (Exception e) {

            wiseCallDurationPojoList = null;
        }

        return false;
    }

    public boolean getWeekWiseCall() {

        List<WeekWiseCallPojo> weekWiseCallPojoList = null;
        try {

            HomeScreenWebservices service = AUtils.createService(HomeScreenWebservices.class, AUtils.SERVER_URL);
            weekWiseCallPojoList = service.getWeekWiseCall(
                    QuickUtils.prefs.getString(AUtils.KEY, ""),
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1")).execute().body();

            if (!AUtils.isNull(weekWiseCallPojoList) && !weekWiseCallPojoList.isEmpty()) {

                Type type = new TypeToken<List<WeekWiseCallPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.WEEK_WISE_CALL_LIST,
                        gson.toJson(weekWiseCallPojoList, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.WEEK_WISE_CALL_LIST, null);
                return false;
            }
        } catch (Exception e) {

            weekWiseCallPojoList = null;
        }

        return false;
    }

    public boolean getMonthWiseCall() {

        List<MonthWiseCallPojo> monthWiseCallPojoList = null;
        try {

            HomeScreenWebservices service = AUtils.createService(HomeScreenWebservices.class, AUtils.SERVER_URL);
            monthWiseCallPojoList = service.getMonthWiseCall(
                    QuickUtils.prefs.getString(AUtils.KEY, ""),
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1")).execute().body();

            if (!AUtils.isNull(monthWiseCallPojoList) && !monthWiseCallPojoList.isEmpty()) {

                Type type = new TypeToken<List<MonthWiseCallPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.MONTH_WISE_CALL_LIST,
                        gson.toJson(monthWiseCallPojoList, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.MONTH_WISE_CALL_LIST, null);
                return true;
            }
        } catch (Exception e) {

            monthWiseCallPojoList = null;
        }

        return false;
    }

    public boolean getHourWiseCall() {

        List<HourWiseCallPojo> hourWiseCallPojoList = null;
        try {

            HomeScreenWebservices service = AUtils.createService(HomeScreenWebservices.class, AUtils.SERVER_URL);
            hourWiseCallPojoList = service.getHourWiseCall(
                    QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(hourWiseCallPojoList) && !hourWiseCallPojoList.isEmpty()) {

                Type type = new TypeToken<List<HourWiseCallPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.MONTH_WISE_CALL_LIST,
                        gson.toJson(hourWiseCallPojoList, type));
                return true;
            }
        } catch (Exception e) {

            hourWiseCallPojoList = null;
        }

        return false;
    }


    public ResultPojo changePassword(ChangePasswordPojo changePasswordPojo) {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.changePassword(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    changePasswordPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo callBlock(CallBlockPojo callBlockPojo) {
        ResultPojo result = null;
        try {

            ActionWebservices service = AUtils.createService(ActionWebservices.class, AUtils.SERVER_URL);
            result = service.callBlock(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    callBlockPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public boolean getCallPriorityList() {

        List<CallPriorityPojo> callPriorityPojoList = null;
        try {

            CallDetailWebservice service = AUtils.createService(CallDetailWebservice.class, AUtils.SERVER_URL);
            callPriorityPojoList = service.getCallPriorityList().execute().body();

            if (!AUtils.isNull(callPriorityPojoList) && !callPriorityPojoList.isEmpty()) {

                Type type = new TypeToken<List<CallPriorityPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.CALL_PRIORITY_LIST,
                        gson.toJson(callPriorityPojoList, type));
                return true;
            }
        } catch (Exception e) {

            callPriorityPojoList = null;
        }
        return false;
    }

    public boolean getAgentMasterList() {

        List<AgentMasterPojo> agentMasterPojoList = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            agentMasterPojoList = service.getAgentMasterList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(agentMasterPojoList) && !agentMasterPojoList.isEmpty()) {

                Type type = new TypeToken<List<AgentMasterPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.AGENT_MASTER_LIST,
                        gson.toJson(agentMasterPojoList, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.AGENT_MASTER_LIST, null);
            }
        } catch (Exception e) {

            agentMasterPojoList = null;
        }
        return false;
    }

    public ResultPojo saveAgentMaster(AgentMasterPojo agentMasterPojo) {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.saveAgentMaster(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    agentMasterPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo deleteAgent(AgentMasterPojo agentMasterPojo) {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.deleteAgent(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    agentMasterPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public boolean getAgentReplacerList() {

        List<AgentReplacerPojo> agentReplacerPojoList = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            agentReplacerPojoList = service.getAgentReplacerList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(agentReplacerPojoList) && !agentReplacerPojoList.isEmpty()) {

                Type type = new TypeToken<List<AgentReplacerPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.AGENT_REPLACER_LIST,
                        gson.toJson(agentReplacerPojoList, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.AGENT_REPLACER_LIST, null);
            }
        } catch (Exception e) {

            agentReplacerPojoList = null;
        }
        return false;
    }

    public ResultPojo saveAgentReplacer(AgentReplacerPojo agentReplacerPojo) {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.saveAgentReplacer(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    agentReplacerPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo deleteAgentReplacer(AgentReplacerPojo agentReplacerPojo) {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.deleteAgentReplacer(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    agentReplacerPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public boolean getMenuList() {

        List<MenuMasterPojo> menuMasterPojoList = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            menuMasterPojoList = service.getMenuMasterList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(menuMasterPojoList) && !menuMasterPojoList.isEmpty()) {

                Type type = new TypeToken<List<MenuMasterPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.MENU_MASTER_LIST,
                        gson.toJson(menuMasterPojoList, type));
                return true;
            }
        } catch (Exception e) {

            menuMasterPojoList = null;
        }
        return false;
    }

    public List<RemarkResponsePojo> getRemarkFeedLIst(){
        ArrayList<RemarkResponsePojo> remarkNotificationList = null;
        try {
            RemarkApi remarkApi = AUtils.createService(RemarkApi.class, AUtils.SERVER_URL);
            remarkNotificationList = remarkApi.remarkNotificationList(
                    QuickUtils.prefs.getString(AUtils.KEY, ""),
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0")).execute().body();

            if (!AUtils.isNull(remarkNotificationList)) {
                return remarkNotificationList;
            }
        }catch (Exception e){
            remarkNotificationList = null;
        }
        return remarkNotificationList;
    }

    public ResultPojo saveMenuMaster(MenuMasterPojo menuMasterPojo) {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.saveMenuMaster(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    menuMasterPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public boolean getRoutindPatternList() {

        List<RoutingPatternPojo> routingPatternPojoList = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            routingPatternPojoList = service.getRoutingPatternList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(routingPatternPojoList) && !routingPatternPojoList.isEmpty()) {

                Type type = new TypeToken<List<RoutingPatternPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.ROUTING_PATTERN_LIST,
                        gson.toJson(routingPatternPojoList, type));
                return true;
            }
        } catch (Exception e) {

            routingPatternPojoList = null;
        }
        return false;
    }

    public boolean getRoutindTypeList() {

        List<RoutingTypePojo> routingTypePojoList = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            routingTypePojoList = service.getRoutingTypeList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(routingTypePojoList) && !routingTypePojoList.isEmpty()) {

                Type type = new TypeToken<List<RoutingTypePojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.ROUTING_TYPE_LIST,
                        gson.toJson(routingTypePojoList, type));
                return true;
            }
        } catch (Exception e) {

            routingTypePojoList = null;
        }
        return false;
    }

    public boolean getLanguageList() {

        List<LanguagePojo> languagePojoList = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            languagePojoList = service.getLanguageList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(languagePojoList) && !languagePojoList.isEmpty()) {

                Type type = new TypeToken<List<LanguagePojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.LANGUAGE_LIST,
                        gson.toJson(languagePojoList, type));
                return true;
            }
        } catch (Exception e) {

            languagePojoList = null;
        }
        return false;
    }

    public ResultPojo saveCallDetails(CallDetailsPojo callDetailsPojo) {
        ResultPojo result = null;
        try {

            CallDetailWebservice service = AUtils.createService(CallDetailWebservice.class, AUtils.SERVER_URL);
            result = service.saveCallDetails(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    callDetailsPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo getFileDownloadData(String cdTrNo) {
        ResultPojo resultPojo = null;

        try {

            ActionWebservices service = AUtils.createService(ActionWebservices.class, AUtils.SERVER_URL);
            resultPojo = service.getFileDownloadData(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    cdTrNo).execute().body();

        } catch (Exception e) {

            resultPojo = null;
        }
        return resultPojo;
    }

    public ResultPojo callIVR(CallifyPojo callifyPojo) {
        ResultPojo resultPojo = null;

        try {

            ActionWebservices service = AUtils.createService(ActionWebservices.class, AUtils.SERVER_URL);
            resultPojo = service.callIVR(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    callifyPojo).execute().body();

        } catch (Exception e) {
            resultPojo = null;
        }
        return resultPojo;
    }

    public List<LiveCallPojo> getLiveCallList() {

        List<LiveCallPojo> liveCallPojoList = null;
        try {

            HomeScreenWebservices service = AUtils.createService(HomeScreenWebservices.class, AUtils.SERVER_URL);
            liveCallPojoList = service.getLiveCallList(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    QuickUtils.prefs.getString(AUtils.AGENT_ID, "0"),
                    QuickUtils.prefs.getString(AUtils.USER_TYPE_ID, "1")).execute().body();
//            liveCallPojoList = service.getLiveCallList("4558").execute().body();

            if (!AUtils.isNull(liveCallPojoList) && !liveCallPojoList.isEmpty()) {

                Type type = new TypeToken<List<LiveCallPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.LIVE_CALL_LIST,
                        gson.toJson(liveCallPojoList, type));
                return liveCallPojoList;
            } else {
                QuickUtils.prefs.save(AUtils.LIVE_CALL_LIST, null);
            }
        } catch (Exception e) {

            liveCallPojoList = null;
        }
        return null;
    }

    public List<CallTranscriptionPojo> getCallTranscriptionList(String cdTrNo) {

        List<CallTranscriptionPojo> callTranscriptionPojoList = null;
        try {

            ActionWebservices service = AUtils.createService(ActionWebservices.class, AUtils.SERVER_URL);
            callTranscriptionPojoList = service.getCallTranscriptionList(QuickUtils.prefs.getString(AUtils.KEY, ""), cdTrNo).execute().body();

            if (!AUtils.isNull(callTranscriptionPojoList) && !callTranscriptionPojoList.isEmpty()) {

                Type type = new TypeToken<List<CallDetailsPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.CALL_TRANSCRIPTION_LIST,
                        gson.toJson(callTranscriptionPojoList, type));
                return callTranscriptionPojoList;
            }
        } catch (Exception e) {

            callTranscriptionPojoList = null;
        }
        return null;
    }

    public boolean getAddressBookList() {

        List<AddressBookPojo> addressBookList = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            addressBookList = service.getAddressBookList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(addressBookList) && !addressBookList.isEmpty()) {

                Type type = new TypeToken<List<CallDetailsPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.ADDRESS_BOOK_LIST,
                        gson.toJson(addressBookList, type));
                return true;
            }
        } catch (Exception e) {

            addressBookList = null;
        }
        return false;
    }

    public ResultPojo deleteAddress(AddressBookPojo addressBookPojo) {

        ResultPojo result = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            result = service.deleteAddressBook(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    addressBookPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo saveAddressBook(AddressBookPojo addressBookPojo) {
        ResultPojo result = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            result = service.saveAddressBook(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    addressBookPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public List<RemarkPojo> getRemarkList(String callerNumber) {

        List<RemarkPojo> remarkPojoList = null;
        try {

            CallDetailWebservice service = AUtils.createService(CallDetailWebservice.class, AUtils.SERVER_URL);
            remarkPojoList = service.getCallRemarkList(QuickUtils.prefs.getString(AUtils.KEY, ""), callerNumber).execute().body();

            if (!AUtils.isNull(remarkPojoList) && !remarkPojoList.isEmpty()) {

                Type type = new TypeToken<List<RemarkPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.CLL_REMARK_LIST,
                        gson.toJson(remarkPojoList, type));
                return remarkPojoList;
            }
        } catch (Exception e) {

            remarkPojoList = null;
        }
        return remarkPojoList;
    }

    public boolean getReportCallDetailsList(String date) {

        ReportCallListAndSummeryPojo reportCallListAndSummeryPojo = null;
        try {

            ReportWebservice service = AUtils.createService(ReportWebservice.class, AUtils.SERVER_URL);
            reportCallListAndSummeryPojo = service.getReportDailyCallDetailsList(
                    QuickUtils.prefs.getString(AUtils.KEY, ""), date).execute().body();

            if (!AUtils.isNull(reportCallListAndSummeryPojo)) {

                Type type = new TypeToken<ReportCallListAndSummeryPojo>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.DAY_WISE_REPORT_CALL_DETAILS,
                        gson.toJson(reportCallListAndSummeryPojo, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.DAY_WISE_REPORT_CALL_DETAILS, null);
                return false;
            }
        } catch (Exception e) {

            reportCallListAndSummeryPojo = null;
        }
        return false;
    }

    public boolean getReportWeeklyCallDetailsList() {

        ReportCallListAndSummeryPojo reportCallListAndSummeryPojo = null;
        try {

            ReportWebservice service = AUtils.createService(ReportWebservice.class, AUtils.SERVER_URL);
            reportCallListAndSummeryPojo = service.getReportWeeklyCallDetailsList(
                    QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(reportCallListAndSummeryPojo)) {

                Type type = new TypeToken<ReportCallListAndSummeryPojo>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.WEEK_WISE_REPORT_CALL_DETAILS,
                        gson.toJson(reportCallListAndSummeryPojo, type));
                return true;
            } else {
                QuickUtils.prefs.save(AUtils.WEEK_WISE_REPORT_CALL_DETAILS, null);
                return false;
            }
        } catch (Exception e) {

            reportCallListAndSummeryPojo = null;
        }
        return false;
    }

    public boolean getGroupAddressBookList() {

        List<GroupAddressBookPojo> groupAddressBookPojoList = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            groupAddressBookPojoList = service.getGroupAddressBookList(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();

            if (!AUtils.isNull(groupAddressBookPojoList) && !groupAddressBookPojoList.isEmpty()) {

                Type type = new TypeToken<List<GroupAddressBookPojo>>() {
                }.getType();
                QuickUtils.prefs.save(AUtils.GROUP_ADDRESS_BOOK_LIST,
                        gson.toJson(groupAddressBookPojoList, type));
                return true;
            }
        } catch (Exception e) {

            groupAddressBookPojoList = null;
        }
        return false;
    }

    public ResultPojo deleteGroupAddress(GroupAddressBookPojo groupAddressBookPojo) {

        ResultPojo result = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            result = service.deleteGroupAddressBook(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    groupAddressBookPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo saveGroupAddressBook(GroupAddressBookPojo groupAddressBookPojo) {
        ResultPojo result = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            result = service.saveGroupAddressBook(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    groupAddressBookPojo).execute().body();

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadXlsFile(String groupId) {

        ResultPojo result = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            result = service.downloadXlsGroupAddressBookList(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    groupId).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadPdfFile(String groupId) {

        ResultPojo result = null;
        try {

            AddressBookWebservices service = AUtils.createService(AddressBookWebservices.class, AUtils.SERVER_URL);
            result = service.downloadPdfGroupAddressBookList(QuickUtils.prefs.getString(AUtils.KEY, ""),
                    groupId).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadAgentMasterXlsFile() {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.downloadXlsAgentMaster(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadAgentMasterPdfFile() {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.downloadPdfAgentMaster(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadMenuMasterXlsFile() {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.downloadXlsMenuMaster(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadMenuMasterPdfFile() {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.downloadPdfMenuMaster(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadAgentReplacerXlsFile() {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.downloadXlsAgentReplacer(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }

    public ResultPojo downloadAgentReplacerPdfFile() {

        ResultPojo result = null;
        try {

            SettingsWebservices service = AUtils.createService(SettingsWebservices.class, AUtils.SERVER_URL);
            result = service.downloadPdfAgentReplacer(QuickUtils.prefs.getString(AUtils.KEY, "")).execute().body();
            Log.e(TAG, "" + result);

        } catch (Exception e) {

            result = null;
        }
        return result;
    }
}
